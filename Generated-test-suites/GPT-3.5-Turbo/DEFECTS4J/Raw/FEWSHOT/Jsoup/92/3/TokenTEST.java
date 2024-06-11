package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.systemIdentifier.append("");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertNull(doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertTrue(doctype.publicIdentifier.toString().isEmpty());
        assertTrue(doctype.systemIdentifier.toString().isEmpty());
        assertTrue(doctype.forceQuirks);
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.EndTag();
        tag.tagName = "div";
        tag.normalName = "DIV";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("div1");
        tag.pendingAttributeValueS = "";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertTrue(tag.pendingAttributeValue.toString().isEmpty());
        assertNull(tag.pendingAttributeValueS);
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "DIV";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("div1");
        tag.pendingAttributeValueS = "";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertTrue(tag.pendingAttributeValue.toString().isEmpty());
        assertNull(tag.pendingAttributeValueS);
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertTrue(comment.data.toString().isEmpty());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertEquals(eof, eof);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        
        String result = doctype.getName();
        
        assertEquals("html", result);
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        
        String result = doctype.getPubSysKey();
        
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", result);
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");
        
        String result = doctype.getPublicIdentifier();
        
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", result);
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("DTD/xhtml1-strict.dtd");
        
        String result = doctype.getSystemIdentifier();
        
        assertEquals("DTD/xhtml1-strict.dtd", result);
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        boolean result = doctype.isForceQuirks();
        
        assertTrue(result);
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = "div";
        
        String result = tag.name();
        
        assertEquals("div", result);
    }
    
    @Test
    public void testStartTagNormalName() {
        Token.StartTag tag = new Token.StartTag();
        tag.normalName = "div";
        
        String result = tag.normalName();
        
        assertEquals("div", result);
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        tag.selfClosing = true;
        
        boolean result = tag.isSelfClosing();
        
        assertTrue(result);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        String result = character.getData();
        
        assertEquals("This is a character", result);
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag tag = new Token.EndTag();
        tag.tagName = "div";
        
        String result = tag.name();
        
        assertEquals("div", result);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        String result = comment.getData();
        
        assertEquals("This is a comment", result);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token resetToken = eof.reset();
        
        assertEquals(eof, resetToken);
    }
    
}