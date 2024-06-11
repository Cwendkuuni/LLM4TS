package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        
        assertEquals("test", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        
        assertEquals("public", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        
        assertEquals("system", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "values";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        Token.StartTag resetTag = tag.reset();
        
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
    
    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.attributes = new Attributes();
        tag.pendingAttributeName = "attribute";
        tag.pendingAttributeValue.append("value");
        
        tag.newAttribute();
        
        assertEquals(1, tag.attributes.size());
        Attribute attribute = tag.attributes.get("attribute");
        assertNotNull(attribute);
        assertEquals("attribute", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.pendingAttributeName = "attribute";
        tag.finaliseTag();
        
        assertNull(tag.pendingAttributeName);
    }
    
    @Test
    public void testTagGetName() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "test";
        
        assertEquals("test", tag.name());
    }
    
    @Test
    public void testTagGetNormalName() {
        Token.StartTag tag = new Token.StartTag();
        tag.normalName = "test";
        
        assertEquals("test", tag.normalName());
    }
    
    @Test
    public void testTagAppendTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendTagName("test");
        
        assertEquals("test", tag.tagName);
        assertEquals("test", tag.normalName);
    }
    
    @Test
    public void testTagAppendAttributeName() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeName("attribute");
        
        assertEquals("attribute", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeValue("value");
        
        assertTrue(tag.hasPendingAttributeValue);
        assertEquals("value", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        
        assertTrue(tag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag tag = new Token.EndTag();
        tag.tagName = "test";
        tag.normalName = "test";
        
        Token.EndTag resetTag = tag.reset();
        
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        
        assertEquals("test", comment.getData());
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "test";
        
        assertEquals("test", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertSame(eof, resetEOF);
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        
        assertTrue(token.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        
        Token.Doctype asDoctype = token.asDoctype();
        
        assertSame(doctype, asDoctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        
        assertTrue(token.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        
        Token.StartTag asStartTag = token.asStartTag();
        
        assertSame(startTag, asStartTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        
        assertTrue(token.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        
        Token.EndTag asEndTag = token.asEndTag();
        
        assertSame(endTag, asEndTag);
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        
        assertTrue(token.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        
        Token.Comment asComment = token.asComment();
        
        assertSame(comment, asComment);
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        
        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        
        Token.Character asCharacter = token.asCharacter();
        
        assertSame(character, asCharacter);
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        
        assertTrue(token.isEOF());
    }
    
    @Test
    public void testTokenType() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}