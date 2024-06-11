package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void defaultSettings_returnsPreserveCaseSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void initialiseParse_withReaderBaseUriAndParser_initializesParse() {
        // Arrange
        Reader input = new StringReader("<xml>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        // Act
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals("html", stack.get(0).nodeName());
        assertEquals("xml", xmlTreeBuilder.getDocument().nodeName());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    @Test
    public void parse_withReaderAndBaseUri_parsesDocument() {
        // Arrange
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("xml", document.nodeName());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void parse_withInputAndBaseUri_parsesDocument() {
        // Arrange
        String input = "<xml></xml>";
        String baseUri = "http://example.com";

        // Act
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("xml", document.nodeName());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void process_withStartTagToken_insertsStartTagElement() {
        // Arrange
        Token.StartTag startTag = Token.StartTag.valueOf("xml");
        startTag.attributes().put("version", "1.0");

        // Act
        xmlTreeBuilder.process(new Token(startTag));

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals("xml", stack.get(0).nodeName());
        assertEquals("1.0", stack.get(0).attr("version"));
    }

    @Test
    public void process_withEndTagToken_closesMatchingStartTagElements() {
        // Arrange
        Token.StartTag startTag = Token.StartTag.valueOf("xml");
        Token.EndTag endTag = Token.EndTag.valueOf("xml");

        // Act
        xmlTreeBuilder.process(new Token(startTag));
        xmlTreeBuilder.process(new Token(endTag));

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(0, stack.size());
    }

    @Test
    public void process_withCommentToken_insertsComment() {
        // Arrange
        Token.Comment comment = Token.Comment.valueOf("This is a comment");

        // Act
        xmlTreeBuilder.process(new Token(comment));

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals(Node.COMMENT_NODE, stack.get(0).nodeType());
        assertEquals("This is a comment", ((org.w3c.dom.Comment) stack.get(0)).getData());
    }

    @Test
    public void process_withCharacterToken_insertsCharacterNode() {
        // Arrange
        Token.Character character = Token.Character.valueOf("This is a text node");

        // Act
        xmlTreeBuilder.process(new Token(character));

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals(Node.TEXT_NODE, stack.get(0).nodeType());
        assertEquals("This is a text node", stack.get(0).toString());
    }

    @Test
    public void process_withDoctypeToken_insertsDoctypeNode() {
        // Arrange
        Token.Doctype doctype = Token.Doctype.valueOf("html");

        // Act
        xmlTreeBuilder.process(new Token(doctype));

        // Assert
        List<Node> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals(Node.DOCUMENT_TYPE_NODE, stack.get(0).nodeType());
        assertEquals("html", stack.get(0).nodeName());
    }

    @Test
    public void parseFragment_withInputFragmentAndBaseUri_parsesAndReturnsChildNodes() {
        // Arrange
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";

        // Act
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser(xmlTreeBuilder));

        // Assert
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertEquals("xml", childNodes.get(0).nodeName());
    }

    @Test
    public void parseFragment_withInputFragmentContextBaseUriAndParser_parsesAndReturnsChildNodes() {
        // Arrange
        String inputFragment = "<xml></xml>";
        Element context = new Element(Tag.valueOf("div"), "http://example.com");
        String baseUri = "http://example.com";

        // Act
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser(xmlTreeBuilder));

        // Assert
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertEquals("xml", childNodes.get(0).nodeName());
    }
}