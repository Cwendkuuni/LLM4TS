import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Assert.assertEquals(token, tokeniser.read());
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
        char c = 'c';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Character);
        Assert.assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState nextState = TokeniserState.Doctype;
        tokeniser.transition(nextState);
        Assert.assertEquals(nextState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState nextState = TokeniserState.TagName;
        tokeniser.advanceTransition(nextState);
        Assert.assertEquals(nextState, tokeniser.getState());
        // TODO: test reader.advance() is called
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        // TODO: test selfClosingFlagAcknowledged is true
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: test
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        Assert.assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Tag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        boolean result = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        Assert.assertTrue(trackErrors);
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        boolean trackErrors = tokeniser.isTrackErrors();
        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testErrorWithState() {
        TokeniserState state = TokeniserState.Doctype;
        tokeniser.error(state);
        // TODO: test errors list is updated correctly
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        // TODO: test errors list is updated correctly
    }

    private void testCharacterReferenceError() {
        // TODO: implement
    }

    private void testErrorWithString(String errorMsg) {
        // TODO: implement
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(result);
    }
}