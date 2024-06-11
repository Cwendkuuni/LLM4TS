package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        
        Assert.assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader testReader = new StringReader("<xml></xml>");
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        xmlTreeBuilder.initialiseParse(testReader, baseUri, parser);

        Document actualDocument = xmlTreeBuilder.getDocument();
        Element actualRootElement = actualDocument.child(0);

        Assert.assertEquals("xml", actualRootElement.tagName());
    }

    @Test
    public void testParseStringReaderString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        StringReader testReader = new StringReader("<xml></xml>");
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        Document actualDocument = xmlTreeBuilder.parse(testReader, baseUri);

        Element actualRootElement = actualDocument.child(0);

        Assert.assertEquals("xml", actualRootElement.tagName());
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);

        Element actualRootElement = actualDocument.child(0);

        Assert.assertEquals("xml", actualRootElement.tagName());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        startTag.attributes.put("attribute", "value");

        xmlTreeBuilder.process(startTag);

        Document actualDocument = xmlTreeBuilder.getDocument();
        Element actualRootElement = actualDocument.child(0);

        Assert.assertEquals("xml", actualRootElement.tagName());
        Assert.assertEquals("value", actualRootElement.attr("attribute"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document testDocument = new Document("");
        Element testRootElement = new Element(Tag.valueOf("xml"), "");
        testDocument.appendChild(testRootElement);
        xmlTreeBuilder.setDocument(testDocument);

        Token.EndTag endTag = new Token.EndTag("xml");

        xmlTreeBuilder.process(endTag);

        List<Node> actualChildNodes = testDocument.childNodes();

        Assert.assertTrue(actualChildNodes.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test Comment");

        xmlTreeBuilder.process(commentToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        Comment actualComment = (Comment) actualDocument.childNode(0);

        Assert.assertEquals("Test Comment", actualComment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test Character");

        xmlTreeBuilder.process(characterToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        TextNode actualTextNode = (TextNode) actualDocument.childNode(0);

        Assert.assertEquals("Test Character", actualTextNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");

        xmlTreeBuilder.process(doctypeToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        DocumentType actualDocumentType = actualDocument.childNode(0).asDocumentType();

        Assert.assertEquals("html", actualDocumentType.getName());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        startTag.attributes.put("attribute", "value");

        Element actualElement = xmlTreeBuilder.insert(startTag);

        Assert.assertEquals("xml", actualElement.tagName());
        Assert.assertEquals("value", actualElement.attr("attribute"));
        Assert.assertEquals("http://www.example.com", actualElement.baseUri());
    }

    @Test
    public void testInsertCommentToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test Comment");

        xmlTreeBuilder.insert(commentToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        Comment actualComment = (Comment) actualDocument.childNode(0);

        Assert.assertEquals("Test Comment", actualComment.getData());
    }

    @Test
    public void testInsertCharacterToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test Character");

        xmlTreeBuilder.insert(characterToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        TextNode actualTextNode = (TextNode) actualDocument.childNode(0);

        Assert.assertEquals("Test Character", actualTextNode.getWholeText());
    }

    @Test
    public void testInsertDoctypeToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");

        xmlTreeBuilder.insert(doctypeToken);

        Document actualDocument = xmlTreeBuilder.getDocument();
        DocumentType actualDocumentType = actualDocument.childNode(0).asDocumentType();

        Assert.assertEquals("html", actualDocumentType.getName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document testDocument = new Document("");
        Element testRootElement = new Element(Tag.valueOf("html"), "");
        Element testChildElement = new Element(Tag.valueOf("body"), "");

        testRootElement.appendChild(testChildElement);
        testDocument.appendChild(testRootElement);
        xmlTreeBuilder.setDocument(testDocument);

        Token.EndTag endTag = new Token.EndTag("html");

        xmlTreeBuilder.popStackToClose(endTag);

        List<Node> actualChildNodes = testDocument.childNodes();
        Assert.assertFalse(actualChildNodes.isEmpty());
        Assert.assertEquals(testChildElement, actualChildNodes.get(0));

        List<Element> actualStack = xmlTreeBuilder.getStack();
        Assert.assertTrue(actualStack.isEmpty());
    }

    @Test
    public void testParseFragmentStringStringParser() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<p>Test Fragment</p>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        Assert.assertEquals(1, actualNodes.size());
        Assert.assertEquals("p", actualNodes.get(0).nodeName());
        Assert.assertEquals("Test Fragment", actualNodes.get(0).childNode(0).outerHtml());
    }

    @Test
    public void testParseFragmentStringElementStringParser() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element context = new Element(Tag.valueOf("div"), "");
        String inputFragment = "<p>Test Fragment</p>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        Assert.assertEquals(1, actualNodes.size());
        Assert.assertEquals("p", actualNodes.get(0).nodeName());
        Assert.assertEquals("Test Fragment", actualNodes.get(0).childNode(0).outerHtml());
    }
}