import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();

        assertEquals(null, token);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("string");

        Token token = tokeniser.read();

        assertEquals("string", token.toString());
    }

    @Test
    public void testEmitCharArray() {
        tokeniser.emit(new char[]{'c', 'h', 'a', 'r'});

        Token token = tokeniser.read();

        assertEquals("char", token.toString());
    }

    @Test
    public void testEmitInt() {
        tokeniser.emit(65);

        Token token = tokeniser.read();

        assertEquals("A", token.toString());
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        tagPending.start();

        tokeniser.emitTagPending();

        Token token = tokeniser.read();

        assertEquals("<start>", token.toString());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();

        Token.Comment comment = tokeniser.commentPending;

        assertEquals("", comment.toString());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.append("comment");

        tokeniser.emitCommentPending();

        Token token = tokeniser.read();

        assertEquals("<!-- comment -->", token.toString());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();

        Token.Doctype doctype = tokeniser.doctypePending;

        assertEquals("", doctype.toString());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.append("doctype");

        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();

        assertEquals("<!doctype doctype>", token.toString());
    }

    // Additional test cases for other methods in Tokeniser class can be added here

}