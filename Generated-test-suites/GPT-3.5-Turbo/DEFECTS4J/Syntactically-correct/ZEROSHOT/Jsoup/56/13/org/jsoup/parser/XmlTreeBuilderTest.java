package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document document;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        document = Jsoup.parse("<root></root>");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document output = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(output);
        assertEquals(document.nodeName(), "root");
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(element.tagName(), "tag");
        assertTrue(startTag.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(comment);
        assertEquals(comment.getData(), "comment");
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);

        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(textNode);
        assertEquals(textNode.text(), "character");
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "", "");
        xmlTreeBuilder.insert(doctype);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(documentType);
        assertEquals(documentType.getName(), "doctype");
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "", ParseSettings.preserveCase);
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }
}