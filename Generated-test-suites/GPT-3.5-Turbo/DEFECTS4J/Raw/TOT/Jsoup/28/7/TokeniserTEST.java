import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = null;
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull("Token should be null for empty reader", token);

        reader = new CharacterReader("<div>Hello World</div>");
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNotNull("Token should not be null", token);
        assertEquals("Token type should be StartTag", Token.TokenType.StartTag, token.type);

        token = tokeniser.read();
        assertNotNull("Token should not be null", token);
        assertEquals("Token type should be Character", Token.TokenType.Character, token.type);

        token = tokeniser.read();
        assertNotNull("Token should not be null", token);
        assertEquals("Token type should be EndTag", Token.TokenType.EndTag, token.type);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);

        token = tokeniser.read();
        assertEquals("Token emitted should be same as emitted token", token, token);
    }

    @Test
    public void testEmitString() {
        String str = "test string";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertEquals("Token emitted should be a Character token", Token.TokenType.Character, token.type);
        assertEquals("Token text should be same as emitted string", str, token.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'A';
        tokeniser.emit(c);

        Token token = tokeniser.read();
        assertEquals("Token emitted should be a Character token", Token.TokenType.Character, token.type);
        assertEquals("Token text should be same as emitted char", Character.toString(c), token.toString());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);

        assertEquals("Current state should be same as transitioned state", state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);

        assertEquals("Current state should be same as transitioned state", state, tokeniser.getState());
        reader.advance();
        assertEquals("Reader position should be advanced after transitioning", 1, reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue("Self closing flag should be acknowledged", tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'A';
        reader = new CharacterReader("&nbsp;");
        tokeniser = new Tokeniser(reader, errors);
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);

        assertNull("Character reference should be null as it is a valid entity", result);

        reader = new CharacterReader("&invalid;");
        tokeniser = new Tokeniser(reader, errors);
        result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);

        assertNull("Character reference should be null as it is an invalid entity", result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);

        assertNotNull("Tag should be created", tag);
        assertTrue("Tag type should be StartTag", tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.StartTag("div");
        tokeniser.tagPending = tag;

        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertEquals("Token emitted should be same as tagPending", tag, token);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertNotNull("Comment should be created", tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment("test comment");
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertEquals("Token emitted should be same as commentPending", comment, token);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertNotNull("Doctype should be created", tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype("html");
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertEquals("Token emitted should be same as doctypePending", doctype, token);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertNotNull("Data buffer should be created", tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag("div");
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;

        assertTrue("Should be appropriate end tag token", tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag("div");
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;

        assertEquals("End tag name should be same as start tag name", startTag.tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Initial;
        tokeniser.error(state);

        assertEquals("An error should be added to the error list", 1, errors.size());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Initial;
        tokeniser.eofError(state);

        assertEquals("An error should be added to the error list", 1, errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);

        assertEquals("An error should be added to the error list", 1, errors.size());
    }

    @Test
    public void testErrorWithString() {
        String errorMsg = "Error occurred";
        tokeniser.error(errorMsg);

        assertEquals("An error should be added to the error list", 1, errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue("Current node should be in HTML namespace", result);
    }
}