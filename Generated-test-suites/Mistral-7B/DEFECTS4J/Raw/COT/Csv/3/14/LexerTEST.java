import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u001A');
        ExtendedBufferedReader in = new ExtendedBufferedReader(System.in);
        Lexer lexer = new Lexer(format, in);

        long lineNumber = lexer.getLineNumber();
        assertEquals(System.lineSeparator(), System.lineSeparator() + "\u001A", System.getProperty("line.separator"));
        assertEquals(1, lineNumber);

        // Test reading multiple lines
        String input = "Line 1\nLine 2\nLine 3\n";
        ExtendedBufferedReader inputStream = new ExtendedBufferedReader(new ByteArrayInputStream(input.getBytes()));
        lexer = new Lexer(format, inputStream);

        lineNumber = lexer.getLineNumber();
        assertEquals(1, lineNumber);

        inputStream.readLine();

        lineNumber = lexer.getLineNumber();
        assertEquals(2, lineNumber);

        inputStream.readLine();

        lineNumber = lexer.getLineNumber();
        assertEquals(3, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u001A');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new ByteArrayInputStream("r\r\n".getBytes()));
        Lexer lexer = new Lexer(format, in);

        int escapedChar = lexer.readEscape();
        assertEquals(Constants.CR, escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidSequence() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u001A');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new ByteArrayInputStream("x\r\n".getBytes()));
        Lexer lexer = new Lexer(format, in);

        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder stringBuilder = new StringBuilder(" abc def   ");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(stringBuilder);
        assertEquals("abc def", stringBuilder.toString());
    }

    @Test
    public void testIsStartOfLine() {
        int[] testCases = {-1, Constants.LF, Constants.CR, ' '};

        for (int c : testCases) {
            Lexer lexer = new Lexer(null, null);
            boolean result = lexer.isStartOfLine(c);

            if (c == Constants.LF) {
                assertTrue(result);
            } else if (c == Constants.CR) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testIsEndOfFile() {
        int[] testCases = {-1, 'a', Constants.END_OF_STREAM};

        for (int c : testCases) {
            Lexer lexer = new Lexer(null, null);
            boolean result = lexer.isEndOfFile(c);

            if (c == Constants.END_OF_STREAM) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
        }
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);
        char delimiter = ',';

        boolean result = lexer.isDelimiter(delimiter);
        assertTrue(result);

        result = lexer.isDelimiter('a');
        assertFalse(result);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);
        char escape = '\u001E';

        boolean result = lexer.isEscape(escape);
        assertTrue(result);

        result = lexer.isEscape('a');
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);
        char quoteChar = '\u001C';

        boolean result = lexer.isQuoteChar(quoteChar);
        assertTrue(result);

        result = lexer.isQuoteChar('a');
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);
        char commentStart = '\u001A';

        boolean result = lexer.isCommentStart(commentStart);
        assertTrue(result);

        result = lexer.isCommentStart('a');
        assertFalse(result);
    }
}