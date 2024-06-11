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
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
    }

    // Add more test cases for other methods in the Token class

}