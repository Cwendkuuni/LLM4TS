package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        
        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        
        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        
        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        
        Token.Character character = new Token.Character("");
        assertEquals("Character", character.tokenType());
        
        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testGetDoctypeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testGetDoctypePublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testGetDoctypeSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }
    
    // Add more basic test cases for other methods
    
    // Add test cases for edge cases and exceptions
    
}

