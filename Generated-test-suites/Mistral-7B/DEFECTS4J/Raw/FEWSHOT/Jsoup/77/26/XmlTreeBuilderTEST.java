package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr='value'>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
        Element root = doc.getRootElement();
        assertEquals("root", root.tagName());

        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseWithEmptyDocument() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "http://localhost");
        assertEquals(Elements.EMPTY_TAG, doc.getRootElement());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'>Hello World</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr='value'>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("element", doctype.tagName("element"));
        Element root = doc.getRootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Hello World", element.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessInvalidToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.Type.START_TAG, "invalid", null);
        builder.process(token);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root", "http://localhost", ParseSettings.preserveCase);
        Node node = new Element("element", "http://localhost", ParseSettings.preserveCase);
        builder.insertNode(node);
        assertEquals(node, root.child(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String comment = "This is a comment";
        Token commentToken = new Token.Comment(comment);
        builder.insert(commentToken);
        Comment commentNode = (Comment) builder.doc.childNodes().get(0);
        assertEquals(comment, commentNode.data());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String character = "This is a character";
        Token characterToken = new Token.Character(character);
        builder.insert(characterToken);
        TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
        assertEquals(character, textNode.text());
    }

    @Test
    public void testInsertDocumentType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String doctypeName = "doctype";
        String publicId = "public id";
        String systemId = "system id";
        DocumentType documentType = new DocumentType(doctypeName, publicId, systemId);
        builder.insert(new Token.Doctype(doctypeName, publicId, null, null, null));
        assertEquals(documentType, builder.doc.childNodes().get(0));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element("element1", "http://localhost", ParseSettings.preserveCase);
        Element element2 = new Element("element2", "http://localhost", ParseSettings.preserveCase);
        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.popStackToClose(new Token.EndTag("element1"));
        assertEquals(element2, builder.stack.get(builder.stack.size() - 1));
    }
}