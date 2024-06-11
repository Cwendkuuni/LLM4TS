import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private ParseErrorList errors;
    private CharacterReader reader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        reader = Mockito.mock(CharacterReader.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() throws IOException {
        Mockito.when(reader.isEmpty()).thenReturn(true);
        Token expectedToken = null;
        Token actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).isEmpty();
    }

    @Test
    public void testReadSingleCharacter() throws IOException {
        Mockito.when(reader.current()).thenReturn('a');
        Mockito.when(reader.read()).thenReturn('a');

        StringBuilder charBuffer = new StringBuilder();
        tokeniser.charBuffer = charBuffer;
        Token expectedToken = new Token.Character("a");
        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals("a", charBuffer.toString());
        Mockito.verify(reader).read();
    }

    @Test
    public void testReadSelfClosingTag() throws IOException {
        Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn('/').thenReturn('>');
        tokeniser.selfClosingFlagAcknowledged = false;

        Token expectedToken = new Token.StartTag(new Token.TagName("img"));
        expectedToken.setSelfClosing(true);
        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).current().equals('<');
        Mockito.verify(reader).current().equals('/');
        Mockito.verify(reader).current().equals('i');
        Mockito.verify(reader).current().equals('m');
        Mockito.verify(reader).current().equals('/');
        Mockito.verify(reader).current().equals('>');
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testReadEndTag() throws IOException {
        Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('h').thenReturn('1').thenReturn('>');
        Token expectedToken = new Token.EndTag(new Token.TagName("h1"));

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).current().equals('<');
        Mockito.verify(reader).current().equals('/');
        Mockito.verify(reader).current().equals('h');
        Mockito.verify(reader).current().equals('1');
        Mockito.verify(reader).current().equals('>');
    }

    @Test
    public void testReadComment() throws IOException {
        Mockito.when(reader.current()).thenReturn('<!--').thenReturn(Character.MIN_VALUE).thenReturn(Character.MIN_VALUE).thenReturn('-->');

        StringBuilder charBuffer = new StringBuilder();
        tokeniser.charBuffer = charBuffer;
        Token expectedToken = new Token.Comment("");

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals("<!--", charBuffer.toString());
        Mockito.verify(reader).current().equals('<!--');
        Mockito.verify(reader).current().equals(Character.MIN_VALUE);
        Mockito.verify(reader).current().equals(Character.MIN_VALUE);
        Mockito.verify(reader).current().equals('-->');
    }

    @Test
    public void testReadCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&#123;".getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        Character expectedCharacter = '\u007B';
        Token expectedToken = new Token.Character(String.valueOf(expectedCharacter));

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedCharacter, actualToken.getValue());
        Mockito.verify(reader).consume(ArgumentMatchers.eq('#'));
        Mockito.verify(reader).consumeHexSequence();
        Mockito.verify(reader).matchConsume(';');
    }

    @Test
    public void testReadNamedCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&copy;".getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        Character expectedCharacter = '©';
        Token expectedToken = new Token.Character(String.valueOf(expectedCharacter));

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedCharacter, actualToken.getValue());
        Mockito.verify(reader).consumeLetterThenDigitSequence();
        Mockito.verify(reader).matches(';');
        Mockito.verify(reader).rewindToMark();
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&#xabcd;".getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser.read();
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidNamedCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&abcd;".getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        tokeniser.read();
    }

    @Test
    public void testReadString() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("abcdef".getBytes());
        Mockito.when(reader.inputStream()).thenReturn(inputStream);

        StringBuilder charBuffer = new StringBuilder();
        tokeniser.charBuffer = charBuffer;
        String expectedString = "abcdef";
        Token expectedToken = new Token.Text(expectedString);

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals(expectedString, actualToken.getValue());
        Mockito.verify(reader).consume(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        Mockito.verify(reader).matchConsume(';');
    }

    @Test
    public void testReadSelfClosingTagWithAttributes() throws IOException {
        Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn(' ').thenReturn('s').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('=')
                .thenReturn('"').thenReturn('h').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn('"').thenReturn('/').thenReturn('>');

        Token expectedToken = new Token.StartTag(new Token.TagName("img"));
        expectedToken.setSelfClosing(true);
        expectedToken.setAttributes(new ArrayList<>());
        expectedToken.getAttributes().add(new Token.Attribute("src", "hello"));

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).current().equals('<');
        Mockito.verify(reader).current().equals('/');
        Mockito.verify(reader).current().equals('i');
        Mockito.verify(reader).current().equals('m');
        Mockito.verify(reader).current().equals('g');
        Mockito.verify(reader).current().equals(' ');
        Mockito.verify(reader).current().equals('s');
        Mockito.verify(reader).current().equals('r');
        Mockito.verify(reader).current().equals('i');
        Mockito.verify(reader).current().equals('p');
        Mockito.verify(reader).current().equals('=');
        Mockito.verify(reader).current().equals('"');
        Mockito.verify(reader).consume('h');
        Mockito.verify(reader).consume('e');
        Mockito.verify(reader).consume('l');
        Mockito.verify(reader).consume('l');
        Mockito.verify(reader).consume('o');
        Mockito.verify(reader).current().equals('"');
        Mockito.verify(reader).current().equals('/');
        Mockito.verify(reader).current().equals('>');
    }

    @Test
    public void testErrorHandling() throws IOException {
        Mockito.when(reader.inputStream()).thenReturn(new ByteArrayInputStream("&abcd;".getBytes()));

        tokeniser.read();

        ParseError expectedError = new ParseError(reader.pos(), "Invalid named reference: 'abcd'");
        assertEquals(1, errors.size());
        assertEquals(expectedError, errors.get(0));
    }

    @Test
    public void testTransitionToErrorState() throws IOException {
        Mockito.when(reader.current()).thenReturn('a').thenReturn('b').thenReturn('c');
        tokeniser.transition(TokeniserState.Error);
        assertTrue(tokeniser.getState() == TokeniserState.Error);
        tokeniser.read(); // should not reach this line
    }

    @Test
    public void testEmitCharacter() throws IOException {
        Mockito.when(reader.current()).thenReturn('a');

        Token expectedToken = new Token.Character("a");
        tokeniser.emit(expectedToken);

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).current().equals('a');
    }

    @Test
    public void testEmitTag() throws IOException {
        Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('h').thenReturn('1').thenReturn('>');

        Token expectedToken = new Token.EndTag(new Token.TagName("h1"));
        tokeniser.emit(expectedToken);

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        Mockito.verify(reader).current().equals('<').thenReturn('/').thenReturn('h').thenReturn('1').thenReturn('>');
    }

    @Test
    public void testEmitComment() throws IOException {
        Mockito.when(reader.current()).thenReturn('<!--').thenReturn(Character.MIN_VALUE).thenReturn(Character.MIN_VALUE).thenReturn('-->');

        StringBuilder charBuffer = new StringBuilder();
        tokeniser.charBuffer = charBuffer;
        Token expectedToken = new Token.Comment("");
        tokeniser.emitCommentPending();

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals("<!--", charBuffer.toString());
        Mockito.verify(reader).current().equals('<!--');
        Mockito.verify(reader).current().equals(Character.MIN_VALUE);
        Mockito.verify(reader).current().equals(Character.MIN_VALUE);
        Mockito.verify(reader).current().equals('-->');
    }

    @Test
    public void testEmitDoctype() throws IOException {
        Mockito.when(reader.current()).thenReturn("<!DOCTYPE html>");

        StringBuilder charBuffer = new StringBuilder();
        tokeniser.charBuffer = charBuffer;
        Token expectedToken = new Token.Doctype("<!DOCTYPE html>");
        tokeniser.emitDoctypePending();

        Token actualToken = tokeniser.read();

        assertEquals(expectedToken, actualToken);
        assertEquals("<!DOCTYPE html>", charBuffer.toString());
        Mockito.verify(reader).current().equals("<!DOCTYPE html>");
    }

    @Test
    public void testEmitTagWithError() throws IOException {
        Mockito.when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('h').thenReturn('1').thenReturn('>');
        Token expectedToken = new Token.EndTag(new Token.TagName("h1"));
        tokeniser.emitPendingTag(expectedToken);
        tokeniser.transition(TokeniserState.Error);

        Token actualToken = tokeniser.read();

        assertNull(actualToken);
        Mockito.verify(errors).canAddError();
        Mockito.verify(errors).add(new ParseError(reader.pos(), "Unexpected character '<' in error state"));
    }
}