
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
    private XmlTreeBuilder builder;
  
    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://www.example.com";
        Reader reader = new StringReader(input);
        
        Document doc = builder.parse(reader, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("root", doc.selectFirst("root").nodeName());
        assertEquals("child", doc.selectFirst("child").nodeName());
        assertEquals("Test", doc.selectFirst("child").text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://www.example.com";
        
        Document doc = builder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("root", doc.selectFirst("root").nodeName());
        assertEquals("child", doc.selectFirst("child").nodeName());
        assertEquals("Test", doc.selectFirst("child").text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child>Test</child></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        Reader reader = new StringReader(input);
        
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        assertEquals("http://www.example.com", builder.baseUri);
        assertEquals(errors, builder.errors);
        
        Document doc = builder.doc;
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("root", doc.selectFirst("root").nodeName());
        assertEquals("child", doc.selectFirst("child").nodeName());
        assertEquals("Test", doc.selectFirst("child").text());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("root");
        startTag.attributes.put("attr1", "value1");
        
        Element el = builder.insert(startTag);
        
        assertNotNull(el);
        assertEquals("root", el.nodeName());
        assertEquals("value1", el.attr("attr1"));
    }

    @Test
    public void testProcessEndTag() {
        Token.StartTag startTag = new Token.StartTag("root");
        builder.insert(startTag);
        
        Token.EndTag endTag = new Token.EndTag("root");
        builder.process(endTag);
        
        assertNotNull(builder.stack);
        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");
        
        builder.process(comment);
        
        Document doc = builder.doc;
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof Comment);
        assertEquals("Test comment", ((Comment) node).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test character");
        
        builder.process(character);
        
        Document doc = builder.doc;
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        
        builder.process(doctype);
        
        Document doc = builder.doc;
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        
        Node node = doc.childNodes().get(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("html", ((DocumentType) node).getName());
    }

    @Test
    public void testInsertNode() {
        Document doc = new Document("");
        Element root = doc.appendElement("root");
        builder.stack.add(root);
        
        Token.Character character = new Token.Character("Test character");
        
        builder.insertNode(character);
        
        assertEquals(1, root.childNodes().size());
        
        Node node = root.childNodes().get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("Test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testPopStackToClose() {
        Document doc = new Document("");
        Element root = doc.appendElement("root");
        Element child = root.appendElement("child");
        builder.stack.add(root);
        builder.stack.add(child);
        
        Token.EndTag endTag = new Token.EndTag("root");
        
        builder.popStackToClose(endTag);
        
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<root><child>Test</child></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        
        Node node = nodes.get(0);
        assertTrue(node instanceof Element);
        assertEquals("root", ((Element) node).nodeName());
        
        Element child = ((Element) node).child(0);
        assertEquals("child", child.nodeName());
        assertEquals("Test", child.text());
    }

}

