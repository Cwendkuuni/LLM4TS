package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().setForceQuirks(true);
        assertEquals("html", doctype.asDoctype().getName());
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.asStartTag().attributes = new Attributes();
        tag.asStartTag().attributes.put("class", "test");
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.asStartTag().attributes);
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.StartTag();
        tag.name("div");
        tag.asStartTag().attributes = new Attributes();
        tag.asStartTag().attributes.put("class", "test");
        assertEquals("div", tag.name());
        assertEquals("test", tag.asStartTag().attributes.get("class"));
    }

    @Test
    public void testTagSelfClosing() {
        Token tag = new Token.StartTag();
        tag.asStartTag().isSelfClosing = true;
        assertTrue(tag.asStartTag().isSelfClosing());

        newToken(Token.EndTag.class).asEndTag().isSelfClosing = false;
        assertFalse(newToken(Token.EndTag.class).asEndTag().isSelfClosing());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData("test comment");
        comment.reset();
        assertNull(comment.asComment().getData());
    }

    @Test
    public void testCommentGetters() {
        Token comment = new Token.Comment();
        comment.asComment().getData("test comment");
        assertEquals("test comment", comment.asComment().getData());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("test");
        character.reset();
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCharacterGetters() {
        Token character = new Token.Character();
        character.asCharacter().data("test");
        assertEquals("test", character.asCharacter().getData());
    }

    @Test
    public void testCDataReset() {
        Token cdata = new Token.CData("test");
        cdata.reset();
        assertNull(cdata.asCharacter().getData());
    }

    @Test
    public void testCDataGetters() {
        Token cdata = new Token.CData("test");
        assertEquals("<![CDATA[test]]>", cdata.toString());
        assertEquals("test", cdata.asCharacter().getData());
    }

    private <T extends Token> T newToken(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating new token", e);
        }
    }

}