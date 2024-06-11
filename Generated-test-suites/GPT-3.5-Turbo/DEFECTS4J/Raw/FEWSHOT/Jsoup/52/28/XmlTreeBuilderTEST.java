package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlTreeBuilderTest {
  
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
        
        // Test if the document is added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // Test if the document output settings are set to xml syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        Token startTagToken = new Token();
        startTagToken.type = Token.TokenType.StartTag;
        
        // Test if insert method is called for StartTag token
        assertTrue(xmlTreeBuilder.process(startTagToken));
        
        Token endTagToken = new Token();
        endTagToken.type = Token.TokenType.EndTag;
        
        // Test if popStackToClose method is called for EndTag token
        assertTrue(xmlTreeBuilder.process(endTagToken));
        
        Token commentToken = new Token();
        commentToken.type = Token.TokenType.Comment;
        
        // Test if insert method is called for Comment token
        assertTrue(xmlTreeBuilder.process(commentToken));
        
        Token characterToken = new Token();
        characterToken.type = Token.TokenType.Character;
        
        // Test if insert method is called for Character token
        assertTrue(xmlTreeBuilder.process(characterToken));
        
        Token doctypeToken = new Token();
        doctypeToken.type = Token.TokenType.Doctype;
        
        // Test if insert method is called for Doctype token
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        
        Token eofToken = new Token();
        eofToken.type = Token.TokenType.EOF;
        
        // Test if process returns true for EOF token
        assertTrue(xmlTreeBuilder.process(eofToken));
        
        Token unknownToken = new Token();
        unknownToken.type = Token.TokenType.Unknown;
        
        // Test if Validate.fail method is called for Unknown token
        try {
            xmlTreeBuilder.process(unknownToken);
            fail("Expected Validate.fail method to be called");
        } catch (Exception e) {
            assertEquals("Unexpected token type: Unknown", e.getMessage());
        }
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.stack.add(element);
        Node node = new TextNode("text", "");
        
        xmlTreeBuilder.insertNode(node);
        
        assertEquals(1, element.childNodes().size());
        assertSame(node, element.childNodes().get(0));
    }
    
    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("");
        
        // Test if insertNode method is called for StartTag token with self-closing flag false
        Element element = xmlTreeBuilder.insert(startTag);
        assertFalse(startTag.isSelfClosing());
        assertSame(element, xmlTreeBuilder.currentElement());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(element, xmlTreeBuilder.stack.get(0));
        
        // Test if insertNode and acknowledgeSelfClosingFlag methods are called for StartTag token with self-closing flag true
        startTag.setSelfClosing();
        Element selfClosingElement = xmlTreeBuilder.insert(startTag);
        assertTrue(startTag.isSelfClosing());
        assertEquals(0, xmlTreeBuilder.stack.size());
        assertSame(selfClosingElement, xmlTreeBuilder.currentElement());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("");
        
        // Test if insertNode method is called for Comment token with bogus flag false
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertSame(Comment.class, xmlTreeBuilder.doc.childNodes().get(0).getClass());
        
        // Test if insertNode and new XmlDeclaration methods are called for Comment token with bogus flag true
        commentToken.bogus = true;
        xmlTreeBuilder.insert(commentToken);
        assertEquals(2, xmlTreeBuilder.doc.childNodes().size());
        assertSame(XmlDeclaration.class, xmlTreeBuilder.doc.childNodes().get(1).getClass());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("");
        
        // Test if insertNode method is called for Character token
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertSame(TextNode.class, xmlTreeBuilder.doc.childNodes().get(0).getClass());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("");
        
        // Test if insertNode method is called for Doctype token
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertSame(DocumentType.class, xmlTreeBuilder.doc.childNodes().get(0).getClass());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("div"), "");
        Element element2 = new Element(Tag.valueOf("div"), "");
        Element element3 = new Element(Tag.valueOf("div"), "");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        
        Token.EndTag endTag = new Token.EndTag("div");
        
        // Test if stack elements are removed up to the first occurrence of the end tag
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(element1, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Tokeniser tokeniser = new Tokeniser("");
        xmlTreeBuilder.tokeniser = tokeniser;
        
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", "", errors);
        
        // Test if initialiseParse and runParser methods are called and document nodes are returned
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(tokeniser, xmlTreeBuilder.tokeniser);
        assertEquals(0, errors.size());
        assertSame(xmlTreeBuilder.doc.childNodes(), nodes);
    }
}