package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("testElement");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("testTag");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(commentToken.getData(), xmlTreeBuilder.currentElement().child(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().child(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.example.com");
        xmlTreeBuilder.insert(doctype);
        assertEquals(doctype.getName(), ((DocumentType) xmlTreeBuilder.currentElement().child(0)).getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("testElement1");
        Element element2 = new Element("testElement2");
        Element element3 = new Element("testElement3");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);
        Token.EndTag endTag = new Token.EndTag("testElement2");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
        assertEquals(element3, xmlTreeBuilder.stack.get(1));
    }

}