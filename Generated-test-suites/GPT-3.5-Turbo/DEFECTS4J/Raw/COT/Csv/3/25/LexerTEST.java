
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        //Test if the line number is initialized correctly
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        Lexer lexer = new Lexer(format, in);
        try {
            int escapeChar = lexer.readEscape();
            //Test if the escape character is successfully read
            assertEquals(escapeChar, '\\');
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("test  ");
        lexer.trimTrailingSpaces(buffer);
        //Test if the trailing spaces are trimmed correctly
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new Lexer(format, in);
        try {
            boolean endOfLine = lexer.readEndOfLine(LF);
            //Test if the line feed character is identified as end of line
            assertTrue(endOfLine);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        boolean whitespace = lexer.isWhitespace(' ');
        //Test if the space character is identified as whitespace
        assertTrue(whitespace);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        boolean startOfLine = lexer.isStartOfLine(LF);
        //Test if the line feed character is identified as start of line
        assertTrue(startOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        boolean endOfFile = lexer.isEndOfFile(END_OF_STREAM);
        //Test if the end of stream character is identified as end of file
        assertTrue(endOfFile);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        boolean delimiter = lexer.isDelimiter(',');
        //Test if the comma character is identified as delimiter
        assertTrue(delimiter);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        boolean escape = lexer.isEscape('\\');
        //Test if the backslash character is identified as escape character
        assertTrue(escape);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        boolean quoteChar = lexer.isQuoteChar('"');
        //Test if the double quote character is identified as quote character
        assertTrue(quoteChar);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        boolean commentStart = lexer.isCommentStart('#');
        //Test if the hash character is identified as comment start
        assertTrue(commentStart);
    }
}
