package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer; // For creating tokens

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype"); // Check initial state

        doctype.asDoctype().getName("html");
        doctype.asDoctype().reset();
        assertEquals("doctype", doctype.tokenType());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");

        doctype.asDoctype().getName().equals("html");
        doctype.asDoctype().getPublicIdentifier().equals("");
        doctype.asDoctype().getSystemIdentifier().equals("");
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());

        doctype.asDoctype().setPublicIdentifier("publicID");
        doctype.asDoctype().setSystemIdentifier("systemID");
        doctype.asDoctype().setForceQuirks(true);

        doctype.asDoctype().getPublicIdentifier().equals("publicID");
        doctype.asDoctype().getSystemIdentifier().equals("systemID");
        doctype.asDoctype().isForceQuirks(). equals(true);
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("tag").attributes(new Attributes());
        tag.asStartTag().name("tag2").attributes(new Attributes());

        tag.asStartTag().name().equals("tag2");

        tag.reset();
        assertEquals("tag", tag.asStartTag().name().toLowerCase());
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("tag").attributes(new Attributes().put("attr", "value"));

        tag.asStartTag().name().equals("tag");
        tag.asStartTag().normalName().equals("tag");
        tag.asStartTag().isSelfClosing().equals(false);

        tag.asStartTag().getAttributes().get("attr").getValue().equals("value");

        tag.asStartTag().name("Tag2").attributes(new Attributes().put("attr2", "value2"));
        tag.asStartTag().getAttributes().get("attr2").getValue().equals("value2");
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("");

        comment.asComment().getData("commentData");
        comment.reset();

        assertEquals("comment", comment.tokenType());
    }

    @Test
    public void testCommentGetter() {
        Token comment = new Token.Comment();
        comment.asComment().getData("commentData");

        comment.asComment().getData().equals("commentData");
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().equals("");

        character.asCharacter().getData("charData");
        character.reset();

        assertEquals("character", character.tokenType());
    }

    @Test
    public void testCharacterGetter() {
        Token character = new Token.Character();
        character.asCharacter().getData("charData");

        character.asCharacter().getData().equals("charData");
    }

    @Test(expected = Exception.class)
    public void testInvalidToken() {
        new Token("InvalidToken");
    }
}