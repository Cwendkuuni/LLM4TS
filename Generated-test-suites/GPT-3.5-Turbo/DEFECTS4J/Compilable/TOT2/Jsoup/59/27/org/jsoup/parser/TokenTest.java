package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeGetName() {
        assertEquals("", doctypeToken.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        assertNull(doctypeToken.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        assertEquals("", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        assertEquals("", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        assertFalse(startTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagToString() {
        assertEquals("</>", endTagToken.toString());
    }

    @Test
    public void testCommentGetData() {
        assertEquals("", commentToken.getData());
    }

    @Test
    public void testCharacterGetData() {
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFReset() {
        assertSame(eofToken, eofToken.reset());
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctypeToken.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertTrue(startTagToken.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertTrue(endTagToken.isEndTag());
    }

    @Test
    public void testIsComment() {
        assertTrue(commentToken.isComment());
    }

    @Test
    public void testIsCharacter() {
        assertTrue(characterToken.isCharacter());
    }

    @Test
    public void testIsEOF() {
        assertTrue(eofToken.isEOF());
    }
}