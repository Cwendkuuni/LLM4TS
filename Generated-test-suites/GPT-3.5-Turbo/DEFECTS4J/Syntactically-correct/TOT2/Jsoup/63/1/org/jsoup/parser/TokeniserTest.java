import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @BeforeClass
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        // Test functionality of read method
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        // Test functionality of emit method
        tokeniser.emit("test");
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testCreateTagPending() {
        // Test functionality of createTagPending method
        assertNotNull(tokeniser.createTagPending(true));
    }

    @Test
    public void testEmitTagPending() {
        // Test functionality of emitTagPending method
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        // Test functionality of createCommentPending method
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        // Test functionality of createDoctypePending method
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        // Test functionality of createTempBuffer method
        tokeniser.createTempBuffer();
        assertEquals(tokeniser.dataBuffer.length(), 0);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test functionality of isAppropriateEndTagToken method
        tokeniser.lastStartTag = "div";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test functionality of appropriateEndTagName method
        tokeniser.lastStartTag = "span";
        assertEquals(tokeniser.appropriateEndTagName(), "span");
    }
}