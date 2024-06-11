import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LexerTest {

    @Test
    public void testLexerInstance() {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader;
        try {
            reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
            Lexer lexer = new Lexer(format, reader);
            assertNotNull(lexer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);
        int lineNumber = lexer.getLineNumber();
        assertEquals(1, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = new CSVFormat(',', '\u001e'); // Set custom delimiter and escape character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);

        int escapedChar = lexer.readEscape();
        switch (escapedChar) {
            case Constants.CR:
                assertEquals('\r', escapedChar);
                break;
            case Constants.LF:
                assertEquals('\n', escapedChar);
                break;
            case Constants.TAB:
                assertEquals('\t', escapedChar);
                break;
            case Constants.FF:
                assertEquals('\f', escapedChar);
                break;
            case Constants.BACKSPACE:
                assertEquals('\b', escapedChar);
                break;
            default:
                Assertions.fail("Unexpected escaped character: " + (char) escapedChar);
        }

        assertEquals(Constants.LF, lexer.in.getLastChar());
    }

    @Test
    public void testReadEscapeWithInvalidChar() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);
        assertThrows(IOException.class, () -> lexer.readEscape());
        assertEquals(Constants.LF, lexer.in.getLastChar());
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with CR
        assertTrue(lexer.isStartOfLine('\r'));
        // Test with LF
        assertTrue(lexer.isStartOfLine('\n'));
        // Test with UNDEFINED (start of the file)
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
        // Test with other characters
        assertFalse(lexer.isStartOfLine('a'));
        assertFalse(lexer.isStartOfLine('b'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with valid characters
        assertFalse(lexer.isEndOfFile('a'));
        assertFalse(lexer.isEndOfFile('b'));
        // Test with end of file
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with valid characters
        assertFalse(lexer.isDelimiter('a'));
        assertFalse(lexer.isDelimiter('b'));
        // Test with delimiter
        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with valid characters
        assertFalse(lexer.isEscape('a'));
        assertFalse(lexer.isEscape('b'));
        // Test with escape character
        assertTrue(lexer.isEscape('\u001e'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with valid characters
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar('b'));
        // Test with quote character
        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        // Test with valid characters
        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart('b'));
        // Test with comment start character
        assertTrue(lexer.isCommentStart('/'));
    }

    @Test
    public void testNextToken() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);

        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        assertEquals(TokenType.DELIMITER, nextToken.getType());
        assertEquals(',', nextToken.getValue());

        token.reset();
        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.STRING, nextToken.getType());
        assertEquals("Hello", nextToken.getValue());

        token.reset();
        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.NUMBER, nextToken.getType());
        assertEquals(123, nextToken.getValue());

        token.reset();
        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.EOF, nextToken.getType());
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        String input = "   test   ";
        StringBuilder buffer = new StringBuilder(input);
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        lexer.trimTrailingSpaces(buffer);

        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);

        int c = lexer.read();
        assertTrue(lexer.readEndOfLine(c));
        assertEquals('\n', lexer.in.getLastChar());
    }

    @Test
    public void testReadEndOfLineWithEndOfFile() throws IOException {
        String csvFile = "testdata/LexerTest.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new FileReader(new File(csvFile)));
        Lexer lexer = new Lexer(format, reader);

        int c = lexer.read();
        lexer.readEndOfLine(c);
        int nextChar = lexer.read();
        assertEquals(Constants.END_OF_STREAM, nextChar);

        assertFalse(lexer.readEndOfLine(nextChar));
    }
}