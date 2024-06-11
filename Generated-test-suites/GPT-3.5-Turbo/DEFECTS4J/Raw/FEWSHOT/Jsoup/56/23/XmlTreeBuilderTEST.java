
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParse() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(input, xmlTreeBuilder.getReader().getInput());
        Assert.assertEquals(baseUri, xmlTreeBuilder.getReader().getBaseUri());
        Assert.assertEquals(errors, xmlTreeBuilder.getErrors());
        Assert.assertEquals(settings, xmlTreeBuilder.getSettings());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new StartTag("div", "div", new Attributes());
        boolean result = xmlTreeBuilder.process(startTag);
        Assert.assertTrue(result);
        Element element = xmlTreeBuilder.getCurrentElement();
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("div"), "http://www.example.com");
        xmlTreeBuilder.getStack().add(element);

        Token.EndTag endTag = new EndTag("div", "div");
        boolean result = xmlTreeBuilder.process(endTag);

        Assert.assertTrue(result);
        Assert.assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("hello");
        boolean result = xmlTreeBuilder.process(character);

        Assert.assertTrue(result);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof TextNode);
        Assert.assertEquals("hello", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("this is a comment", "http://www.example.com");
        boolean result = xmlTreeBuilder.process(comment);

        Assert.assertTrue(result);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof Comment);
        Assert.assertEquals("this is a comment", ((Comment) node).getData());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.example.com");
        boolean result = xmlTreeBuilder.process(doctype);

        Assert.assertTrue(result);
        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof DocumentType);
        DocumentType documentType = (DocumentType) node;
        Assert.assertEquals("html", documentType.getName());
        Assert.assertEquals("", documentType.getPublicIdentifier());
        Assert.assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Document document = new Document("http://www.example.com");
        Element element = new Element("div", "http://www.example.com");
        document.appendChild(element);

        xmlTreeBuilder.getStack().add(document);
        
        Node node = new TextNode("text", "http://www.example.com");
        xmlTreeBuilder.insertNode(node);

        Assert.assertEquals(node, element.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new StartTag("div", "div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
        Assert.assertFalse(element.tag().isKnownTag());
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        Token.StartTag startTag = new StartTag("div", "div", new Attributes());
        startTag.selfClosing = true;
        xmlTreeBuilder.insert(startTag);

        Assert.assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("this is a comment", "http://www.example.com");
        xmlTreeBuilder.insert(comment);

        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof Comment);
        Assert.assertEquals("this is a comment", ((Comment) node).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("hello");
        xmlTreeBuilder.insert(character);

        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof TextNode);
        Assert.assertEquals("hello", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.example.com");
        xmlTreeBuilder.insert(doctype);

        Node node = xmlTreeBuilder.getCurrentElement().childNode(0);
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof DocumentType);
        DocumentType documentType = (DocumentType) node;
        Assert.assertEquals("html", documentType.getName());
        Assert.assertEquals("", documentType.getPublicIdentifier());
        Assert.assertEquals("", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element element2 = new Element(Tag.valueOf("span"), "http://www.example.com");
        Element element3 = new Element(Tag.valueOf("div"), "http://www.example.com");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);
        xmlTreeBuilder.getStack().add(element3);

        Token.EndTag endTag = new EndTag("div", "div");
        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(1, xmlTreeBuilder.getStack().size());
        Assert.assertEquals(element1, xmlTreeBuilder.getStack().get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>fragment</div>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        Node node = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings).get(0);

        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof Element);
        Element element = (Element) node;
        Assert.assertEquals("div", element.nodeName());
        Assert.assertEquals("fragment", element.text());
    }
}
