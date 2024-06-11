package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<html><body><div>Test</div></body></html>";
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <body>\n  <div> Test </div>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html><body><div>Test</div></body></html>";
        String baseUri = "https://www.example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("<html>\n <body>\n  <div> Test </div>\n </body>\n</html>", document.outerHtml());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><div>Test</div></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, ParseSettings.preserveCase);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Document);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);

        Token.StartTag startTag = new Token.StartTag(tag, "", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(tag, element.tag());
        assertEquals("", element.baseUri());
        assertNull(element.parent());
        assertEquals("<div></div>", element.outerHtml());
    }
    
    @Test
    public void testProcessEndTag() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);

        Token.StartTag startTag = new Token.StartTag(tag, "", new Attributes());
        
        Element element = xmlTreeBuilder.insert(startTag);

        Token.EndTag endTag = new Token.EndTag(tag);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) nodes.get(0)).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Test");

        xmlTreeBuilder.insert(characterToken);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        assertEquals("Test", ((TextNode) nodes.get(0)).text());
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        xmlTreeBuilder.insert(doctype);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) nodes.get(0)).name());
    }
    
    @Test
    public void testPopStackToClose() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);

        Token.StartTag startTag1 = new Token.StartTag(tag, "", new Attributes());
        Token.StartTag startTag2 = new Token.StartTag(tag, "", new Attributes());

        xmlTreeBuilder.insert(startTag1);
        xmlTreeBuilder.insert(startTag2);
        
        Token.EndTag endTag = new Token.EndTag(tag);
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Element);
        assertEquals("<div></div>", xmlTreeBuilder.stack.get(0).outerHtml());
    }
    
    @Test
    public void testParseFragment() {
        String input = "<div>Test</div>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, ParseSettings.preserveCase);
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("<div> Test </div>", nodes.get(0).outerHtml());
    }
}