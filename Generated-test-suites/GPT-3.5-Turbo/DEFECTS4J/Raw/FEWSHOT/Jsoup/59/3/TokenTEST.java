package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Character;
import org.jsoup.parser.Comment;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.jsoup.parser.Util;
import org.jsoup.parser.UtilTest;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testReset() {
        // Test Doctype
        Token.Doctype doctype = new Token.Doctype();
        doctype.name = new StringBuilder("test");
        doctype.pubSysKey = "testKey";
        doctype.publicIdentifier = new StringBuilder("testPublic");
        doctype.systemIdentifier = new StringBuilder("testSystem");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertNull(doctype.name);
        assertNull(doctype.pubSysKey);
        assertNull(doctype.publicIdentifier);
        assertNull(doctype.systemIdentifier);
        assertFalse(doctype.forceQuirks);
        
        // Test StartTag
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.normalName = "testName";
        startTag.pendingAttributeName = "testName";
        startTag.pendingAttributeValue = new StringBuilder("testValue");
        startTag.pendingAttributeValueS = "testString";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        
        // Test EndTag
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        endTag.normalName = "testName";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        
        // Test Comment
        Token.Comment comment = new Token.Comment();
        comment.data = new StringBuilder("test");
        comment.bogus = true;
        
        comment.reset();
        
        assertNull(comment.data);
        assertFalse(comment.bogus);
        
        // Test Character
        Token.Character character = new Token.Character();
        character.data = "test";
        
        character.reset();
        
        assertNull(character.data);
        
        // Test EOF
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
    }
    
    @Test
    public void testName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        
        assertEquals("test", startTag.name());
    }
    
    @Test
    public void testNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "test";
        
        assertEquals("test", startTag.normalName());
    }
    
    @Test
    public void testIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        
        assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        
        assertEquals(startTag.attributes, startTag.getAttributes());
    }
    
    @Test
    public void testAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tes";
        
        startTag.appendTagName("t");
        
        assertEquals("test", startTag.tagName);
        assertEquals("test", startTag.normalName);
    }
    
    @Test
    public void testAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "tes";
        
        startTag.appendAttributeName("t");
        
        assertEquals("test", startTag.pendingAttributeName);
    }
    
    @Test
    public void testAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder("hello");
        
        startTag.appendAttributeValue(" world");
        
        assertEquals("hello world", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testSetEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
        assertEquals("Doctype", doctype.tokenType());
    }
    
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
        assertEquals("StartTag", startTag.tokenType());
    }
    
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
        assertEquals("EndTag", endTag.tokenType());
    }
    
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
        assertEquals("Comment", comment.tokenType());
    }
    
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
        assertEquals("Character", character.tokenType());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
        assertEquals("EOF", eof.tokenType());
    }
}