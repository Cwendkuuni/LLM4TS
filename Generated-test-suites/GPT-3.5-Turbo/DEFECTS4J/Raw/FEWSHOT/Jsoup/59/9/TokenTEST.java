package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertNull(resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertNull(resetDoctype.publicIdentifier.toString());
        assertNull(resetDoctype.systemIdentifier.toString());
        assertEquals(false, resetDoctype.forceQuirks);
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("id", "header"));
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertNull(resetStartTag.pendingAttributeValue.toString());
        assertEquals(false, resetStartTag.hasEmptyAttributeValue);
        assertEquals(false, resetStartTag.hasPendingAttributeValue);
        assertEquals(false, resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "/div";
        endTag.normalName = "/div";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertNull(resetComment.data.toString());
        assertEquals(false, resetComment.bogus);
        
        Token.Character character = new Token.Character();
        character.data = "This is a text";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
        
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEof = eof.reset();
        
        assertNull(resetEof);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("id", "header"));
        
        assertEquals("<div id=\"header\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "/div";
        endTag.normalName = "/div";
        
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "This is a text";
        
        assertEquals("This is a text", character.toString());
    }
}