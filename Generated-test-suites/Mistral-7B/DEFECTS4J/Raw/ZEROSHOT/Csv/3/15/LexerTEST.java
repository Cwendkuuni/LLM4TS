import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormatBuilder().setDelimiter(';').build();
        in = new ExtendedBufferedReader(System.in);
        lexer = new Lexer(format, in);
    }

    @Test
    public void testIsStartOfLine() throws Exception {
        int c = Constants.LF;
        assertTrue(lexer.isStartOfLine(c));

        c = Constants.CR;
        assertTrue(lexer.isStartOfLine(c));

        c = Constants.UNDEFINED;
        assertTrue(lexer.isStartOfLine(c));

        c = 'A';
        assertFalse(lexer.isStartOfLine(c));
    }

    @Test
    public void testIsWhitespace() throws Exception {
        for (char c = '\u0000'; c <= '\u007F'; c++) {
            int i = (int) c;
            if (Character.isWhitespace(c)) {
                assertTrue(lexer.isWhitespace(i));
            } else {
                assertFalse(lexer.isWhitespace(i));
            }
        }
    }

    @Test
    public void testIsEndOfFile() throws Exception {
        int c = Constants.END_OF_STREAM;
        assertTrue(lexer.isEndOfFile(c));
    }

    @Test
    public void testIsDelimiter() throws Exception {
        int c = format.getDelimiter();
        assertTrue(lexer.isDelimiter(c));
    }

    @Test
    public void testIsEscape() throws Exception {
        int c = lexer.escape;
        assertTrue(lexer.isEscape(c));
    }

    @Test
    public void testIsQuoteChar() throws Exception {
        int c = lexer.quoteChar;
        assertTrue(lexer.isQuoteChar(c));
    }

    @Test
    public void testIsCommentStart() throws Exception {
        int c = lexer.commmentStart;
        assertTrue(lexer.isCommentStart(c));
    }

    @Test
    public void testReadEndOfLine() throws Exception {
        int c = Constants.CR;
        in.read(); // read CR
        assertTrue(lexer.readEndOfLine(in.read())); // read LF
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLineEndOfStream() throws Exception {
        int c = Constants.END_OF_STREAM;
        lexer.readEndOfLine(c);
    }

    @Test
    public void testNextToken() throws Exception {
        Token token = new Token();
        String testString = "a;b;c\r\n";
        in.readLine();
        in.reset();
        in.readText(testString);

        Token nextToken = lexer.nextToken(token);

        assertEquals("a", nextToken.getValue());
        assertEquals(Constants.NOT_QUOTED_STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);

        assertEquals("b", nextToken.getValue());
        assertEquals(Constants.NOT_QUOTED_STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);

        assertEquals("c", nextToken.getValue());
        assertEquals(Constants.NOT_QUOTED_STRING, nextToken.getType());

        nextToken = lexer.nextToken(token);

        assertEquals(Constants.END_OF_STREAM, nextToken.getValue());
        assertEquals(Constants.END_OF_STREAM, nextToken.getType());
    }

    @Test
    public void testTrimTrailingSpaces() throws Exception {
        StringBuilder buffer = new StringBuilder("a b ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("a b", buffer.toString());
    }
}