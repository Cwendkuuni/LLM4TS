package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Nodes.Document;
import org.jsoup.Nodes.Element;
import org.jsoup.Nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(""), "", new Parser(parser));
        assertNotNull(doc);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseRootElement() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<root/>"), "", new Parser(parser));
        Element root = doc.getRootElement();
        assertNotNull(root);
        assertTrue(root.children().isEmpty());
        assertTrue(root.tagName().equals("root"));
    }

    @Test
    public void testParseRootElementWithAttributes() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<root attr1=\"val1\" attr2=\"val2\"/>"), "", new Parser(parser));
        Element root = doc.getRootElement();
        assertNotNull(root);
        assertTrue(root.children().isEmpty());
        assertTrue(root.tagName().equals("root"));
        List<Attribute> attrs = root.attributes();
        assertTrue(attrs.size() == 2);
        assertTrue(attrs.get(0).name().equals("attr1"));
        assertTrue(attrs.get(0).value().equals("val1"));
        assertTrue(attrs.get(1).name().equals("attr2"));
        assertTrue(attrs.get(1).value().equals("val2"));
    }

    @Test
    public void testParseElementWithTextNode() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<elem>Hello World</elem>"), "", new Parser(parser));
        Element elem = doc.getElementsByTag("elem").get(0);
        assertNotNull(elem);
        TextNode textNode = (TextNode) elem.childNodes().get(0);
        assertNotNull(textNode);
        assertTrue(textNode.text().equals("Hello World"));
    }

    @Test
    public void testParseElementWithAttributesAndTextNode() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<elem attr1=\"val1\" attr2=\"val2\">Hello World</elem>"), "", new Parser(parser));
        Element elem = doc.getElementsByTag("elem").get(0);
        assertNotNull(elem);
        TextNode textNode = (TextNode) elem.childNodes().get(0);
        assertNotNull(textNode);
        assertTrue(textNode.text().equals("Hello World"));
        List<Attribute> attrs = elem.attributes();
        assertTrue(attrs.size() == 2);
        assertTrue(attrs.get(0).name().equals("attr1"));
        assertTrue(attrs.get(0).value().equals("val1"));
        assertTrue(attrs.get(1).name().equals("attr2"));
        assertTrue(attrs.get(1).value().equals("val2"));
    }

    @Test
    public void testParseElementWithNestedElements() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<root><elem><subElem>Hello World</subElem></elem></root>"), "", new Parser(parser));
        Element root = doc.getRootElement();
        List<Element> elemList = root.getElementsByTag("elem");
        assertTrue(elemList.size() > 0);
        Element elem = elemList.get(0);
        List<Element> subElemList = elem.getElementsByTag("subElem");
        assertTrue(subElemList.size() > 0);
        Element subElem = subElemList.get(0);
        assertNotNull(subElem);
        TextNode textNode = (TextNode) subElem.childNodes().get(0);
        assertNotNull(textNode);
        assertTrue(textNode.text().equals("Hello World"));
    }

    @Test
    public void testParseComment() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<!--- This is a comment ---><root/>"), "", new Parser(parser));
        List<Node> commentList = doc.getElementsByTag("comment");
        assertTrue(commentList.isEmpty());
        List<Comment> commentNodes = doc.getAllElements().stream()
                .filter(n -> n instanceof Comment)
                .map(n -> (Comment) n)
                .collect(Collectors.toList());
        assertTrue(commentNodes.size() == 1);
        Comment comment = commentNodes.get(0);
        assertTrue(comment.text().equals(" This is a comment "));
    }

    @Test
    public void testParseDoctype() {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST elem attr CDATA #REQUIRED> ]><root/>"), "", new Parser(parser));
        List<Node> doctypeList = doc.getElementsByTag("!DOCTYPE");
        assertTrue(doctypeList.size() > 0);
        DocumentType doctype = (DocumentType) doctypeList.get(0);
        assertTrue(doctype.nodeName().equals("!DOCTYPE"));
        List<Attribute> elemAttrs = doctype.attributes();
        assertTrue(elemAttrs.size() > 0);
        Attribute attr = elemAttrs.get(0);
        assertTrue(attr.name().equals("root"));
        assertTrue(attr.value().equals(""));
        List<Attribute> attrList = doctype.attributes().stream()
                .filter(a -> a.name().equals("ATTLIST"))
                .findFirst()
                .get()
                .children()
                .stream()
                .filter(n -> n instanceof Element)
                .map(e -> (Element) e)
                .flatMap(e -> e.children().stream())
                .filter(n -> n instanceof Attribute)
                .map(a -> (Attribute) n)
                .collect(Collectors.toList());
        assertTrue(attrList.size() == 1);
        Attribute elemAttr = attrList.get(0);
        assertTrue(elemAttr.name().equals("elem"));
        assertTrue(elemAttr.children().isEmpty());
        List<Attribute> attrMap = elemAttr.attributes();
        assertTrue(attrMap.size() == 1);
        Attribute attrName = attrMap.get(0);
        assertTrue(attrName.name().equals("attr"));
        assertTrue(attrName.value().equals("CDATA"));
        assertTrue(attrName.isRequired());
    }
}