package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(parseSettings);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root>Hello World</root>"), "https://www.example.com");

        assertNotNull(document);
        assertEquals("root", document.nodeName());
        assertEquals("Hello World", document.childNode(0).toString());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<root>Hello World</root>", "https://www.example.com");

        assertNotNull(document);
        assertEquals("root", document.nodeName());
        assertEquals("Hello World", document.childNode(0).toString());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "https://www.example.com", null, ParseSettings.htmlDefault);

        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", "div", null, false);
        xmlTreeBuilder.process(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", "div", null, false);
        xmlTreeBuilder.process(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("div", element.tagName());

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        Element elementAfter = xmlTreeBuilder.currentElement();
        assertNull(elementAfter);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Sample comment");
        xmlTreeBuilder.process(comment);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("<!--Sample comment-->", element.toString());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Character data");
        xmlTreeBuilder.process(character);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("Character data", element.childNode(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.process(doctype);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertTrue(element instanceof DocumentType);
        assertEquals("<!doctype html>", element.toString());
    }
  
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "https://www.example.com", ParseSettings.preserveCase.attributes);
        xmlTreeBuilder.stack.add(root);

        Element child = new Element(Tag.valueOf("child"), "https://www.example.com", ParseSettings.preserveCase.attributes);
        xmlTreeBuilder.insertNode(child);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("child", element.tagName());
        assertEquals(root, element.parent());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", "div", null, false);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Sample comment");
        xmlTreeBuilder.insert(comment);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("<!--Sample comment-->", element.toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Character data");
        xmlTreeBuilder.insert(character);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("Character data", element.childNode(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);

        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertTrue(element instanceof DocumentType);
        assertEquals("<!doctype html>", element.toString());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("div"), "https://www.example.com", ParseSettings.preserveCase.attributes);
        xmlTreeBuilder.stack.add(element1);
        Element element2 = new Element(Tag.valueOf("p"), "https://www.example.com", ParseSettings.preserveCase.attributes);
        xmlTreeBuilder.stack.add(element2);
        Element element3 = new Element(Tag.valueOf("div"), "https://www.example.com", ParseSettings.preserveCase.attributes);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        Element elementAfter = xmlTreeBuilder.currentElement();
        assertNotNull(elementAfter);
        assertEquals("p", elementAfter.tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div>Hello World</div>", "https://www.example.com", null, ParseSettings.htmlDefault);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).nodeName());
        assertEquals("Hello World", nodes.get(0).childNode(0).toString());
    }

}