package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        // Mock reader and errors
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyInput_ExpectNoToken() {
        Token token = tokeniser.read();

        assert(token == null);
    }

    @Test
    public void testRead_SelfClosingFlagNotAcknowledged_ErrorThrown() {
        // Set up the tokeniser to not acknowledge self closing flag
        tokeniser.acknowledgeSelfClosingFlag(); // Set flag to false

        Token token = tokeniser.read();

        // Ensure that an error is thrown
        // Depending on the ParseErrorList implementation, we can further check if the error message is correct
        assert(token == null);
        assert(tokeniser.getErrors().get(0).getMessage().equals("Self closing flag not acknowledged"));
    }

    @Test
    public void testEmit_CharacterToken_ExpectTokenEmitted() {
        // Create a character token
        Token token = new Token.Character();

        tokeniser.emit(token);

        // Ensure that the emitted token is the same as the one emitted
        assert(tokeniser.read() == token);
    }

    @Test
    public void testEmit_String_EmptyStringBuilder_ExpectStringEmitted() {
        // Set up the tokeniser with an empty string builder
        tokeniser.emit("");

        // Ensure that the emitted token is a character token with the empty string
        Token token = tokeniser.read();
        assert(token != null);
        assert(token instanceof Token.Character);
        assert(((Token.Character) token).getData().equals(""));
    }

    @Test
    public void testEmit_String_NonEmptyStringBuilder_ExpectStringBuilderContentsEmitted() {
        // Set up the tokeniser with a non-empty string builder
        tokeniser.emit("<tag>");

        // Ensure that the emitted token is a character token with the string "<tag>"
        Token token = tokeniser.read();
        assert(token != null);
        assert(token instanceof Token.Character);
        assert(((Token.Character) token).getData().equals("<tag>"));
    }

    @Test
    public void testEmit_CharArray_ExpectCharArrayEmitted() {
        // Set up the tokeniser with a character array
        tokeniser.emit(new char[]{'<', 't', 'a', 'g', '>'});

        // Ensure that the emitted token is a character token with the character array
        Token token = tokeniser.read();
        assert(token != null);
        assert(token instanceof Token.Character);
        assert(((Token.Character) token).getData().equals("<tag>"));
    }

    @Test
    public void testEmit_Codepoints_ExpectCodepointsEmitted() {
        // Set up the tokeniser with an array of codepoints
        tokeniser.emit(new int[]{60, 116, 97, 103, 62});

        // Ensure that the emitted token is a character token with the codepoints
        Token token = tokeniser.read();
        assert(token != null);
        assert(token instanceof Token.Character);
        assert(((Token.Character) token).getData().equals("<tag>"));
    }

    @Test
    public void testEmit_Char_ExpectCharEmitted() {
        // Set up the tokeniser with a character
        tokeniser.emit('<');

        // Ensure that the emitted token is a character token with the character
        Token token = tokeniser.read();
        assert(token != null);
        assert(token instanceof Token.Character);
        assert(((Token.Character) token).getData().equals("<"));
    }

    // Add more test cases for the other methods in the Tokeniser class

}