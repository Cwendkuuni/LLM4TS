package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.parser.Token;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        // Add tests for other token types as needed
    }
}