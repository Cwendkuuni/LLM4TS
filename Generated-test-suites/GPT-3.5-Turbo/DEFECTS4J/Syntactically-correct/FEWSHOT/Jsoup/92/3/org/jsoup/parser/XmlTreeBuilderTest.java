package org.jsoup.parser;

import static org.junit.Assert.*;
import java.io.StringReader;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<doc></doc>");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        builder.initialiseParse(input, baseUri, parser);
        Element doc = builder.stack.get(0);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testParseStringReaderStringParser() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<doc></doc>");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document doc = builder.parse(input, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<doc></doc>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        Document doc = builder.parse(input, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes("attr1", "value1");
        startTag.attributes("attr2", "value2");
        startTag.attributes("attr3", "value3");
        builder.process(startTag);
        
        Element element = builder.currentElement();
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));
        assertEquals("value3", element.attr("attr3"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        builder.currentElement().tagName("tag");
        builder.stack.add(builder.currentElement());
        builder.process(endTag);
        
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "comment data";
        builder.process(commentToken);
        
        Node node = builder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        assertEquals("comment data", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        characterToken.data = "character data";
        builder.process(characterToken);
        
        Node node = builder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        assertEquals("character data", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        doctype.setPublicIdentifier("public");
        doctype.setSystemIdentifier("system");
        doctype.setPubSysKey("pubSysKey");
        builder.process(doctype);
        
        Node node = builder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals("html", doctypeNode.getName());
        assertEquals("public", doctypeNode.getPublicIdentifier());
        assertEquals("system", doctypeNode.getSystemIdentifier());
        assertEquals("pubSysKey", doctypeNode.getPubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.push(new Element("tag1"));
        builder.stack.push(new Element("tag2"));
        builder.stack.push(new Element("tag1"));
        builder.stack.push(new Element("tag3"));
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag1";
        builder.popStackToClose(endTag);
        
        assertEquals(2, builder.stack.size());
        assertEquals("tag2", builder.stack.get(0).tagName());
        assertEquals("tag3", builder.stack.get(1).tagName());
    }

    @Test
    public void testParseFragmentStringStringParser() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testParseFragmentStringElementStringParser() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag></tag>";
        Element context = new Element("context");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
    }
}