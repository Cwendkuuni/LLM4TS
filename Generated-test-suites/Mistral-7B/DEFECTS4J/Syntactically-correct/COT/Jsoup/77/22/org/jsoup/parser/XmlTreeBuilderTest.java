package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.parser.Parser;

import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertTrue(parseSettings.isPreserveCase());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xml = "<xml><tag>Test</tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        assertNotNull(document);
        Element rootElement = document.rootElement();
        assertTrue(rootElement.tagName().equals("xml"));
        assertTrue(rootElement.child(0).tagName().equals("tag"));
        assertTrue(rootElement.child(0).text().equals("Test"));
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xml = "<xml><tag>Test</tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "baseUri");
        assertNotNull(document);
        Element rootElement = document.rootElement();
        assertTrue(rootElement.tagName().equals("xml"));
        assertTrue(rootElement.child(0).tagName().equals("tag"));
        assertTrue(rootElement.child(0).text().equals("Test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessUnknownTagType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(1, "UnknownTag", Token.TokenType.OTHER);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertElement() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag(1, "tag", null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.tagName().equals("tag"));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().child(0);
        assertTrue(comment.data().equals("This is a comment"));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character('A');
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().child(0);
        assertTrue(textNode.text().equals("A"));
    }

    @Test
    public void testInsertDocumentType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype documentTypeToken = new Token.Doctype("doctype", null, null, null, null);
        xmlTreeBuilder.insert(documentTypeToken);
        DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.currentElement().child(0);
        assertTrue(documentTypeNode.tagName().equals("doctype"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element tag1 = new Element("tag1");
        Element tag2 = new Element("tag2");
        xmlTreeBuilder.stack.add(tag1);
        xmlTreeBuilder.stack.add(tag2);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
        assertTrue(xmlTreeBuilder.stack.size() == 1);
        assertTrue(xmlTreeBuilder.stack.get(0).equals(tag2));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<tag1><tag2>Test</tag2></tag1>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element tag1 = (Element) nodes.get(0);
        Element tag2 = (Element) tag1.child(0);
        assertTrue(tag1.tagName().equals("tag1"));
        assertTrue(tag2.tagName().equals("tag2"));
        assertTrue(tag2.text().equals("Test"));
    }
}