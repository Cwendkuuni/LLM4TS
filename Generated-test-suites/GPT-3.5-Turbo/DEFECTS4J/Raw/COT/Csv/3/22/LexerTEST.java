import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        // TODO: Write assertion
    }

    @Test
    public void testReadEscape() {
        try {
            int escape = lexer.readEscape();
            // TODO: Write assertion
        } catch (IOException e) {
            // TODO: Handle exception
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder();
        lexer.trimTrailingSpaces(buffer);
        // TODO: Write assertion
    }

    @Test
    public void testReadEndOfLine() {
        try {
            boolean endOfLine = lexer.readEndOfLine(10);
            // TODO: Write assertion
        } catch (IOException e) {
            // TODO: Handle exception
        }
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(32);
        // TODO: Write assertion
    }

    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(10);
        // TODO: Write assertion
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        // TODO: Write assertion
    }

    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(44);
        // TODO: Write assertion
    }

    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape(92);
        // TODO: Write assertion
    }

    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar(34);
        // TODO: Write assertion
    }

    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart(35);
        // TODO: Write assertion
    }

}