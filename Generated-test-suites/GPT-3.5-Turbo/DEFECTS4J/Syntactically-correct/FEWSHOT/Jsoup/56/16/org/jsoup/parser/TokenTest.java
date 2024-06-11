package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokenTest {

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//Public");
        doctype.systemIdentifier.append("http://system");

        doctype.forceQuirks = true;

        doctype = doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myId");
        startTag.selfClosing = true;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;

        startTag = startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertEquals(false, startTag.selfClosing);
        assertNull(startTag.attributes);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
    }

    @Test
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag = endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment = comment.reset();

        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "Some character";

        character = character.reset();

        assertNull(character.data);
    }

    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();

        eof = eof.reset();

        assertEquals(eof, eof.reset());
    }
}