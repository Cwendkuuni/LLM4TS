package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<xml><foo>bar</foo></xml>");
        Document document = xmlTreeBuilder.parse(reader, "http://example.com");
        assertNotNull(document);
        assertEquals("<xml>\n <foo>\n  bar\n </foo>\n</xml>\n", document.toString());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><foo>bar</foo></xml>";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertNotNull(document);
        assertEquals("<xml>\n <foo>\n  bar\n </foo>\n</xml>\n", document.toString());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<xml><foo>bar</foo></xml>");
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, settings);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals("xml", xmlTreeBuilder.getCurrentElement().nodeName());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("foo", "foo", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("foo", element.nodeName());
        assertEquals("", element.baseUri());
        assertTrue(element.attributes().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("test", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("test", false);
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("test", textNode.getWholeText());
    }

    @Test
    public void testProcessCDataCharacter() {
        Token.Character characterToken = new Token.Character("test", true);
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof CDataNode);
        CDataNode cDataNode = (CDataNode) childNodes.get(0);
        assertEquals("test", cDataNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);
        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("foo");
        Element element1 = new Element(Tag.valueOf("foo"), "");
        Element element2 = new Element(Tag.valueOf("bar"), "");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element2, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<foo>bar</foo>";
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element element = (Element) childNodes.get(0);
        assertEquals("foo", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("bar", element.text());
    }

}