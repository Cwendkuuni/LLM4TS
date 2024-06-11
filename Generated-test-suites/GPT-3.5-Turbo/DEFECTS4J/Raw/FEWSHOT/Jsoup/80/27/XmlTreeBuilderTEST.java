package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder = new XmlTreeBuilder();

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testParseWithReader() {
        Reader input = new StringReader("<xml>data</xml>");
        String baseUri = "baseUri";
        Document doc = builder.parse(input, baseUri);
        
        assertEquals("<xml>data</xml>", doc.html());
        assertEquals("baseUri", doc.baseUri());

        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("xml", childNodes.get(0).nodeName());
        assertEquals("data", ((TextNode) childNodes.get(0).childNodes().get(0)).getWholeText());
    }
    
    @Test
    public void testParseWithString() {
        String input = "<xml>data</xml>";
        String baseUri = "baseUri";
        Document doc = builder.parse(input, baseUri);
        
        assertEquals("<xml>data</xml>", doc.html());
        assertEquals("baseUri", doc.baseUri());

        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("xml", childNodes.get(0).nodeName());
        assertEquals("data", ((TextNode) childNodes.get(0).childNodes().get(0)).getWholeText());
    }
    
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml>data</xml>");
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        builder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("xml");
        
        Element el = builder.insert(token);
        
        assertEquals("xml", el.nodeName());
        assertEquals("xml", el.tagName());
        assertNull(el.parent());
        assertEquals("baseUri", el.baseUri());
        assertEquals(settings.normalizeAttributes(new Attributes()), el.attributes());
        assertEquals(doc, builder.currentElement().parent());
        assertEquals(el, builder.currentElement());
        assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag token = new Token.EndTag("xml");

        builder.process(token);
        
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        
        builder.insert(commentToken);
        
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#comment", childNodes.get(0).nodeName());
        assertEquals("comment", ((Comment) childNodes.get(0)).getData());
        assertEquals(doc, ((Comment) childNodes.get(0)).ownerDocument());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character token = new Token.Character("data");
        
        builder.insert(token);
        
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#text", childNodes.get(0).nodeName());
        assertEquals("data", ((TextNode) childNodes.get(0)).getWholeText());
        assertEquals(doc, ((TextNode) childNodes.get(0)).ownerDocument());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype d = new Token.Doctype("name", "public", "system");

        builder.insert(d);
        
        List<Node> childNodes = doc.childNodes();
        assertEquals(1, childNodes.size());
        assertEquals("#doctype", childNodes.get(0).nodeName());
        assertEquals("name", ((DocumentType) childNodes.get(0)).name());
        assertEquals("public", ((DocumentType) childNodes.get(0)).publicId());
        assertEquals("system", ((DocumentType) childNodes.get(0)).systemId());
        assertEquals(doc, ((DocumentType) childNodes.get(0)).ownerDocument());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("xml");
        
        builder.stack.add(new Element("xml"));
        
        builder.popStackToClose(endTag);
        
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<xml>data</xml>";
        String baseUri = "baseUri";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> childNodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(1, childNodes.size());
        assertEquals("xml", childNodes.get(0).nodeName());
        assertEquals("data", ((TextNode) childNodes.get(0).childNodes().get(0)).getWholeText());
    }
}