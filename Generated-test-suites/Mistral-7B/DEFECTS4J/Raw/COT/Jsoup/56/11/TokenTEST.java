package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    // Basic test cases for public methods

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system.xml");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals(doctype.getClass(), resetDoctype.getClass());
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertTrue(resetDoctype.name.toString().isEmpty());
        assertTrue(resetDoctype.publicIdentifier.toString().isEmpty());
        assertTrue(resetDoctype.systemIdentifier.toString().isEmpty());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "container");

        Token.StartTag resetStartTag = startTag.reset();

        assertEquals(doGetClass(startTag), doGetClass(resetStartTag));
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.attributes);
    }

    // Edge cases and exception handling test cases

    @Test(expected = AssertionError.class)
    public void testEmptyDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        fail("Doctype reset should not return null");
    }

    @Test
    public void testDoctypeWithEmptyValuesReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("");
        doctype.systemIdentifier.append("");

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals(doctype.getClass(), resetDoctype.getClass());
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertTrue(resetDoctype.name.toString().isEmpty());
        assertTrue(resetDoctype.publicIdentifier.toString().isEmpty());
        assertTrue(resetDoctype.systemIdentifier.toString().isEmpty());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagWithEmptyValuesReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();

        assertEquals(doGetClass(startTag), doGetClass(resetStartTag));
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.attributes);
    }

    @Test(expected = AssertionError.class)
    public void testStartTagNullAttributesReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = null;

        Token.StartTag resetStartTag = startTag.reset();

        fail("StartTag reset should not return null");
    }

    @Test
    public void testStartTagWithSingleAttributeReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "container");

        Token.StartTag resetStartTag = startTag.reset();

        assertEquals(doGetClass(startTag), doGetClass(resetStartTag));
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.attributes);
    }

    @Test(expected = AssertionError.class)
    public void testStartTagWithMultipleAttributesReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "container");
        startTag.attributes.put("class", "container");

        Token.StartTag resetStartTag = startTag.reset();

        fail("StartTag reset should not return null");
    }

    // Helper method to get the class of an object
    private Class<?> doGetClass(Object obj) {
        try {
            return obj.getClass();
        } catch (ClassCastException e) {
            throw new AssertionError("Unexpected class: " + e.getMessage());
        }
    }

    // 
}