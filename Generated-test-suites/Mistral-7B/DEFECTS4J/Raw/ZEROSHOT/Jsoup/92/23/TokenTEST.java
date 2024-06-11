package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.Arrays;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals(Token.Doctype.class, resetDoctypeToken.getClass());

        doctypeToken.name("html");
        assertEquals("html", doctypeToken.name());

        doctypeToken.pubSysKey("public sys key");
        assertEquals("public sys key", doctypeToken.getPubSysKey());

        doctypeToken.publicIdentifier("public identifier");
        assertEquals("public identifier", doctypeToken.getPublicIdentifier());

        doctypeToken.systemIdentifier("system identifier");
        assertEquals("system identifier", doctypeToken.getSystemIdentifier());

        doctypeToken.forceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testResetDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.name("html");
        doctypeToken.pubSysKey("public sys key");
        doctypeToken.publicIdentifier("public identifier");
        doctypeToken.systemIdentifier("system identifier");
        doctypeToken.forceQuirks(true);

        Token resetDoctypeToken = doctypeToken.reset();
        assertSame(doctypeToken, resetDoctypeToken);
        assertNull(resetDoctypeToken.getName());
        assertNull(resetDoctypeToken.getPubSysKey());
        assertNull(resetDoctypeToken.getPublicIdentifier());
        assertNull(resetDoctypeToken.getSystemIdentifier());
        assertFalse(resetDoctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.Tag();
        Token resetTagToken = tagToken.reset();
        assertEquals(Token.Tag.class, resetTagToken.getClass());

        tagToken.name("div");
        assertEquals("div", tagToken.name());

        tagToken.selfClosing(true);
        assertTrue(tagToken.isSelfClosing());

        tagToken.newAttribute().name("class").value("classValue");
        assertEquals("classValue", tagToken.attributes().getValue("class"));
    }

    @Test
    public void testResetTagToken() {
        Token tagToken = new Token.Tag();
        tagToken.name("div");
        tagToken.selfClosing(true);
        tagToken.newAttribute().name("class").value("classValue");

        Token resetTagToken = tagToken.reset();
        assertSame(tagToken, resetTagToken);
        assertNull(resetTagToken.getName());
        assertNull(resetTagToken.getAttributes());
        assertFalse(resetTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Token resetStartTagToken = startTagToken.reset();
        assertEquals(Token.StartTag.class, resetStartTagToken.getClass());

        startTagToken.name("div");
        assertEquals("div", startTagToken.name());

        startTagToken.attributes(new Attributes().put("class", "classValue"));
        assertEquals("classValue", startTagToken.attributes().getValue("class"));

        StartTag startTagTokenAsStartTag = startTagToken.asStartTag();
        assertEquals(startTagToken, startTagTokenAsStartTag);
    }

    @Test
    public void testResetStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.name("div");
        startTagToken.attributes(new Attributes().put("class", "classValue"));

        Token resetStartTagToken = startTagToken.reset();
        assertSame(startTagToken, resetStartTagToken);
        assertNull(resetStartTagToken.getName());
        assertNull(resetStartTagToken.getAttributes());
        assertFalse(resetStartTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Token resetEndTagToken = endTagToken.reset();
        assertEquals(Token.EndTag.class, resetEndTagToken.getClass());

        endTagToken.name("div");
        assertEquals("div", endTagToken.name());

        EndTag endTagTokenAsEndTag = endTagToken.asEndTag();
        assertEquals(endTagToken, endTagTokenAsEndTag);
    }

    @Test
    public void testResetEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("div");

        Token resetEndTagToken = endTagToken.reset();
        assertSame(endTagToken, resetEndTagToken);
        assertNull(resetEndTagToken.getName());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();
        assertEquals(Token.Comment.class, resetCommentToken.getClass());

        commentToken.data("This is a comment");
        assertEquals("This is a comment", commentToken.getData());

        Comment commentTokenAsComment = commentToken.asComment();
        assertEquals(commentToken, commentTokenAsComment);
    }

    @Test
    public void testResetCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.data("This is a comment");

        Token resetCommentToken = commentToken.reset();
        assertSame(commentToken, resetCommentToken);
        assertNull(resetCommentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();
        assertEquals(Token.Character.class, resetCharacterToken.getClass());

        characterToken.data("A character");
        assertEquals("A character", characterToken.getData());

        Character characterTokenAsCharacter = characterToken.asCharacter();
        assertEquals(characterToken, characterTokenAsCharacter);
    }

    @Test
    public void testResetCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.data("A character");

        Token resetCharacterToken = characterToken.reset();
        assertSame(characterToken, resetCharacterToken);
        assertNull(resetCharacterToken.getData());
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("CDATA[This is CData]");
        Token resetCDataToken = cDataToken.reset();
        assertEquals(Token.CData.class, resetCDataToken.getClass());

        assertEquals("CDATA[This is CData]", cDataToken.toString());
        assertEquals("This is CData", cDataToken.getData());

        Character cDataTokenAsCharacter = cDataToken.asCharacter();
        assertEquals(cDataToken, cDataTokenAsCharacter);
    }

    @Test
    public void testResetCDataToken() {
        Token cDataToken = new Token.CData("CDATA[This is CData]");

        Token resetCDataToken = cDataToken.reset();
        assertSame(cDataToken, resetCDataToken);
        assertNull(resetCDataToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();
        assertEquals(Token.EOF.class, resetEofToken.getClass());

        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());

        EOF eofTokenAsEOF = eofToken.asEOF();
        assertEquals(eofToken, eofTokenAsEOF);
    }

    @Test
    public void testResetEOFToken() {
        Token eofToken = new Token.EOF();

        Token resetEofToken = eofToken.reset();
        assertSame(eofToken, resetEofToken);
        assertNull(resetEofToken.getData());
    }
}