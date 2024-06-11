package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataSection;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.xml.XmlDeclaration;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @org.junit.Test
    public void testParseEmptyXml() {
        String xml = "<root></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodeSize());
    }

    @org.junit.Test
    public void testParseXmlWithAttributes() {
        String xml = "<root attr1='value1' attr2='value2'/>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        List<Attribute> attrs = root.attributes();
        assertEquals(2, attrs.size());
        assertEquals("value1", attrs.get(0).toString());
        assertEquals("value2", attrs.get(1).toString());
    }

    @org.junit.Test
    public void testParseXmlWithComments() {
        String xml = "<root><!-- comment1 --></root><!-- comment2 -->";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        List<Comment> comments = doc.select("comment()");
        assertEquals(2, comments.size());
        assertEquals("comment1", comments.get(0).text());
        assertEquals("comment2", comments.get(1).text());
    }

    @org.junit.Test
    public void testParseXmlWithCDataSection() {
        String xml = "<root><![CDATA[ <p>Some content here</p> ]]></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> nodes = root.childNodes();
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertEquals(Node.Element.class, node.getClass());
        Element element = (Element) node;
        List<Node> childNodes = element.childNodes();
        assertEquals(1, childNodes.size());
        Node cdataNode = childNodes.get(0);
        assertEquals(Node.CdataSection.class, cdataNode.getClass());
        assertEquals("<p>Some content here</p>", cdataNode.toString());
    }

    @org.junit.Test
    public void testParseXmlWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED> ]><root></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Node doctypeNode = nodes.get(0);
        assertEquals(Node.DocumentType.class, doctypeNode.getClass());
        DocumentType doctype = (DocumentType) doctypeNode;
        assertEquals("root", doctype.tagName());
        assertEquals("root", doctype.publicId());
        assertEquals(null, doctype.systemId());
        List<Attribute> attributes = doctype.attributes();
        assertEquals(1, attributes.size());
        assertEquals("attr", attributes.get(0).toString());
        assertEquals("CDATA", attributes.get(0).getValue());
    }

    @org.junit.Test
    public void testParseXmlWithError() {
        String xml = "<root><![CDATA[ <p>Some content here<script>alert('Hello')</sc" + "ript> ]]></root>";
        ParseSettings settings = ParseSettings.defaultSettings();
        try {
            Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://www.example.com");
            fail("Expected ParseException");
        } catch (ParseException e) {
            List<ParseError> errors = e.getErrors();
            assertEquals(1, errors.size());
            ParseError error = errors.get(0);
            assertEquals(ParseError.parseErrorType.START_TAG_INVALID_END_TAG, error.type());
            assertEquals(23, error.line()); // assuming line numbers start at 1
            assertEquals(5, error.column());
            assertEquals("startTag", error.source(Token.START_TAG.class).name());
            assertEquals("script", error.source(Token.EndTag.class).name());
        }
    }

    @org.junit.Test
    public void testParseXmlWithFragment() {
        String xmlFragment = "<root><p>Content 1</p><![CDATA[ <p>Content 2</p> ]]></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.defaultSettings()).get(0).childNodes();
        assertEquals(3, nodes.size());
        Node firstChild = nodes.get(0);
        assertEquals(Node.Element.class, firstChild.getClass());
        Element root = (Element) firstChild;
        assertEquals("root", root.tagName());
        Node secondChild = root.childNodes().get(0);
        assertEquals(Node.Element.class, secondChild.getClass());
        Element p1 = (Element) secondChild;
        assertEquals("p", p1.tagName());
        assertEquals("Content 1", p1.text());
        Node thirdChild = root.childNodes().get(1);
        assertEquals(Node.CdataSection.class, thirdChild.getClass());
        CDataSection cdata = (CDataSection) thirdChild;
        assertEquals("<p>Content 2</p>", cdata.toString());
    }
}