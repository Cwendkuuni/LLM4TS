import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {
    private Lexer lexer;
    private ExtendedBufferedReader mockReader;

    @Before
    public void setup() throws IOException {
        mockReader = new MockExtendedBufferedReader();
        lexer = new Lexer(new CSVFormat(',', '\u0000'), mockReader);
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        // test with CR
        int cr = (int) '\r';
        assertTrue(lexer.isStartOfLine(cr));

        // test with LF
        int lf = (int) '\n';
        assertTrue(lexer.isStartOfLine(lf));

        // test with EOF
        int eof = ExtendedBufferedReader.EOF;
        assertTrue(lexer.isStartOfLine(eof));

        // test with other character
        int other = 'A';
        assertFalse(lexer.isStartOfLine(other));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        // test with valid character
        int valid = 'A';
        assertFalse(lexer.isEndOfFile(valid));

        // test with EOF
        int eof = ExtendedBufferedReader.EOF;
        assertTrue(lexer.isEndOfFile(eof));
    }

    @Test
    public void testIsWhitespace() throws IOException {
        StringBuilder buffer = new StringBuilder();
        lexer.nextToken(null); // read next character
        int c = lexer.getLineNumber();

        // test with whitespace character
        buffer.append(' ');
        lexer.trimTrailingSpaces(buffer);
        assertTrue(lexer.isWhitespace(buffer.charAt(0)));

        // test with non-whitespace character
        buffer.setLength(0);
        buffer.append('A');
        assertFalse(lexer.isWhitespace(buffer.charAt(0)));

        // test with Delimiter character
        lexer.delimiter = 'B';
        lexer.trimTrailingSpaces(buffer);
        assertFalse(lexer.isWhitespace(buffer.charAt(0)));

        // test with EOF
        lexer.in.close();
        lexer.nextToken(null); // read next character
        assertFalse(lexer.isWhitespace(lexer.getLastChar()));
    }

    // add other test methods for other methods in the Lexer class
}