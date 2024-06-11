package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test string";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(str, emittedToken.toString());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(new String(chars), emittedToken.toString());
    }

    @Test
    public void testEmitIntArrray() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(new String(codepoints, 0, codepoints.length), emittedToken.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, emittedToken.type);
        assertEquals(String.valueOf(c), emittedToken.toString());
    }

    @Test
    public void testGetState() {
        TokeniserState expectedState = TokeniserState.Data;
        TokeniserState currentState = tokeniser.getState();
        assertEquals(expectedState, currentState);
    }

    @Test
    public void testTransition() {
        TokeniserState expectedState = TokeniserState.EndTag;
        tokeniser.transition(expectedState);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(expectedState, currentState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState expectedState = TokeniserState.EndTag;
        tokeniser.advanceTransition(expectedState);
        TokeniserState currentState = tokeniser.getState();
        assertEquals(expectedState, currentState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        int[] expectedCodepoints = new int[]{116, 101, 115, 116};
        int[] codepoints = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertArrayEquals(expectedCodepoints, codepoints);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertTrue(tag.attributes.isEmpty());
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.Tag();
        tag.name("test");
        tag.attributes.put("attr", "value");
        tokeniser.tagPending = tag;
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(comment, emittedToken);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(doctype, emittedToken);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        String expectedTagName = "test";
        tokeniser.lastStartTag = expectedTagName;
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals(expectedTagName, tagName);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.AttributeValue_unquoted;
        tokeniser.error(state);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(String.format("Unexpected character '%s' in input state [%s]", tokeniser.reader.current(), state), error.toString());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.EncodingName;
        tokeniser.eofError(state);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(String.format("Unexpectedly reached end of file (EOF) in input state [%s]", state), error.toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(String.format("Invalid character reference: %s", message), error.toString());
    }

    @Test
    public void testErrorWithString() {
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMsg, error.toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNS);
    }

    @Test
    public void testUnescapeEntities() {
        String str = "test&amp;string&lt;";
        String expected = "test&string<";
        String unescapedStr = tokeniser.unescapeEntities(false);
        assertEquals(expected, unescapedStr);
    }
}