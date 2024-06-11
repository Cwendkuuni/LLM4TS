package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "https://www.example.com";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        // Verify that default settings are Preserve Case
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderAndBaseUri() {
        String input = "<xml><tag1>text1</tag1><tag2>text2</tag2></xml>";
        Reader reader = new StringReader(input);
        Document doc = xmlTreeBuilder.parse(reader, baseUri);

        // Verify that the parsed document is not null
        Assert.assertNotNull(doc);

        // Verify that the parsed document contains the correct base URI
        Assert.assertEquals(baseUri, doc.baseUri());

        // Verify that the parsed document contains the correct number of child nodes
        Assert.assertEquals(1, doc.childNodes().size());

        // Verify that the parsed document contains the correct tags and text nodes
        Element root = doc.child(0);
        Assert.assertEquals("xml", root.tagName());
        Assert.assertEquals("text1", root.child(0).text());
        Assert.assertEquals("text2", root.child(1).text());
    }

    @Test
    public void testParseStringAndBaseUri() {
        String input = "<xml><tag1>text1</tag1><tag2>text2</tag2></xml>";
        Document doc = xmlTreeBuilder.parse(input, baseUri);

        // Verify that the parsed document is not null
        Assert.assertNotNull(doc);

        // Verify that the parsed document contains the correct base URI
        Assert.assertEquals(baseUri, doc.baseUri());

        // Verify that the parsed document contains the correct number of child nodes
        Assert.assertEquals(1, doc.childNodes().size());

        // Verify that the parsed document contains the correct tags and text nodes
        Element root = doc.child(0);
        Assert.assertEquals("xml", root.tagName());
        Assert.assertEquals("text1", root.child(0).text());
        Assert.assertEquals("text2", root.child(1).text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1>text1</tag1><tag2>text2</tag2></xml>";
        Reader reader = new StringReader(input);
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        // Verify that the document is added to the stack during initialization
        List<Node> stack = xmlTreeBuilder.stack;
        Assert.assertEquals(1, stack.size());
        Assert.assertTrue(stack.get(0) instanceof Document);

        // Verify that the document's output settings are set to XML syntax
        Document doc = (Document) stack.get(0);
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", null, true);
        Element element = xmlTreeBuilder.insert(startTag);

        // Verify that the returned element is not null
        Assert.assertNotNull(element);

        // Verify that the element's tag name is correct
        Assert.assertEquals("tag", element.tagName());

        // Verify that the element's base URI is correct
        Assert.assertEquals(baseUri, element.baseUri());

        // Verify that the element's attributes are normalized
        Assert.assertNull(element.attributes().asList());

        // Verify that the element is added to the current stack
        List<Node> stack = xmlTreeBuilder.stack;
        Assert.assertEquals(2, stack.size());
        Assert.assertEquals(element, stack.get(stack.size() - 1));
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        // Verify that the comment node is added to the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        Assert.assertNotNull(currentElement);
        Assert.assertEquals(1, currentElement.childNodes().size());
        Assert.assertTrue(currentElement.childNode(0) instanceof Comment);

        // Verify that the comment node contains the correct data
        Comment commentNode = (Comment) currentElement.childNode(0);
        Assert.assertEquals("This is a comment", commentNode.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is some text");
        xmlTreeBuilder.insert(characterToken);

        // Verify that the text node is added to the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        Assert.assertNotNull(currentElement);
        Assert.assertEquals(1, currentElement.childNodes().size());
        Assert.assertTrue(currentElement.childNode(0) instanceof TextNode);

        // Verify that the text node contains the correct data
        TextNode textNode = (TextNode) currentElement.childNode(0);
        Assert.assertEquals("This is some text", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        // Verify that the doctype node is added to the current element
        Element currentElement = xmlTreeBuilder.currentElement();
        Assert.assertNotNull(currentElement);
        Assert.assertEquals(1, currentElement.childNodes().size());
        Assert.assertTrue(currentElement.childNode(0) instanceof DocumentType);

        // Verify that the doctype node contains the correct data
        DocumentType doctypeNode = (DocumentType) currentElement.childNode(0);
        Assert.assertEquals("html", doctypeNode.name());
        Assert.assertEquals("", doctypeNode.getPublicId());
        Assert.assertEquals("", doctypeNode.getSystemId());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        Element element3 = new Element("tag3");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("tag2");
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that elements after the closed tag are removed from the stack
        List<Node> stack = xmlTreeBuilder.stack;
        Assert.assertEquals(1, stack.size());
        Assert.assertEquals(element1, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag1>text1</tag1><tag2>text2</tag2>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Verify that the parsed fragment is not null
        Assert.assertNotNull(childNodes);

        // Verify that the parsed fragment contains the correct number of child nodes
        Assert.assertEquals(2, childNodes.size());

        // Verify that the parsed fragment contains the correct tags and text nodes
        Assert.assertEquals("tag1", childNodes.get(0).nodeName());
        Assert.assertEquals("text1", childNodes.get(0).childNode(0).toString());
        Assert.assertEquals("tag2", childNodes.get(1).nodeName());
        Assert.assertEquals("text2", childNodes.get(1).childNode(0).toString());
    }
}