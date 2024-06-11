import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyToken_WhenReaderIsEmpty() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_SelfClosingFlagNotAcknowledged_ErrorThrown() {
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.StartTag());
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Doctype);
        assertEquals("Self closing flag not acknowledged", token.inspect());
    }

    @Test
    public void testRead_CharsInBuffer_WhenCharsBuilderHasLengthGreaterThanZero() {
        tokeniser.emit("Hello");
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Character);
        assertEquals("Hello", token.inspect());
    }

    @Test
    public void testRead_CharsInBuffer_WhenCharsStringIsNotEmpty() {
        tokeniser.emit("World");
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Character);
        assertEquals("World", token.inspect());
    }

    @Test
    public void testRead_EmitPending_WhenIsEmitPendingIsTrue() {
        tokeniser.emit(new Token.StartTag());
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending_FinaliseTag_WhenTagIsPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.name("div");
        tokeniser.tagPending.attribute("id", "test");
        tokeniser.tagPending.appendChild(new Token.Character("Hello"));

        tokeniser.emitTagPending();
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.StartTag);
        assertEquals("div", token.inspect());
        assertEquals("id=\"test\"", token.toString());
        assertTrue(((Token.StartTag) token).attributes.size() > 0);
        assertTrue(((Token.StartTag) token).childNodes.size() > 0);
    }

    @Test
    public void testEmitCommentPending_WhenCommentIsPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.appendChild(new Token.Character("This is a comment"));

        tokeniser.emitCommentPending();
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Comment);
        assertEquals("This is a comment", token.inspect());
    }

    @Test
    public void testEmitDoctypePending_WhenDoctypeIsPending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name("html");
        tokeniser.doctypePending.publicIdentifier("publicId");
        tokeniser.doctypePending.systemIdentifier("systemId");
        tokeniser.doctypePending.forceQuirks(true);

        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();

        assertTrue(token instanceof Token.Doctype);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", token.inspect());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsTrue_WhenLastStartTagMatchesCurrentTagName() {
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_ReturnsFalse_WhenLastStartTagDoesNotMatchCurrentTagName() {
        tokeniser.lastStartTag = "div";
        tokeniser.tagPending.name("p");
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName_ReturnsLastStartTag_WhenLastStartTagIsNotNull() {
        tokeniser.lastStartTag = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagName_ReturnsNull_WhenLastStartTagIsNull() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError_AddErrorToErrorList_WhenErrorsCanAddError() {
        tokeniser.error("Error occurred");

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Error occurred", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testError_NotAddErrorToErrorList_WhenErrorsCannotAddError() {
        Validate.fail("Error");

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testEofError_AddErrorToErrorList_WhenErrorsCanAddError() {
        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testEofError_NotAddErrorToErrorList_WhenErrorsCannotAddError() {
        Validate.fail("Error");

        assertEquals(0, tokeniser.errors.size());
    }

    @Test
    public void testUnescapeEntities_ReturnsUnescapedStringFromReader() {
        tokeniser.reader.addText("Hello &amp; World &lt;&gt;");
        String unescaped = tokeniser.unescapeEntities(false);

        assertEquals("Hello & World <>", unescaped);
    }
}