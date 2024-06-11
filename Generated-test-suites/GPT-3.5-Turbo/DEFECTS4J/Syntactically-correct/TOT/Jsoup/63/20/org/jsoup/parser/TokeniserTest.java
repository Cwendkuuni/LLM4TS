import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.helper.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterReader reader;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character();
        expectedToken.data(str);
        assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character();
        expectedToken.data(new String(chars));
        assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testEmitIntArrray() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character();
        expectedToken.data(new String(codepoints, 0, codepoints.length));
        assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        Token expectedToken = new Token.Character();
        expectedToken.data(String.valueOf(c));
        assertEquals(expectedToken, emittedToken);
    }

    @Test
    public void testReadWithEmitPending() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        Token emittedToken1 = tokeniser.read();
        Token emittedToken2 = tokeniser.read();
        assertEquals(token, emittedToken1);
        assertNull(emittedToken2);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token.Comment comment = tokeniser.commentPending;
        assertNotNull(comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token.Doctype doctype = tokeniser.doctypePending;
        assertNotNull(doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken instanceof Token.Doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder dataBuffer = tokeniser.dataBuffer;
        assertNotNull(dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagTokenWithoutStartTag() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenWithStartTag() {
        tokeniser.lastStartTag = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagNameWithoutStartTag() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testAppropriateEndTagNameWithStartTag() {
        String tagName = "test";
        tokeniser.lastStartTag = tagName;
        assertEquals(tagName, tokeniser.appropriateEndTagName());
    }

    @Test
    public void testErrorWithState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertEquals(1, errors.size());
        assertEquals(String.format("Unexpected character '%s' in input state [%s]", reader.current(), state), errors.get(0).getErrorMessage());
    }

    @Test
    public void testEOFErrorWithState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals(1, errors.size());
        assertEquals(String.format("Unexpectedly reached end of file (EOF) in input state [%s]", state), errors.get(0).getErrorMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "test";
        tokeniser.characterReferenceError(message);
        assertEquals(1, errors.size());
        assertEquals(String.format("Invalid character reference: %s", message), errors.get(0).getErrorMessage());
    }

    @Test
    public void testError() {
        String errorMsg = "test";
        tokeniser.error(errorMsg);
        assertEquals(1, errors.size());
        assertEquals(errorMsg, errors.get(0).getErrorMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntitiesInAttribute() {
        String attribute = "&lt;test&gt;";
        reader = new CharacterReader(attribute);
        tokeniser = new Tokeniser(reader, errors);
        String unescapedString = tokeniser.unescapeEntities(true);
        assertEquals("<test>", unescapedString);
    }
}