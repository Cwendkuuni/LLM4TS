package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        assertEquals("root", xmlDoc.tagName());
        Element root = xmlDoc.rootElement();
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attribute(0).getName());
        assertEquals("attr", root.child(0).attribute(0).getName());
        assertEquals("value", root.child(0).attribute(0).getValue());
        assertEquals("Content", root.child(0).text());
    }

    @Test
    public void testParseWithComments() {
        String xmlInput = "<root><!--comment--><element attr=\"value\">Content</element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = xmlDoc.rootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Node child = children.get(0);
        assertEquals("element", child.tagName());
        List<Node> childChildren = child.children();
        assertEquals(0, childChildren.size());

        List<Node> comments = xmlDoc.select("//comment()");
        assertEquals(1, comments.size());
        Node comment = comments.get(0);
        assertEquals("comment", comment.nodeName());
        assertEquals("comment", comment.tagName());
        assertEquals("comment", comment.toString());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlInput = "<root><img src=\"image.png\" /></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        Element root = xmlDoc.rootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());

        Node imgNode = children.get(0);
        assertEquals("img", imgNode.tagName());
        List<Node> imgChildren = imgNode.children();
        assertEquals(0, imgChildren.size());
        assertTrue(imgNode.hasAttr("src"));
        assertEquals("image.png", imgNode.attr("src"));
    }

    @Test
    public void testParseWithDocumentType() {
        String xmlInput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>Test</p>" +
                "</body>" +
                "</html>";
        Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("html", doctype.nodeName());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.systemId());

        Element html = xmlDoc.rootElement();
        List<Node> children = html.children();
        assertEquals(2, children.size());

        Element head = (Element) children.get(0);
        List<Node> headChildren = head.children();
        assertEquals(1, headChildren.size());
        Element title = (Element) headChildren.get(0);
        assertEquals("title", title.tagName());
        assertEquals("Test", title.text());

        Element body = (Element) children.get(1);
        List<Node> bodyChildren = body.children();
        assertEquals(1, bodyChildren.size());
        Element p = (Element) bodyChildren.get(0);
        assertEquals("p", p.tagName());
        assertEquals("Test", p.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        Document doc = Jsoup.parse(new Element(Elements.xmlDeclaration("1.0", null, null, null), null).outputSettings(OutputSettings.prettyPrint()).toString(), "http://example.com", Parser.xmlParser());
        doc.setRootElement(new Element("root"));
        doc.insertBefore(doc.select("root").get(0), new Element("element").addAttr("attr", "value").addText("Content"));

        List<Node> docNodes = doc.select("*");
        assertEquals(2, fragmentNodes.size());
        assertEquals(2, docNodes.size());

        for (int i = 0; i < fragmentNodes.size(); i++) {
            Node fragmentNode = fragmentNodes.get(i);
            Node docNode = docNodes.get(i);
            if (fragmentNode instanceof TextNode) {
                assertEquals(((TextNode) fragmentNode).toString(), ((TextNode) docNode).toString());
            } else if (fragmentNode instanceof Element) {
                assertEquals(((Element) fragmentNode).tagName(), ((Element) docNode).tagName());
                assertEquals(((Element) fragmentNode).attributes().size(), ((Element) docNode).attributes().size());
                List<Node> fragmentChildren = ((Element) fragmentNode).children();
                List<Node> docChildren = ((Element) docNode).children();
                assertEquals(fragmentChildren.size(), docChildren.size());
                for (int j = 0; j < fragmentChildren.size(); j++) {
                    Node fragmentChild = fragmentChildren.get(j);
                    Node docChild = docChildren.get(j);
                    if (fragmentChild instanceof TextNode) {
                        assertEquals(((TextNode) fragmentChild).toString(), ((TextNode) docChild).toString());
                    } else if (fragmentChild instanceof Element) {
                        assertEquals(((Element) fragmentChild).tagName(), ((Element) docChild).tagName());
                        assertEquals(((Element) fragmentChild).attributes().size(), ((Element) docChild).attributes().size());
                        List<Node> fragmentChildChildren = ((Element) fragmentChild).children();
                        List<Node> docChildChildren = ((Element) docChild).children();
                        assertEquals(fragmentChildChildren.size(), docChildChildren.size());
                        for (int k = 0; k < fragmentChildChildren.size(); k++) {
                            Node fragmentChildChild = fragmentChildChildren.get(k);
                            Node docChildChild = docChildChildren.get(k);
                            if (fragmentChildChild instanceof TextNode) {
                                assertEquals(((TextNode) fragmentChildChild).toString(), ((TextNode) docChildChild).toString());
                            } else if (fragmentChildChild instanceof Element) {
                                assertEquals(((Element) fragmentChildChild).tagName(), ((Element) docChildChild).tagName());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testParseNullInput() {
        Document xmlDoc = new XmlTreeBuilder().parse(null, "http://example.com");
        assertNull(xmlDoc);
    }

    @Test
    public void testParseEmptyInput() {
        Document xmlDoc = new XmlTreeBuilder().parse("", "http://example.com");
        assertNotNull(xmlDoc);
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInput() {
        new XmlTreeBuilder().parse("Invalid input", "http://example.com");
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        String testInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content</element>\n</root>";

        TreeBuilder parser = new XmlTreeBuilder();
        Token token = parser.tokenizer().next();
        parser.initialiseParse(testInput, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = null;
        Element element = null;

        while (token != null) {
            parser.process(token);
            token = parser.tokenizer().next();

            switch (parser.tag()) {
                case StartTag:
                    if (root == null) {
                        root = parser.currentElement();
                    } else if (element == null) {
                        element = parser.currentElement();
                        break;
                    }
                    break;
            }
        }

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).getValue());
        assertEquals("attr", element.attribute(0).getName());
        assertEquals("Content", element.text());
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><element><subelement></subelement></element></root>";

        TreeBuilder parser = new XmlTreeBuilder();
        parser.initialiseParse(xmlInput, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = parser.doc;
        Element element = null;
        Element subelement = null;

        Token token = parser.tokenizer().next();
        while (token != null) {
            parser.process(token);
            token = parser.tokenizer().next();

            switch (parser.tag()) {
                case StartTag:
                    if (root == null) {
                        root = parser.currentElement();
                    } else if (element == null) {
                        element = parser.currentElement();
                    } else if (subelement == null) {
                        subelement = parser.currentElement();
                    }
                    break;
                case EndTag:
                    if (parser.currentElement().tagName().equals("subelement")) {
                        subelement = null;
                    } else if (parser.currentElement().tagName().equals("element")) {
                        element = null;
                    }
                    break;
            }
        }

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("subelement", subelement.tagName());
    }
}