package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * XmlTreeBuilderTest class for testing all the methods in XmlTreeBuilder class
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testInitialiseParse() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testParseEmptyDocument() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(""), "");
        assertTrue(document.body() == null);
    }

    @Test
    public void testParseDocumentWithRootElement() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader("<root></root>"), "");
        Element rootElement = document.rootElement();
        assertEquals("root", rootElement.nodeName());
        assertTrue(rootElement.children().isEmpty());
    }

    @Test
    public void testProcessStartTag() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null, false, false);
        Element tagElement = xmlTreeBuilder.insert(startTagToken);
        Document document = xmlTreeBuilder.doc;
        Element rootElement = document.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertEquals(tagElement, children.get(0));
    }

    @Test
    public void testProcessSelfClosingTag() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null, true, false);
        Element tagElement = xmlTreeBuilder.insert(startTagToken);
        Document document = xmlTreeBuilder.doc;
        Element rootElement = document.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertEquals(tagElement, children.get(0));
        assertTrue(tagElement.tagName().equals("tag"));
        assertTrue(tagElement.children().isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token unexpectedToken = new Token(5, "test");
        xmlTreeBuilder.process(unexpectedToken);
    }

    @Test
    public void testInsertNode() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node nodeToInsert = new TextNode("test");
        xmlTreeBuilder.insertNode(nodeToInsert);
        Element rootElement = xmlTreeBuilder.doc.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertEquals(nodeToInsert, children.get(0));
    }

    @Test
    public void testInsertComment() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String commentData = "test comment";
        Token commentToken = new Token.Comment(commentData);
        xmlTreeBuilder.insert(commentToken);
        Element rootElement = xmlTreeBuilder.doc.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof Comment);
        assertEquals(commentData, ((Comment) children.get(0)).data());
    }

    @Test
    public void testInsertCharacter() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String characterData = "test character";
        Token characterToken = new Token.Character(characterData);
        xmlTreeBuilder.insert(characterToken);
        Element rootElement = xmlTreeBuilder.doc.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof TextNode);
        assertEquals(characterData, ((TextNode) children.get(0)).text());
    }

    @Test
    public void testInsertDoctype() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String doctypeName = "doctype";
        String publicId = "public id";
        String systemId = "system id";
        Token doctypeToken = new Token.Doctype(doctypeName, publicId, systemId);
        xmlTreeBuilder.insert(doctypeToken);
        Element rootElement = xmlTreeBuilder.doc.rootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) children.get(0);
        assertEquals(doctypeName, documentType.nodeName());
        assertEquals(publicId, documentType.publicId());
        assertEquals(systemId, documentType.systemId());
    }

    @Test
    public void testPopStackToClose() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);

        Token endTagToken = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTagToken);

        List<Node> parentChildren = parentElement.children();
        assertEquals(1, parentChildren.size());
        assertEquals(childElement, parentChildren.get(0));
    }

    @Test
    public void testParseFragment() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child></child></root>";
        Document document = xmlTreeBuilder.parseFragment(inputFragment, "", "");
        assertEquals(1, document.childNodes().size());
        assertEquals("root", document.rootElement().nodeName());

        Element rootElement = document.rootElement();
        List<Node> rootChildren = rootElement.children();
        assertEquals(1, rootChildren.size());
        Element childElement = (Element) rootChildren.get(0);
        assertEquals(1, childElement.childNodes().size());
        assertTrue(childElement.children().get(0) instanceof TextNode);
    }

    @Test
    public void testParseFragmentWithContext() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child></child></root>";
        Element contextElement = new Element("context");
        Document document = xmlTreeBuilder.parseFragment(inputFragment, contextElement, "", "");
        assertEquals(1, document.childNodes().size());
        assertEquals("root", document.rootElement().nodeName());

        Element rootElement = document.rootElement();
        List<Node> rootChildren = rootElement.children();
        assertEquals(1, rootChildren.size());
        Element childElement = (Element) rootChildren.get(0);
        assertEquals(1, childElement.childNodes().size());
        assertTrue(childElement.children().get(0) instanceof TextNode);

        Element contextRootElement = contextElement.child();
        assertEquals(1, contextRootElement.children().size());
        assertEquals(document, contextRootElement.child(0));
    }

    @Test(expected = AssertionError.class)
    public void testParseFragmentWithNullInputFragment() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(null, "", "");
    }

    @Test(expected = AssertionError.class)
    public void testParseFragmentWithEmptyInputFragment() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment("", "", "");
    }

    @Test
    public void testParseFragmentWithEmptyDocument() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parseFragment("", "", "");
        assertTrue(document.body() == null);
    }

    @Test
    public void testParseFragmentWithRootElementAndChildren() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child><grandchild></grandchild></child></root>";
        Document document = xmlTreeBuilder.parseFragment(inputFragment, "", "");

        Element rootElement = document.rootElement();
        List<Node> rootChildren = rootElement.children();
        assertEquals(1, rootChildren.size());
        Element childElement = (Element) rootChildren.get(0);
        assertEquals(1, childElement.children().size());
        Element grandchildElement = (Element) childElement.children().get(0);
        assertEquals(1, grandchildElement.childNodes().size());
        assertTrue(grandchildElement.children().get(0) instanceof TextNode);
    }

    @Test
    public void testParseFragmentWithAttributes() throws Exception {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root attr1='value1' attr2='value2'><child attr3='value3'></child></root>";
        Document document = xmlTreeBuilder.parseFragment(inputFragment, "", "");

        Element rootElement = document.rootElement();
        List<Node> rootAttributes = rootElement.attributes();
        assertEquals(2, rootAttributes.size());
        Attribute attr1 = rootAttributes.get(0);
        assertEquals("attr1", attr1.getAttributeName());
        assertEquals("value1", attr1.attr());

        Attribute attr2 = rootAttributes.get(1);
        assertEquals("attr2", attr2.getAttributeName());
        assertEquals("value2", attr2.attr());

        Element childElement = (Element) rootElement.children().get(0);
        List<Node> childAttributes = childElement.attributes();
        assertEquals(1, childAttributes.size());
        Attribute attr3 = childAttributes.get(0);
        assertEquals("attr3", attr3.getAttributeName());
        assertEquals("value3", attr3.attr());
    }
}