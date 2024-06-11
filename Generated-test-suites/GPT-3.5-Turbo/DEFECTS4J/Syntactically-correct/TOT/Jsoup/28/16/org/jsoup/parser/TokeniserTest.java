
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;

    @Before
    public void setUp() {
        errors = new ParseErrorList(16);
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader, errors);
    }

    // Test read() method
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    // Test emit(Token) method
    @Test
    public void testEmitToken() {
        Token.Comment comment = new Token.Comment();
        tokeniser.emit(comment);
        assertEquals(comment, tokeniser.read());
    }

    // Test emit(String) method
    @Test
    public void testEmitString() {
        String str = "This is a test";
        tokeniser.emit(str);
        assertEquals(str, ((Token.Character) tokeniser.read()).getData());
    }

    // Test emit(char) method
    @Test
    public void testEmitChar() {
        char c = 'A';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), ((Token.Character) tokeniser.read()).getData());
    }

    // Test getState() method
    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    // Test transition(TokeniserState) method
    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
    }

    // Test advanceTransition(TokeniserState) method
    @Test
    public void testAdvanceTransition() {
        reader = new CharacterReader("input");
        tokeniser = new Tokeniser(reader, errors);
        tokeniser.advanceTransition(TokeniserState.Rawtext);

        assertEquals(TokeniserState.Rawtext, tokeniser.getState());
        assertEquals('n', reader.current());
    }

    // Test acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.consumedCharacterReference('A', true);
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isEmitPending);
    }

    // Test consumeCharacterReference(Character, boolean) method
    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference('A', true));

        String entityStr = "&lt;";
        reader = new CharacterReader(entityStr);
        tokeniser = new Tokeniser(reader, errors);
        assertEquals('<', (char) tokeniser.consumeCharacterReference(null, false));

        entityStr = "&#60;";
        reader = new CharacterReader(entityStr);
        tokeniser = new Tokeniser(reader, errors);
        assertEquals('<', (char) tokeniser.consumeCharacterReference(null, false));
    }

    // Test createTagPending(boolean) method
    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertTrue(tagPending instanceof Token.StartTag);

        tagPending = tokeniser.createTagPending(false);
        assertTrue(tagPending instanceof Token.EndTag);
    }

    // Test emitTagPending() method
    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tagPending, tokeniser.read());
    }

    // Test createCommentPending() method
    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    // Test emitCommentPending() method
    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }

    // Test createDoctypePending() method
    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    // Test emitDoctypePending() method
    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }

    // Test createTempBuffer() method
    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    // Test isAppropriateEndTagToken() method
    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());

        Token.StartTag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "div";

        tokeniser.lastStartTag = startTag;

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    // Test appropriateEndTagName() method
    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "div";

        tokeniser.lastStartTag = startTag;

        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    // Test error(TokeniserState) method
    @Test
    public void testErrorState() {
        tokeniser.error(TokeniserState.TagName);
        assertEquals(1, errors.size());
    }

    // Test eofError(TokeniserState) method
    @Test
    public void testEofErrorState() {
        tokeniser.eofError(TokeniserState.TagName);
        assertEquals(1, errors.size());
    }

    // Test characterReferenceError(String) method
    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid character reference");
        assertEquals(1, errors.size());
    }

    // Test error(String) method
    @Test
    public void testError() {
        tokeniser.error("Invalid character found");
        assertEquals(1, errors.size());
    }

    // Test currentNodeInHtmlNS() method
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    // Test unescapeEntities() method
    @Test
    public void testUnescapeEntities() {
        // TODO: Implement test for unescapeEntities() method
    }
}

