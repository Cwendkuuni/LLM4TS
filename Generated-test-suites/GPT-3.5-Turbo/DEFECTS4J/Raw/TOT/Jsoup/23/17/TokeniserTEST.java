import org.jsoup.helper.SystemUtil;
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

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("Test Input");
        ParseErrorList errors = new ParseErrorList(0);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("Test Character");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmit_String() {
        String str = "Test String";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmit_Char() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&nbsp;");
        tokeniser = new Tokeniser(reader, new ParseErrorList(0));
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNotNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        tokeniser.createTagPending(start);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        boolean start = true;
        tokeniser.createTagPending(start);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "TestTag";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "TestTag";
        assertEquals("TestTag", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_State() {
        tokeniser.error(TokeniserState.Data);
        // Verify that the error is added to the error list
        assertEquals(1, tokeniser.errors.size());
        // Verify the error message
        assertEquals("Unexpected character 'T' in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testEofError_State() {
        tokeniser.eofError(TokeniserState.Data);
        // Verify that the error is added to the error list
        assertEquals(1, tokeniser.errors.size());
        // Verify the error message
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        // Verify that the error is added to the error list
        assertEquals(1, tokeniser.errors.size());
        // Verify the error message
        assertEquals("Invalid character reference: " + message, tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testError() {
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);
        // Verify that the error is added to the error list
        assertEquals(1, tokeniser.errors.size());
        // Verify the error message
        assertEquals(errorMsg, tokeniser.errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}