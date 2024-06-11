import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testReadSimpleCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('b', token.data);

        // ...
    }

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(null, token);

        // ...
    }

    @Test
    public void testReadStartTag() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.data.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('>', token.data);

        // ...
    }

    @Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src='image.png' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.data.tagName);
        assertEquals(true, token.data.isSelfClosing);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('/', token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('>', token.data);

        // ...
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, false);
        assertEquals(1, codePoints.length);
        assertTrue(Character.isUnicodeIdentifierStart(codePoints[0]));
        assertEquals('\uD83D\uDE00', codePoints[0]);

        // ...
    }

    @Test
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&#%#;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codePoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codePoints);

        // ...
    }

    // Add test cases for other methods here
}