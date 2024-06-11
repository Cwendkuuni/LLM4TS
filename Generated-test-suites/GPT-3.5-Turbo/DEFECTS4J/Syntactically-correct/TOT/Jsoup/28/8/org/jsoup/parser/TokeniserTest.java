
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead_emptyCharacterReader() {
        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testRead_emitCharacterToken() {
        tokeniser.emit("abc");

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("abc", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_emitToken() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        Token token = tokeniser.read();

        assertEquals(startTag, token);
    }

    @Test
    public void testRead_emitStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals(startTag.getType(), token.getType());
    }

    @Test
    public void testRead_emitEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.EndTag, token.getType());
    }

    @Test
    public void testEmit_emitTokenWithStartTagType() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(startTag, tokeniser.emitPending);
    }

    @Test
    public void testEmit_emitTokenWithEndTagType() {
        Token.EndTag endTag = new Token.EndTag();
        tokeniser.emit(endTag);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(endTag, tokeniser.emitPending);
    }

    @Test
    public void testEmit_emitString() {
        String str = "abc";
        tokeniser.emit(str);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(str, tokeniser.charBuffer.toString());
    }

    @Test
    public void testEmit_emitCharacter() {
        char c = 'a';
        tokeniser.emit(c);

        assertTrue(tokeniser.isEmitPending);
        assertEquals(c, tokeniser.charBuffer.charAt(0));
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.transition(newState);

        assertEquals(newState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.AttributeValue_unquoted;
        tokeniser.advanceTransition(newState);

        assertEquals(newState, tokeniser.getState());
        assertTrue(tokeniser.reader.pos() > 0);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_noAdditionalAllowedCharacter() {
        tokeniser.reader = new CharacterReader("&#65;");

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_withAdditionalAllowedCharacter_notMatching() {
        tokeniser.reader = new CharacterReader("@&#65;");

        Character allowedChar = '@';
        Character character = tokeniser.consumeCharacterReference(allowedChar, false);

        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_withAdditionalAllowedCharacter_matching() {
        tokeniser.reader = new CharacterReader("@");

        Character allowedChar = '@';
        Character character = tokeniser.consumeCharacterReference(allowedChar, false);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_emptyReader() {
        tokeniser.reader = new CharacterReader("");

        Character character = tokeniser.consumeCharacterReference(null, false);

        assertNull(character);
    }

    @Test
    public void testCreateTagPending_start() {
        boolean start = true;
        tokeniser.createTagPending(start);

        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_end() {
        boolean start = false;
        tokeniser.createTagPending(start);

        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.tagPending = new Token.StartTag();

        tokeniser.emitTagPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.commentPending = new Token.Comment();

        tokeniser.emitCommentPending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.doctypePending = new Token.Doctype();

        tokeniser.emitDoctypePending();

        assertTrue(tokeniser.isEmitPending);
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
    }

    @Test
    public void testIsAppropriateEndTagToken_matching() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.setTagName(tokeniser.lastStartTag.getTagName());

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testIsAppropriateEndTagToken_notMatching() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.setTagName("div");

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertFalse(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.setTagName(tokeniser.lastStartTag.getTagName());

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals(tokeniser.lastStartTag.getTagName(), tagName);
    }

    @Test
    public void testError_withState() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.reader = new CharacterReader("");
        tokeniser.reader.advance();

        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character ' ' in input state [TagName]", tokeniser.errors.getFirst().toString());
    }

    @Test
    public void testEOFError_withState() {
        TokeniserState state = TokeniserState.AttributeName;
        tokeniser.reader = new CharacterReader("");

        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [AttributeName]", tokeniser.errors.getFirst().toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "numeric reference with no numerals";
        tokeniser.reader = new CharacterReader("");

        tokeniser.characterReferenceError(message);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference: " + message, tokeniser.errors.getFirst().toString());
    }

    @Test
    public void testError_withErrorMessage() {
        String errorMessage = "Attributes incorrectly present on end tag";
        tokeniser.reader = new CharacterReader("");

        tokeniser.error(errorMessage);

        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMessage, tokeniser.errors.getFirst().toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();

        assertTrue(result);
    }

}

