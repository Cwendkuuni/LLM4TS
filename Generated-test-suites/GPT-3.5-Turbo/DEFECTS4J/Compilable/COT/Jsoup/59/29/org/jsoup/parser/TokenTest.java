package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token.Doctype();
    }
    
    @Test
    public void testTokenType() {
        String expected = "Doctype";
        String actual = token.tokenType();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testReset() {
        Token.Doctype expected = new Token.Doctype();
        Token.Doctype actual = (Token.Doctype) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testGetName() {
        String expected = "";
        String actual = ((Token.Doctype) token).getName();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPubSysKey() {
        String expected = null;
        String actual = ((Token.Doctype) token).getPubSysKey();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPublicIdentifier() {
        String expected = "";
        String actual = ((Token.Doctype) token).getPublicIdentifier();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSystemIdentifier() {
        String expected = "";
        String actual = ((Token.Doctype) token).getSystemIdentifier();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsForceQuirks() {
        boolean expected = false;
        boolean actual = ((Token.Doctype) token).isForceQuirks();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testStartTagReset() {
        token = new Token.StartTag();
        Token.StartTag expected = new Token.StartTag();
        Token.StartTag actual = (Token.StartTag) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testEndTagReset() {
        token = new Token.EndTag();
        Token.EndTag expected = new Token.EndTag();
        Token.EndTag actual = (Token.EndTag) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testCommentReset() {
        token = new Token.Comment();
        Token.Comment expected = new Token.Comment();
        Token.Comment actual = (Token.Comment) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testCharacterReset() {
        token = new Token.Character();
        Token.Character expected = new Token.Character();
        Token.Character actual = (Token.Character) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
    
    @Test
    public void testEOFReset() {
        token = new Token.EOF();
        Token.EOF expected = new Token.EOF();
        Token.EOF actual = (Token.EOF) token.reset();
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }
}