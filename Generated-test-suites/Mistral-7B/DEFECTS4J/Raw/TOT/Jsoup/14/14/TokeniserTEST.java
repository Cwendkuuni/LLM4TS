import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setup() {
        reader = new CharacterReader(new ByteArrayInputStream("<html><head></head><body><script type='text/javascript'>function test() { alert('Hello World!'); }</script></body></html>".getBytes()));
        tokeniser = new Tokeniser(reader);
        errors = tokeniser.getErrors();
    }

    @Test
    public void testReadEmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadHtmlDocument() {
        Token token;

        // Read the HTML document
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Attribute, token.type);
        assertEquals("type", token.attributeName);
        assertEquals("text/javascript", token.attributeValue);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("script", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        assertNull(tokeniser.read());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingTag() {
        Token token;

        tokeniser.transition(TokeniserState.SelfClosingStartTag);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.tagName);
        assertTrue(token.selfClosing);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Attribute, token.type);
        assertEquals("src", token.attributeName);
        assertEquals("image.jpg", token.attributeValue);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("img", token.tagName);

        assertNull(tokeniser.read());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);

        // Read an invalid character reference
        tokeniser.consumeCharacterReference(null, false);
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).getMessage());
    }

    @Test
    public void testReadNamedCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);

        // Read a named character reference
        tokeniser.consumeCharacterReference("amp", false);
        assertEquals(Token.Character.AMPERSAND, tokeniser.read().character);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);

        // Read an invalid named character reference
        tokeniser.consumeCharacterReference("invalid", false);
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference", errors.get(0).getMessage());
    }

    @Test
    public void testReadComment() {
        tokeniser.transition(TokeniserState.Comment);

        // Read a comment
        tokeniser.emitCommentPending();
        assertEquals(Token.TokenType.Comment, tokeniser.read().type);
        assertEquals("<!--", tokeniser.read().tagName);
        assertEquals(" -->", tokeniser.read().tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadDoctypeDeclaration() {
        tokeniser.transition(TokeniserState.Doctype);

        // Read a doctype declaration
        tokeniser.createDoctypePending();
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.emitDoctypePending();
        assertEquals(Token.TokenType.Doctype, tokeniser.read().type);
        assertEquals("DOCTYPE", tokeniser.read().tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadUnexpectedEndOfFile() {
        tokeniser.transition(TokeniserState.Data);

        // Read unexpected end of file
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
    }
}