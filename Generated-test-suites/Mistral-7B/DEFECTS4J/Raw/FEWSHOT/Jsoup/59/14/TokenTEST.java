package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().getPublicIdentifier().append("public");
        doctype.asDoctype().getSystemIdentifier().append("system");
        doctype.asDoctype().isForceQuirks(true);

        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.asDoctype().getName());
        assertNull(resetDoctype.asDoctype().getPubSysKey());
        assertNull(resetDoctype.asDoctype().getPublicIdentifier());
        assertNull(resetDoctype.asDoctype().getSystemIdentifier());
        assertFalse(resetDoctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().attributes().put(new Attribute("class", "test"));

        Token resetTag = tag.reset();
        assertEquals(TokenType.StartTag, resetTag.type);
        assertNull(resetTag.asStartTag().tagName);
        assertNull(resetTag.asStartTag().normalName());
        assertNull(resetTag.asStartTag().getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("This is a comment");

        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.asComment().getData());
        assertFalse(resetComment.isComment());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().append('A');

        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.asCharacter().getData());
        assertFalse(resetCharacter.isCharacter());
    }

    @Test
    public void testTokenTypeMismatches() {
        Token doctype = new Token.Doctype();
        Token tag = new Token.StartTag();

        assertFalse(doctype.isStartTag());
        assertFalse(tag.isDoctype());
    }
}