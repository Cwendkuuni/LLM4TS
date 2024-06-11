package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Test
    public void testParseEmptyXml() {
        Document doc = builder.parse(new StringReader(""), "");
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXmlWithRootElement() {
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        Element root = doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseSimpleXmlWithRootElementAndContent() {
        Document doc = builder.parse(new StringReader("<root><child>Hello World</child></root>"), "");
        Element root = doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        Element child = root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        Document doc = builder.parse(new StringReader("<root><empty/></root>"), "");
        Element root = doc.childNodes().get(0);
        Element empty = root.childNodes().get(0);
        assertEquals("empty", empty.nodeName());
        assertEquals(0, empty.childNodes().size());
    }

    @Test
    public void testParseXmlWithTextNode() {
        Document doc = builder.parse(new StringReader("<root>Text</root>"), "");
        Element root = doc.childNodes().get(0);
        TextNode textNode = root.childNodes().get(0);
        assertEquals("Text", textNode.text().toString());
    }

    @Test
    public void testParseXmlWithAttribute() {
        Document doc = builder.parse(new StringReader("<root attr='value'></root>"), "");
        Element root = doc.childNodes().get(0);
        assertEquals("value", root.attr("attr"));
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        Document doc = builder.parse(new StringReader("<root><img src='image.png'/></root>"), "");
        Element root = doc.childNodes().get(0);
        Element img = root.childNodes().get(0);
        assertEquals("img", img.nodeName());
        assertTrue(img.isSelfClosing());
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        Document doc = builder.parse(new StringReader("<root><!--comment--></root>"), "");
        Element root = doc.childNodes().get(0);
        Comment comment = root.childNodes().get(0);
        assertEquals("comment", comment.toString());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        Document doc = builder.parse(new StringReader("<root><![CDATA[This is a CData section]]></root>"), "");
        Element root = doc.childNodes().get(0);
        CDataNode cdataNode = (CDataNode) root.childNodes().get(0);
        assertEquals("This is a CData section", cdataNode.childNodes().get(0).text().toString());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithDoctype() {
        Document doc = builder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED> ]><root></root>"), "");
        DocumentType doctype = doc.childNodes().get(0);
        assertEquals("root", doctype.name());
        assertEquals("[ <!ATTLIST root attr CDATA #REQUIRED> ]", doctype.systemId());
        Element root = doc.childNodes().get(1);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child>Hello World</child></root>", "", "");
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.nodeName());
        assertEquals("Hello World", child.text().toString());
    }

    @Test
    public void testParseXmlFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element context = new Element("context");
        List<Node> nodes = builder.parseFragment("<root><child>Hello World</child></root>", context, "", "");
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.nodeName());
        assertEquals("Hello World", child.text().toString());
        assertEquals(context, root.parent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        builder = new XmlTreeBuilder();
        builder.parse(new StringReader("Invalid input"), "");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullInput() {
        builder = new XmlTreeBuilder();
        try {
            builder.parse(null, "");;
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlWithNullBaseUri() {
        builder = new XmlTreeBuilder();
        try {
            builder.parse(new StringReader("<root></root>"), null);
        } catch (IllegalArgumentException e) {
            Validate.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        try {
            Document doc = builder.parse(new StringReader("<root></root>"), null);
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            doc.toString();
        } catch (NullPointerException e) {
            return;
        }
        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlWithNullParser() {
        builder = new XmlTreeBuilder();
        try {
            builder.parse(new StringReader("<root></root>"), null, null);
        } catch (IllegalArgumentException e) {
            Validate.fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        try {
            Document doc = builder.parse(new StringReader("<root></root>"), null, null);
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            doc.toString();
        } catch (NullPointerException e) {
            return;
        }
        fail("Expected NullPointerException");
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><a><b/><c/></a></root>"), "");
        Element root = doc.childNodes().get(0);
        Element a = root.childNodes().get(0);
        Element b = a.childNodes().get(0);
        Element c = a.childNodes().get(1);
        builder.popStackToClose(Token.EndTag.valueOf("b"));
        assertEquals(1, a.childNodes().size());
        assertEquals(0, b.childNodes().size());

        builder.popStackToClose(Token.EndTag.valueOf("c"));
        assertEquals(1, a.childNodes().size());
        assertEquals(0, c.childNodes().size());

        builder.popStackToClose(Token.EndTag.valueOf("a"));
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testPopStackToCloseWithNonExistentTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        builder.popStackToClose(Token.EndTag.valueOf("nonExistentTag"));
        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testProcessToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child>Hello World</child></root>", "", "");
        Node token = nodes.get(0).childNodes().get(0);
        builder.process(token);
        assertEquals(1, nodes.size());
        assertEquals(1, nodes.get(0).childNodes().size());
    }

    @Test
    public void testProcessTokenEmptyInput() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(""), "");
        Node token = doc.childNodes().get(0).childNodes().get(0);
        builder.process(token);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testProcessTokenComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<!--comment--><root></root>"), "");
        Node token = doc.childNodes().get(0);
        builder.process(token);
        assertEquals(1, doc.childNodes().size());
        assertEquals(0, token.childNodes().size());
    }

    @Test
    public void testProcessTokenCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("&lt;character&gt;<root></root>"), "");
        Node token = doc.childNodes().get(0).childNodes().get(0);
        builder.process(token);
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals("&lt;character&gt;", token.toString());
    }

    @Test
    public void testProcessTokenDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED> ]><root></root>"), "");
        Node token = doc.childNodes().get(0);
        builder.process(token);
        assertEquals(2, doc.childNodes().size());
        assertEquals(0, token.childNodes().size());
    }

    @Test
    public void testProcessTokenEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><a><b/><c/></a></root>"), "");
        Node token = doc.childNodes().get(0).childNodes().get(1);
        builder.process(token);
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doc.childNodes().get(0).childNodes().size());
        assertEquals("a", doc.childNodes().get(0).nodeName());
    }

    @Test
    public void testProcessTokenStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root><a><b/><c/></a></root>"), "");
        Node token = doc.childNodes().get(0).childNodes().get(1);
        builder.process(token);
        assertEquals(1, doc.childNodes().size());
        assertEquals(4, doc.childNodes().get(0).childNodes().size());
        assertEquals("a", doc.childNodes().get(0).childNodes().get(0).nodeName());
    }

    @Test
    public void testProcessTokenEOF() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        Node token = doc.childNodes().get(0).childNodes().get(0);
        token.setType(Token.Type.EOF);
        builder.process(token);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = builder.parse(new StringReader("<root></root>"), "").childNodes().get(0);
        TextNode textNode = new TextNode("Hello World");
        builder.insertNode(textNode);
        root.appendChild(textNode);
        assertEquals("Hello World", textNode.text().toString());
    }

    @Test
    public void testInsertEmptyElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = builder.parse(new StringReader("<root></root>"), "").childNodes().get(0);
        Element emptyElement = new Element("empty");
        builder.insert(emptyElement);
        root.appendChild(emptyElement);
        assertEquals("empty", emptyElement.nodeName());
        assertEquals(0, emptyElement.childNodes().size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        Comment comment = new Comment("comment");
        Node commentNode = null;
        if (comment.isXmlDeclaration()) {
            XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
            commentNode = xmlDeclaration;
        } else {
            commentNode = comment;
        }
        builder.insert(commentNode);
        doc.childNodes().get(0).appendChild(commentNode);
        assertEquals("comment", commentNode.toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        Character character = new Character('A');
        TextNode textNode = character.isCData() ? new CDataNode("A") : new TextNode("A");
        builder.insert(textNode);
        doc.childNodes().get(0).appendChild(textNode);
        assertEquals("A", textNode.text().toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "");
        DocumentType doctype = new DocumentType("root", null, null);
        Node doctypeNode = doc.childNodes().add(doctype);
        builder.insert(doctypeNode);
        assertEquals("root", doctypeNode.nodeName());
    }

    static class TestSettings extends ParseSettings {
        @Override
        public ParseSettings deepClone() {
            return new TestSettings();
        }
    }

    @Test
    public void testParseXmlWithCustomSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><child>Hello World</child></root>", "", new TestSettings());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("Hello World", child.text().toString());
    }
}