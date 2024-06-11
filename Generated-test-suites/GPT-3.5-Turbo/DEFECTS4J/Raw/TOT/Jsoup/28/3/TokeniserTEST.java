package org.jsoup.parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader characterReader = new CharacterReader("test");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
        assertEquals("test", ((Token.Character) token).getData());

        characterReader = new CharacterReader("");
        tokeniser = new Tokeniser(characterReader, parseErrorList);
        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmitChar() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(Character.toString(c), tokeniser.charBuffer.toString());
    }

    @Test
    public void testGetState() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
    }

    @Test
    public void testTransition() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader characterReader = new CharacterReader("test");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader characterReader = new CharacterReader("#65;");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('A', character.charValue());

        characterReader = new CharacterReader(";");
        tokeniser = new Tokeniser(characterReader, parseErrorList);
        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(tag, tokeniser.tagPending);

        start = false;
        tag = tokeniser.createTagPending(start);
        assertTrue(tag instanceof Token.EndTag);
        assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.applyTag("test", true);
        tokeniser.emitTagPending();
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tag, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("test");
        tokeniser.emitCommentPending();
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("test");
        tokeniser.emitDoctypePending();
        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.lastStartTag = null;
        assertFalse(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = tokeniser.createTagPending(true);
        tokeniser.lastStartTag.tagName = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.lastStartTag = null;
        assertNull(tokeniser.appropriateEndTagName());

        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = tokeniser.createTagPending(true);
        tokeniser.lastStartTag.tagName = "test";
        assertEquals("test", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, parseErrorList.size());
        assertEquals("Unexpected character '' in input state [Data]", parseErrorList.get(0).getErrorMessage());
    }

    @Test
    public void testEofError() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, parseErrorList.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseErrorList.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.characterReferenceError("test");
        assertEquals(1, parseErrorList.size());
        assertEquals("Invalid character reference: test", parseErrorList.get(0).getErrorMessage());
    }

    @Test
    public void testErrorWithMessage() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.error("test");
        assertEquals(1, parseErrorList.size());
        assertEquals("test", parseErrorList.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader characterReader = new CharacterReader("");
        ParseErrorList parseErrorList = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, parseErrorList);
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}