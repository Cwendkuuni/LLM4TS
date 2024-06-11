import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead_nextToken() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        Token.StartTag startTagToken = (Token.StartTag) token;
        assertEquals("html", startTagToken.tagName);
        assertEquals(0, startTagToken.attributes.size());
        assertFalse(startTagToken.selfClosing);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        startTagToken = (Token.StartTag) token;
        assertEquals("head", startTagToken.tagName);
        assertEquals(0, startTagToken.attributes.size());
        assertFalse(startTagToken.selfClosing);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        startTagToken = (Token.StartTag) token;
        assertEquals("title", startTagToken.tagName);
        assertEquals(0, startTagToken.attributes.size());
        assertFalse(startTagToken.selfClosing);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        Token.Character charToken = (Token.Character) token;
        assertEquals("Test", charToken.getData());

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        Token.EndTag endTagToken = (Token.EndTag) token;
        assertEquals("title", endTagToken.tagName);
        assertEquals(0, endTagToken.attributes.size());

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        endTagToken = (Token.EndTag) token;
        assertEquals("head", endTagToken.tagName);
        assertEquals(0, endTagToken.attributes.size());

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        startTagToken = (Token.StartTag) token;
        assertEquals("body", startTagToken.tagName);
        assertEquals(0, startTagToken.attributes.size());
        assertFalse(startTagToken.selfClosing);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        endTagToken = (Token.EndTag) token;
        assertEquals("body", endTagToken.tagName);
        assertEquals(0, endTagToken.attributes.size());

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        endTagToken = (Token.EndTag) token;
        assertEquals("html", endTagToken.tagName);
        assertEquals(0, endTagToken.attributes.size());
    }

    @Test
    public void testEmit_CharacterToken() {
        Token.Character charToken = new Token.Character("Test");
        tokeniser.emit(charToken);

        Token token = tokeniser.read();
        assertEquals(charToken, token);
    }

    @Test
    public void testEmit_StartTagToken() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.tagName = "div";
        startTagToken.selfClosing = true;
        tokeniser.emit(startTagToken);

        Token token = tokeniser.read();
        assertEquals(startTagToken, token);
    }

    @Test
    public void testEmit_EndTagToken() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName = "div";
        tokeniser.emit(endTagToken);

        Token token = tokeniser.read();
        assertEquals(endTagToken, token);
    }

    @Test
    public void testCreateTagPending_StartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertEquals(Token.TokenType.StartTag, tag.type);
    }

    @Test
    public void testCreateTagPending_EndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
        assertEquals(Token.TokenType.EndTag, tag.type);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "div";
        tag.attributes.put("id", "test");
        tag.attributes.put("class", "example");
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        Token.StartTag startTagToken = (Token.StartTag) token;
        assertEquals("div", startTagToken.tagName);
        assertEquals(2, startTagToken.attributes.size());
        assertEquals("test", startTagToken.attributes.get("id"));
        assertEquals("example", startTagToken.attributes.get("class"));

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        Token.EndTag endTagToken = (Token.EndTag) token;
        assertEquals("div", endTagToken.tagName);
        assertEquals(0, endTagToken.attributes.size());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("This is a comment");
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        Token.Comment commentToken = (Token.Comment) token;
        assertEquals("This is a comment", commentToken.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("html");
        tokeniser.doctypePending.setPublicIdentifier("-//W3C//DTD HTML 4.01//EN");
        tokeniser.doctypePending.setSystemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        Token.Doctype doctypeToken = (Token.Doctype) token;
        assertEquals("html", doctypeToken.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctypeToken.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = tokeniser.getState();
        reader.advance();
        tokeniser.advanceTransition(TokeniserState.Data);

        assertNotEquals(state, tokeniser.getState());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_numeric() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        reader.advance(); // consume '<'
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertNotNull(character);
        assertEquals('&', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_named() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        reader.advance(); // consume '<'
        reader.advance(); // consume 'h'
        Character character = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertNotNull(character);
        assertEquals('h', character.charValue());
    }

    @Test
    public void testIsAppropriateEndTagToken_true() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";

        boolean appropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertTrue(appropriateEndTagToken);
    }

    @Test
    public void testIsAppropriateEndTagToken_false() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "body";

        boolean appropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        assertFalse(appropriateEndTagToken);
    }

    @Test
    public void testError() {
        TokeniserState state = tokeniser.getState();
        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpected character in input", error.getErrorMessage());
        assertEquals(state, error.getState());
        assertEquals(0, error.getPosition());
    }

    @Test
    public void testEofError() {
        TokeniserState state = tokeniser.getState();
        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMessage());
        assertEquals(state, error.getState());
        assertEquals(0, error.getPosition());
    }

    @Test
    public void testCharacterReferenceError() {
        TokeniserState state = tokeniser.getState();
        tokeniser.characterReferenceError();

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Invalid character reference", error.getErrorMessage());
        assertEquals(state, error.getState());
        assertEquals(0, error.getPosition());
    }

    @Test
    public void testError_withErrorMessage() {
        TokeniserState state = tokeniser.getState();
        tokeniser.error("Custom error message");

        assertEquals(1, tokeniser.errors.size());
        ParseError error = tokeniser.errors.get(0);
        assertEquals("Custom error message", error.getErrorMessage());
        assertEquals(state, error.getState());
        assertEquals(0, error.getPosition());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNs = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNs);
    }

}