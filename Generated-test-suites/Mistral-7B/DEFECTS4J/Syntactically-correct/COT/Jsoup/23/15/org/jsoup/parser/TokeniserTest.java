import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = mock(CharacterReader.class);
        errors = new ParseErrorList(new ArrayList<>());
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_StateNormal_ShouldReturnCharacterToken() {
        when(reader.current()).thenReturn('a');
        when(reader.read()).thenReturn(null);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);
    }

    @Test
    public void testRead_StateNormal_ShouldBufferCharsAndReturnWhenEndTagIsFound() {
        String input = "<h1>Test</h1>";
        when(reader.input()).thenReturn(input.toCharArray());
        when(reader.position()).thenReturn(0);

        tokeniser.read(); // read start tag "<h1"
        tokeniser.read(); // read tag name "h1"
        tokeniser.read(); // read ">"

        tokeniser.emit('a'); // buffer 'a'
        tokeniser.emit('b'); // buffer 'b'

        tokeniser.read(); // read "/h1"
        tokeniser.read(); // read "/"
        tokeniser.read(); // read "h"
        tokeniser.read(); // read "1"
        tokeniser.read(); // read ">"

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("h1", token.data);

        Character charToken = (Character) tokeniser.read();
        assertNull(charToken);

        List<Token> expectedTokens = new ArrayList<>();
        expectedTokens.add(new StartTag("h1"));
        expectedTokens.addAll(List.of(new Character('a'), new Character('b')));
        expectedTokens.add(new EndTag("h1"));

        assertEquals(expectedTokens, tokeniser.getState().tokens);
    }

    @Test
    public void testRead_StateError_ShouldThrowParseError() {
        when(reader.current()).thenThrow(new RuntimeException("Unexpected error"));

        try {
            tokeniser.read();
            fail();
        } catch (RuntimeException e) {
            // pass
        }

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character 'Unexpected error' in input state Data", error.getMessage());
        assertEquals(reader.pos(), error.getPos());
    }

    @Test
    public void testEmit_ShouldStoreTokenPending() {
        when(reader.current()).thenReturn('a');

        tokeniser.emit(new Character('a'));

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);

        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals("a", tokeniser.emitPending.data);
    }

    @Test
    public void testTransition_ShouldChangeState() {
        tokeniser.transition(TokeniserState.Script);

        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition_ShouldChangeStateAndAdvanceReader() {
        char[] input = {'<', 's', 'c', 'r', 'i', 'p', 't', '>', '\n'};
        when(reader.input()).thenReturn(input);
        when(reader.position()).thenReturn(0);

        tokeniser.transition(TokeniserState.Script);
        tokeniser.advanceTransition(TokeniserState.Data);

        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertEquals(1, reader.getPosition());
    }

    @Test
    public void testConsumeCharacterReference_ShouldReturnCharacterIfValid() {
        when(reader.current()).thenReturn('#');
        when(reader.matchConsume('X')).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("FC");
        when(reader.matchConsume(';')).thenReturn(true);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(character);
        assertEquals((char) 0xFC, character.data[0]);
    }

    @Test
    public void testConsumeCharacterReference_ShouldReturnNullIfInvalid() {
        when(reader.current()).thenReturn('&');
        when(reader.consumeLetterSequence()).thenReturn("amp");
        when(reader.matchConsume(';')).thenReturn(false);

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending_ShouldCreateNewTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
    }

    @Test
    public void testCreateEndTagPending_ShouldCreateNewEndTagPending() {
        tokeniser.createTagPending(false);
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.EndTag, tokeniser.tagPending.type);
    }

    @Test
    public void testEmitTagPending_ShouldStoreEndTagPendingAsEndTag() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("div", token.data);
    }

    @Test
    public void testEmitCommentPending_ShouldStoreCommentPendingAsComment() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.text = "-- Test";
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("-- Test", token.data);
    }

    @Test
    public void testCreateDoctypePending_ShouldCreateNewDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
        assertEquals(Token.TokenType.Doctype, tokeniser.doctypePending.type);
    }

    @Test
    public void testEmitDoctypePending_ShouldStoreDoctypePendingAsDoctype() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.publicId = "public";
        tokeniser.doctypePending.systemId = "system";
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("public", token.data.publicId);
        assertEquals("system", token.data.systemId);
    }

    @Test
    public void testError_ShouldStoreErrorMessage() {
        tokeniser.error(TokeniserState.Data);
        assertNotNull(errors);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character '.' in input state Data", error.getMessage());
    }
}