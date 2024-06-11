import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        Token readToken = tokeniser.read();

        assertEquals(token, readToken);
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
        String str = "test string";
        tokeniser.emit(str);

        Token emittedToken = tokeniser.read();

        assertEquals(str, emittedToken.toString());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);

        Token emittedToken = tokeniser.read();

        assertEquals(Character.toString(c), emittedToken.toString());
    }

    @Test
    public void testTransition() {
        TokeniserState newState = TokeniserState.Data;
        tokeniser.transition(newState);

        TokeniserState currentState = tokeniser.getState();
        
        assertEquals(newState, currentState);
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState newState = TokeniserState.Comment;
        tokeniser.advanceTransition(newState);

        TokeniserState currentState = tokeniser.getState();
        
        assertEquals(newState, currentState);
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        int[] expectedResult = new int[]{65};
        tokeniser.reader.consume("&#65;");
        int[] codeRef = tokeniser.consumeCharacterReference(null, false);

        assertEquals(expectedResult[0], codeRef[0]);
    }

    @Test
    public void testCreateTagPendingStart() {
        Token.Tag tag = tokeniser.createTagPending(true);

        assertTrue(tag.startTag);
    }

    @Test
    public void testCreateTagPendingEnd() {
        Token.Tag tag = tokeniser.createTagPending(false);

        assertFalse(tag.startTag);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "div";
        tokeniser.emitTagPending();

        Token emittedToken = tokeniser.read();

        assertEquals("div", ((Token.Tag)emittedToken).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        assertTrue(tokeniser.commentPending.isComment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "Test comment";
        tokeniser.emitCommentPending();

        Token emittedToken = tokeniser.read();

        assertEquals("Test comment", ((Token.Comment)emittedToken).data);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        assertTrue(tokeniser.doctypePending.isDoctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.emitDoctypePending();

        Token emittedToken = tokeniser.read();

        assertEquals("html", ((Token.Doctype)emittedToken).name);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();

        assertTrue(tokeniser.dataBuffer.isEmpty());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "div";
        tokeniser.createTagPending(false);
        tokeniser.tagPending.tagName = "div";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "div";

        String tagName = tokeniser.appropriateEndTagName();

        assertEquals("div", tagName);
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Comment;
        tokeniser.error(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character '' in input state [Comment]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.eofError(state);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [TagName]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "invalid named referenece 'test'";
        tokeniser.characterReferenceError(message);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference: invalid named referenece 'test'", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testErrorString() {
        String errorMsg = "Unexpected error occurred";
        tokeniser.error(errorMsg);

        assertEquals(1, tokeniser.errors.size());
        assertEquals("Parse error at 0: Unexpected error occurred", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(inHtmlNS);
    }

    @Test
    public void testUnescapeEntities() {
        String str = "This &lt; is a &amp; example &gt; string";
        tokeniser.reader = new CharacterReader(str);
        
        String unescapedString = tokeniser.unescapeEntities(false);

        assertEquals("This < is a & example > string", unescapedString);
    }
}