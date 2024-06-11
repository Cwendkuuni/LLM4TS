package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.select.Elements;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitializeParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(Document.class, xmlTreeBuilder.currentElement().getClass());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertTrue(xmlTreeBuilder.process(new Token.StartTag("div")));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element elem = new Element("div");
        xmlTreeBuilder.insertNode(elem);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element elem = xmlTreeBuilder.insert(startTag);
        assertEquals("div", elem.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(TextNode.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("text");
        xmlTreeBuilder.insert(character);
        assertEquals(TextNode.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals(Doctype.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("span"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(new Token.EndTag("div"));
        assertEquals(2, xmlTreeBuilder.stack.size());
    }
}