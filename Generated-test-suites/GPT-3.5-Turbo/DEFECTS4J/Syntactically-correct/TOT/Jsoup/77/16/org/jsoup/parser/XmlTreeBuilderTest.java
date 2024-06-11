package org.jsoup.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

class XmlTreeBuilderTest {

    @Test
    void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    void testParseReaderString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><child1></child1><child2></child2></root>");
        String baseUri = "http://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<root>\n <child1></child1>\n <child2></child2>\n</root>\n", doc.html());
    }
    
    @Test
    void testParseString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<root>\n <child1></child1>\n <child2></child2>\n</root>\n", doc.html());
    }
    
    @Test
    void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><child1></child1><child2></child2></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        treeBuilder.initialiseParse(input, baseUri, errors, settings);
        List<Element> stack = treeBuilder.getStack();
        Document doc = treeBuilder.getDoc();
        
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertEquals(doc, stack.get(0));
        assertNotNull(doc);
        assertEquals(settings, doc.outputSettings().syntax());
    }
    
    @Test
    void testProcess() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        
        Token startTagToken = new Token.StartTag("tag", "name", null, false);
        assertTrue(treeBuilder.process(startTagToken));
        
        Token endTagToken = new Token.EndTag("tag");
        assertTrue(treeBuilder.process(endTagToken));
        
        Token commentToken = new Token.Comment("comment");
        assertTrue(treeBuilder.process(commentToken));
        
        Token characterToken = new Token.Character("character");
        assertTrue(treeBuilder.process(characterToken));
        
        Token doctypeToken = new Token.Doctype("doctype", null, null, null);
        assertTrue(treeBuilder.process(doctypeToken));
        
        Token eofToken = new Token.EOF();
        assertTrue(treeBuilder.process(eofToken));
        
        // Invalid token type
        Token invalidToken = new Token.Invalid();
        assertThrows(AssertionError.class, () -> treeBuilder.process(invalidToken));
    }
    
    @Test
    void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        treeBuilder.insertNode(root);
        
        List<Node> childNodes = root.childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertEquals(root, childNodes.get(0).parentNode());
    }
    
    @Test
    void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, false);
        
        Element element = treeBuilder.insert(startTag);
        
        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals(0, element.siblingIndex());
        assertFalse(element.isBlock());
    }
    
    @Test
    void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        
        treeBuilder.insert(commentToken);
        
        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
    }
    
    @Test
    void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("character");
        
        treeBuilder.insert(characterToken);
        
        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
    }
    
    @Test
    void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("doctype", null, null, null);
        
        treeBuilder.insert(doctypeToken);
        
        List<Node> childNodes = treeBuilder.currentElement().childNodes();
        assertNotNull(childNodes);
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
    }
    
    @Test
    void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"));
        Element child1 = new Element(Tag.valueOf("child1"));
        Element child2 = new Element(Tag.valueOf("child2"));
        treeBuilder.getStack().add(root);
        treeBuilder.getStack().add(child1);
        treeBuilder.getStack().add(child2);
        
        Token.EndTag endTag = new Token.EndTag("child1");
        treeBuilder.popStackToClose(endTag);
        
        List<Element> stack = treeBuilder.getStack();
        assertNotNull(stack);
        assertEquals(2, stack.size());
        assertEquals(child2, stack.get(0));
        assertEquals(root, stack.get(1));
    }
    
    @Test
    void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child1></child1><child2></child2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}