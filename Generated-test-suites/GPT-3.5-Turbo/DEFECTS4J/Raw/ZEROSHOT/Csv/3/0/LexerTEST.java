import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {
    private CSVFormat format;
    private ExtendedBufferedReader in;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        in = new ExtendedBufferedReader(new StringReader(""));
    }

    @Test
    public void testConstructor() {
        Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);
    }

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new Lexer(format, in);
        int result = lexer.readEscape();
        assertEquals(Lexer.END_OF_STREAM, result);
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.readEndOfLine('\n');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isStartOfLine(0);
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEndOfFile(-1);
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isDelimiter(',');
        assertTrue(result);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEscape('\\');
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isQuoteChar('"');
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isCommentStart('#');
        assertFalse(result);
    }
}