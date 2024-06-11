import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;
    
    @Before
    public void setup() {
        errors = new ParseErrorList(0);
        reader = new CharacterReader();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_noTokensInBuffer_returnsNull() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_characterTokenInBuffer_returnsCharacterToken() {
        reader.addBuffer("abc");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", ((Token.Character) token).getData());
    }

    @Test
    public void testRead_emitPendingTokenIsStartTag_returnsStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("div", null);
        tokeniser.emit(startTag);

        Token token = tokeniser.read();

        assertSame(startTag, token);
    }

    @Test
    public void testEmitTagPending_emitPendingTagIsStartTag_tagsAreEqualAfterEmit() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.attributes.put("attr", "value");
        tag.pendingAttributeValue = "value";
        
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertSame(tag, token);
    }

    // Complete JUnit test file

    @Test
    public void testRead_emitPendingTokenIsEndTag_returnsEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("div");
        tokeniser.emit(endTag);

        Token token = tokeniser.read();

        assertSame(endTag, token);
    }

    @Test
    public void testEmitTagPending_emitPendingTagIsEndTag_tagsAreEqualAfterEmit() {
        Token.Tag tag = tokeniser.createTagPending(false);
        tag.attributes.put("attr", "value");

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertSame(tag, token);
    }

    @Test
    public void testEmit_stringToken_returnsCharacterToken() {
        tokeniser.emit("abc");

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_charToken_returnsCharacterToken() {
        tokeniser.emit('a');

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testState_getState_returnsCurrentState() {
        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition_setState_setsStateToGivenState() {
        tokeniser.transition(TokeniserState.Rcdata);

        TokeniserState state = tokeniser.getState();

        assertEquals(TokeniserState.Rcdata, state);
    }

    @Test
    public void testAdvanceTransition_setStateAndAdvance_setsStateToGivenStateAndAdvancesReader() {
        reader.addBuffer("abc");
        reader.advance();

        tokeniser.advanceTransition(TokeniserState.Data);

        TokeniserState state = tokeniser.getState();
        char currentChar = reader.current();

        assertEquals(TokeniserState.Data, state);
        assertEquals('b', currentChar);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_setFlag_stateFlagIsTrue() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_readerIsEmpty_returnsNull() {
        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('a');
        Character character = characterReference.consumeCharacterReference(true);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_additionalAllowedCharIsCurrentCharacter_returnsNull() {
        reader.addBuffer("abc");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('a');
        Character character = characterReference.consumeCharacterReference(true);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_currentCharacterIsWhitespaceOrSpecialCharacter_returnsNull() {
        reader.addBuffer("abc");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('z');
        characterReference.setInAttribute(true);
        Character character = characterReference.consumeCharacterReference(true);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_numericReferenceWithSemicolon_returnsCharacter() {
        reader.addBuffer("&#65;");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('z');
        Character character = characterReference.consumeCharacterReference(true);

        assertNotNull(character);
        assertEquals('A', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_namedReferenceThanOrEqualToGivenName_returnsCharacter() {
        reader.addBuffer("&amp;");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('z');
        Character character = characterReference.consumeCharacterReference(true);

        assertNotNull(character);
        assertEquals('&', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_inAttributeAndFollowingCharIsLetterOrDigitOrSpecialCharacter_returnsNull() {
        reader.addBuffer("&amp;");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('z');
        characterReference.setInAttribute(true);
        Character character = characterReference.consumeCharacterReference(true);

        assertNull(character);
    }

    @Test
    public void testConsumeCharacterReference_missingSemicolon_returnsReplacementChar() {
        reader.addBuffer("&#65");
        reader.advance();

        CharacterReference characterReference = new CharacterReference(tokeniser, null);
        characterReference.setAdditionalAllowedCharacter('z');
        Character character = characterReference.consumeCharacterReference(true);

        assertNotNull(character);
        assertEquals('\uFFFD', character.charValue());
    }

    @Test
    public void testCreateTagPending_startIsTrue_returnsStartTag() {
        Tokeniser.Tag tag = tokeniser.createTagPending(true);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_startIsFalse_returnsEndTag() {
        Tokeniser.Tag tag = tokeniser.createTagPending(false);

        assertNotNull(tag);
        assertTrue(tag instanceof Token.EndTag);
    }

    @Test
    public void testEmitCommentPending_emitsCommentToken() {
        Token.Comment comment = new Token.Comment();
        tokeniser.createCommentPending();
        tokeniser.commentPending = comment;

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertSame(comment, token);
    }

    @Test
    public void testCreateDoctypePending_createsDoctypeToken() {
        tokeniser.createDoctypePending();

        Token token = tokeniser.doctypePending;

        assertNotNull(token);
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending_emitsDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        tokeniser.doctypePending = doctype;

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertSame(doctype, token);
    }

    @Test
    public void testCreateTempBuffer_createsStringBuffer() {
        tokeniser.createTempBuffer();

        StringBuilder buffer = tokeniser.dataBuffer;

        assertNotNull(buffer);
    }
    
    @Test
    public void testIsAppropriateEndTagToken_tagPendingIsSameAsLastStartTag_returnsTrue() {
        tokeniser.tagPending = new Token.StartTag("div", null);
        tokeniser.lastStartTag = (Token.StartTag) tokeniser.tagPending;

        boolean result = tokeniser.isAppropriateEndTagToken();

        assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName_returnsLastStartTagName() {
        tokeniser.lastStartTag = new Token.StartTag("div", null);

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals("div", tagName);
    }

    @Test
    public void testError_addErrorToErrorList() {
        tokeniser.errors = new ParseErrorList(1);

        tokeniser.error(TokeniserState.Data);

        assertTrue(tokeniser.errors.size() > 0);
    }
    
    @Test
    public void testEofError_addErrorToErrorList() {
        tokeniser.errors = new ParseErrorList(1);

        tokeniser.eofError(TokeniserState.Data);

        assertTrue(tokeniser.errors.size() > 0);
    }

    private class CharacterReference extends Tokeniser {
        private Character additionalAllowedCharacter;
        private boolean inAttribute;

        CharacterReference(CharacterReader reader, ParseErrorList errors) {
            super(reader, errors);
            additionalAllowedCharacter = null;
            inAttribute = false;
        }

        void setAdditionalAllowedCharacter(Character additionalAllowedCharacter) {
            this.additionalAllowedCharacter = additionalAllowedCharacter;
        }

        void setInAttribute(boolean inAttribute) {
            this.inAttribute = inAttribute;
        }

        @Override
        void error(TokeniserState state) {
        }

        @Override
        void emit(Token token) {
        }

        @Override
        void emit(String str) {
        }

        @Override
        void emit(char c) {
        }
    }
}