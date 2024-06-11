
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(str), emittedToken);
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(String.valueOf(c)), emittedToken);
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Tag;
        tokeniser.transition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Tag;
        tokeniser.advanceTransition(newState);
        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        tokeniser.reader = reader;

        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertEquals((Character) 'A', characterReference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        tag.name("test");
        tokeniser.tagPending = tag;

        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();

        assertTrue(emittedToken instanceof Token.StartTag);
        Token.StartTag emittedTag = (Token.StartTag) emittedToken;
        assertEquals(tag, emittedTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        String commentData = "test";
        Token.Comment comment = new Token.Comment(commentData);
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();

        assertTrue(emittedToken instanceof Token.Comment);
        Token.Comment emittedComment = (Token.Comment) emittedToken;
        assertEquals(comment, emittedComment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        String doctypeName = "html";
        Token.Doctype doctype = new Token.Doctype(doctypeName);
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();

        assertTrue(emittedToken instanceof Token.Doctype);
        Token.Doctype emittedDoctype = (Token.Doctype) emittedToken;
        assertEquals(doctype, emittedDoctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean start = true;
        Token.Tag startTag = tokeniser.createTagPending(start);
        startTag.name("test");

        boolean end = false;
        Token.Tag endTag = tokeniser.createTagPending(end);
        endTag.name("test");

        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = endTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        boolean start = true;
        Token.Tag startTag = tokeniser.createTagPending(start);
        String tagName = "test";
        startTag.name(tagName);

        tokeniser.lastStartTag = startTag;

        assertEquals(tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorWithState() {
        TokeniserState state = TokeniserState.Tag;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpected character ' ' in input state [Tag]", error.toString());
    }

    @Test
    public void testEofErrorWithState() {
        TokeniserState state = TokeniserState.Tag;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Tag]", error.toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference: incorrect format";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Invalid character reference: incorrect format", error.toString());
    }

    @Test
    public void testErrorWithMessage() {
        String message = "Missing closing tag";
        tokeniser.error(message);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Missing closing tag", error.toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}
