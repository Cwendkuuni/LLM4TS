package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXmlWithNoContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        assertFalse(xmlDoc.body().text().isEmpty());
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseXmlWithTextContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><Text>Hello World</Text></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        assertEquals("Text", root.firstChild().tagName());
        assertEquals("Hello World", root.firstChild().text());
        assertEquals(1, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseXmlWithElementContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><Child><Text>Hello World</Text></Child></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        assertEquals("Child", root.firstChild().tagName());
        Element child = root.firstChild();
        assertEquals("Text", child.firstChild().tagName());
        assertEquals("Hello World", child.firstChild().text());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithMixedContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><Text>Hello</Text><Child></Child><Text>World</Text></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        assertEquals("Text", root.childNodes().get(0).tagName());
        assertEquals("Hello", root.childNodes().get(0).text());
        assertEquals("Child", root.childNodes().get(1).tagName());
        assertEquals(0, root.childNodes().get(1).childNodes().size());
        assertEquals("Text", root.childNodes().get(2).tagName());
        assertEquals("World", root.childNodes().get(2).text());
        assertEquals(3, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><img src=\"image.png\" alt=\"Image\" /></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        Element img = (Element) root.childNodes().get(0);
        assertEquals("img", img.tagName());
        assertEquals("image.png", img.attr("src"));
        assertEquals("Image", img.attr("alt"));
        assertEquals(0, img.childNodes().size());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><![CDATA[Hello World]]></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        CDataNode cdata = (CDataNode) root.childNodes().get(0);
        assertEquals("CDATA", cdata.tagName());
        assertEquals("Hello World", cdata.text());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><!-- This is a comment --></Root>", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        Element root = xmlDoc.body();
        Comment comment = (Comment) root.childNodes().get(0);
        assertEquals("--", comment.tagName());
        assertEquals("This is a comment", comment.text());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithDoctypeDeclaration() {
        Document xmlDoc = new XmlTreeBuilder().parse("<!DOCTYPE Root SYSTEM \"root.dtd\">", "http://example.com");
        assertEquals("Root", xmlDoc.tagName());
        DocumentType doctype = (DocumentType) xmlDoc.childNodes().get(0);
        assertEquals("Root", doctype.nodeName());
        assertEquals("root.dtd", doctype.systemId());
        assertEquals(1, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseXmlWithAttributes() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root <attr1=\"value1\" attr2='value2'/>></Root>", "http://example.com");
        Element root = xmlDoc.body();
        Map<String, Attribute> attrs = root.attributes();
        assertEquals(2, attrs.size());
        Attribute attr1 = attrs.get("attr1");
        assertEquals("value1", attr1.value());
        Attribute attr2 = attrs.get("attr2");
        assertEquals("value2", attr2.value());
    }

    @Test
    public void testParseXmlWithNestedElements() {
        Document xmlDoc = new XmlTreeBuilder().parse("<Root><Child><GrandChild><Text>Hello</Text></GrandChild></Child></Root>", "http://example.com");
        Element root = xmlDoc.body();
        Element child = root.firstChild();
        Element grandChild = child.firstChild();
        assertEquals("Text", grandChild.tagName());
        assertEquals("Hello", grandChild.text());
        assertEquals(1, child.childNodes().size());
        assertEquals(1, root.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        String input = "<Root><Child><Text>Hello</Text></Child></Root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(input, null, null, null);
        assertEquals(3, nodes.size());
        assertEquals("Root", nodes.get(0).tagName());
        Element root = (Element) nodes.get(0);
        assertEquals("Child", nodes.get(1).tagName());
        Element child = (Element) nodes.get(1);
        assertEquals("Text", nodes.get(2).tagName());
        TextNode text = (TextNode) nodes.get(2);
        assertEquals("Hello", text.text());
    }

    @Test
    public void testParseFragmentWithBaseUri() {
        String input = "<Root><Child href=\"child.xml\"><Text>Hello</Text></Child></Root>";
        String baseUri = "http://example.com/root";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(input, null, baseUri, null);
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("http://example.com/child.xml", child.attr("href"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("Root");
        Element child = new Element("Child");
        Element grandChild = new Element("GrandChild");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child);
        xmlTreeBuilder.stack.add(grandChild);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("GrandChild"));
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(child, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testPopStackToCloseNotFound() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("Root");
        Element child = new Element("Child");
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("UnknownTag"));
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(child, xmlTreeBuilder.stack.get(1));
    }
}