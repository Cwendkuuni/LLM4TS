package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.Iterator;

/**
 * @author Jonathan Hedley
 */
public class XmlTreeBuilderTest {

    // Test initialiseParse method
    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(input, baseUri, errors);
        
        // Verify that the document is added to the stack
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0), builder.doc);
    }
    
    // Test process method with StartTag token
    @Test
    public void testProcessWithStartTagToken() {
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        Token.StartTag startTag = new Token.StartTag();
        startTag.setName("tag");
        startTag.setAttributes(new Attributes());
        token.setAsStartTag(startTag);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        boolean result = builder.process(token);
        
        // Verify that the start tag is inserted and the result is true
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0).tagName(), "tag");
        assertTrue(result);
    }

    // Test process method with EndTag token
    @Test
    public void testProcessWithEndTagToken() {
        Token token = new Token();
        token.type = Token.TokenType.EndTag;
        Token.EndTag endTag = new Token.EndTag();
        endTag.setName("tag");
        token.setAsEndTag(endTag);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        boolean result = builder.process(token);
        
        // Verify that the stack is updated correctly and the result is true
        assertEquals(builder.stack.size(), 0);
        assertTrue(result);
    }
    
    // Test process method with Comment token
    @Test
    public void testProcessWithCommentToken() {
        Token token = new Token();
        token.type = Token.TokenType.Comment;
        Token.Comment commentToken = new Token.Comment();
        commentToken.setData("comment");
        token.setAsComment(commentToken);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        boolean result = builder.process(token);
        
        // Verify that the comment is inserted and the result is true
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0).nodeName(), "#comment");
        assertTrue(result);
    }
    
    // Test process method with Character token
    @Test
    public void testProcessWithCharacterToken() {
        Token token = new Token();
        token.type = Token.TokenType.Character;
        Token.Character characterToken = new Token.Character();
        characterToken.setData("text");
        token.setAsCharacter(characterToken);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        boolean result = builder.process(token);
        
        // Verify that the text node is inserted and the result is true
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0).nodeName(), "#text");
        assertTrue(result);
    }
    
    // Test process method with Doctype token
    @Test
    public void testProcessWithDoctypeToken() {
        Token token = new Token();
        token.type = Token.TokenType.Doctype;
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.setName("doctype");
        doctypeToken.setPublicIdentifier("");
        doctypeToken.setSystemIdentifier("");
        token.setAsDoctype(doctypeToken);
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("", "", new ParseErrorList());
        
        boolean result = builder.process(token);
        
        // Verify that the doctype node is inserted and the result is true
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0).nodeName(), "#doctype");
        assertTrue(result);
    }
    
    // Test popStackToClose method
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.setName("tag");
        
        XmlTreeBuilder builder = new XmlTreeBuilder();
        
        // Add elements to the stack
        Element firstElement = new Element("tag");
        builder.stack.add(firstElement);
        
        Element secondElement = new Element("tag");
        builder.stack.add(secondElement);
        
        Element thirdElement = new Element("otherTag");
        builder.stack.add(thirdElement);
        
        assertEquals(builder.stack.size(), 3);
        
        // Call the popStackToClose method
        builder.popStackToClose(endTag);
        
        // Verify that the stack is updated correctly
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.get(0), thirdElement);
    }
}