
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the Tokeniser class
 */
public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader(""); // empty string
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testRead_emptyInput_returnNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit_singleCharacterToken_returnCharacterToken() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_stringToken_returnCharacterToken() {
        tokeniser.emit("hello");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("hello", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_startTag_resultInStartTagToken() {
        // create a tag
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.name("div");
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", ((Token.StartTag) token).tagName());
    }

    @Test
    public void testEmit_endTag_resultInEndTagToken() {
        // create a tag
        Token.Tag tag = tokeniser.createTagPending(false);
        tag.name("div");
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("div", ((Token.EndTag) token).tagName());
    }

    @Test
    public void testConsumeCharacterReference_numericRef_returnsCharacter() {
        tokeniser.emit("&#65;");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("A", ((Token.Character) token).getData());
    }

    @Test
    public void testConsumeCharacterReference_namedRef_returnsCharacter() {
        tokeniser.emit("&amp;");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("&", ((Token.Character) token).getData());
    }

    @Test
    public void testConsumeCharacterReference_invalidRef_returnsNull() {
        tokeniser.emit("&invalid;");
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testCreateTagPending_startTag_returnsStartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_endTag_returnsEndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testIsAppropriateEndTagToken_noStartTag_returnsFalse() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertFalse(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_startTagDifferentTagName_returnsFalse() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        tokeniser.lastStartTag = startTag;

        boolean result = tokeniser.isAppropriateEndTagToken();
        assertFalse(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_startTagSameTagName_returnsTrue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        tokeniser.lastStartTag = startTag;

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        tokeniser.tagPending = endTag;

        boolean result = tokeniser.isAppropriateEndTagToken();
        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName_returnsTagNameOfLastStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        tokeniser.lastStartTag = startTag;

        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("div", tagName);
    }

    @Test
    public void testError_addErrorToList() {
        ParseErrorList errors = new ParseErrorList();
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.error("Error message");

        List<ParseError> errorList = errors.getList();
        assertEquals(1, errorList.size());
        assertEquals("Error message", errorList.get(0).toString());
    }

    @Test
    public void testEofError_addErrorToList() {
        ParseErrorList errors = new ParseErrorList();
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.eofError(TokeniserState.Data);

        List<ParseError> errorList = errors.getList();
        assertEquals(1, errorList.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errorList.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError_addErrorToList() {
        ParseErrorList errors = new ParseErrorList();
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.characterReferenceError("Invalid character reference");

        List<ParseError> errorList = errors.getList();
        assertEquals(1, errorList.size());
        assertEquals("Invalid character reference: Invalid character reference", errorList.get(0).toString());
    }
}

