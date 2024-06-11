package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("1");
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
    }
    
    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "id";
        endTag.pendingAttributeValue.append("1");
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        character.data("data");
        
        character.reset();
        
        assertNull(character.getData());
    }
    
    @Test
    public void testEOFReset() {
        eof.reset();
        
        assertSame(eof, eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        assertFalse(doctype.isDoctype());
        assertTrue(doctype.reset().isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        assertEquals(doctype, doctype.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        assertFalse(startTag.isStartTag());
        assertTrue(startTag.reset().isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        assertEquals(startTag, startTag.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        assertFalse(endTag.isEndTag());
        assertTrue(endTag.reset().isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        assertEquals(endTag, endTag.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        assertFalse(comment.isComment());
        assertTrue(comment.reset().isComment());
    }
    
    @Test
    public void testAsComment() {
        assertEquals(comment, comment.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        assertFalse(character.isCharacter());
        assertTrue(character.reset().isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        assertEquals(character, character.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        assertFalse(eof.isEOF());
        assertTrue(eof.reset().isEOF());
    }
}