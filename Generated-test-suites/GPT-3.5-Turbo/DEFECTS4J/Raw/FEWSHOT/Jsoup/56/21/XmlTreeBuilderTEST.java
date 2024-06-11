package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParse() {
        String input = "<html><title>Title</title></html>";
        String baseUri = "";
        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(parsedDoc);
        assertEquals(input, parsedDoc.toString());
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<html><body><div></div></body></html>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        Token startTagToken = Token.StartTag.valueOf("div");
        Token endTagToken = Token.EndTag.valueOf("div");
        Token commentToken = Token.Comment.valueOf("comment");
        Token characterToken = Token.Character.valueOf("text");
        Token doctypeToken = Token.Doctype.valueOf("doctype");
        
        assertTrue(xmlTreeBuilder.process(startTagToken));
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertTrue(xmlTreeBuilder.process(commentToken));
        assertTrue(xmlTreeBuilder.process(characterToken));
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        
        // Unexpected token type
        Token unknownToken = new Token(Tag.valueOf("p", ParseSettings.preserveCase));
        assertFalse(xmlTreeBuilder.process(unknownToken));
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.insertNode(element);
        
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }
    
    @Test
    public void testInsert() {
        Token.StartTag startTag = Token.StartTag.valueOf("div");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals(Tag.valueOf("div", xmlTreeBuilder.settings), element.tag());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertEquals(startTag.attributes, element.attributes());
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void testInsertWithSelfClosingFlag() {
        Token.StartTag startTag = Token.StartTag.valueOf("div", ParseSettings.preserveCase, false);
        startTag._isSelfClosing = true;
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals(Tag.valueOf("div", xmlTreeBuilder.settings), element.tag());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertEquals(startTag.attributes, element.attributes());
        assertNull(tag.isKnownTag());
    }
    
    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = Token.Comment.valueOf("comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(commentToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertWithBogusCommentToken() {
        Token.Comment commentToken = new Token.Comment("bogus comment");
        commentToken.bogus = true;
        
        xmlTreeBuilder.insert(commentToken);
        
        // Test comment insertion
        
        // Test xml declaration
    }
    
    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = Token.Character.valueOf("text");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = Token.Doctype.valueOf("html");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Test doctype insertion
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = Token.EndTag.valueOf("div");
        
        xmlTreeBuilder.stack = new ArrayList<>();
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), ""));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div></div>";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("<div></div>", nodes.get(0).outerHtml());
    }
}