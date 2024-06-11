
package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testDoctypeType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        Assert.assertEquals("test", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        Assert.assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        Assert.assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToString() {
        Token.Tag tag = new Token.StartTag("testTag");
        Assert.assertEquals("<testTag>", tag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.Tag tag = new Token.EndTag("testTag");
        Assert.assertEquals("</testTag>", tag.toString());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("testData");
        Assert.assertEquals("testData", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.Tag tag = new Token.StartTag("testTag");
        Assert.assertTrue(tag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.Tag tag = new Token.EndTag("testTag");
        Assert.assertTrue(tag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("testData");
        Assert.assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
}
