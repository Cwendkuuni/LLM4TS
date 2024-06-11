package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EOF;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();
        
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "paragraph"));
        startTag.newAttribute();
        assertEquals("p", startTag.name());
        assertTrue(startTag.isSelfClosing());
        assertEquals(1, startTag.getAttributes().size());
    }
    
    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.name("p");
        assertEquals("p", endTag.name());
    }
    
    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacter() {
        Character character = new Character("a");
        assertEquals("a", character.getData());
    }
    
    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Doctype();
        Doctype doctype = token.asDoctype();
        assertTrue(doctype instanceof Doctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new StartTag();
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new StartTag();
        StartTag startTag = token.asStartTag();
        assertTrue(startTag instanceof StartTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new EndTag();
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new EndTag();
        EndTag endTag = token.asEndTag();
        assertTrue(endTag instanceof EndTag);
    }
    
    @Test
    public void testIsComment() {
        Token token = new Comment();
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Comment();
        Comment comment = token.asComment();
        assertTrue(comment instanceof Comment);
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Character("a");
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Character("a");
        Character character = token.asCharacter();
        assertTrue(character instanceof Character);
    }
    
    @Test
    public void testIsEOF() {
        Token token = new EOF();
        assertTrue(token.isEOF());
    }
}