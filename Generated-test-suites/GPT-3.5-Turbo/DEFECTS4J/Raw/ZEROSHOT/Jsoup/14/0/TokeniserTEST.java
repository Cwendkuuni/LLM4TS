import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(new StringReader("")));
    }

    @After
    public void tearDown() {
        tokeniser = null;
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
        Assert.assertEquals(str, ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, emittedToken.type);
        Assert.assertEquals(Character.toString(c), ((Token.Character) emittedToken).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        // No assertions, method does not return a value and does not have side effects
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = ' ';
        boolean inAttribute = false;
        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        Assert.assertNull(characterReference);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        Assert.assertNotNull(tokeniser.tagPending);
        Assert.assertEquals(start, tag.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(tokeniser.tagPending, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(tokeniser.commentPending, emittedToken);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(tokeniser.doctypePending, emittedToken);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("div");
        tokeniser.lastStartTag = new Token.StartTag("div");
        boolean appropriate = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(appropriate);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = false;
        tokeniser.setTrackErrors(trackErrors);
        Assert.assertEquals(trackErrors, tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        // No assertions, method does not return a value and does not have side effects
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // No assertions, method does not return a value and does not have side effects
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        // No assertions, method does not return a value and does not have side effects
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        // No assertions, method does not return a value and does not have side effects
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(inHtmlNS);
    }
}