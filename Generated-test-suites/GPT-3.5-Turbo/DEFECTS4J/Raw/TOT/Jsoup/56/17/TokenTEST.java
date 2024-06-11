package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        
        assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//W3C//DTD XHTML 1.0 Transitional//EN");
        
        assertEquals("//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myDiv");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertEquals(null, resetStartTag.tagName);
        assertEquals(null, resetStartTag.normalName);
        assertEquals(null, resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertEquals(null, resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertEquals(null, resetStartTag.attributes);
    }
    
    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = null;
        
        startTag.newAttribute();
        
        assertTrue(startTag.attributes.containsKey("id"));
        assertEquals("", startTag.attributes.get("id"));
    }
    
    @Test
    public void testStartTagFinalizeTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myDiv");
        
        startTag.finaliseTag();
        
        assertEquals("", startTag.pendingAttributeName);
        assertTrue(startTag.attributes.containsKey("id"));
        assertEquals("myDiv", startTag.attributes.get("id"));
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        
        assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "div";
        
        assertEquals("div", startTag.normalName());
    }
    
    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        
        startTag.appendTagName("Test");
        
        assertEquals("divTest", startTag.tagName);
        assertEquals("divtest", startTag.normalName);
    }
    
    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "id";
        
        startTag.appendAttributeName("Test");
        
        assertEquals("idTest", startTag.pendingAttributeName);
    }
    
    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        
        startTag.appendAttributeValue("Test");
        
        assertEquals("Test", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagSetEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertEquals(null, resetEndTag.tagName);
        assertEquals(null, resetEndTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is character data";
        
        Token.Character resetCharacter = character.reset();
        
        assertEquals(null, resetCharacter.data);
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "This is character data";
        
        assertEquals("This is character data", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertEquals(eof, resetEOF);
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertEquals(doctype, doctype.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertEquals(startTag, startTag.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertEquals(endTag, endTag.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertEquals(comment, comment.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        
        assertEquals(character, character.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
    
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();
        
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.EOF, eof.type);
    }
    
}