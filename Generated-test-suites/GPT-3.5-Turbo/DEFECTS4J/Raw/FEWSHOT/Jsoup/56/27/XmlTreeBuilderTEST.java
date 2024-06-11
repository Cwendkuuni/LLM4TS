
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();

        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<xml><test>123</test></xml>";
        String baseUri = "http://example.com";

        Document document = builder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(input, document.outerHtml());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><test>123</test></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(input, builder.tokeniser.getInput());
        assertEquals(baseUri, builder.baseUri);
        assertEquals(errors, builder.errors);
        assertEquals(settings, builder.settings);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("test", "name", "value");
        Token endTagToken = new Token.EndTag("test");
        Token commentToken = new Token.Comment("comment");
        Token characterToken = new Token.Character("text");
        Token doctypeToken = new Token.Doctype("html");

        boolean processStartTag = builder.process(startTagToken);
        boolean processEndTag = builder.process(endTagToken);
        boolean processComment = builder.process(commentToken);
        boolean processCharacter = builder.process(characterToken);
        boolean processDoctype = builder.process(doctypeToken);

        assertTrue(processStartTag);
        assertTrue(processEndTag);
        assertTrue(processComment);
        assertTrue(processCharacter);
        assertTrue(processDoctype);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("test");
        builder.stack.add(element);

        Node node = new TextNode("text");
        builder.insertNode(node);

        assertEquals(node, element.childNode(0));
    }

    @Test
    public void testInsertStartElementTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("test", "name", "value");
        startTag.setSelfClosing(true);

        Element element = builder.insert(startTag);

        assertNotNull(element);
        assertEquals("test", element.tagName());
        assertEquals("name", element.attr("name"));
        assertEquals("value", element.attr("value"));
        assertEquals(null, builder.stack.get(1));
    }

    @Test
    public void testInsertStartElementTag() {
        Token.StartTag startTag = new Token.StartTag("test", "name", "value");

        Element element = builder.insert(startTag);

        assertNotNull(element);
        assertEquals("test", element.tagName());
        assertEquals("name", element.attr("name"));
        assertEquals("value", element.attr("value"));
        assertEquals(element, builder.stack.get(1));
    }

    @Test
    public void testInsertCommentBogus() {
        Token.Comment commentToken = new Token.Comment("comment");
        commentToken.setBogus(true);
        builder.baseUri = "http://example.com";

        builder.insert(commentToken);

        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("comment", comment.getData());
        assertEquals("http://example.com", comment.baseUri());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");

        builder.insert(commentToken);

        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("comment", comment.getData());
        assertEquals("", comment.baseUri());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("text");

        builder.insert(characterToken);

        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("text", textNode.getWholeText());
        assertEquals("", textNode.baseUri());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html");

        builder.insert(doctypeToken);

        DocumentType documentType = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", documentType.getName());
        assertNull(documentType.getPublicIdentifier());
        assertNull(documentType.getSystemIdentifier());
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("test");

        Element element1 = new Element("div");
        Element element2 = new Element("test");
        Element element3 = new Element("div");
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);

        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertEquals(element1, builder.stack.get(0));
        assertEquals(element3, builder.stack.get(1));
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("test");

        Element element1 = new Element("div");
        Element element2 = new Element("div");
        Element element3 = new Element("div");
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);

        builder.popStackToClose(endTag);

        assertEquals(3, builder.stack.size());
        assertEquals(element1, builder.stack.get(0));
        assertEquals(element2, builder.stack.get(1));
        assertEquals(element3, builder.stack.get(2));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>text</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        assertEquals("div", ((Element) node).tagName());
    }
}

