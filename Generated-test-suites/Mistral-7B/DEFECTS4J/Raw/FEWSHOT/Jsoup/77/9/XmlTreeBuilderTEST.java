package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document doc = new XmlTreeBuilder().parse(new StringReader(""), "");
        assertNotNull(doc);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseRootElement() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root/>"), "");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertTrue(root.childNodes().isEmpty());
    }

    @Test
    public void testParseRootElementWithTextContent() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root>Content</root>"), "");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.childNodes();
        assertTrue(children.size() == 1);
        assertTrue(children.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) children.get(0);
        assertTrue(textNode.text().equals("Content"));
    }

    @Test
    public void testParseElementWithAttributes() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<element attr1=\"value1\" attr2=\"value2\"/>"), "");
        Element element = doc.select("element").get(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 2);
        assertTrue(attributes.get(0).getKey().equals("attr1"));
        assertTrue(attributes.get(0).getValue().equals("value1"));
        assertTrue(attributes.get(1).getKey().equals("attr2"));
        assertTrue(attributes.get(1).getValue().equals("value2"));
    }

    @Test
    public void testParseElementWithTextContentAndAttributes() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<element attr1=\"value1\" attr2=\"value2\">Content</element>"), "");
        Element element = doc.select("element").get(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 2);
        assertTrue(attributes.get(0).getKey().equals("attr1"));
        assertTrue(attributes.get(0).getValue().equals("value1"));
        assertTrue(attributes.get(1).getKey().equals("attr2"));
        assertTrue(attributes.get(1).getValue().equals("value2"));
        List<Node> children = element.childNodes();
        assertTrue(children.size() == 1);
        assertTrue(children.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) children.get(0);
        assertTrue(textNode.text().equals("Content"));
    }

    @Test
    public void testParseElementWithNestedElements() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><element><subElement/></element></root>"), "");
        Element root = doc.rootElement();
        Element element = root.select("element").get(0);
        Element subElement = element.select("subElement").get(0);
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertTrue(element.tagName().equals("element"));
        assertTrue(subElement.tagName().equals("subElement"));
        assertTrue(root.childNodes().size() == 1);
        assertTrue(element.childNodes().size() == 1);
        assertTrue(subElement.childNodes().isEmpty());
    }

    @Test
    public void testParseComment() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<!-- This is a comment -->"), "");
        List<Node> comments = doc.select("comment()");
        assertTrue(comments.size() == 1);
        Comment comment = (Comment) comments.get(0);
        assertTrue(comment.getData().equals(" This is a comment "));
    }

    @Test
    public void testParseCDataSection() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<![CDATA[This is a CData section]]>"), "");
        List<Node> cdataSections = doc.select("cdata-section()");
        assertTrue(cdataSections.size() == 1);
        CDataNode cdataNode = (CDataNode) cdataSections.get(0);
        assertTrue(cdataNode.getData().equals("This is a CData section"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        new XmlTreeBuilder().parse(new StringReader("<root>"), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlWithComment() {
        new XmlTreeBuilder().parse(new StringReader("<!--"), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlWithCData() {
        new XmlTreeBuilder().parse(new StringReader("<![CDATA["), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXmlWithDocumentType() {
        new XmlTreeBuilder().parse(new StringReader("<!DOCTYPE root ["), "");
    }
}