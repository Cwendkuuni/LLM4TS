package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Doctype doctype = new Doctype();
        Assert.assertEquals("Doctype", doctype.tokenType());
        
        StartTag startTag = new StartTag();
        Assert.assertEquals("StartTag", startTag.tokenType());
        
        EndTag endTag = new EndTag();
        Assert.assertEquals("EndTag", endTag.tokenType());
        
        Comment comment = new Comment();
        Assert.assertEquals("Comment", comment.tokenType());
        
        Character character = new Character();
        Assert.assertEquals("Character", character.tokenType());
        
        EOF eof = new EOF();
        Assert.assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        Assert.assertEquals("", doctype.name.toString());
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testDoctypeGetName() {
        Doctype doctype = new Doctype();
        doctype.name.append("name");
        
        Assert.assertEquals("name", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("publicIdentifier");
        
        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("systemIdentifier");
        
        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        
        Assert.assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
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
    public void testStartTagName() {
        StartTag startTag = new StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        
        Assert.assertEquals("tag", startTag.name());
    }
    
    @Test
    public void testStartTagNormalName() {
        StartTag startTag = new StartTag();
        startTag.normalName = "tag";
        
        Assert.assertEquals("tag", startTag.normalName());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        StartTag startTag = new StartTag();
        startTag.selfClosing = true;
        
        Assert.assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testEndTagName() {
        EndTag endTag = new EndTag();
        endTag.tagName = "tag";
        
        Assert.assertEquals("tag", endTag.name());
    }
    
    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        Comment comment = new Comment();
        comment.data.append("data");
        
        Assert.assertEquals("data", comment.getData());
    }
    
    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "data";
        
        character.reset();
        
        Assert.assertNull(character.data);
    }
    
    @Test
    public void testCharacterGetData() {
        Character character = new Character();
        character.data = "data";
        
        Assert.assertEquals("data", character.getData());
    }
    
    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        
        eof.reset();
        
        // No assertion, just checking if the method doesn't fail
    }
    
    @Test
    public void testIsDoctype() {
        Doctype doctype = new Doctype();
        
        Assert.assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Doctype();
        Doctype doctype = token.asDoctype();
        
        Assert.assertEquals(token, doctype);
    }
    
    @Test
    public void testIsStartTag() {
        StartTag startTag = new StartTag();
        
        Assert.assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new StartTag();
        StartTag startTag = token.asStartTag();
        
        Assert.assertEquals(token, startTag);
    }
    
    @Test
    public void testIsEndTag() {
        EndTag endTag = new EndTag();
        
        Assert.assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new EndTag();
        EndTag endTag = token.asEndTag();
        
        Assert.assertEquals(token, endTag);
    }
    
    @Test
    public void testIsComment() {
        Comment comment = new Comment();
        
        Assert.assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Comment();
        Comment comment = token.asComment();
        
        Assert.assertEquals(token, comment);
    }
    
    @Test
    public void testIsCharacter() {
        Character character = new Character();
        
        Assert.assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Character();
        Character character = token.asCharacter();
        
        Assert.assertEquals(token, character);
    }
    
    @Test
    public void testIsEOF() {
        EOF eof = new EOF();
        
        Assert.assertTrue(eof.isEOF());
    }
    
    @Test
    public void testParsingTokens() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();
        
        Assert.assertTrue(doctype instanceof Token);
        Assert.assertTrue(startTag instanceof Token);
        Assert.assertTrue(endTag instanceof Token);
        Assert.assertTrue(comment instanceof Token);
        Assert.assertTrue(character instanceof Token);
        Assert.assertTrue(eof instanceof Token);
    }
}