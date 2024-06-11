import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private InputStream inputStream;
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        inputStream = new ByteArrayInputStream("<html><head></head><body><script src=\"script.js\"></script></body></html>".getBytes());
        reader = new CharacterReader(inputStream);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadNormalCase() throws IOException {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName);

        token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadErrorCases() throws IOException {
        inputStream = new ByteArrayInputStream("<html><body><script src=\"script.js\"<script>//<![CDATA[</script>");
        reader = new CharacterReader(inputStream);
        tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <html>
        tokeniser.read(); // <body>

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName); // <script src=" should be <script>

        token = tokeniser.read();
        assertNull(token); // Should have error here

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(2, error.line);
        assertEquals(4, error.column);
        assertEquals("Unexpected character '<' in input state [DATA]", error.getMessage());

        inputStream = new ByteArrayInputStream("<html><head><script>//<![CDATA[</script></head></html>");
        reader = new CharacterReader(inputstream);
        tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <html>
        tokeniser.read(); // <head>

        token = tokeniser.read();
        assertNull(token); // Should have error here

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals(2, error.line);
        assertEquals(1, error.column);
        assertEquals("Unexpected character '<' in input state [DATA]", error.getMessage());

        inputStream = new ByteArrayInputStream("<html><head><script></script></head></html>");
        reader = new CharacterReader(inputstream);
        tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // <html>
        tokeniser.read(); // <head>

        tokeniser.read(); // <script>
        tokeniser.read(); // </script>

        token = tokeniser.read();
        assertNull(token); // Should have error here

        assertTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals(5, error.line);
        assertEquals(1, error.column);
        assertEquals("Unexpected character '>' in input state [DATA]", error.getMessage());
    }

    @Test
    public void testEmissionNormalCase() throws IOException {
        Token token;

        token = new Token.StartTag("html");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = new Token.StartTag("head");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        token = new Token.EndTag("head");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = new Token.StartTag("body");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName);

        token = new Token.StartTag("script");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);

        token = new Token.EndTag("script");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);

        token = new Token.EndTag("body");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);

        token = new Token.EndTag("html");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmissionErrorCases() throws IOException {
        tokeniser = new Tokeniser(Mockito.mock(CharacterReader.class), errors);

        Token startTag = new Token.StartTag("html");
        tokeniser.emit(startTag);

        Token endTag = new Token.EndTag("html");
        tokeniser.emit(endTag);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("There is an unread token pending!", error.getMessage());

        token = new Token.StartTag("html");
        tokeniser.emit(token);

        token = new Token.EndTag("head");
        tokeniser.emit(token);

        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals("There is an unread token pending!", error.getMessage());

        token = new Token.EndTag("head");
        tokeniser.emit(token);

        token = new Token.StartTag("html");
        tokeniser.emit(token);

        token = new Token.StartTag("head");
        tokeniser.emit(token);

        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals("There is an unread token pending!", error.getMessage());

        token = new Token.EndTag("html");
        tokeniser.emit(token);

        token = new Token.StartTag("html");
        tokeniser.emit(token);

        token = new Token.StartTag("head");
        tokeniser.emit(token);

        token = new Token.EndTag("head");
        tokeniser.emit(token);

        token = new Token.EmptyTag("head");
        tokeniser.emit(token);

        token = new Token.StartTag("html");
        tokeniser.emit(token);

        token = new Token.StartTag("head");
        tokeniser.emit(token);

        token = new Token.EndTag("head");
        tokeniser.emit(token);

        token = new Token.EndTag("head");
        tokeniser.emit(token);

        assertEquals(1, errors.size());
        error = errors.get(0);
        assertEquals("Attributes incorrectly present on end tag", error.getMessage());
    }
}