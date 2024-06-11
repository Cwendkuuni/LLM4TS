package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeSet;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.Doctype;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><element attr='value'>Content</element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        assertEquals("root", xmlDoc.tagName());
        Element root = xmlDoc.child(0);
        assertEquals("element", root.tagName());
        Attribute attr = root.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        assertEquals("Content", root.child(0).text());
    }

    @Test
    public void testParseEmpty() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertNotNull(xmlDoc);
        assertEquals("", xmlDoc.outerHtml());
    }

    @Test
    public void testParseWithComment() {
        String xmlInput = "<root><!-- This is a comment --></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        assertEquals("root", root.tagName());
        Comment comment = (Comment) root.child(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseWithCData() {
        String xmlInput = "<root><![CDATA[This is CData]]></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        assertEquals("root", root.tagName());
        CDataNode cdata = (CDataNode) root.child(0);
        assertNotNull(cdata);
        assertEquals("This is CData", cdata.getData());
    }

    @Test
    public void testParseWithEmptyElement() {
        String xmlInput = "<root><element/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("", element.outerHtml());
    }

    @Test
    public void testParseWithSelfClosingElement() {
        String xmlInput = "<root><element/></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertTrue(element.hasAttr("xmlns:xml"));
        assertEquals("http://www.w3.org/2000/xmlns/", element.attr("xmlns:xml").getValue());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlInput = "<!DOCTYPE root SYSTEM 'http://example.com/root.dtd'><root></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        DocumentType doctype = xmlDoc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertEquals("http://example.com/root.dtd", doctype.systemId());
    }

    @Test
    public void testParseWithMultipleRoots() {
        String xmlInput = "<root1><root2><element/></root2></root1>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");
        assertNotNull(xmlDoc);
        assertEquals("root1", xmlDoc.tagName());
        Element root1 = xmlDoc.child(0);
        assertEquals("root2", root1.tagName());
        Element root2 = root1.child(0);
        assertEquals("element", root2.tagName());
    }

    @Test
    public void testParseWithMixedElements() {
        String xmlInput = "<html><head><title>Title</title></head><body><root><element attr='value'>Content</element></root></body></html>";
        Document xmlDoc = Jsoup.parse(xmlInput, "http://example.com", Parser.xmlParser());
        Element html = xmlDoc.select("html").first();
        assertEquals("html", html.tagName());
        Element head = html.select("head").first();
        assertEquals("head", head.tagName());
        Element title = head.select("title").first();
        assertEquals("title", title.tagName());
        assertEquals("Title", title.text());
        Element root = html.select("body > root").first();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr='value'>Content</element></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        assertEquals("root", xmlDoc.tagName());
        Element root = xmlDoc.child(0);
        assertEquals("element", root.tagName());
        Attribute attr = root.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        assertEquals("Content", root.child(0).text());
    }

    @Test
    public void testParseFragmentWithComment() {
        String xmlFragment = "<root><!-- This is a comment --></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        Comment comment = (Comment) root.child(0);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseFragmentWithCData() {
        String xmlFragment = "<root><![CDATA[This is CData]]></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        CDataNode cdata = (CDataNode) root.child(0);
        assertNotNull(cdata);
        assertEquals("This is CData", cdata.getData());
    }

    @Test
    public void testParseFragmentWithEmptyElement() {
        String xmlFragment = "<root><element/></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("", element.outerHtml());
    }

    @Test
    public void testParseFragmentWithSelfClosingElement() {
        String xmlFragment = "<root><element/></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertTrue(element.hasAttr("xmlns:xml"));
        assertEquals("http://www.w3.org/2000/xmlns/", element.attr("xmlns:xml").getValue());
    }

    @Test
    public void testParseFragmentWithDoctype() {
        String xmlFragment = "<!DOCTYPE root SYSTEM 'http://example.com/root.dtd'><root></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        DocumentType doctype = xmlDoc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertEquals("http://example.com/root.dtd", doctype.systemId());
    }

    @Test
    public void testParseFragmentWithMixedElements() {
        String xmlFragment = "<root><element attr='value'>Content</element></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document xmlDoc = new Document();
        xmlDoc.appendChild(nodes.get(0));
        assertNotNull(xmlDoc);
        Element root = xmlDoc.child(0);
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element attr='value'>Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("root", root.tagName());
        token = new Token.StartTag("element");
        xmlTreeBuilder.process(token);
        Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.getValue());
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><element attr='value'>Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.stack.get(0);
        Element element = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
        token = new Token.EndTag("element");
        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.stack.size() - 1, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<root><!-- This is a comment --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>Content</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        TextNode text = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("Content", text.text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlInput = "<!DOCTYPE root SYSTEM 'http://example.com/root.dtd'><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        DocumentType doctype = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals("root", doctype.name());
        assertEquals("http://example.com/root.dtd", doctype.systemId());
    }

    @Test
    public void testProcessEOF() {
        String xmlInput = "<root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        assertEquals(xmlTreeBuilder.stack.size() - 1, xmlTreeBuilder.stack.size());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedToken() {
        String xmlInput = "<root><![CDATA[This is not a token]]>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("root");
        xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        xmlTreeBuilder.process(new Token.Character('!'));
    }
}