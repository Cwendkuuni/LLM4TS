package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.TokenType.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.tokenType());
        Assert.assertNull(token.asDoctype().pubSysKey);
        Assert.assertNull(token.asDoctype().publicIdentifier.toString());
        Assert.assertNull(token.asDoctype().systemIdentifier.toString());
        Assert.assertFalse(token.asDoctype().isForceQuirks());

        token.asDoctype().name = "html";
        Assert.assertEquals("Doctype[html]", token.toString());

        token.asDoctype().pubSysKey = "PUBLIC";
        token.asDoctype().publicIdentifier.append("html");
        token.asDoctype().systemIdentifier.append("system.html");
        token.asDoctype().forceQuirks = true;

        Assert.assertEquals("Doctype[html system.html]", token.toString());
        Assert.assertEquals("html", token.asDoctype().getName());
        Assert.assertEquals("PUBLIC", token.asDoctype().getPubSysKey());
        Assert.assertEquals("html", token.asDoctype().getPublicIdentifier());
        Assert.assertEquals("system.html", token.asDoctype().getSystemIdentifier());
        Assert.assertTrue(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        Assert.assertEquals(StartTag.class.getSimpleName(), token.tokenType());
        Assert.assertNull(token.asStartTag().tagName);
        Assert.assertNull(token.asStartTag().normalName);
        Assert.assertNull(token.asStartTag().attributes);
        Assert.assertFalse(token.asStartTag().isSelfClosing());

        token.asStartTag().name("html");
        Assert.assertEquals("StartTag[html]", token.toString());
        Assert.assertEquals("html", token.asStartTag().name());
        Assert.assertEquals("html", token.asStartTag().normalName());

        token.asStartTag().attributes = new Attributes();
        token.asStartTag().attributes.put("id", "1");
        Assert.assertEquals("StartTag[html id=1]", token.toString());

        token.asStartTag().selfClosing = true;
        Assert.assertEquals("StartTag[html selfClosing=true]", token.toString());
        Assert.assertTrue(token.asStartTag().isSelfClosing());

        token.asStartTag().newAttribute();
        token.asStartTag().attributes.put("class", "class1 class2");
        Assert.assertEquals("StartTag[html class=class1 class2]", token.toString());

        token.asStartTag().finaliseTag();
        Assert.assertEquals("StartTag[html class=class1 class2]", token.toString());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        Assert.assertEquals(EndTag.class.getSimpleName(), token.tokenType());
        Assert.assertNull(token.asEndTag().tagName);
        Assert.assertNull(token.asEndTag().normalName);

        token.asEndTag().name("html");
        Assert.assertEquals("EndTag[html]", token.toString());
        Assert.assertEquals("html", token.asEndTag().name());
        Assert.assertEquals("html", token.asEndTag().normalName());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        Assert.assertEquals(Comment.class.getSimpleName(), token.tokenType());
        Assert.assertNull(token.asComment().data);

        token.asComment().data("-- This is a comment");
        Assert.assertEquals("Comment[-- This is a comment]", token.toString());
        Assert.assertEquals("-- This is a comment", token.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        Assert.assertEquals(Character.class.getSimpleName(), token.tokenType());
        Assert.assertNull(token.asCharacter().data);

        token.asCharacter().data("A");
        Assert.assertEquals("Character[A]", token.toString());
        Assert.assertEquals("A", token.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        Assert.assertEquals(EOF.class.getSimpleName(), token.tokenType());
        Assert.assertNull(token.asEOF());
        Assert.assertEquals("EOF", token.toString());
    }
}