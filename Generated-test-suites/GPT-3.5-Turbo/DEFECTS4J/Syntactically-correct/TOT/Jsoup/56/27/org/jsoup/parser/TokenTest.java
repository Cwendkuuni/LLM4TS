package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("html");
        doctype.publicIdentifier.append("+//Silmaril//dtd html Pro v0r11 19970101//EN//LIBRARY//DTD HTML PRO V0R11 Office//EN");
        doctype.systemIdentifier.append("http://www.silmaril.ie/software/DTD/HTMLPro.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        Assert.assertEquals("", doctype.name.toString());
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        
        character.data = "This is a character";
        
        character.reset();
        
        Assert.assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = (Token.EOF) eof.reset();
        
        Assert.assertEquals(eof, resetEOF);
    }
}