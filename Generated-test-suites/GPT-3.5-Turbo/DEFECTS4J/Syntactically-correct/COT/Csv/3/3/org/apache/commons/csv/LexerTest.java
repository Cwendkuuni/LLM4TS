import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testReadEndOfLine() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsWhitespace() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsStartOfLine() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsEndOfFile() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsDelimiter() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsEscape() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsQuoteChar() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

    @Test
    public void testIsCommentStart() {
        // Arrange
        Lexer lexer = new Lexer(format, in);
        
        // Act
        
        // Assert
        
    }

}