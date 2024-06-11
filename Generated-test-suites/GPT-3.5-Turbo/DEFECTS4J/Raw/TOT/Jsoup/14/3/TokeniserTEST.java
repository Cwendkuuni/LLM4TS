import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("This is a test");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals("This is a test", token.toString());
    }

    @Test
    public void testEmit() {
        Token.Character character = new Token.Character("abc");
        tokeniser.emit(character);
        Token emittedToken = tokeniser.read();
        assertEquals(character.toString(), emittedToken.toString());
    }

    @Test
    public void testEmitString() {
        String str = "example";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(str, emittedToken.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(String.valueOf(c), emittedToken.toString());
    }

    @Test
    public void testTransition() {
        TokeniserState nextState = TokeniserState.TagName;
        tokeniser.transition(nextState);
        assertEquals(nextState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState nextState = TokeniserState.AttributeName;
        tokeniser.advanceTransition(nextState);
        assertEquals(nextState, tokeniser.getState());
        assertEquals('h', tokeniser.reader.current());
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
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(characterReference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertNotNull(tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals("div", ((Token.Tag) emittedToken).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.commentData = "This is a comment";
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals("This is a comment", ((Token.Comment) emittedToken).commentData);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals("html", ((Token.Doctype) emittedToken).name);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError_State() {
        tokeniser.error(TokeniserState.AttributeName);
        assertFalse(tokeniser.currentNodeInHtmlNS());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError_State() {
        tokeniser.eofError(TokeniserState.Attributes);
        assertFalse(tokeniser.currentNodeInHtmlNS());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testError_String() {
        String errorMsg = "Invalid input";
        tokeniser.error(errorMsg);
        assertFalse(tokeniser.currentNodeInHtmlNS());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}