package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("");
        String baseUri = "";
        Document expected = new Document("");
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "";
        String baseUri = "";
        Document expected = new Document("");
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("");
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Document doc = xmlTreeBuilder.getDoc();

        Assert.assertEquals(doc, xmlTreeBuilder.getStack().get(0));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("");
        xmlTreeBuilder.process(token);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.EndTag("");
        xmlTreeBuilder.process(token);

        Assert.assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("");
        xmlTreeBuilder.process(token);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("");
        xmlTreeBuilder.process(token);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Doctype("");
        xmlTreeBuilder.process(token);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node node = new Element("", "", new ParseSettings());
        xmlTreeBuilder.insertNode(node);

        Node expected = node;
        Node actual = xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("");
        xmlTreeBuilder.insert(startTag);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.currentElement() instanceof Element);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("");
        xmlTreeBuilder.insert(commentToken);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("");
        xmlTreeBuilder.insert(token);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype d = new Token.Doctype("");
        xmlTreeBuilder.insert(d);

        Assert.assertFalse(xmlTreeBuilder.getStack().isEmpty());
        Assert.assertTrue(xmlTreeBuilder.getStack().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("");
        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> expected = xmlTreeBuilder.doc.childNodes();
        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Assert.assertEquals(expected, actual);
    }
}