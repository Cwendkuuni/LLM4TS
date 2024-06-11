package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    // Test Doctype class
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        // Test getName()
        Assert.assertEquals("", doctype.getName());
        
        // Test getPublicIdentifier()
        Assert.assertEquals("", doctype.getPublicIdentifier());
        
        // Test getSystemIdentifier()
        Assert.assertEquals("", doctype.getSystemIdentifier());
        
        // Test isForceQuirks()
        Assert.assertFalse(doctype.isForceQuirks());
    }

    // Test StartTag class
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        // Test name()
        Assert.assertNull(startTag.name());
        
        // Test isSelfClosing()
        Assert.assertFalse(startTag.isSelfClosing());
        
        // Test getAttributes()
        Assert.assertNull(startTag.getAttributes());
        
        // Test appendTagName()
        startTag.appendTagName("tag");
        Assert.assertEquals("tag", startTag.name());
        
        // Test appendAttributeName()
        startTag.appendAttributeName("attr");
        Assert.assertEquals("attr", startTag.pendingAttributeName);
        
        // Test appendAttributeValue()
        startTag.appendAttributeValue("value");
        Assert.assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    // Test EndTag class
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        // Test name()
        Assert.assertNull(endTag.name());
        
        // Test appendTagName()
        endTag.appendTagName("tag");
        Assert.assertEquals("tag", endTag.name());
    }

    // Test Comment class
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        
        // Test getData()
        Assert.assertEquals("", comment.getData());
        
        // Test toString()
        Assert.assertEquals("<!-- -->", comment.toString());
    }

    // Test Character class
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("");
        
        // Test getData()
        Assert.assertEquals("", character.getData());
        
        // Test toString()
        Assert.assertEquals("", character.toString());
    }

    // Test EOF class
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
    }

    // Test isDoctype()
    @Test
    public void testIsDoctype() {
        Token doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    // Test asDoctype()
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        Assert.assertEquals(doctype, token.asDoctype());
    }

    // Test isStartTag()
    @Test
    public void testIsStartTag() {
        Token startTag = new Token.StartTag();
        Assert.assertTrue(startTag.isStartTag());
    }

    // Test asStartTag()
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        Assert.assertEquals(startTag, token.asStartTag());
    }

    // Test isEndTag()
    @Test
    public void testIsEndTag() {
        Token endTag = new Token.EndTag();
        Assert.assertTrue(endTag.isEndTag());
    }

    // Test asEndTag()
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        Assert.assertEquals(endTag, token.asEndTag());
    }

    // Test isComment()
    @Test
    public void testIsComment() {
        Token comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
    }

    // Test asComment()
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        Assert.assertEquals(comment, token.asComment());
    }

    // Test isCharacter()
    @Test
    public void testIsCharacter() {
        Token character = new Token.Character("");
        Assert.assertTrue(character.isCharacter());
    }

    // Test asCharacter()
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("");
        Token token = character;
        Assert.assertEquals(character, token.asCharacter());
    }

    // Test isEOF()
    @Test
    public void testIsEOF() {
        Token eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
}