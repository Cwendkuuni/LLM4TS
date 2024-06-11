package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.*;

import static org.junit.Assert.*;

public class TokeniserTest {
    static final char replacementChar = '\uFFFD';

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
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
        Token token = new Token.Character("text");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }
    
    @Test
    public void testEmit_String() {
        String str = "text";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(str), emittedToken);
    }

    @Test
    public void testEmit_Char() {
        char c = 'a';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character(String.valueOf(c)), emittedToken);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.StartTag();
        tokeniser.tagPending = tag;
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(comment, emittedToken);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(doctype, emittedToken);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        String tagName = "tag";
        tokeniser.lastStartTag = new Token.StartTag(tagName);
        tokeniser.tagPending = new Token.EndTag(tagName);
        assertEquals(tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        String errorMsg = "Error";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals(errorMsg, error.getErrorMessage());
    }

    @Test
    public void testEofError() {
        String state = "state";
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [" + state + "]", error.getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "message";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Invalid character reference: " + message, error.getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // To be implemented
    }
}