package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("value");

        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();
        endTag.appendAttributeName("attribute");

        endTag.reset();

        assertNull(endTag.tagName);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testEofReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertTrue(true); // Just checking that it resets without throwing an exception
    }

    // Include more test cases for other methods and edge cases as needed

}