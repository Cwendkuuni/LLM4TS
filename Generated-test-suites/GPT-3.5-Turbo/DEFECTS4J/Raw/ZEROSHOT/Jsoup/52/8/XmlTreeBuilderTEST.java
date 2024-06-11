package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><test></test></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Assert.assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("xml", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", null, null);
        xmlTreeBuilder.process(startTag);
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals("test", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.push(new Element("test"));
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.process(endTag);
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
        Assert.assertEquals("xml", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.process(comment);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("<!--test comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.process(character);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("test character", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.process(doctype);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element("test");
        xmlTreeBuilder.insertNode(element);
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", null, null);
        xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals("test", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.insert(comment);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("<!--test comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.insert(character);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("test character", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.insert(doctype);
        Assert.assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.push(new Element("test"));
        xmlTreeBuilder.stack.push(new Element("test"));
        xmlTreeBuilder.stack.push(new Element("test2"));
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.popStackToClose(endTag);
        Assert.assertEquals("test2", xmlTreeBuilder.currentElement().nodeName());
        Assert.assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<test>fragment</test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        Assert.assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        Assert.assertEquals("<test>fragment</test>", xmlTreeBuilder.doc.childNodes().get(0).toString());
    }
}