package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    // Test reset method of Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("test");
        doctype.pubSysKey = "test";
        doctype.publicIdentifier.append("test");
        doctype.systemIdentifier.append("test");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }
    
    // Test reset method of Tag class
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();
        
        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "test";
        tag.pendingAttributeValue.append("test");
        tag.pendingAttributeValueS = "test";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();
        
        Token.Tag resetTag = tag.reset();
        
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }
    
    // Test reset method of Comment class
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("test");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    // Test reset method of Character class
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "test";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    // Test reset method of EOF class
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEof = eof.reset();
        
        assertSame(resetEof, eof);
    }
    
    // Test tokenType method
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();
        
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }
}