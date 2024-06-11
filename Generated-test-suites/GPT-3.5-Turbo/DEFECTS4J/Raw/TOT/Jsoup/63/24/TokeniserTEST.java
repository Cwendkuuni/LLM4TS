package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead_returnsNullWhenSelfClosingFlagNotAcknowledged() {
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.StartTag());
        tokeniser.selfClosingFlagAcknowledged = false;

        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testRead_returnsCharacterTokenWhenThereAreCharactersInBuffer() {
        tokeniser.emit("abc");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_returnsCharacterTokenWhenThereIsAStringToken() {
        tokeniser.emit("abc");
        tokeniser.emit("def");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abcdef", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_returnsStringTokenWhenThereIsAStringTokenFromCharsBuilder() {
        tokeniser.emit("abc");
        tokeniser.emit("defgh");
        tokeniser.emit("ijk");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abcdefghijk", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_returnsEmitPendingTokenWhenThereIsAnEmitPending() {
        tokeniser.emit(new Token.Tag());
        tokeniser.isEmitPending = true;

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Tag, token.type);
    }

    @Test
    public void testEmit_setsEmitPendingAndIsEmitPendingToTrue() {
        Token token = new Token.StartTag();

        tokeniser.emit(token);

        assertEquals(token, tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitTagPending_finalisesTagAndCallsEmit() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.name("div");

        tokeniser.emitTagPending();

        assertNotNull(tokeniser.emitPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.emitPending.type);
        assertEquals("div", ((Token.StartTag) tokeniser.emitPending).tagName);
    }

    @Test
    public void testCreateTempBuffer_resetsDataBuffer() {
        tokeniser.dataBuffer.append("Some data");

        tokeniser.createTempBuffer();

        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken_returnsTrueIfLastStartTagExistsAndMatchesTagName() {
        tokeniser.lastStartTag = "div";

        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();

        assertTrue(isAppropriate);
    }

    @Test
    public void testIsAppropriateEndTagToken_returnsFalseIfLastStartTagDoesNotExist() {
        tokeniser.lastStartTag = null;

        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();

        assertFalse(isAppropriate);
    }

    @Test
    public void testAppropriateEndTagName_returnsLastStartTagIfItExists() {
        tokeniser.lastStartTag = "div";

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals("div", tagName);
    }

    @Test
    public void testAppropriateEndTagName_returnsNullIfLastStartTagDoesNotExist() {
        tokeniser.lastStartTag = null;

        String tagName = tokeniser.appropriateEndTagName();

        assertNull(tagName);
    }

    @Test
    public void testError_addsParseErrorToErrorsWithErrorMessage() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.error("Invalid character");

        assertEquals(1, errors.size());
        assertEquals("Invalid character", errors.get(0).getErrorMessage());
    }

    @Test
    public void testError_addsParseErrorToErrorsWithFormattedErrorMessageWhenCurrentCharacterIsGiven() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;
        tokeniser.reader = new CharacterReader("test");

        tokeniser.error(TokeniserState.Data);

        assertEquals(1, errors.size());
        assertEquals("Unexpected character 't' in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_addsParseErrorToErrorsWithErrorMessage() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError_addsParseErrorToErrorsWithErrorMessage() {
        ParseErrorList errors = new ParseErrorList();
        tokeniser.errors = errors;

        tokeniser.characterReferenceError("Invalid character reference");

        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS_returnsTrueByDefault() {
        boolean currentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(currentNodeInHtmlNS);
    }

    @Test
    public void testUnescapeEntities_returnsUnescapedStringFromReader() {
        tokeniser.reader = new CharacterReader("This &amp; is &lt; a &gt; test");

        String unescapedString = tokeniser.unescapeEntities(false);

        assertEquals("This & is < a > test", unescapedString);
    }

}