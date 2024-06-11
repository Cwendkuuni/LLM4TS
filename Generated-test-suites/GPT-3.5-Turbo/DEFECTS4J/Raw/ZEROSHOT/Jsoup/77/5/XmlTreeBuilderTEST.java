package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseStringString() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><element>text</element></root>";
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertEquals(baseUri, xmlTreeBuilder.baseUri);
        Assert.assertNotNull(xmlTreeBuilder.stack);
        Assert.assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.attributes().put("attr", "value");
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
        Assert.assertEquals("element", element.nodeName());
        Assert.assertTrue(element.hasAttr("attr"));
        Assert.assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertNotNull(childNodes);
        Assert.assertFalse(childNodes.isEmpty());
        Assert.assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        Assert.assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacterText() {
        Token.Character token = new Token.Character("text");
        xmlTreeBuilder.insert(token);
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertNotNull(childNodes);
        Assert.assertFalse(childNodes.isEmpty());
        Assert.assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        Assert.assertEquals("text", textNode.getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertNotNull(childNodes);
        Assert.assertFalse(childNodes.isEmpty());
        Assert.assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        Assert.assertEquals("html", documentType.getName());
        Assert.assertEquals("", documentType.getPublicIdentifier());
        Assert.assertEquals("", documentType.getSystemIdentifier());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag1 = new Token.StartTag("root");
        Token.StartTag startTag2 = new Token.StartTag("element");
        Token.EndTag endTag2 = new Token.EndTag("element");
        Token.EndTag endTag1 = new Token.EndTag("root");
        
        xmlTreeBuilder.insert(startTag1);
        Element element = xmlTreeBuilder.insert(startTag2);
        xmlTreeBuilder.insert(endTag2);
        xmlTreeBuilder.popStackToClose(endTag1);
        
        Assert.assertTrue(xmlTreeBuilder.stack.isEmpty());
        Assert.assertFalse(element.parentNode() instanceof Element);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>text</element>";
        String baseUri = "http://example.com";
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Assert.assertNotNull(childNodes);
        Assert.assertFalse(childNodes.isEmpty());
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Element);
        Element element = (Element) childNodes.get(0);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
        Assert.assertEquals("element", element.nodeName());
        Assert.assertFalse(element.hasAttr("attr"));
    }

}