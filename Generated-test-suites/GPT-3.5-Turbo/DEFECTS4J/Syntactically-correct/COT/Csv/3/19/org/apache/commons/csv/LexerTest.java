
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test getLineNumber method
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        try {
            // Test readEscape method with valid escape character 'r'
            int escapedCharacter = lexer.readEscape();
            assertEquals('\r', escapedCharacter);

            // Test readEscape method with valid escape character 'n'
            escapedCharacter = lexer.readEscape();
            assertEquals('\n', escapedCharacter);

            // Test readEscape method with valid escape character 't'
            escapedCharacter = lexer.readEscape();
            assertEquals('\t', escapedCharacter);

            // Test readEscape method with valid escape character 'b'
            escapedCharacter = lexer.readEscape();
            assertEquals('\b', escapedCharacter);

            // Test readEscape method with valid escape character 'f'
            escapedCharacter = lexer.readEscape();
            assertEquals('\f', escapedCharacter);

            // Test readEscape method with invalid escape character 'x'
            escapedCharacter = lexer.readEscape();
            assertEquals(Lexer.END_OF_STREAM, escapedCharacter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test trimTrailingSpaces method with a StringBuilder containing trailing spaces
        StringBuilder buffer = new StringBuilder("Hello World   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello World", buffer.toString());

        // Test trimTrailingSpaces method with a StringBuilder containing no trailing spaces
        buffer = new StringBuilder("Hello World");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello World", buffer.toString());

        // Test trimTrailingSpaces method with an empty StringBuilder
        buffer = new StringBuilder();
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        try {
            // Test readEndOfLine method with LF character
            boolean endOfLine = lexer.readEndOfLine('\n');
            assertEquals(true, endOfLine);

            // Test readEndOfLine method with CR character
            endOfLine = lexer.readEndOfLine('\r');
            assertEquals(true, endOfLine);

            // Test readEndOfLine method with CR and LF characters together
            endOfLine = lexer.readEndOfLine('\r\n');
            assertEquals(true, endOfLine);

            // Test readEndOfLine method with a non-line-terminator character
            endOfLine = lexer.readEndOfLine('A');
            assertEquals(false, endOfLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsWhitespace() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isWhitespace method with a whitespace character
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertEquals(true, isWhitespace);

        // Test isWhitespace method with a non-whitespace character
        isWhitespace = lexer.isWhitespace('A');
        assertEquals(false, isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isStartOfLine method with a start of line character
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        assertEquals(true, isStartOfLine);

        // Test isStartOfLine method with a non-start of line character
        isStartOfLine = lexer.isStartOfLine('A');
        assertEquals(false, isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isEndOfFile method with an end of file character
        boolean isEndOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);
        assertEquals(true, isEndOfFile);

        // Test isEndOfFile method with a non-end of file character
        isEndOfFile = lexer.isEndOfFile('A');
        assertEquals(false, isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isDelimiter method with a delimiter character
        boolean isDelimiter = lexer.isDelimiter(',');
        assertEquals(true, isDelimiter);

        // Test isDelimiter method with a non-delimiter character
        isDelimiter = lexer.isDelimiter('A');
        assertEquals(false, isDelimiter);
    }

    @Test
    public void testIsEscape(){
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isEscape method with an escape character
        boolean isEscape = lexer.isEscape('\\');
        assertEquals(true, isEscape);

        // Test isEscape method with a non-escape character
        isEscape = lexer.isEscape('A');
        assertEquals(false, isEscape);
    }

    @Test
    public void testIsQuoteChar(){
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isQuoteChar method with a quote character
        boolean isQuoteChar = lexer.isQuoteChar('"');
        assertEquals(true, isQuoteChar);

        // Test isQuoteChar method with a non-quote character
        isQuoteChar = lexer.isQuoteChar('A');
        assertEquals(false, isQuoteChar);
    }

    @Test
    public void testIsCommentStart(){
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());

        // Test isCommentStart method with a comment start character
        boolean isCommentStart = lexer.isCommentStart('#');
        assertEquals(true, isCommentStart);

        // Test isCommentStart method with a non-comment start character
        isCommentStart = lexer.isCommentStart('A');
        assertEquals(false, isCommentStart);
    }
}
