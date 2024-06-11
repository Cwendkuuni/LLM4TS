
package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("", doctype.getName());
    }

    @Test
    public void testFinaliseTag() {
        Token.Tag tag = new Token.EndTag();
        tag.finaliseTag();
        assertEquals("", tag.name());
    }

    // Add other test cases for public methods in the Token class

}
