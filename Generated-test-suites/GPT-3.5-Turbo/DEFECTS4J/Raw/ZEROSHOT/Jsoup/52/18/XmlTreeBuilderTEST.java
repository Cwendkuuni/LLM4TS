package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag1>text</tag1></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "xml");
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag1");
        xmlTreeBuilder.process(startTag);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "tag1");

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag1");
        xmlTreeBuilder.process(endTag);
        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag1"), "http://example.com");
        xmlTreeBuilder.insertNode(element);
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), element);
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag1");
        startTag.attributes(new Attributes());
        xmlTreeBuilder.insert(startTag);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "tag1");
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        commentToken.setData("comment data");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), new Comment("comment data", "http://example.com"));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        characterToken.setData("character data");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), new TextNode("character data", "http://example.com"));
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("doctype");
        doctype.setPublicIdentifier("public identifier");
        doctype.setSystemIdentifier("system identifier");
        xmlTreeBuilder.insert(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), new DocumentType("doctype", "public identifier", "system identifier", "http://example.com"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag1 = new Token.EndTag();
        endTag1.name("tag1");
        Token.EndTag endTag2 = new Token.EndTag();
        endTag2.name("tag2");
        Element element1 = new Element(Tag.valueOf("tag1"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("tag2"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("tag3"), "http://example.com");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        xmlTreeBuilder.popStackToClose(endTag2);
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.get(0), element1);
        assertEquals(xmlTreeBuilder.stack.get(1), element2);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag1>text</tag1>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals(element.nodeName(), "tag1");
    }
}