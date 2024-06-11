package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CharacterData;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr='value'><subelement/></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        assertNotNull(doc);
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        assertTrue(elementChildren.get(0) instanceof Element);
        Element subelement = (Element) elementChildren.get(0);
        assertEquals("subelement", subelement.tagName());
        assertTrue(subelement.children().isEmpty());
    }

    @Test
    public void testParseWithComments() {
        String xml = "<root><!-- comment1 --><element attr='value'><!-- comment2 --><subelement/><!-- comment3 --></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        Element root = doc.rootElement();
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());
        Element subelement = (Element) elementChildren.get(0);
        assertEquals("subelement", subelement.tagName());
        assertTrue(subelement.children().isEmpty());

        List<Node> comments = doc.body().childNodes();
        assertEquals(3, comments.size());
        for (int i = 0; i < 3; i++) {
            Node comment = comments.get(i);
            assertTrue(comment instanceof Comment);
            String commentText = ((Comment) comment).text();
            if (i == 0) {
                assertEquals(" comment1 ", commentText);
            } else if (i == 1) {
                assertEquals(" comment2 ", commentText);
            } else {
                assertEquals(" comment3 ", commentText);
            }
        }
    }

    @Test
    public void testProcessTokens() {
        String xml = "<root><!-- comment1 --><element attr='value'><!-- comment2 --><subelement/><!-- comment3 --></element></root>";
        Tokenizer tokenizer = new Tokenizer(new StringReader(xml), "http://example.com", ParseSettings.preserveCase);
        List<Node> nodes = new ArrayList<>();
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        while (tokenizer.nextToken() != null) {
            Token token = tokenizer.current();
            xmlTreeBuilder.process(token);
            if (token.type != Token.Type.EOF) {
                Node node = xmlTreeBuilder.stack.peek();
                nodes.add(node);
            }
        }
        assertEquals(11, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
        assertEquals(Element.class, nodes.get(0).getClass());

        assertEquals("!-- comment1 --", nodes.get(1).nodeName());
        assertEquals(Comment.class, nodes.get(1).getClass());

        assertEquals("root", nodes.get(2).nodeName());
        assertEquals(Element.class, nodes.get(2).getClass());
        assertEquals("element", nodes.get(3).nodeName());
        assertEquals(Element.class, nodes.get(4).getClass());
        assertEquals("!-- comment2 --", nodes.get(5).nodeName());
        assertEquals(Comment.class, nodes.get(5).getClass());
        assertEquals("subelement", nodes.get(6).nodeName());
        assertEquals(Element.class, nodes.get(7).getClass());
        assertEquals("!-- comment3 --", nodes.get(8).nodeName());
        assertEquals(Comment.class, nodes.get(8).getClass());
        assertEquals("element", nodes.get(9).nodeName());
        assertEquals(Element.class, nodes.get(10).getClass());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<subelement/>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
        assertEquals(1, nodes.size());
        assertEquals("subelement", nodes.get(0).nodeName());
        assertEquals(Element.class, nodes.get(0).getClass());
    }

    @Test
    public void testParseWithEmptyInput() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertTrue(doc.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        XmlTreeBuilder.parse(new StringReader("<root/>"), null);
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root SYSTEM \"system.dtd\">\n<root></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.nodeName());
        assertTrue(doctype.isRootElement());
        assertEquals("system.dtd", doctype.systemIdentifier());
        assertEquals("", doctype.publicIdentifier());
        assertEquals("", doctype.internalSubset());
        assertTrue(doctype.isStandalone());
    }

    @Test
    public void testParseWithInvalidXml() {
        String xml = "<root><element><!--";
        Document doc = null;
        try {
            doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(doc);
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xml = "<root><selfclosing/></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element selfclosing = root.child(0);
        assertEquals("selfclosing", selfclosing.tagName());
        assertTrue(selfclosing.isSelfClosing());
        assertTrue(selfclosing.children().isEmpty());
    }

    @Test
    public void testParseWithEmptyElement() {
        String xml = "<root><element/></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertTrue(element.children().isEmpty());
    }
}