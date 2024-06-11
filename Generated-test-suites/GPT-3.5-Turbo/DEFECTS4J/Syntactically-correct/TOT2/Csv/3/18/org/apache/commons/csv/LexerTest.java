import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import java.io.StringReader;
import java.io.IOException;

public class LexerTest {
    
    private Lexer lexer;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testMapNullToDisabled() {
        char disabled = lexer.mapNullToDisabled(null);
        assertEquals(lexer.DISABLED, disabled);
    }

    @Test
    public void testReadEscape() throws IOException {
        int escape1 = lexer.readEscape();
        assertEquals(Lexer.END_OF_STREAM, escape1);

        int escape2 = lexer.readEscape();
        assertEquals(CSVFormat.CR, escape2);
        
        // additional test cases for reading escape
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        boolean endOfLine = lexer.readEndOfLine(Lexer.LF);
        assertTrue(endOfLine);

        boolean endOfLine2 = lexer.readEndOfLine(Lexer.CR);
        assertTrue(endOfLine2);

        boolean endOfLine3 = lexer.readEndOfLine(Lexer.CR);
        assertFalse(endOfLine3);
        
        // additional test cases for reading end of line
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = lexer.isWhitespace(32); // space
        assertTrue(whitespace);

        boolean whitespace2 = lexer.isWhitespace(9); // tab
        assertTrue(whitespace2);

        boolean whitespace3 = lexer.isWhitespace(44); // comma
        assertFalse(whitespace3);
        
        // additional test cases for checking whitespace
    }

    @Test
    public void testIsStartOfLine() {
        boolean startOfLine = lexer.isStartOfLine(Lexer.UNDEFINED);
        assertTrue(startOfLine);

        boolean startOfLine2 = lexer.isStartOfLine(Lexer.LF);
        assertTrue(startOfLine2);

        boolean startOfLine3 = lexer.isStartOfLine(32); // space
        assertFalse(startOfLine3);
        
        // additional test cases for checking start of line
    }

    @Test
    public void testIsEndOfFile() {
        boolean endOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);
        assertTrue(endOfFile);

        boolean endOfFile2 = lexer.isEndOfFile(32); // space
        assertFalse(endOfFile2);
        
        // additional test cases for checking end of file
    }

    @Test
    public void testIsDelimiter() {
        boolean delimiter = lexer.isDelimiter(44); // comma
        assertFalse(delimiter);

        boolean delimiter2 = lexer.isDelimiter(59); // semicolon
        assertFalse(delimiter2);

        boolean delimiter3 = lexer.isDelimiter(lexer.format.getDelimiter());
        assertTrue(delimiter3);
        
        // additional test cases for checking delimiter
    }

    @Test
    public void testIsEscape() {
        boolean escape = lexer.isEscape(lexer.format.getEscape());
        assertTrue(escape);
        
        // additional test cases for checking escape
    }

    @Test
    public void testIsQuoteChar() {
        boolean quoteChar = lexer.isQuoteChar(lexer.format.getQuoteChar());
        assertTrue(quoteChar);
        
        // additional test cases for checking quote character
    }

    @Test
    public void testIsCommentStart() {
        boolean commentStart = lexer.isCommentStart(lexer.format.getCommentStart());
        assertTrue(commentStart);
        
        // additional test cases for checking comment start
    }
}