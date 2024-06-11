package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser.TokeniserState;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    private List<Token> expectedTokens;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_GivenNoPendingToken_ShouldReadAndEmitNextToken() {
        when(reader.current()).thenReturn('a');

        Token actualToken = tokeniser.read();

        Token expectedToken = new Character("a");
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verify(reader).advance();
    }

    @Test
    public void testRead_GivenPendingToken_ShouldReturnPendingToken() {
        String pendingToken = "pending";
        tokeniser.emit(new Token.Character(pendingToken));
        tokeniser.isEmitPending = true;

        Token actualToken = tokeniser.read();

        Token expectedToken = new Character(pendingToken);
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testRead_GivenPendingCommentToken_ShouldReturnPendingCommentToken() {
        String commentText = "comment";
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token actualToken = tokeniser.read();

        Token expectedToken = new Comment(commentText);
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testRead_GivenPendingDoctypeToken_ShouldReturnPendingDoctypeToken() {
        String doctypeName = "doctype";
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token actualToken = tokeniser.read();

        Token expectedToken = new Doctype(doctypeName);
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testRead_GivenPendingTagToken_ShouldReturnPendingTagToken() {
        String tagName = "tag";
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token actualToken = tokeniser.read();

        Token expectedToken = new StartTag(tagName);
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testRead_GivenPendingEndTagToken_ShouldReturnPendingEndTagToken() {
        String tagName = "tag";
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();

        Token actualToken = tokeniser.read();

        Token expectedToken = new EndTag(tagName);
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testRead_GivenCharacterReference_ShouldConsumeAndReturnCharacterReference() throws IOException {
        Character expectedCharacter = 'é';
        when(reader.current()).thenReturn('#');
        when(reader.matchConsumeIgnoreCase("X")).thenReturn(true);
        when(reader.consumeHexSequence()).thenReturn("00E9");
        when(reader.matchConsume(";")).thenReturn(true);

        Token actualToken = tokeniser.read();

        Token expectedToken = new Character(expectedCharacter.toString());
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verify(reader).consume(1);
    }

    @Test
    public void testRead_GivenNamedCharacterReference_ShouldConsumeAndReturnNamedCharacterReference() throws IOException {
        Character expectedCharacter = 'µ';
        when(reader.consumeLetterThenDigitSequence()).thenReturn("mumu");
        when(reader.matches(';')).thenReturn(true);
        when(Entities.getCharacterByName("mumu")).thenReturn(expectedCharacter);
        when(reader.matchConsume(";")).thenReturn(true);

        Token actualToken = tokeniser.read();

        Token expectedToken = new Character(expectedCharacter.toString());
        expectedTokens = new ArrayList<>();
        expectedTokens.add(expectedToken);

        assertEquals(expectedTokens, tokeniser.getTokens());
        verify(reader).consume(3);
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_GivenErrorState_ShouldThrowIllegalStateException() {
        tokeniser.errors.add(new ParseError(1, "Unexpected character 'a' in input state [Data]"));

        tokeniser.read();
    }

    @Test
    public void testRead_GivenEOFState_ShouldThrowEOFError() {
        tokeniser.transition(TokeniserState.EOF);

        try {
            tokeniser.read();
        } catch (ParseError e) {
            tokeniser.eofError(TokeniserState.EOF);
            verify(reader).mark();
            verify(reader).rewindToMark();
            throw e;
        }
    }

    @Test
    public void testGetState() {
        TokeniserState expectedState = TokeniserState.Data;

        TokeniserState actualState = tokeniser.getState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void testTransition() {
        TokeniserState expectedState = TokeniserState.Script;

        tokeniser.transition(expectedState);

        TokeniserState actualState = tokeniser.getState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState expectedState = TokeniserState.ScriptData;

        tokeniser.advanceTransition(expectedState);

        TokeniserState actualState = tokeniser.getState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        verify(reader).advance();
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        String tagName = "div";

        StartTag startTag = new StartTag("div");
        EndTag endTag = new EndTag("div");

        tokeniser.lastStartTag = startTag;

        TokeniserState expectedState = TokeniserState.Data;

        boolean actualResult = tokeniser.isAppropriateEndTagToken();
        boolean expectedResult = false;

        assertEquals(expectedResult, actualResult);

        tokeniser.transition(expectedState);
        tokeniser.tagPending = endTag;

        expectedResult = true;

        actualResult = tokeniser.isAppropriateEndTagToken();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "div";

        StartTag startTag = new StartTag("div");
        EndTag endTag = new EndTag("div");

        tokeniser.lastStartTag = startTag;

        String actualTagName = tokeniser.appropriateEndTagName();

        String expectedTagName = tagName;

        assertEquals(expectedTagName, actualTagName);
    }

    @Test
    public void testError() {
        TokeniserState expectedState = TokeniserState.Data;

        tokeniser.transition(expectedState);
        tokeniser.error(expectedState);

        ParseError expectedError = new ParseError(reader.pos(), "Unexpected character 'a' in input state [Data]");
        ParseError actualError = tokeniser.errors.get(0);

        assertEquals(expectedError.getMessage(), actualError.getMessage());
        assertEquals(expectedError.getPos(), actualError.getPos());
        assertEquals(expectedError.getState(), actualError.getState());

        tokeniser.errors.remove(0);
    }

    @Test
    public void testEOFError() {
        TokeniserState expectedState = TokeniserState.EOF;

        tokeniser.transition(expectedState);
        tokeniser.eofError(expectedState);

        ParseError expectedError = new ParseError(reader.pos(), "Unexpectedly reached end of file (EOF) in input state [EOF]");
        ParseError actualError = tokeniser.errors.get(0);

        assertEquals(expectedError.getMessage(), actualError.getMessage());
        assertEquals(expectedError.getPos(), actualError.getPos());
        assertEquals(expectedError.getState(), actualError.getState());

        tokeniser.errors.remove(0);
    }

    @Test
    public void testCharacterReferenceError() {
        TokeniserState expectedState = TokeniserState.Data;

        tokeniser.transition(expectedState);
        tokeniser.characterReferenceError("Invalid character reference: invalid named referenece 'foo'");

        ParseError expectedError = new ParseError(reader.pos(), "Invalid character reference: invalid named referenece 'foo'");
        ParseError actualError = tokeniser.errors.get(0);

        assertEquals(expectedError.getMessage(), actualError.getMessage());
        assertEquals(expectedError.getPos(), actualError.getPos());
        assertEquals(expectedError.getState(), actualError.getState());

        tokeniser.errors.remove(0);
    }

    @Test
    public void testErrorWithAttributes() {
        TokeniserState expectedState = TokeniserState.StartTagName;

        tokeniser.transition(expectedState);
        tokeniser.error("Invalid start tag name: <foo");

        ParseError expectedError = new ParseError(reader.pos(), "Invalid start tag name: <foo");
        ParseError actualError = tokeniser.errors.get(0);

        assertEquals(expectedError.getMessage(), actualError.getMessage());
        assertEquals(expectedError.getPos(), actualError.getPos());
        assertEquals(expectedError.getState(), actualError.getState());

        // test error with attributes
        Attribute attribute = new Attribute("foo", "bar");

        tokeniser.tagPending = new StartTag("div");
        tokeniser.tagPending.attributes = new ArrayList<>();
        tokeniser.tagPending.attributes.add(attribute);

        tokeniser.transition(expectedState);
        tokeniser.error("Attributes incorrectly present on start tag <div>");

        ParseError error = tokeniser.errors.get(0);
        assertEquals("Attributes incorrectly present on start tag <div>", error.getMessage());
        assertEquals(expectedState, error.getState());

        tokeniser.errors.remove(0);
    }

    private void verify(Object object) {
        if (object instanceof CharacterReader) {
            verify(reader);
        }
    }

    private void verifyNoMoreInteractions(CharacterReader reader) {
        verify(reader).mark();
        verify(reader).rewindToMark();
        verify(reader, never()).consume(anyInt());
        verify(reader, never()).match(anyChar());
        verify(reader, never()).advance();
        verify(reader, never()).matchConsume(anyString());
        verifyNoMoreInteractions(mock(ParseErrorList.class));
    }

    private void verifyNoMoreInteractions(Object object) {
        if (object instanceof ParseErrorList) {
            verifyNoMoreInteractions(reader);
            verifyNoMoreInteractions(tokeniser);
        }
    }
}