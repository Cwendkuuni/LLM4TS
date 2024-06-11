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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() {
        Mockito.when(reader.current()).thenReturn((char) 0);
        Mockito.when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadValidCharacter() {
        Mockito.when(reader.current()).thenReturn('a');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.asCharacter().getEntityValue());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingTag() {
        String input = "<img src=\"image.png\" alt=\"Image\" />";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        Token startTag = tag;
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("img", startTag.tagName);
        assertTrue(startTag.isSelfClosing());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("img", token.tagName);
        assertFalse(token.isSelfClosing());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadInvalidCharacterReference() {
        Mockito.when(reader.current()).thenReturn('#');

        Character referenceChar = tokeniser.consumeCharacterReference(null, false);
        assertNull(referenceChar);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.CHAR_REF, error.getType());
        assertEquals(1, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        assertEquals("invalid named referenece'", error.getMessage());
    }

    @Test
    public void testReadValidCharacterReference() {
        Mockito.when(reader.current()).thenReturn('#').thenReturn('x').thenReturn(';');

        Character referenceChar = tokeniser.consumeCharacterReference('X', false);
        assertEquals('\u0030', referenceChar);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadValidStartTag() {
        String input = "<html lang=\"en\">";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("html", tag.tagName);
        assertFalse(tag.isSelfClosing());

        Token attributes = tag;
        while (tag.nextSibling() != null) {
            tag = tag.nextSibling();
            if (tag.type == Token.TokenType.Attribute) {
                attributes = tag;
            }
        }
        assertEquals("lang", attributes.getAttributeName());
        assertEquals("en", attributes.getAttributeValue());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadEndTagMismatch() {
        String input = "<html lang=\"en\"><body><p>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        Token startTag = tag;
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("html", startTag.tagName);
        assertFalse(startTag.isSelfClosing());

        tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("body", tag.tagName);
        assertFalse(tag.isSelfClosing());

        tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertEquals("p", tag.tagName);
        assertFalse(tag.isSelfClosing());

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertEquals("html", tag.tagName); // should be body
        assertFalse(tag.isSelfClosing());

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.TAG_MISMATCH, error.getType());
        assertEquals(1, error.getLineNumber());
        assertEquals(6, error.getColumnNumber());
        assertEquals("Unexpected end tag: html", error.getMessage());
    }

    @Test
    public void testReadEof() {
        String input = "<html lang=\"en\">";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        Token startTag = tag;
        assertEquals(Token

 .TokenType.StartTag, tag.type);
        assertEquals("html", startTag.tagName);
        assertFalse(startTag.isSelfClosing());

        tag = tokeniser.read();
        assertEquals(Token.TokenType.EndOfFile, tag.type);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.EOF, error.getType());
        assertEquals(1, error.getLineNumber());
        assertEquals(input.length() + 1, error.getColumnNumber());
        assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
    }

    @Test
    public void testReadInvalidState() {
        Mockito.when(reader.current()).thenReturn('>');

        tokeniser = new Tokeniser(reader, errors);

        tokeniser.getState().transition(TokeniserState.Comment);

        assertEquals(ParseError.Type. expectation_failed, errors.get(0).getType());
        assertEquals(1, errors.size());
    }
}