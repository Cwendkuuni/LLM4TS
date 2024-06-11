import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testNextToken() throws Exception {
        // Arrange
        String input = "1,2,3\n4,5,6\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);
        Token reusableToken = new Token();

        // Act
        Token token1 = lexer.nextToken(reusableToken);
        Token token2 = lexer.nextToken(reusableToken);
        Token token3 = lexer.nextToken(reusableToken);
        Token token4 = lexer.nextToken(reusableToken);
        Token token5 = lexer.nextToken(reusableToken);
        Token token6 = lexer.nextToken(reusableToken);
        Token token7 = lexer.nextToken(reusableToken);

        // Assert
        assertEquals(Token.Type.TOKEN, token1.getType());
        assertEquals("1", token1.getContent());
        assertEquals(Token.Type.TOKEN, token2.getType());
        assertEquals("2", token2.getContent());
        assertEquals(Token.Type.TOKEN, token3.getType());
        assertEquals("3", token3.getContent());
        assertEquals(Token.Type.NEWLINE, token4.getType());
        assertEquals(null, token4.getContent());
        assertEquals(Token.Type.TOKEN, token5.getType());
        assertEquals("4", token5.getContent());
        assertEquals(Token.Type.TOKEN, token6.getType());
        assertEquals("5", token6.getContent());
        assertEquals(Token.Type.TOKEN, token7.getType());
        assertEquals("6", token7.getContent());
    }

    @Test
    public void testGetLineNumber() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("1,2,3\n4,5,6"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        long lineNumber1 = lexer.getLineNumber();
        lexer.nextToken(new Token());
        long lineNumber2 = lexer.getLineNumber();
        lexer.nextToken(new Token());
        long lineNumber3 = lexer.getLineNumber();

        // Assert
        assertEquals(0, lineNumber1);
        assertEquals(1, lineNumber2);
        assertEquals(1, lineNumber3);
    }

    @Test
    public void testReadEscape_R() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("r"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        int c = lexer.readEscape();

        // Assert
        assertEquals(13, c);
    }

    @Test
    public void testReadEscape_N() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("n"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        int c = lexer.readEscape();

        // Assert
        assertEquals(10, c);
    }

    @Test
    public void testReadEscape_T() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("t"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        int c = lexer.readEscape();

        // Assert
        assertEquals(9, c);
    }

    @Test
    public void testReadEscape_B() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("b"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        int c = lexer.readEscape();

        // Assert
        assertEquals(8, c);
    }

    @Test
    public void testReadEscape_F() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("f"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        int c = lexer.readEscape();

        // Assert
        assertEquals(12, c);
    }

    @Test
    public void testReadEscape_InvalidChar() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("x"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Assert
        try {
            // Act
            lexer.readEscape();
        } catch (Exception e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() throws Exception {
        // Arrange
        StringBuilder buffer = new StringBuilder("abc   ");

        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Act
        lexer.trimTrailingSpaces(buffer);

        // Assert
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LF() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("hello\nworld"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        boolean result = lexer.readEndOfLine('h');

        // Assert
        assertEquals(false, result);
        assertEquals(0, in.getLastChar());
    }

    @Test
    public void testReadEndOfLine_CR() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("hello\rworld"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        boolean result = lexer.readEndOfLine('h');

        // Assert
        assertEquals(false, result);
        assertEquals(0, in.getLastChar());
    }

    @Test
    public void testReadEndOfLine_CRLF() throws Exception {
        // Arrange
        BufferedReader reader = new BufferedReader(new StringReader("hello\r\nworld"));
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        CSVFormat format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');

        Lexer lexer = new Lexer(format, in);

        // Act
        boolean result = lexer.readEndOfLine('h');

        // Assert
        assertEquals(false, result);
        assertEquals(0, in.getLastChar());
    }

    @Test
    public void testIsWhitespace() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isWhitespace(' '));
        assertEquals(true, lexer.isWhitespace('\t'));
        assertEquals(true, lexer.isWhitespace('\n'));
        assertEquals(true, lexer.isWhitespace('\r'));

        assertEquals(false, lexer.isWhitespace('a'));
        assertEquals(false, lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isStartOfLine('\n'));
        assertEquals(true, lexer.isStartOfLine('\r'));

        assertEquals(false, lexer.isStartOfLine('a'));
        assertEquals(false, lexer.isStartOfLine(','));

    }

    @Test
    public void testIsEndOfFile() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isEndOfFile(-1));
        assertEquals(false, lexer.isEndOfFile(0));
        assertEquals(false, lexer.isEndOfFile(1));
    }

    @Test
    public void testIsDelimiter() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isDelimiter(','));

        assertEquals(false, lexer.isDelimiter('a'));
        assertEquals(false, lexer.isDelimiter('\n'));
        assertEquals(false, lexer.isDelimiter('\r'));
    }

    @Test
    public void testIsEscape() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isEscape('\\'));

        assertEquals(false, lexer.isEscape('a'));
        assertEquals(false, lexer.isDelimiter('\n'));
        assertEquals(false, lexer.isDelimiter('\r'));
    }

    @Test
    public void testIsQuoteChar() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isQuoteChar('"'));

        assertEquals(false, lexer.isQuoteChar('a'));
        assertEquals(false, lexer.isDelimiter('\n'));
        assertEquals(false, lexer.isDelimiter('\r'));
    }

    @Test
    public void testIsCommentStart() throws Exception {
        // Arrange
        Lexer lexer = new Lexer(mock(CSVFormat.class), null);

        // Assert
        assertEquals(true, lexer.isCommentStart('#'));

        assertEquals(false, lexer.isCommentStart('a'));
        assertEquals(false, lexer.isDelimiter('\n'));
        assertEquals(false, lexer.isDelimiter('\r'));
    }
}