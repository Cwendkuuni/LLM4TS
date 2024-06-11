package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype"); // assert initial state

        doctype.asDoctype().getName("html");
        doctype.asDoctype().getName().equals("html");

        doctype.reset();
        doctype.asDoctype().getName().equals("doctype");
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().getPublicIdentifier().equals("");
        doctype.asDoctype().getSystemIdentifier().equals("");
        doctype.asDoctype().isForceQuirks().equals(false);
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name().equals("starttag");

        tag.asStartTag().name("div");
        tag.asStartTag().name().equals("div");

        tag.reset();
        tag.asStartTag().name().equals("starttag");
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().normalName().equals("div");

        tag.asStartTag().selfClosing(true);
        tag.asStartTag().isSelfClosing().equals(true);

        tag.reset();
        tag.asStartTag().name().equals("starttag");
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");

        tag.asStartTag().newAttribute().name("class").value("foo");
        tag.asStartTag().attributes().get("class").equals("foo");

        tag.asStartTag().newAttribute().name("id").value("bar");
        tag.asStartTag().attributes().get("id").equals("bar");

        tag.asStartTag().newAttribute().name("name").value("");
        tag.asStartTag().attributes().get("name").equals("");
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");

        tag.asStartTag().newAttribute().name("class").value("foo");
        tag.asStartTag().newAttribute().name("id").value("bar");

        tag.asStartTag().finaliseTag();

        tag.asStartTag().attributes() != null;
        tag.asStartTag().attributes().size() == 2;
    }

    @Test
    public void testTokenToString() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("div");

        assertTrue(startTag.toString().contains("<div>"));

        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("div");

        assertTrue(endTag.toString().contains("</div>"));

        Token comment = new Token.Comment();
        comment.asComment().data("Hello World!");

        assertTrue(comment.toString().contains("<!--Hello World!-->"));

        Token character = new Token.Character();
        character.asCharacter().data("!");

        assertTrue(character.toString().contains("!"));
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("");

        comment.asComment().getData("Hello World!");
        comment.asComment().getData().equals("Hello World!");

        comment.reset();
        comment.asComment().getData().equals("");
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token.Character();

        token.isDoctype().equals(false);
        token.isStartTag().equals(false);
        token.isEndTag().equals(false);
        token.isComment().equals(false);
        token.isCharacter().equals(true);
        token.isEOF().equals(false);

        token = new Token.Doctype();
        token.isDoctype().equals(true);
        token.isStartTag().equals(false);
        token.isEndTag().equals(false);
        token.isComment().equals(false);
        token.isCharacter().equals(false);
        token.isEOF().equals(false);

        token = new Token.StartTag();
        token.isDoctype().equals(false);
        token.isStartTag().equals(true);
        token.isEndTag().equals(false);
        token.isComment().equals(false);
        token.isCharacter().equals(false);
        token.isEOF().equals(false);

        token = new Token.EndTag();
        token.isDoctype().equals(false);
        token.isStartTag().equals(false);
        token.isEndTag().equals(true);
        token.isComment().equals(false);
        token.isCharacter().equals(false);
        token.isEOF().equals(false);

        token = new Token.Comment();
        token.isDoctype().equals(false);
        token.isStartTag().equals(false);
        token.isEndTag().equals(false);
        token.isComment().equals(true);
        token.isCharacter().equals(false);
        token.isEOF().equals(false);

        token = new Token.Character();
        token.isDoctype().equals(false);
        token.isStartTag().equals(false);
        token.isEndTag().equals(false);
        token.isComment().equals(false);
        token.isCharacter().equals(true);
        token.isEOF().equals(false);

        token = new Token.EOF();
        token.isDoctype().equals(false);
        token.isStartTag().equals(false);
        token.isEndTag().equals(false);
        token.isComment().equals(false);
        token.isCharacter().equals(false);
        token.isEOF().equals(true);
    }
}