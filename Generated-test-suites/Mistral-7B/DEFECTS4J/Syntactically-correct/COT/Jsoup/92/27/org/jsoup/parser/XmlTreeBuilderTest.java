package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Test cases for {@link XmlTreeBuilder} class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
        assertEquals(XmlTreeBuilder.class, xmlTreeBuilder.getClass());
        assertEquals(Document.class, xmlTreeBuilder.doc.getClass());
        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(Document.class, document.getClass());
        assertEquals(1, document.childNodes().size());
        assertEquals(XmlTreeBuilder.class, document.parser());
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(Document.class, document.getClass());
        assertEquals(1, document.childNodes().size());
        assertEquals(XmlTreeBuilder.class, document.parser());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Token.Type.StartTag, "<xml>", null, null, null, null, null, null, null, null);
        xmlTreeBuilder.process(token);
        assertTrue(xmlTreeBuilder.stack.size() > 0);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element node = new Element("xml");
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, xmlTreeBuilder.currentElement().lastChild());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("xml");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("xml", element.tagName());
        assertEquals(1, element.childNodes().size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("<!-- test comment -->");
        xmlTreeBuilder.insert(commentToken);
        assertTrue(xmlTreeBuilder.currentElement().lastChild() instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character(' ', " ");
        xmlTreeBuilder.insert(characterToken);
        assertTrue(xmlTreeBuilder.currentElement().lastChild() instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "public", "systemId", "publicId");
        xmlTreeBuilder.insert(doctypeToken);
        assertTrue(xmlTreeBuilder.currentElement().lastChild() instanceof DocumentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Integer.MIN_VALUE, "", null, null, null, null, null, null, null, null);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        stack.push(element1);
        stack.push(element2);
        xmlTreeBuilder.popStackToClose(new Token.EndTag("element1"));
        assertEquals(1, stack.size());
        assertEquals(element2, stack.peek());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<xml><element1 ><element2></element2></element1></xml>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, null, null);
        assertEquals(4, nodes.size());
    }
}