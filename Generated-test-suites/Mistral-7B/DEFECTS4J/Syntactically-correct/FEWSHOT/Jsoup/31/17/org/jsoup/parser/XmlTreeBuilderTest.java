package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", null, new Attribute("attr", "value"));
        Element expectedElement = new Element("tag", "", new Attribute("attr", "value"));

        xmlTreeBuilder.insert(startTag);
        Element actualElement = xmlTreeBuilder.currentElement();

        assertEquals(expectedElement.nodeName(), actualElement.nodeName());
        assertEquals(expectedElement.baseUri(), actualElement.baseUri());
        assertEquals(expectedElement.attribute(0).getName(), actualElement.attribute(0).getName());
        assertEquals(expectedElement.attribute(0).getValue(), actualElement.attribute(0).getValue());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element startElement = new Element("tag", "", new Attribute("attr", "value"));
        xmlTreeBuilder.insertNode(startElement);
        xmlTreeBuilder.stack.push(startElement);
        Token endTag = new Token.EndTag("tag");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(xmlTreeBuilder.stack.size(), 0);
        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        Comment expectedComment = new Comment("This is a comment", "");
        Comment actualComment = (Comment) xmlTreeBuilder.lastSibling();

        assertEquals(expectedComment.data(), actualComment.data());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');

        xmlTreeBuilder.insert(characterToken);

        Text expectedText = new TextNode("A", "");
        Text actualText = (Text) xmlTreeBuilder.lastSibling();

        assertEquals(expectedText.text(), actualText.text());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "", "", "");

        xmlTreeBuilder.insert(doctypeToken);

        DocumentType expectedDoctype = new DocumentType("html", "", "", "");
        DocumentType actualDoctype = (DocumentType) xmlTreeBuilder.lastSibling();

        assertEquals(expectedDoctype.name(), actualDoctype.name());
        assertEquals(expectedDoctype.publicIdentifier(), actualDoctype.publicIdentifier());
        assertEquals(expectedDoctype.systemIdentifier(), actualDoctype.systemIdentifier());
        assertEquals(expectedDoctype.baseUri(), actualDoctype.baseUri());
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("unknown", null, new Attribute("unknown", "unknown"));

        xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.currentElement();
        assertTrue(actualElement.isSelfClosing());
        assertEquals("unknown", actualElement.nodeName());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("selfClosing", null, null);
        startTag.setSelfClosing(true);

        xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.currentElement();
        assertTrue(actualElement.isSelfClosing());
        assertEquals("selfClosing", actualElement.nodeName());
    }

    @Test
    public void testProcessTokenWithEmptyAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", null);

        xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.currentElement();
        assertTrue(actualElement.attributes().isEmpty());
        assertEquals("tag", actualElement.nodeName());
    }

    @Test
    public void testProcessTokenWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("tag", null, new Attribute("attr1", "value1"), new Attribute("attr2", "value2"));

        xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.currentElement();
        assertEquals(2, actualElement.attributeSize());
        assertEquals("attr1", actualElement.attribute(0).getName());
        assertEquals("value1", actualElement.attribute(0).getValue());
        assertEquals("attr2", actualElement.attribute(1).getName());
        assertEquals("value2", actualElement.attribute(1).getValue());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("tag1", "", new Attribute("attr1", "value1"));
        xmlTreeBuilder.insertNode(element1);
        xmlTreeBuilder.stack.push(element1);

        Element element2 = new Element("tag2", "", new Attribute("attr2", "value2"));
        xmlTreeBuilder.insertNode(element2);
        xmlTreeBuilder.stack.push(element2);

        Element element3 = new Element("tag3", "", new Attribute("attr3", "value3"));
        xmlTreeBuilder.insertNode(element3);
        xmlTreeBuilder.stack.push(element3);

        Token endTag = new Token.EndTag("tag1");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals("tag2", xmlTreeBuilder.stack.peek().nodeName());
        assertEquals("tag3", xmlTreeBuilder.document().nodeName());
    }
}