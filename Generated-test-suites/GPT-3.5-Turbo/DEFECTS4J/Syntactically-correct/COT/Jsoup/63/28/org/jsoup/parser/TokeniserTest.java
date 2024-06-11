import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        tokeniser.emit(Token.StartTag());
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitInts() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        assertNotNull(tokeniser.emitPending);
        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.CharacterReferenceInData);
        assertEquals(TokeniserState.CharacterReferenceInData, tokeniser.getState());
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.reader.add("&#65;");
        int[] code = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{65}, code);
    }

    @Test
    public void testCreateTagPending() {
        assertNotNull(tokeniser.createTagPending(true));
        assertNotNull(tokeniser.tagPending);
        assertEquals(Token.TokenType.StartTag, tokeniser.tagPending.type);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.emitTagPending();
        assertEquals("test", ((Token.StartTag) tokeniser.emitPending).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        assertNotNull(tokeniser.createCommentPending());
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "test comment";
        tokeniser.emitCommentPending();
        assertEquals("test comment", ((Token.Comment) tokeniser.emitPending).data);
    }

    @Test
    public void testCreateDoctypePending() {
        assertNotNull(tokeniser.createDoctypePending());
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();
        assertEquals("html", ((Token.Doctype) tokeniser.emitPending).name);
    }

    @Test
    public void testCreateTempBuffer() {
        assertNotNull(tokeniser.createTempBuffer());
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.tagPending = Token.StartTag();
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "div";
        assertEquals("div", tokeniser.appropriateEndTagName());
        tokeniser.lastStartTag = null;
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character ' ' in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("error message");
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference: error message", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testErrorWithString() {
        tokeniser.error("error message");
        assertEquals(1, tokeniser.errors.size());
        assertEquals("error message", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        tokeniser.reader.add("&lt;&gt;&amp;&#65;");
        String unescaped = tokeniser.unescapeEntities(false);
        assertEquals("<>&A", unescaped);
    }
}