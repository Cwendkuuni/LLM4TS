package org.jsoup.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse_withReaderAndBaseUri_shouldReturnDocumentObject() throws IOException {
        // Arrange
        String input = "<xml>Test XML</xml>";
        Reader reader = new StringReader(input);
        String baseUri = "https://example.com";

        // Act
        Document actualResult = xmlTreeBuilder.parse(reader, baseUri);

        // Assert
        Assert.assertEquals("xml", actualResult.tagName());
    }

    @Test
    public void testParse_withStringAndBaseUri_shouldReturnDocumentObject() {
        // Arrange
        String input = "<xml>Test XML</xml>";
        String baseUri = "https://example.com";

        // Act
        Document actualResult = xmlTreeBuilder.parse(input, baseUri);

        // Assert
        Assert.assertEquals("xml", actualResult.tagName());
    }

    @Test
    public void testInitialiseParse_withReaderBaseUriErrorsAndSettings_shouldInitializeDocumentAndOutputSettings() throws IOException {
        // Arrange
        String input = "<xml>Test XML</xml>";
        Reader reader = new StringReader(input);
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Act
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        // Assert
        Assert.assertEquals("xml", xmlTreeBuilder.currentElement().tagName());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_withStartTag_shouldInsertElementToStack() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("xml");

        // Act
        xmlTreeBuilder.process(startTag);

        // Assert
        Assert.assertEquals("xml", xmlTreeBuilder.currentElement().tagName());
    }

    @Test
    public void testProcess_withEndTag_shouldPopStackToCloseTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("xml");
        xmlTreeBuilder.process(startTag); // Push tag onto stack

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("xml");

        // Act
        xmlTreeBuilder.process(endTag);

        // Assert
        Assert.assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess_withComment_shouldInsertCommentNode() {
        // Arrange
        Token.Comment comment = new Token.Comment();
        comment.data("Test Comment");

        // Act
        xmlTreeBuilder.process(comment);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        Assert.assertEquals("Test Comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_withCharacter_shouldInsertTextNode() {
        // Arrange
        Token.Character character = new Token.Character();
        character.data("Test Character");

        // Act
        xmlTreeBuilder.process(character);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        Assert.assertEquals("Test Character", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_withDoctype_shouldInsertDoctypeNode() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("-//IETF//DTD HTML 2.0//EN");
        doctype.setSystemIdentifier("http://www.w3.org/MarkUp/dtd/xhtml1-transitional.dtd");

        // Act
        xmlTreeBuilder.process(doctype);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        Assert.assertEquals("html", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertNode_withElement_shouldAppendElementToCurrentElement() {
        // Arrange
        Element element = new Element(Tag.valueOf("div"), "");

        // Act
        xmlTreeBuilder.insertNode(element);

        // Assert
        Assert.assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsert_withStartTag_shouldInsertElementWithAttributesToCurrentElement() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div")
                .attributes(new Attributes()
                        .put("id", "test")
                        .put("class", "sample"));

        // Act
        xmlTreeBuilder.insert(startTag);

        // Assert
        Assert.assertEquals("div", xmlTreeBuilder.currentElement().tagName());
        Assert.assertEquals("test", xmlTreeBuilder.currentElement().attr("id"));
        Assert.assertEquals("sample", xmlTreeBuilder.currentElement().attr("class"));
    }

    @Test
    public void testInsert_withSelfClosingStartTagAndUnknownTag_shouldAddSelfClosingFlagToTheTag() {
        // Arrange
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("unknown")
                .selfClosing(true);

        // Act
        xmlTreeBuilder.insert(startTag);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().tag().isSelfClosing());
    }

    @Test
    public void testInsert_withCommentTokenAndBogusFlag_shouldInsertCommentWithAttributes() {
        // Arrange
        Token.Comment comment = new Token.Comment();
        comment.data("<!doctype html>");
        comment.bogus(true);

        // Act
        xmlTreeBuilder.insert(comment);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        Assert.assertEquals("<!doctype html>", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_withCharacterTokenAndIsCDataFlag_shouldInsertCDataNode() {
        // Arrange
        Token.Character character = new Token.Character();
        character.data("Test Character");
        character.isCData(true);

        // Act
        xmlTreeBuilder.insert(character);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof CDataNode);
        Assert.assertEquals("Test Character", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsert_withDoctypeToken_shouldInsertDoctypeNode() {
        // Arrange
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");

        // Act
        xmlTreeBuilder.insert(doctype);

        // Assert
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        Assert.assertEquals("html", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testPopStackToClose_withEndTag_shouldPopElementsFromStackToCloseTag() {
        // Arrange
        Element firstElement = new Element(Tag.valueOf("div"), "");
        firstElement.appendChild(new Element(Tag.valueOf("p"), ""));

        xmlTreeBuilder.insertNode(firstElement);
        xmlTreeBuilder.convert(currentElement -> {
            Assert.assertEquals("p", currentElement.tagName());
        });

        Token.StartTag startTag = new Token.StartTag();
        startTag.name("p");
        xmlTreeBuilder.insert(startTag); // Push another p element onto stack

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");

        // Act
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert
        Assert.assertEquals(firstElement, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testParseFragment_withInputFragmentBaseUriErrorsAndSettings_shouldReturnListofNodes() {
        // Arrange
        String inputFragment = "<div>Sample</div>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        // Act
        List<Node> actualResult = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Assert
        Assert.assertEquals(1, actualResult.size());
        Assert.assertTrue(actualResult.get(0) instanceof Element);
        Assert.assertEquals("div", actualResult.get(0).nodeName());
    }
}