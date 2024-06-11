import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LexerTest {

    @Mock
    private ExtendedBufferedReader mockReader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLineNumber() {
        // Arrange
        Mockito.when(mockReader.getLineNumber()).thenReturn(5L);
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        long lineNumber = lexer.getLineNumber();

        // Assert
        assertEquals(5L, lineNumber);
    }

    @Test
    public void testIsWhitespace_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isWhitespace(' ');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isWhitespace('A');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isStartOfLine('\r');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isStartOfLine('A');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isEndOfFile(-1);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isEndOfFile(0);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isDelimiter(',');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isDelimiter(';');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEscape_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isEscape('\\');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEscape_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isEscape('/');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isQuoteChar('\"');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isQuoteChar('\'');

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_Positive() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isCommentStart('#');

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_Negative() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, mockReader);

        // Act
        boolean result = lexer.isCommentStart('@');

        // Assert
        assertFalse(result);
    }
}