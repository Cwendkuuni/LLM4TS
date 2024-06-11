
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);

        Token emittedToken = tokeniser.read();
        Assert.assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Character);
        Assert.assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);

        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Character);
        Assert.assertEquals(Character.toString(c), ((Token.Character) token).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertNotNull(state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.transition(state);

        TokeniserState nextState = tokeniser.getState();
        Assert.assertEquals(state, nextState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Rcdata;
        tokeniser.advanceTransition(state);

        TokeniserState nextState = tokeniser.getState();
        Assert.assertEquals(state, nextState);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        // Nothing to assert, just testing the method
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'c';
        boolean inAttribute = false;
        Character expected = 'a';

        // Mock reader methods and match the HTML named character reference
        tokeniser.reader = new CharacterReader("#97;");
        Character actual = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);

        Assert.assertNotNull(tokeniser.tagPending);
        Assert.assertEquals(tag, tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.StartTag);
        Assert.assertEquals(tokeniser.tagPending, token);
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

        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Comment);
        Assert.assertEquals(tokeniser.commentPending, token);
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

        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Doctype);
        Assert.assertEquals(tokeniser.doctypePending, token);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("div");

        boolean actual = tokeniser.isAppropriateEndTagToken();

        Assert.assertTrue(actual);
    }

    @Test
    public void testAppropriateEndTagName() {
        String expected = "div";
        tokeniser.lastStartTag = new Token.StartTag(expected);

        String actual = tokeniser.appropriateEndTagName();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);

        // Nothing to assert, just testing the method
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);

        // Nothing to assert, just testing the method
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);

        // Nothing to assert, just testing the method
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);

        // Nothing to assert, just testing the method
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean actual = tokeniser.currentNodeInHtmlNS();

        Assert.assertEquals(true, actual);
    }
}
