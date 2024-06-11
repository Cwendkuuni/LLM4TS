package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings defaultSettings = builder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }
    
    @Test
    public void testInitialiseParse() {
        // Test case with non-null input and baseUri
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("");
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(builder);
        builder.initialiseParse(input, baseUri, parser);
        assertNotNull(builder.stack);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
        
        // Test case with null input
        builder = new XmlTreeBuilder();
        baseUri = "http://www.example.com";
        parser = new Parser(builder);
        builder.initialiseParse(null, baseUri, parser);
        assertNotNull(builder.stack);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testParse() {
        // Test case with non-null input and baseUri
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<xml></xml>");
        String baseUri = "http://www.example.com";
        Document parsedDoc = builder.parse(input, baseUri);
        assertNotNull(parsedDoc);
        
        // Test case with non-null input and null baseUri
        builder = new XmlTreeBuilder();
        input = new StringReader("<xml></xml>");
        baseUri = null;
        parsedDoc = builder.parse(input, baseUri);
        assertNotNull(parsedDoc);
        
        // Test case with null input and non-null baseUri
        builder = new XmlTreeBuilder();
        input = null;
        baseUri = "http://www.example.com";
        parsedDoc = builder.parse(input, baseUri);
        assertNull(parsedDoc);
        
        // Test case with null input and null baseUri
        builder = new XmlTreeBuilder();
        input = null;
        baseUri = null;
        parsedDoc = builder.parse(input, baseUri);
        assertNull(parsedDoc);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // Test case with non-null node
        Element element = new Element("test");
        builder.insertNode(element);
        assertEquals(element, builder.currentElement().childNode(0));
        
        // Test case with null node
        builder.insertNode(null);
        assertEquals(element, builder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("xml");
        
        // Test case with self-closing tag
        startTag.setSelfClosing();
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertTrue(element.tag().isSelfClosing());
        
        // Test case without self-closing tag
        startTag = new Token.StartTag("test");
        element = builder.insert(startTag);
        assertNotNull(element);
        assertFalse(element.tag().isSelfClosing());
        assertEquals(builder.currentElement(), element);
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("test");
        
        // Test case with non-bogus comment
        builder.insert(commentToken);
        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertNotNull(comment);
        assertEquals(commentToken.getData(), comment.getData());
        
        // Test case with bogus comment
        commentToken = new Token.Comment("xml declaration");
        commentToken.setBogus(true);
        builder.insert(commentToken);
        XmlDeclaration xmlDecl = (XmlDeclaration) builder.currentElement().childNode(1);
        assertNotNull(xmlDecl);
        assertEquals(commentToken.getData(), xmlDecl.outerHtml());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("test");
        
        // Test case with regular character
        builder.insert(characterToken);
        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertNotNull(textNode);
        assertEquals(characterToken.getData(), textNode.getWholeText());
        
        // Test case with CDATA character
        characterToken = new Token.Character("CDATA[test]");
        builder.insert(characterToken);
        CDataNode cdataNode = (CDataNode) builder.currentElement().childNode(1);
        assertNotNull(cdataNode);
        assertEquals(characterToken.getData(), cdataNode.getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        
        builder.insert(doctypeToken);
        DocumentType doctypeNode = (DocumentType) builder.currentElement().childNode(0);
        assertNotNull(doctypeNode);
        assertEquals(doctypeToken.getName(), doctypeNode.nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");
        
        // Test case with existing element in stack
        Element element = new Element("test");
        builder.stack.add(element);
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
        
        // Test case without existing element in stack
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<xml></xml>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(builder);
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("xml", nodes.get(0).nodeName());
    }
}