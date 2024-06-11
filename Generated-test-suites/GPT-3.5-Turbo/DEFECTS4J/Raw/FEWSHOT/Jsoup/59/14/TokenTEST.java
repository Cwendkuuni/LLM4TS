package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testReset_Doctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doctype.systemIdentifier.append("xhtml1-strict.dtd");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testReset_StartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;

        Token.StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testReset_EndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "span";
        endTag.normalName = "span";

        Token.EndTag resetEndTag = endTag.reset();

        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testReset_Comment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testReset_Character() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }   
}