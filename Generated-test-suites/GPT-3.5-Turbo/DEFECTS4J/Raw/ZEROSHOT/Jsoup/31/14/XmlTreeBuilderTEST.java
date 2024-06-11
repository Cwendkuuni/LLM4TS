package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = Jsoup.parse("<html></html>");
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, document.select("div").first());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StartTag startTag = new StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment commentToken = new Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Document document = Jsoup.parse("<html><!-- This is a comment --></html>");
        assertEquals("This is a comment", document.select("html").first().childNodes().get(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Character characterToken = new Character("This is a character");
        xmlTreeBuilder.insert(characterToken);
        Document document = Jsoup.parse("<html>This is a character</html>");
        assertEquals("This is a character", document.select("html").first().childNodes().get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Doctype doctype = new Doctype("html" ,"pubid", "sysid");
        xmlTreeBuilder.insert(doctype);
        Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"pubid\" \"sysid\"><html></html>");
        assertEquals("html", document.childNodes().get(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StartTag startTag = new StartTag("div", new Attributes());
        xmlTreeBuilder.insert(startTag);
        EndTag endTag = new EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(null, xmlTreeBuilder.currentElement());
    }
}