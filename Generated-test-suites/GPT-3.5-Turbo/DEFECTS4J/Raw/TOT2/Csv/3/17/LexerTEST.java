import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(format, in);
        
        assertEquals('a', lexer.mapNullToDisabled('a')); // typical case
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null)); // edge case: null input
    }

    @Test
    public void testReadEscape() {
        // to be filled with test cases for readEscape method
    }

    @Test
    public void testTrimTrailingSpaces() {
        // to be filled with test cases for trimTrailingSpaces method
    }

    @Test
    public void testReadEndOfLine() {
        // to be filled with test cases for readEndOfLine method
    }

    @Test
    public void testIsWhitespace() {
        // to be filled with test cases for isWhitespace method
    }

    @Test
    public void testIsStartOfLine() {
        // to be filled with test cases for isStartOfLine method
    }

    @Test
    public void testIsEndOfFile() {
        // to be filled with test cases for isEndOfFile method
    }

    @Test
    public void testIsDelimiter() {
        // to be filled with test cases for isDelimiter method
    }

    @Test
    public void testIsEscape() {
        // to be filled with test cases for isEscape method
    }

    @Test
    public void testIsQuoteChar() {
        // to be filled with test cases for isQuoteChar method
    }

    @Test
    public void testIsCommentStart() {
        // to be filled with test cases for isCommentStart method
    }

}