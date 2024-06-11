package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TestName");
        doctype.reset();
        assertEquals("", doctype.getName());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("TestTag", new Attributes());
        assertEquals("TestTag", startTag.tagName);
        assertNotNull(startTag.getAttributes());
    }
}