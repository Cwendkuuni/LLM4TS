import org.jsoup.parser.*;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(new CharacterReader(""), errors);
    }

    @Test
    public void testRead() {
        // Test reading a token
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        // Test emitting a token
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testTagPending() {
        // Test creating and emitting a tag pending
        tokeniser.createTagPending(true);
        tagPending = tokeniser.tagPending;
        assertNotNull(tagPending);
        tagPending.tagName = "test";
        tokeniser.emitTagPending();
        assertEquals("test", tagPending.tagName);
    }

    @Test
    public void testEmitCommentPending() {
        // Test creating and emitting a comment pending
        tokeniser.createCommentPending();
        commentPending = tokeniser.commentPending;
        assertNotNull(commentPending);
        commentPending.setData("Test comment");
        tokeniser.emitCommentPending();
        assertEquals("Test comment", commentPending.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        // Test creating and emitting a doctype pending
        tokeniser.createDoctypePending();
        doctypePending = tokeniser.doctypePending;
        assertNotNull(doctypePending);
        doctypePending.setPublicIdentifier("Test public identifier");
        tokeniser.emitDoctypePending();
        assertEquals("Test public identifier", doctypePending.getPublicIdentifier());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consuming a character reference
        tokeniser.emit("&copy;");
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A9', reference.charValue());
    }

    // Test other public methods in Tokeniser class

}