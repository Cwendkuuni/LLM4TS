
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token.StartTag startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token.EndTag endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token.Comment commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token.Character characterToken = new Token.Character("");
        assertEquals("Character", characterToken.tokenType());

        Token.EOF eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name.append("example");
        assertEquals("example", doctypeToken.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.publicIdentifier.append("public");
        assertEquals("public", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.systemIdentifier.append("system");
        assertEquals("system", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.tagName = "example";
        assertEquals("example", startTagToken.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTagToken = new Token.StartTag();
        assertFalse(startTagToken.isSelfClosing());

        startTagToken.selfClosing = true;
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.attributes = new Attributes();
        assertEquals(0, startTagToken.getAttributes().size());

        startTagToken.attributes.put(new Attribute("attr1", "value1"));
        assertEquals(1, startTagToken.getAttributes().size());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName = "example";
        assertEquals("example", endTagToken.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data.append("example");
        assertEquals("example", commentToken.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character characterToken = new Token.Character("data");
        assertEquals("data", characterToken.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertTrue(doctypeToken.isDoctype());

        Token.StartTag startTagToken = new Token.StartTag();
        assertFalse(startTagToken.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTagToken = new Token.StartTag();
        assertTrue(startTagToken.isStartTag());

        Token.EndTag endTagToken = new Token.EndTag();
        assertFalse(endTagToken.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTagToken = new Token.EndTag();
        assertTrue(endTagToken.isEndTag());

        Token.Comment commentToken = new Token.Comment();
        assertFalse(commentToken.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment commentToken = new Token.Comment();
        assertTrue(commentToken.isComment());

        Token.Character characterToken = new Token.Character("");
        assertFalse(characterToken.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character characterToken = new Token.Character("");
        assertTrue(characterToken.isCharacter());

        Token.Doctype doctypeToken = new Token.Doctype();
        assertFalse(doctypeToken.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eofToken = new Token.EOF();
        assertTrue(eofToken.isEOF());

        Token.StartTag startTagToken = new Token.StartTag();
        assertFalse(startTagToken.isEOF());
    }
}
