import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        // Test empty input
        Token token = tokeniser.read();
        assertNull(token);

        // Test non-empty input
        tokeniser.reader = new CharacterReader("html");
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("html", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        // Test emitting a token
        Token token = new Token.Comment("test");
        tokeniser.emit(token);
        token = tokeniser.read();
        assertEquals("test", ((Token.Comment) token).getData());
    }

    @Test
    public void testEmitString() {
        // Test emitting a string
        tokeniser.emit("test");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        // Test emitting a char
        tokeniser.emit('c');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", ((Token.Character) token).getData());
    }

    @Test
    public void testGetState() {
        // Test getting the current state
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        // Test transitioning to a new state
        tokeniser.transition(TokeniserState.RCDATA);
        assertEquals(TokeniserState.RCDATA, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test advancing and transitioning to a new state
        tokeniser.reader = new CharacterReader("html");
        tokeniser.advanceTransition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());
        assertEquals('h', tokeniser.reader.current());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test acknowledging the self-closing flag
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consuming a character reference
        tokeniser.reader = new CharacterReader("&#97;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals("a", character.toString());
    }

    @Test
    public void testCreateTagPending() {
        // Test creating a start tag pending
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
        tag = tokeniser.createTagPending(false);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        // Test emitting a tag pending
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        // Test creating a comment pending
        tokeniser.createCommentPending();
        Token token = tokeniser.commentPending;
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        // Test emitting a comment pending
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test creating a doctype pending
        tokeniser.createDoctypePending();
        Token token = tokeniser.doctypePending;
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test emitting a doctype pending
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test creating a temporary buffer
        tokeniser.createTempBuffer();
        StringBuilder buffer = tokeniser.dataBuffer;
        assertEquals("", buffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test checking if the current tag is an appropriate end tag token
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.StartTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test getting the appropriate end tag name
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals("div", tagName);
    }

    @Test
    public void testError() {
        // Test adding an error
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        // Test adding an EOF error
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test adding a character reference error
        tokeniser.characterReferenceError("test");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithMessage() {
        // Test adding an error with a custom message
        tokeniser.error("test error");
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test checking if the current node is in the HTML namespace
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNS);
    }

    @Test
    public void testUtilityMethod() {
        // Test the utility method
        // ...
    }
}