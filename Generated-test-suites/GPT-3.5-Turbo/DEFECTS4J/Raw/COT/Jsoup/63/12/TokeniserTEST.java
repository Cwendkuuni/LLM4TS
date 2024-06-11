import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.Arrays;

public class TokeniserTest {

    Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        String str = "";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        Assert.assertEquals(token.type, Token.TokenType.Character);
        Assert.assertEquals(token.data, str);
    }

    @Test
    public void testEmit_charArray() {
        char[] chars = "".toCharArray();
        tokeniser.emit(chars);
        Token token = tokeniser.read();
        Assert.assertEquals(token.type, Token.TokenType.Character);
        Assert.assertEquals(token.data, String.valueOf(chars));
    }

    @Test
    public void testEmit_codepoints() {
        int[] codepoints = "".codePoints().toArray();
        tokeniser.emit(codepoints);
        Token token = tokeniser.read();
        Assert.assertEquals(token.type, Token.TokenType.Character);
        Assert.assertEquals(token.data, new String(codepoints, 0, codepoints.length));
    }

    @Test
    public void testEmit_char() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        Assert.assertEquals(token.type, Token.TokenType.Character);
        Assert.assertEquals(token.data, String.valueOf(c));
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(state, TokeniserState.Data);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        Assert.assertEquals(tokeniser.getState(), state);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        Assert.assertEquals(tokeniser.getState(), state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        int[] codeRef = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        Assert.assertNull(codeRef);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        Assert.assertNotNull(tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.Tag();
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertEquals(token.type, Token.TokenType.Tag);
        Assert.assertEquals(token, tokeniser.tagPending);
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
        Assert.assertEquals(token.type, Token.TokenType.Comment);
        Assert.assertEquals(token, tokeniser.commentPending);
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
        Assert.assertEquals(token.type, Token.TokenType.Doctype);
        Assert.assertEquals(token, tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        Assert.assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        String result = tokeniser.appropriateEndTagName();
        Assert.assertNull(result);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        Assert.assertEquals(tokeniser.errors.size(), 1);
        Assert.assertEquals(tokeniser.errors.get(0).getMessage(), "Unexpected character 'null' in input state [Data]");
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        Assert.assertEquals(tokeniser.errors.size(), 1);
        Assert.assertEquals(tokeniser.errors.get(0).getMessage(), "Unexpectedly reached end of file (EOF) in input state [Data]");
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        Assert.assertEquals(tokeniser.errors.size(), 1);
        Assert.assertEquals(tokeniser.errors.get(0).getMessage(), "Invalid character reference: " + message);
    }

    @Test
    public void testError_custom() {
        String errorMsg = "Custom error message";
        tokeniser.error(errorMsg);
        Assert.assertEquals(tokeniser.errors.size(), 1);
        Assert.assertEquals(tokeniser.errors.get(0).getMessage(), errorMsg);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(result);
    }

    @Test
    public void testUnescapeEntities() {
        boolean inAttribute = false;
        String result = tokeniser.unescapeEntities(inAttribute);
        Assert.assertEquals(result, "");
    }
}