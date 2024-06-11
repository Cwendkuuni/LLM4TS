package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        // Typical use case
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;
        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("PUBLIC", doctype.getPubSysKey());
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        Assert.assertTrue(doctype.isForceQuirks());
        
        // Edge case
        doctype.reset();
        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        // Typical use case
        startTag.nameAttr("a", new Attributes().put("href", "https://example.com"));
        startTag.selfClosing = false;
        Assert.assertEquals("a", startTag.name());
        Assert.assertEquals("a", startTag.normalName());
        Assert.assertFalse(startTag.isSelfClosing());
        Assert.assertEquals(1, startTag.getAttributes().size());
        Assert.assertEquals("https://example.com", startTag.getAttributes().get("href"));
        
        // Edge case
        startTag.reset();
        Assert.assertNull(startTag.name());
        Assert.assertNull(startTag.normalName());
        Assert.assertFalse(startTag.isSelfClosing());
        Assert.assertNull(startTag.getAttributes());
    }
    
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        // Typical use case
        endTag.name("a");
        Assert.assertEquals("a", endTag.name());
        Assert.assertEquals("a", endTag.normalName());
        
        // Edge case
        endTag.reset();
        Assert.assertNull(endTag.name());
        Assert.assertNull(endTag.normalName());
    }
    
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        
        // Typical use case
        comment.data.append("This is a comment");
        comment.bogus = false;
        Assert.assertEquals("This is a comment", comment.getData());
        Assert.assertFalse(comment.bogus);
        
        // Edge case
        comment.reset();
        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        
        // Typical use case
        character.data("This is a character");
        Assert.assertEquals("This is a character", character.getData());
        
        // Edge case
        character.reset();
        Assert.assertNull(character.getData());
    }
    
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        // No test cases for EOF as it doesn't have any properties
        
        // Edge case
        Assert.assertNotNull(eof.reset());
    }
}