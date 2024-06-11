
import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test basic functionality of the read() method
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token token = tokeniser.read();
        assertEquals("t", token.toString());
    }

    @Test
    public void testTokeniserError() {
        // Test if an error occurs when tokenising
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.error(TokeniserState.Data);
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testEofError() {
        // Test if an EOF error occurs when tokenising
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.eofError(TokeniserState.Data);
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test if a character reference error occurs when tokenising
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""));
        tokeniser.characterReferenceError();
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        // Test if a general error occurs when tokenising
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.error("Error");
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testCreateTagPendingStart() {
        // Test creating a pending start tag
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
    }

    @Test
    public void testCreateTagPendingEnd() {
        // Test creating a pending end tag
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token.Tag tag = tokeniser.createTagPending(false);
        assertNotNull(tag);
    }

    @Test
    public void testEmitTagPending() {
        // Test emitting a tag pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tag, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        // Test creating a comment pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        // Test emitting a comment pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createCommentPending();
        tokeniser.commentPending.append("test");
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        // Test creating a doctype pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        // Test emitting a doctype pending
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.append("test");
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        // Test creating a temporary buffer
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test if the current end tag token is appropriate
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.createTagPending(true);
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        // Test if tracking errors is enabled
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        // Test setting the track errors flag
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        // Test if the current node is in the HTML namespace
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("test"));
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

}
