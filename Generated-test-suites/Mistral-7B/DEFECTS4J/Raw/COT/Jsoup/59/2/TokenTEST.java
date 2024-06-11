package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    

    // Basic test cases for each method in the Token class

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertNull(token.type);
        assertNull(token.getName());
        assertNull(token.getPubSysKey());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token token = new Token.StartTag();
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token token = new Token.Comment();
        token.reset();
        assertNull(token.data);
        assertFalse(token.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token token = new Token.Character();
        token.reset();
        assertNull(token.data);
    }

    @Test
    public void testEOFReset() {
        Token token = new Token.EOF();
        token.reset();
        assertNull(token.type);
    }

    // Test cases for Token.Doctype method

    @Test
    public void testDoctypeSetName() {
        Token token = new Token.Doctype();
        token.name("html");
        assertEquals("html", token.getName());
    }

    @Test
    public void testDoctypeSetPublicIdentifier() {
        Token token = new Token.Doctype();
        token.getPublicIdentifier().append("public");
        assertEquals("public", token.getPublicIdentifier().toString());
    }

    @Test
    public void testDoctypeSetSystemIdentifier() {
        Token token = new Token.Doctype();
        token.getSystemIdentifier().append("system");
        assertEquals("system", token.getSystemIdentifier().toString());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token token = new Token.Doctype();
        token.isForceQuirks(true);
        assertTrue(token.isForceQuirks());
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctypeSetNameAfterNameSet() {
        Token token = new Token.Doctype();
        token.name("html");
        token.name("body");
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctypeSetPublicIdentifierAfterNameSet() {
        Token token = new Token.Doctype();
        token.name("html");
        token.getPublicIdentifier().append("public");
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctypeSetSystemIdentifierAfterNameSet() {
        Token token = new Token.Doctype();
        token.name("html");
        token.getSystemIdentifier().append("system");
    }

    @Test
    public void testDoctypeToString() {
        Token token = new Token.Doctype();
        token.name("html");
        token.getPublicIdentifier().append("public");
        token.getSystemIdentifier().append("system.html");
        token.isForceQuirks(true);
        String expected = "<!DOCTYPE html PUBLIC \"public\" SYSTEM \"system.html\">\n";
        assertEquals(expected, token.toString());
    }

    // Test cases for Token.Tag (StartTag and EndTag) methods

    @Test
    public void testTagSetName() {
        Token token = new Token.StartTag();
        token.name("html");
        assertEquals("html", token.name());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token token = new Token.StartTag();
        token.setEmptyAttributeValue();
        assertTrue(token.hasEmptyAttributeValue);
    }

    @Test
    public void testTagSetAttribute() {
        Token token = new Token.StartTag();
        token.appendAttributeName("class");
        token.appendAttributeValue("test");
        assertEquals("class", token.pendingAttributeName);
        assertEquals("test", token.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetAttributeWithS() {
        Token token = new Token.StartTag();
        token.appendAttributeName("class");
        token.pendingAttributeValueS = "test";
        assertEquals("class", token.pendingAttributeName);
        assertEquals("test", token.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetAttributeValueWithAppender() {
        Token token = new Token.StartTag();
        token.appendAttributeName("class");
        token.appendAttributeValue("test");
        token.appendAttributeValue(" another");
        assertEquals("test another", token.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetAttributeValueWithAppenderCodepoints() {
        Token token = new Token.StartTag();
        token.appendAttributeName("class");
        token.appendAttributeValue("test");
        token.appendAttributeValue(" another");
        char[] codepoints = {' ', 'a', 'n', 'o', 't', 'h', 'e', 'r'};
        token.appendAttributeValue(codepoints);
        assertEquals("test another other", token.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetSelfClosing() {
        Token token = new Token.StartTag();
        token.isSelfClosing(true);
        assertTrue(token.isSelfClosing());
    }

    @Test
    public void testTagSetAttributes() {
        Token token = new Token.StartTag();
        Attributes attributes = new Attributes();
        token.attributes = attributes;
        assertEquals(attributes, token.getAttributes());
    }

    @Test
    public void testTagToString() {
        Token token = new Token.StartTag();
        token.name("html");
        token.appendAttributeName("class").appendAttributeValue("test");
        token.appendAttributeName("style").appendAttributeValue("background-color: red");
        token.isSelfClosing(true);
        String expected = "<html class=\"test\" style=\"background-color: red\" />\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithEmptyAttributeValue() {
        Token token = new Token.StartTag();
        token.name("html");
        token.appendAttributeName("class");
        token.setEmptyAttributeValue();
        String expected = "<html class=\"\" />\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithNoAttributes() {
        Token token = new Token.StartTag();
        token.name("html");
        String expected = "<html>\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithAttributes() {
        Token token = new Token.StartTag();
        token.name("html");
        token.appendAttributeName("class").appendAttributeValue("test");
        token.appendAttributeName("style").appendAttributeValue("background-color: red");
        String expected = "<html class=\"test\" style=\"background-color: red\" />\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithSelfClosingAndAttributes() {
        Token token = new Token.StartTag();
        token.name("img");
        token.appendAttributeName("src").appendAttributeValue("image.png");
        token.appendAttributeName("alt").appendAttributeValue("alt text");
        token.isSelfClosing(true);
        String expected = "<img src=\"image.png\" alt=\"alt text\" />\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithEmptyName() {
        Token token = new Token.StartTag();
        token.name("");
        String expected = "<>\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithNullName() {
        Token token = new Token.StartTag();
        token.name(null);
        String expected = "<>\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testTagToStringWithNullAttributes() {
        Token token = new Token.StartTag();
        token.name("html");
        token.attributes = null;
        String expected = "<html>\n";
        assertEquals(expected, token.toString());
    }

    // Test cases for Token.Comment method

    @Test
    public void testCommentSetData() {
        Token token = new Token.Comment();
        token.data("This is a comment.");
        assertEquals("This is a comment.", token.getData());
    }

    @Test
    public void testCommentToString() {
        Token token = new Token.Comment();
        token.data("This is a comment.");
        String expected = "<!--This is a comment.-->";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testCommentToStringWithBogus() {
        Token token = new Token.Comment();
        token.bogus(true);
        String expected = "<!--\n";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testCommentToStringWithBogusAndData() {
        Token token = new Token.Comment();
        token.bogus(true);
        token.data("This is a comment.");
        String expected = "<!--\nThis is a comment.\n-->";
        assertEquals(expected, token.toString());
    }

    // Test cases for Token.Character method

    @Test
    public void testCharacterSetData() {
        Token token = new Token.Character();
        token.data("A");
        assertEquals("A", token.getData());
    }

    @Test
    public void testCharacterToString() {
        Token token = new Token.Character();
        token.data("A");
        String expected = "A";
        assertEquals(expected, token.toString());
    }

    // Test cases for Token.EOF method

    @Test
    public void testEOFToString() {
        Token token = new Token.EOF();
        String expected = "";
        assertEquals(expected, token.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

    
}