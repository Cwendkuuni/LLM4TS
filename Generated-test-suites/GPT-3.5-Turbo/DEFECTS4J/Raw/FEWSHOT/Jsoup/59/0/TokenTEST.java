package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {
    
    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3O//DTD XHTML 1.0 Transitional//EN";
        doctype.publicIdentifier.append("-//W3O//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        Assert.assertEquals("", doctype.name.toString());
        Assert.assertNull(doctype.pubSysKey);
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.name = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        startTag.reset();
        
        Assert.assertNull(startTag.name);
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
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }
    
    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }
    
    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";
        
        character.reset();
        
        Assert.assertNull(character.data);
    }
    
    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        Assert.assertNotNull(eof);
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        Assert.assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        Assert.assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        Assert.assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        Assert.assertTrue(comment.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        
        Assert.assertTrue(character.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        
        Assert.assertTrue(eof.isEOF());
    }
}