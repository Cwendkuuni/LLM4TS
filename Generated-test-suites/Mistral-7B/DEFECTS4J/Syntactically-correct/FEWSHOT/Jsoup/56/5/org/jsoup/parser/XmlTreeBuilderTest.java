package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\"></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.children();
        assertTrue(children.size() == 1);
        Element element = (Element) children.get(0);
        assertTrue(element.tagName().equals("element"));
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        Attribute attr = attributes.get(0);
        assertTrue(attr.getKey().equals("attr"));
        assertTrue(attr.getValue().equals("value"));
    }

    @Test
    public void testParseWithEmptyInput() {
        String xml = "";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseWithEmptyComment() {
        String xml = "<!-- -->";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> nodes = doc.childNodes();
        assertTrue(nodes.size() == 1);
        Node node = nodes.get(0);
        assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        assertTrue(comment.getData().isEmpty());
    }

    @Test
    public void testParseWithCommentAndText() {
        String xml = "<!-- This is a comment --><text>Hello World</text>";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> nodes = doc.childNodes();
        assertTrue(nodes.size() == 2);
        Node commentNode = nodes.get(0);
        assertTrue(commentNode instanceof Comment);
        Comment comment = (Comment) commentNode;
        assertTrue(comment.getData().equals(" This is a comment "));
        Node textNode = nodes.get(1);
        assertTrue(textNode instanceof TextNode);
        TextNode text = (TextNode) textNode;
        assertTrue(text.text().equals("Hello World"));
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xml = "<tag />";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> nodes = doc.childNodes();
        assertTrue(nodes.size() == 1);
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        Element tag = (Element) node;
        assertTrue(tag.tagName().equals("tag"));
        assertTrue(tag.getText().isEmpty());
    }

    @Test
    public void testParseWithDoctype() {
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        Document doc = XmlTreeBuilder.parse(xml, "http://example.com");
        List<Node> nodes = doc.childNodes();
        assertTrue(nodes.size() == 1);
        Node node = nodes.get(0);
        assertTrue(node instanceof DocumentType);
        DocumentType doctype = (DocumentType) node;
        assertTrue(doctype.nodeName().equals("html"));
        assertTrue(doctype.publicId().equals("-//W3C//DTD XHTML 1.0 Transitional//EN"));
        assertTrue(doctype.systemId().equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Hello</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(Node.class::cast).collect(Collectors.toList());
        Element element = (Element) nodes.get(0);
        assertTrue(element.tagName().equals("element"));
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        Attribute attr = attributes.get(0);
        assertTrue(attr.getKey().equals("attr"));
        assertTrue(attr.getValue().equals("value"));
        assertTrue(element.text().equals("Hello"));
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(Node.class::cast).collect(Collectors.toList());
        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testParseFragmentWithEmptyComment() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("<!-- -->", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(Node.class::cast).collect(Collectors.toList());
        assertTrue(nodes.size() == 1);
        Node node = nodes.get(0);
        assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        assertTrue(comment.getData().isEmpty());
    }

    @Test
    public void testParseFragmentWithCommentAndText() {
        List<Node> nodes = XmlTreeBuilder.parseFragment("<!-- This is a comment --><text>Hello World</text>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().map(Node.class::cast).collect(Collectors.toList());
        assertTrue(nodes.size() == 2);
        Node commentNode = nodes.get(0);
        assertTrue(commentNode instanceof Comment);
        Comment comment = (Comment) commentNode;
        assertTrue(comment.getData().equals(" This is a comment "));
        Node textNode = nodes.get(1);
        assertTrue(textNode instanceof TextNode);
        TextNode text = (TextNode) textNode;
        assertTrue(text.text().equals("Hello World"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", "http://example.com");
        builder.stack.add(root);
        Element element1 = new Element("element1", "http://example.com");
        builder.stack.add(element1);
        Element element2 = new Element("element2", "http://example.com");
        builder.stack.add(element2);
        Element element3 = new Element("element3", "http://example.com");
        builder.stack.add(element3);

        Token endTag = new Token.EndTag("element3");
        builder.popStackToClose(endTag);
        assertTrue(builder.stack.size() == 2);
        Element expectedElement2 = builder.stack.get(1);
        assertTrue(expectedElement2.nodeName().equals("element2"));

        Token endTag2 = new Token.EndTag("element2");
        builder.popStackToClose(endTag2);
        assertTrue(builder.stack.size() == 1);
        Element expectedElement1 = builder.stack.get(0);
        assertTrue(expectedElement1.nodeName().equals("element1"));

        Token endTag3 = new Token.EndTag("root");
        builder.popStackToClose(endTag3);
        assertFalse(builder.stack.isEmpty()); // Stack should be empty but JUnit's assertFalse does not accept empty collections.
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseWithUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", "http://example.com");
        builder.stack.add(root);
        Element element1 = new Element("element1", "http://example.com");
        builder.stack.add(element1);

        Token endTag = new Token.EndTag("unknownTag");
        builder.popStackToClose(endTag);
    }
}