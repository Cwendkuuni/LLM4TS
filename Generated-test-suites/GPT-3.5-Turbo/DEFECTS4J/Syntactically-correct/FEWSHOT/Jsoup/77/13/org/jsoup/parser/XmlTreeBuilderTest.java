package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings defaultSettings = builder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><body><p>Test</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document document = builder.parse(reader, baseUri);
        assertNotNull(document);
        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        Element bodyElement = htmlElement.getElementsByTag("body").first();
        assertNotNull(bodyElement);
        Element pElement = bodyElement.getElementsByTag("p").first();
        assertNotNull(pElement);
        assertEquals("Test", pElement.text());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><body><p>Test</p></body></html>";
        String baseUri = "http://example.com";
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        Element bodyElement = htmlElement.getElementsByTag("body").first();
        assertNotNull(bodyElement);
        Element pElement = bodyElement.getElementsByTag("p").first();
        assertNotNull(pElement);
        assertEquals("Test", pElement.text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html><body><p>Test</p></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        builder.initialiseParse(reader, baseUri, errors, settings);
        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertNotNull(builder.doc.outputSettings().syntax());
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("p");
        startTag.attributes().put("id", "test");
        startTag.attributes().put("class", "paragraph");
        startTag.attributes().put("data", "dataValue");
        
        builder.initialiseParse(new StringReader(""), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element element = builder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("p", element.tagName());
        assertEquals("test", element.attr("id"));
        assertEquals("paragraph", element.attr("class"));
        assertEquals("dataValue", element.attr("data"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("p"), "");
        Element element2 = new Element(Tag.valueOf("div"), "");

        builder.stack.add(element1);
        builder.stack.add(element2);

        Token.EndTag endTag = new Token.EndTag("p");
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals(element2, builder.stack.get(0));

        endTag = new Token.EndTag("div");
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        builder.insert(commentToken);

        Document document = builder.doc;
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test character");
        builder.insert(characterToken);

        Document document = builder.doc;
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("test character", textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        builder.insert(doctypeToken);

        Document document = builder.doc;
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
        Node node = document.childNodes().get(0);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("", doctypeNode.attr("publicId"));
        assertEquals("", doctypeNode.attr("systemId"));

    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<div><p>Test</p></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = builder.parseFragment(input, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element element = (Element) node;
        assertEquals("div", element.tagName());
        Element pElement = element.getElementsByTag("p").first();
        assertNotNull(pElement);
        assertEquals("Test", pElement.text());
    }
}