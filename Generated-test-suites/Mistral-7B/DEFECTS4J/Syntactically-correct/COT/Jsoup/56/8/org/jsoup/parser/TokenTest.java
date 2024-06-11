package org.jsoup.parser; // replace this package with the actual one if needed

import org.junit.Assert;
import org.junit.Test;

import static org.jsoup.parser.Token.*;
import static org.jsoup.parser.Token.TokenType.*;

public class TokenTest {
    // Basic test cases for Token class methods

    @Test
    public void testTokenType() {
        Token token = new Comment();
        Assert.assertEquals(COMMENT, token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.reset();
        Assert.assertNull(doctype.getName());
    }

    @Test
    public void testResetTag() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.reset();
        Assert.assertNull(tag.tagName);
    }

    @Test
    public void testResetComment() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        comment.reset();
        Assert.assertNull(comment.getData());
    }

    @Test
    public void testResetCharacter() {
        Character character = new Character();
        character.data("A");
        character.reset();
        Assert.assertNull(character.getData());
    }

    @Test
    public void testResetEOF() {
        EOF eof = new EOF();
        eof.reset();
        // EOF doesn't have any data to reset
    }

    // Test cases for Doctype methods

    @Test
    public void testNewDoctype() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier("public");
        Assert.assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier("system");
        Assert.assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.isForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
    }

    // Test cases for Tag methods

    @Test
    public void testNewTag() {
        StartTag tag = new StartTag();
        tag.name("div");
        Assert.assertEquals("div", tag.name());
    }

    @Test
    public void testNewTagWithAttributes() {
        StartTag tag = new StartTag();
        tag.name("input")
                .attributes(new Attributes().put("type", "text").put("name", "username"));
        Assert.assertEquals("input", tag.name());
        Assert.assertEquals("text", tag.getAttributes().getClass().getSimpleName());
        Assert.assertNotNull(tag.getAttributes().getValue("type"));
        Assert.assertNotNull(tag.getAttributes().getValue("name"));
    }

    @Test
    public void testFinaliseTag() {
        StartTag tag = new StartTag();
        tag.name("p");
        tag.finaliseTag();
        // Finalised tag doesn't have any attributes anymore
        Assert.assertNull(tag.getAttributes());
    }

    @Test(expected = AssertionError.class)
    public void testSettingEmptyTagName() {
        StartTag tag = new StartTag();
        tag.name("");
    }

    @Test
    public void testToStringStartTag() {
        StartTag tag = new StartTag().name("div").attributes(new Attributes().put("class", "class1 class2"));
        Assert.assertEquals("<div class=\"class1 class2\"></div>", tag.toString());
    }

    // Test cases for Character methods

    @Test
    public void testNewCharacter() {
        Character character = new Character();
        character.data("A");
        Assert.assertEquals("A", character.getData());
    }

    @Test
    public void testNewCharacterWithCodepoint() {
        Character character = new Character();
        character.data((char) 0x20AC); // Unicode UTF-8 for Euro sign
        Assert.assertEquals("\u20AC", character.getData());
    }

    // Test cases for Comment methods

    @Test
    public void testNewComment() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    // Test cases for EOF methods

    @Test
    public void testNewEOF() {
        EOF eof = new EOF();
        // EOF doesn't have any data to test
    }

    // Test for is* methods

    @Test
    public void testIsDoctype() {
        Token token = new Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Character();
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new EOF();
        Assert.assertTrue(token.isEOF());
    }
}