package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser.TokeniserState;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;
    private List<ParseError> parseErrors;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
        parseErrors = new ArrayList<>();
        errors.setErrors(parseErrors);
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testEmit() {
        Token token = new Token("tagName", Token.TokenType.StartTag);
        tokeniser.emit(token);
        assertSame(token, tokeniser.emitPending);
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('c');
        assertEquals("c", tokeniser.charBuffer.toString());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("str");
        assertEquals("str", tokeniser.charBuffer.toString());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testReadCharacterReference() {
        when(reader.current()).thenReturn('&').thenReturn('A').thenReturn('#').thenReturn('x').thenReturn(';');
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', result);

        when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('#').thenReturn('x').thenReturn(';');
        result = tokeniser.consumeCharacterReference('a', false);
        assertEquals('a', result);

        when(reader.current()).thenReturn('&').thenReturn('X').thenReturn('#').thenReturn('X').thenReturn(';');
        result = tokeniser.consumeCharacterReference(Character.toChars('X')[0], false);
        assertEquals(Character.toChars('X')[0], result);

        when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn(';');
        result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);

        when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('#').thenReturn('x').thenReturn(';');
        result = tokeniser.consumeCharacterReference('z', false);
        assertNull(result);

        when(reader.current()).thenReturn('&').thenReturn('&').thenReturn(';');
        result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);

        when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('#').thenReturn('a').thenReturn(';');
        result = tokeniser.consumeCharacterReference('a', false);
        assertEquals(Entities.getCharacterByName("aa"), result);

        when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('#').thenReturn('x').thenReturn(';');
        when(reader.current()).thenReturn('=');
        result = tokeniser.consumeCharacterReference('a', true);
        assertEquals(Entities.getCharacterByName("xa"), result);

        when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('#').thenReturn('x').thenReturn(';').thenReturn(';');
        tokeniser.characterReferenceError("invalid character reference with no semicolon");
        assertSame(TokeniserState.Data, tokeniser.getState());
        assertNull(tokeniser.emitPending);
        assertEquals(1, parseErrors.size());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertSame(tokeniser.tagPending, tokeniser.emitPending);
        assertNull(tokeniser.tagPending);
        assertEquals(Token.StartTag.class, tokeniser.tagPending.getClass());
        assertEquals("", tokeniser.tagPending.tagName);
        assertEquals(0, tokeniser.tagPending.attributes.size());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertSame(tokeniser.tagPending, tokeniser.emitPending);
        assertNull(tokeniser.tagPending);
        assertEquals(Token.StartTag.class, tokeniser.emitPending.getClass());
        assertEquals("", tokeniser.emitPending.tagName);
        assertEquals(0, tokeniser.emitPending.attributes.size());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertSame(tokeniser.commentPending, tokeniser.emitPending);
        assertNull(tokeniser.commentPending);
        assertEquals(Token.Comment.class, tokeniser.emitPending.getClass());
        assertEquals("", tokeniser.emitPending.text);
    }

    @Test   
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertSame(tokeniser.commentPending, tokeniser.emitPending);
        assertNull(tokeniser.commentPending);
        assertEquals(Token.Comment.class, tokeniser.emitPending.getClass());
        assertEquals("", tokeniser.emitPending.text);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertSame(tokeniser.doctypePending, tokeniser.emitPending);
        assertNull(tokeniser.doctypePending);
        assertEquals(Token.Doctype.class, tokeniser.emitPending.getClass());
        assertEquals("", tokeniser.emitPending.publicId);
        assertEquals("", tokeniser.emitPending.systemId);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertSame(tokeniser.doctypePending, tokeniser.emitPending);
        assertNull(tokeniser.doctypePending);
        assertEquals(Token.Doctype.class, tokeniser.emitPending.getClass());
        assertEquals("", tokeniser.emitPending.publicId);
        assertEquals("", tokeniser.emitPending.systemId);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(false);
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        assertEquals("", tokeniser.appropriateEndTagName());

        tokeniser.createTagPending("tag1").emitTagPending();
        tokeniser.createTagPending("tag2").emitTagPending();
        assertEquals("tag1", tokeniser.appropriateEndTagName());

        tokeniser.createTagPending("tag1").emitTagPending();
        tokeniser.createTagPending("tag3").emitTagPending();
        assertEquals("tag1", tokeniser.appropriateEndTagName());

        tokeniser.createTagPending("tag1").emitTagPending();
        tokeniser.createTagPending("tag1").emitTagPending();
        assertEquals("tag1", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, parseErrors.size());
        assertEquals("Unexpected character '', in input state [Data]", parseErrors.get(0).getMessage());
    }

    @Test
    public void testEOFError() {
        when(reader.isEmpty()).thenReturn(true);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, parseErrors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseErrors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        when(reader.current()).thenReturn('&').thenReturn('#').thenReturn('x').thenReturn(';');
        tokeniser.characterReferenceError("missing semicolon");
        assertEquals(1, parseErrors.size());
        assertEquals("Invalid character reference: missing semicolon", parseErrors.get(0).getMessage());
    }

    @Test
    public void testErrorWithState() {
        tokeniser.error(TokeniserState.Script);
        assertEquals(1, parseErrors.size());
        assertEquals("Unexpected character '', in input state [Script]", parseErrors.get(0).getMessage());
    }

}