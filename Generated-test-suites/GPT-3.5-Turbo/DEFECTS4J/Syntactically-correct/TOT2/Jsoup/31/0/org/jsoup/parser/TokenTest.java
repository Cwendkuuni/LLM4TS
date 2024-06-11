package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeMethods() {
        Token.Doctype doctype = new Token.Doctype();
        
        // Test getName()
        assertEquals("", doctype.getName());
        
        // Test getPublicIdentifier()
        assertEquals("", doctype.getPublicIdentifier());
        
        // Test getSystemIdentifier()
        assertEquals("", doctype.getSystemIdentifier());
        
        // Test isForceQuirks()
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagMethods() {
        Token.StartTag startTag = new Token.StartTag("div");
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("id", "myId"));
        startTag.newAttribute();
        
        // Test name()
        assertEquals("div", startTag.name());
        
        // Test isSelfClosing()
        assertFalse(startTag.isSelfClosing());
        
        // Test getAttributes()
        assertNotNull(startTag.getAttributes());
        
        // Test appendTagName()
        startTag.appendTagName("p");
        assertEquals("divp", startTag.name());
        
        // Test appendAttributeName()
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("myClass");
        startTag.newAttribute();
        assertEquals("myClass", startTag.getAttributes().get("class"));
    }

    @Test
    public void testEndTagMethods() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        // Test name()
        assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentMethods() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Sample comment");
        
        // Test getData()
        assertEquals("Sample comment", comment.getData());
    }

    @Test
    public void testCharacterMethods() {
        Token.Character character = new Token.Character("Test Character");
        
        // Test getData()
        assertEquals("Test Character", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        // Test type
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeMethods() {
        Token token = new Token.Character("test");
        
        // Test isDoctype()
        assertFalse(token.isDoctype());
        
        // Test isStartTag()
        assertFalse(token.isStartTag());
        
        // Test isEndTag()
        assertFalse(token.isEndTag());
        
        // Test isComment()
        assertFalse(token.isComment());
        
        // Test isCharacter()
        assertTrue(token.isCharacter());
        
        // Test isEOF()
        assertFalse(token.isEOF());
    }

}