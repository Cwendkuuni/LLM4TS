package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    // Test read method
    @Test
    public void testRead() {
        // Create Tokeniser instance
        CharacterReader reader = new CharacterReader("test");
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test reading token
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    // Test emit method
    @Test
    public void testEmit() {
        // Create Tokeniser instance
        CharacterReader reader = new CharacterReader("test");
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test emitting token
        Token token = new Token.StartTag();
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    // Test acknowledgeSelfClosingFlag method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Create Tokeniser instance
        CharacterReader reader = new CharacterReader("test");
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test acknowledging self closing flag
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.isSelfClosingFlagAcknowledgged());
    }

    // Add more test cases for other methods

}