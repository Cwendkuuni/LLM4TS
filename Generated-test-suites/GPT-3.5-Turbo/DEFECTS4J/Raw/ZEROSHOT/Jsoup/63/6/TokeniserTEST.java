import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null, null);
    }

    @Test
    public void testRead() {
        Token token1 = tokeniser.read();
        assertNotNull(token1);
        
        Token token2 = tokeniser.read();
        assertNotNull(token2);
        
        assertNotSame(token1, token2);
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        
        assertTrue(tag.start);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        
        tokeniser.emitTagPending();
        
        assertEquals(tag, tokeniser.read());
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
        Token.Comment comment = tokeniser.commentPending;
        
        tokeniser.emitCommentPending();
        
        assertEquals(comment, tokeniser.read());
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
        Token.Doctype doctype = tokeniser.doctypePending;
        
        tokeniser.emitDoctypePending();
        
        assertEquals(doctype, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.lastStartTag = "tag1";
        tokeniser.createTagPending(false);
        
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.lastStartTag = "tag1";
        
        assertEquals("tag1", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        
        assertFalse(tokeniser.errorList.isEmpty());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        
        assertFalse(tokeniser.errorList.isEmpty());
    }
}