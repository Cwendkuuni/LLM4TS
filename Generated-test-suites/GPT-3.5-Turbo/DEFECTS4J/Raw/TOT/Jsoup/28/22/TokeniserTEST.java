import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("Test input");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_emptyInput_shouldReturnNull() {
        Token result = tokeniser.read();
        assertNull(result);
    }

    @Test
    public void testRead_validInput_shouldReturnToken() {
        tokeniser.emit(new Token.Character("Test token"));
        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("Test token", result.toString());
    }

    @Test
    public void testEmitString_validInput_shouldEmitToken() {
        tokeniser.emit("Test string");
        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("Test string", result.toString());
    }

    @Test
    public void testEmitChar_validInput_shouldEmitToken() {
        tokeniser.emit('A');
        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("A", result.toString());
    }

    @Test
    public void testConsumeCharacterReference_emptyInput_shouldReturnNull() {
        Character result = tokeniser.consumeCharacterReference(null, true);
        assertNull(result);
    }

    @Test
    public void testConsumeCharacterReference_numericReferenceWithSemicolon_shouldReturnCharacter() {
        tokeniser.emit("&#65;");
        Character result = tokeniser.consumeCharacterReference(null, true);
        assertNotNull(result);
        assertEquals('A', result.charValue());
    }

    @Test
    public void testConsumeCharacterReference_namedReferenceWithSemicolon_shouldReturnCharacter() {
        tokeniser.emit("&lt;");
        Character result = tokeniser.consumeCharacterReference(null, true);
        assertNotNull(result);
        assertEquals('<', result.charValue());
    }

    @Test
    public void testCreateTagPending_startTag_shouldCreateStartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.StartTag);
    }

    @Test
    public void testCreateTagPending_endTag_shouldCreateEndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tokeniser.tagPending);
        assertTrue(tokeniser.tagPending instanceof Token.EndTag);
    }

    @Test
    public void testEmitTagPending_shouldEmitTag() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.tagPending.attributes.put("id", "test");
        tokeniser.emitTagPending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("<div id=\"test\">", result.toString());
    }

    @Test
    public void testCreateCommentPending_shouldCreateComment() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending_shouldEmitComment() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "Test comment";
        tokeniser.emitCommentPending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("<!--Test comment-->", result.toString());
    }

    @Test
    public void testCreateDoctypePending_shouldCreateDoctype() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending_shouldEmitDoctype() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.doctypePending.publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        tokeniser.doctypePending.systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        tokeniser.emitDoctypePending();

        Token result = tokeniser.read();
        assertNotNull(result);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", result.toString());
    }

    @Test
    public void testCreateTempBuffer_shouldCreateDataBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken_matchingTags_shouldReturnTrue() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_nonMatchingTags_shouldReturnFalse() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "p";

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_shouldReturnTagName() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "div";

        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_shouldAddErrorToList() {
        tokeniser.error(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
        assertEquals("Unexpected character 'T' in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testEofError_shouldAddErrorToList() {
        tokeniser.eofError(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError_shouldAddErrorToList() {
        tokeniser.characterReferenceError("Invalid character reference");
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
        assertEquals("Invalid character reference: Invalid character reference", errors.get(0).toString());
    }

    @Test
    public void testError_withErrorMessage_shouldAddErrorToList() {
        tokeniser.error("Test error");
        ParseErrorList errors = tokeniser.errors;
        assertFalse(errors.isEmpty());
        assertEquals("Test error", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS_shouldReturnTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}