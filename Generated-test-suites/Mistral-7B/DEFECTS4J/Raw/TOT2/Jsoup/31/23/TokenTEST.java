package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        // TODO: Test for getName(), getPublicIdentifier(), systemIdentifier, and isForceQuirks() methods
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag("html");
        assertEquals("StartTag", token.tokenType());
        assertEquals("html", token.name());
        // TODO: Test for newAttribute(), finaliseTag(), attributes, isSelfClosing, and toString() methods
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag("html");
        assertEquals("EndTag", token.tokenType());
        assertEquals("html", token.name());
        // TODO: Test for toString() method
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        // TODO: Test for getData() and toString() methods
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character("a");
        assertEquals("Character", token.tokenType());
        assertEquals("a", token.getData());
        // TODO: Test for toString() method
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
        // No methods to test in this token
    }

    @Test
    public void testEmptyDoctype() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        // TODO: Test for empty getName(), getPublicIdentifier(), systemIdentifier, and isForceQuirks() methods
    }

    @Test
    public void testEmptyStartTag() {
        Token token = new Token.StartTag("");
        assertEquals("StartTag", token.tokenType());
        // TODO: Test for empty name(), newAttribute(), finaliseTag(), attributes, isSelfClosing, and toString() methods
    }

    @Test
    public void testEmptyEndTag() {
        Token token = new Token.EndTag("");
        assertEquals("EndTag", token.tokenType());
        // TODO: Test for empty name(), and toString() methods
    }

    @Test
    public void testCommentWithEmptyData() {
        Token token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        // TODO: Test for empty getData() method
    }

    @Test
    public void testCommentWithMalformedSyntax() {
        Token token = new Token.Comment("<!--");
        assertEquals("Comment", token.tokenType());
        // TODO: Test for getData() and toString() methods
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTagWithInvalidAttributeName() {
        Token token = new Token.StartTag("div").newAttribute(" attributeName");
        // TODO: Test for attributes.put() method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTagWithRedundantAttribute() {
        Token token = new Token.StartTag("div").name("div").newAttribute("class").newAttribute("class");
        // TODO: Test for attributes.put() method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTagWithMalformedAttributeValue() {
        Token token = new Token.StartTag("img").newAttribute("src").appendAttributeValue("<!--").appendAttributeValue("-->");
        // TODO: Test for appendAttributeValue() and attributes.put() methods
    }

    @Test(expected = IllegalStateException.class)
    public void testMismatchedTags() {
        Token startTag = new Token.StartTag("div");
        Token endTag = new Token.EndTag("p");
        startTag.newAttribute("class", "testclass");
        // TODO: Test for isDoctype(), isStartTag(), isEndTag(), isComment(), and isCharacter() methods
    }

    @Test(expected = IllegalStateException.class)
    public void testSelfClosingTagWithLessThanSign() {
        Token token = new Token.StartTag("img").isSelfClosing(true).newAttribute("src").appendAttributeValue("<");
        // TODO: Test for appendAttributeValue() method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTagWithInvalidCharacters() {
        Token token = new Token.StartTag("div").appendTagName('$').appendAttributeName("class").newAttribute("class");
        // TODO: Test for appendTagName(), appendAttributeName(), and attributes.put() methods
    }
}