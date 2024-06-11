import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("1\n2\n3\n");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(new BufferedReader(reader));
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, bufReader);

        assertEquals(0, lexer.getLineNumber());

        // Read a line
        bufReader.readLine();

        assertEquals(1, lexer.getLineNumber());

        // Read two more lines
        bufReader.readLine();
        bufReader.readLine();

        assertEquals(3, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        StringReader reader = new StringReader("One\\nTwo\\tThree\\rFour\\bFive\\fSix\\Escape");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(new BufferedReader(reader));
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, bufReader);

        int escape1 = lexer.readEscape();
        int escape2 = lexer.readEscape();
        int escape3 = lexer.readEscape();
        int escape4 = lexer.readEscape();
        int escape5 = lexer.readEscape();
        int escape6 = lexer.readEscape();
        int escape7 = lexer.readEscape(); // Invalid escape

        assertEquals('\n', escape1);
        assertEquals('\t', escape2);
        assertEquals('\r', escape3);
        assertEquals('\b', escape4);
        assertEquals('\f', escape5);
        assertEquals('S', escape6); // 'Six'
        assertEquals(Lexer.END_OF_STREAM, escape7);
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("Hello World   ");
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("Hello World", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\r\nLine 3\rLine 4");
        ExtendedBufferedReader bufReader = new ExtendedBufferedReader(new BufferedReader(reader));
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, bufReader);

        boolean endOfLine1 = lexer.readEndOfLine(bufReader.read());
        boolean endOfLine2 = lexer.readEndOfLine(bufReader.read());
        boolean endOfLine3 = lexer.readEndOfLine(bufReader.read());
        boolean endOfLine4 = lexer.readEndOfLine(bufReader.read());

        assertTrue(endOfLine1);
        assertTrue(endOfLine2);
        assertTrue(endOfLine3);
        assertFalse(endOfLine4); // 'Line 4' does not end with a line terminator
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\r'));
        assertTrue(lexer.isWhitespace('\f'));

        assertFalse(lexer.isWhitespace('A'));
        assertFalse(lexer.isWhitespace('9'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));

        assertFalse(lexer.isStartOfLine(' '));
        assertFalse(lexer.isStartOfLine('A'));
        assertFalse(lexer.isStartOfLine('9'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));

        assertFalse(lexer.isEndOfFile(' '));
        assertFalse(lexer.isEndOfFile('A'));
        assertFalse(lexer.isEndOfFile('9'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isDelimiter(','));

        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('A'));
        assertFalse(lexer.isDelimiter('9'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEscape('\\'));

        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('A'));
        assertFalse(lexer.isEscape('9'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isQuoteChar('"'));

        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('A'));
        assertFalse(lexer.isQuoteChar('9'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isCommentStart('#'));

        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('A'));
        assertFalse(lexer.isCommentStart('9'));
    }
}