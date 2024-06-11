package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {
   
    @Test
    public void testResetToken() {
        Token token = new Token.EOF();
        Token resetToken = token.reset();
        assertTrue(resetToken instanceof Token.EOF);
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//IETF//DTD HTML 2.0//EN");
        doctype.systemIdentifier.append("http://www.ibm.com/data/dtd/v11/ibm.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        assertTrue(resetDoctype instanceof Token.Doctype);
        assertEquals("", resetDoctype.getName());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "container"));
        startTag.attributes.put(new Attribute("id", "main"));
        startTag.selfClosing = true;
        
        Token.StartTag resetStartTag = startTag.reset();
        assertTrue(resetStartTag instanceof Token.StartTag);
        assertNull(resetStartTag.tagName);
        assertEquals("", resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        
        Token.EndTag resetEndTag = endTag.reset();
        assertTrue(resetEndTag instanceof Token.EndTag);
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertEquals("", resetEndTag.pendingAttributeValue.toString());
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        assertTrue(resetComment instanceof Token.Comment);
        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Some character data";
        
        Token.Character resetCharacter = character.reset();
        assertTrue(resetCharacter instanceof Token.Character);
        assertNull(resetCharacter.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        assertTrue(resetEOF instanceof Token.EOF);
    }
    
    @Test
    public void testTokenTypeIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.asDoctype() instanceof Token.Doctype);
    }
    
    @Test
    public void testTokenTypeIsStartTag() {
        Token token = new Token.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.asStartTag() instanceof Token.StartTag);
    }
    
    @Test
    public void testTokenTypeIsEndTag() {
        Token token = new Token.EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.asEndTag() instanceof Token.EndTag);
    }
    
    @Test
    public void testTokenTypeIsComment() {
        Token token = new Token.Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertTrue(token.asComment() instanceof Token.Comment);
    }
    
    @Test
    public void testTokenTypeIsCharacter() {
        Token token = new Token.Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.asCharacter() instanceof Token.Character);
    }
    
    @Test
    public void testTokenTypeIsEOF() {
        Token token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }
    
    @Test
    public void testIsDoctype() {
        Token token = new Token.EndTag();
        assertFalse(token.isDoctype());
    }
    
    @Test
    public void testAsDoctypeWithNonDoctypeToken() {
        Token token = new Token.EndTag();
        assertNull(token.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token token = new Token.Comment();
        assertFalse(token.isStartTag());
    }
    
    @Test
    public void testAsStartTagWithNonStartTagToken() {
        Token token = new Token.Comment();
        assertNull(token.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token token = new Token.Comment();
        assertFalse(token.isEndTag());
    }
    
    @Test
    public void testAsEndTagWithNonEndTagToken() {
        Token token = new Token.Character();
        assertNull(token.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token token = new Token.StartTag();
        assertFalse(token.isComment());
    }
    
    @Test
    public void testAsCommentWithNonCommentToken() {
        Token token = new Token.EndTag();
        assertNull(token.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token token = new Token.EOF();
        assertFalse(token.isCharacter());
    }
    
    @Test
    public void testAsCharacterWithNonCharacterToken() {
        Token token = new Token.Doctype();
        assertNull(token.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token token = new Token.Comment();
        assertFalse(token.isEOF());
    }
    
    @Test
    public void testResetStringBuilder() {
        StringBuilder sb = new StringBuilder("Some data");
        Token.reset(sb);
        assertEquals(0, sb.length());
    }
    
}