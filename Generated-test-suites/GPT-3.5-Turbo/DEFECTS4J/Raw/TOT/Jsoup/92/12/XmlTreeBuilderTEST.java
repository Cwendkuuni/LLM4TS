package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String xml;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xml = "<root>\n" +
                "    <element attribute=\"value\">\n" +
                "        <child>text</child>\n" +
                "    </element>\n" +
                "</root>";
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(new StringReader(xml), "http://example.com", new Parser(xmlTreeBuilder));
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParse_Reader() {
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");
        assertNotNull(document);
        // verify the parsed elements
        assertEquals("root", document.nodeName());
        assertEquals(1, document.getAllElements().size());
        assertEquals("element", document.getAllElements().get(0).nodeName());
        assertEquals(1, document.getAllElements().get(0).children().size());
        assertEquals("child", document.getAllElements().get(0).children().get(0).nodeName());
        assertEquals("text", document.getAllElements().get(0).children().get(0).text());
    }

    @Test
    public void testParse_String() {
        Document document = xmlTreeBuilder.parse(xml, "http://example.com");
        assertNotNull(document);
        // verify the parsed elements
        assertEquals("root", document.nodeName());
        assertEquals(1, document.getAllElements().size());
        assertEquals("element", document.getAllElements().get(0).nodeName());
        assertEquals(1, document.getAllElements().get(0).children().size());
        assertEquals("child", document.getAllElements().get(0).children().get(0).nodeName());
        assertEquals("text", document.getAllElements().get(0).children().get(0).text());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("element");
        startTag.attributes().put("attribute", "value");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attribute"));
    }

    @Test
    public void testProcess_EndTag() {
        // Insert a start tag first
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("element");
        startTag.attributes().put("attribute", "value");
        xmlTreeBuilder.insert(startTag);

        // Now insert the corresponding end tag
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "element";
        xmlTreeBuilder.popStackToClose(endTag);

        Element element = xmlTreeBuilder.currentElement();
        assertNull(element); // the current element should be null after the end tag is processed
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment();
        comment.setData("This is a comment");

        xmlTreeBuilder.insert(comment);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(node);
        assertEquals("#comment", node.nodeName());
        assertEquals("This is a comment", node.outerHtml());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character();
        character.setData("text");

        xmlTreeBuilder.insert(character);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(node);
        assertEquals("#text", node.nodeName());
        assertEquals("text", node.outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("");
        doctype.setSystemIdentifier("");

        xmlTreeBuilder.insert(doctype);

        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(node);
        assertEquals("!doctype", node.nodeName());
        assertEquals("<!DOCTYPE html>", node.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        // Insert a start tag first
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("element");
        startTag.attributes().put("attribute", "value");
        xmlTreeBuilder.insert(startTag);

        // Insert another start tag
        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.name("element");
        startTag2.attributes().put("attribute", "value");
        xmlTreeBuilder.insert(startTag2);

        // Now insert the corresponding end tag
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "element";
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size()); // the stack should have only one element now
    }

    @Test
    public void testParseFragment_String() {
      String inputFragment = "<child>text</child>";
      List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", new Parser(xmlTreeBuilder));
      assertNotNull(nodes);
      assertEquals(1, nodes.size());
      assertEquals("child", nodes.get(0).nodeName());
      assertEquals("text", nodes.get(0).text());
    }

    @Test
    public void testParseFragment_Element() {
      String inputFragment = "<child>text</child>";
      Element parent = new Element(Tag.valueOf("element"), "http://example.com", ParseSettings.preserveCase);
      List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, parent, "http://example.com", new Parser(xmlTreeBuilder));
      assertNotNull(nodes);
      assertEquals(1, nodes.size());
      assertEquals("child", nodes.get(0).nodeName());
      assertEquals("text", nodes.get(0).text());
    }

}