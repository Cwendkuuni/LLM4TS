import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() {
        when(reader.current()).thenReturn((char) 0);
        when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertNull(token);
        verify(reader, times(1)).advance();
    }

    @Test
    public void testReadSingleCharacter() {
        when(reader.current()).thenReturn('a');

        Token token = tokeniser.read();
        assertEquals("Character", Token.TokenType.Character, token.type);
        assertEquals("a", token.getData());
        verify(reader, times(1)).consume();
    }

    @Test
    public void testReadTags() {
        when(reader.current()).thenReturn('<');
        tokeniser.state = TokeniserState.Tag;

        Token startTag = tokeniser.read();
        assertEquals("StartTag", Token.TokenType.StartTag, startTag.type);
        verify(reader, times(1)).consume();

        tokeniser.transition(TokeniserState.TagName);
        tokeniser.emit("html");
        tokeniser.transition(TokeniserState.TagEnd);

        Token endTag = tokeniser.read();
        assertEquals("EndTag", Token.TokenType.EndTag, endTag.type);
        assertEquals("html", endTag.getData());
        verify(reader, times(2)).consume();
    }

    @Test
    public void testReadSelfClosingTag() {
        when(reader.current()).thenReturn('<');

        tokeniser.state = TokeniserState.Tag;
        tokeniser.transition(TokeniserState.TagName);
        tokeniser.emit("img");
        tokeniser.transition(TokeniserState.TagEnd);
        tokeniser.emit("/>");

        Token token = tokeniser.read();
        assertEquals("StartTag", Token.TokenType.StartTag, token.type);
        assertEquals("img", token.getData());
        assertTrue(token.isSelfClosing());
        verify(reader, times(1)).consume();
    }

    @Test
    public void testReadScriptTag() {
        when(reader.current()).thenReturn('<');

        tokeniser.state = TokeniserState.Tag;
        tokeniser.transition(TokeniserState.TagName);
        tokeniser.emit("script");
        tokeniser.transition(TokeniserState.ScriptData);

        tokeniser.emit("type=\"text/javascript\"");
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        tokeniser.emit("</script>");
        Token endTag = tokeniser.read();
        verify(reader, times(1)).consume();

        assertEquals("EndTag", Token.TokenType.EndTag, endTag.type);
        assertEquals("script", endTag.getData());
    }

    @Test
    public void testReadComments() {
        when(reader.current()).thenReturn('<').thenReturn('/').thenReturn('!').thenReturn('--');

        tokeniser.state = TokeniserState.Comment;
        tokeniser.createCommentPending();

        Token comment = tokeniser.read();
        assertEquals("Comment", Token.TokenType.Comment, comment.type);
        assertEquals("<!--", comment.getData());
        verify(reader, times(3)).consume();

        tokeniser.emitCommentPending();
    }

    @Test
    public void testCharacterReference() {
        when(reader.current()).thenReturn('#').thenReturn('x').thenReturn('A').thenReturn(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u0041', character);
        verify(reader, times(1)).consume();
    }

    @Test
    public void testNumericCharacterReference() {
        when(reader.current()).thenReturn('#').thenReturn('x').thenReturn('3').thenReturn(';');

        Character character = tokeniser.consumeCharacterReference('\u0061', false);
        assertEquals('\u0061', character);
        verify(reader, times(1)).consume();

        character = tokeniser.consumeCharacterReference('\u0062', false);
        assertEquals('\u0062', character);
        verify(reader, times(2)).consume();

        character = tokeniser.consumeCharacterReference('\u0063', false);
        assertEquals('\u0063', character);
        verify(reader, times(3)).consume();

        character = tokeniser.consumeCharacterReference('\u0064', false);
        assertEquals('\u0064', character);
        verify(reader, times(4)).consume();
    }

    @Test
    public void testNamedCharacterReference() {
        when(reader.current()).thenReturn('#').thenReturn('x').thenReturn('a').thenReturn(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', character);
        verify(reader, times(1)).consume();

        when(reader.current()).thenReturn('a').thenReturn(';');

        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('a', character);
        verify(reader, times(2)).consume();
    }

    @Test
    public void testCharacterReferenceError() {
        when(reader.current()).thenReturn('#').thenReturn('x').thenReturn('A').thenReturn('B').thenReturn(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
        verify(tokeniser, times(1)).characterReferenceError("invalid named referenece 'AB'");
    }

    @Test
    public void testInvalidInput() {
        String input = "&<";

        reader = new CharacterReader(input);

        tokeniser = new Tokeniser(reader, errors);

        tokeniser.read();
        tokeniser.read();

        assertEquals(2, errors.size());
        List<ParseError> parseErrors = errors.getErrors();

        assertEquals("Unexpected character '<' in tag state", parseErrors.get(0).getMessage());
        assertEquals("Unexpected character '&' in tag state", parseErrors.get(1).getMessage());
    }

    @Test
    public void testEOF() {
        when(reader.isEmpty()).thenReturn(true);

        tokeniser.read();
        tokeniser.read();

        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, errors.size());
        List<ParseError> parseErrors = errors.getErrors();

        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseErrors.get(0).getMessage());
    }
}