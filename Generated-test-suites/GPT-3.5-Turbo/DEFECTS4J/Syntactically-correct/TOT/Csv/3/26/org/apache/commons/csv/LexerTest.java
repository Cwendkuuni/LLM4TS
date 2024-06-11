import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Arrange
        ExtendedBufferedReader reader = new ExtendedBufferedReader(); 
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        
        // Act
        long lineNumber = lexer.getLineNumber();
        
        // Assert
        Assert.assertEquals(0, lineNumber);
    }
    
    @Test
    public void testReadEscape_WithValidEscapeSequence() {
        // Arrange
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        reader.setNextChar('\\');
        reader.setNextChar('n');
        
        // Act
        int escapedChar = lexer.readEscape();
        
        // Assert
        Assert.assertEquals('\n', escapedChar);
    }
    
    @Test(expected = IOException.class)
    public void testReadEscape_WithInvalidEscapeSequence() throws IOException {
        // Arrange
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        reader.setNextChar('\\');
        reader.setNextChar('x'); // Invalid escape sequence
        
        // Act
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces_WithTrailingSpaces() {
        // Arrange
        StringBuilder buffer = new StringBuilder("text   "); // trailing spaces
        
        // Act
        lexer.trimTrailingSpaces(buffer);
        
        // Assert
        Assert.assertEquals("text", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine_WithNewLineLF() {
        // Arrange
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        reader.setNextChar('\n');
        
        // Act
        boolean isEndOfLine = lexer.readEndOfLine('\n');
        
        // Assert
        Assert.assertTrue(isEndOfLine);
    }
    
    @Test
    public void testIsWhitespace_WithWhitespaceCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isWhitespace = lexer.isWhitespace(' ');
        
        // Assert
        Assert.assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsStartOfLine_WithStartOfLineCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        
        // Assert
        Assert.assertTrue(isStartOfLine);
    }
    
    @Test
    public void testIsEndOfFile_WithEndOfFileCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        
        // Assert
        Assert.assertTrue(isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter_WithDelimiterCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isDelimiter = lexer.isDelimiter(',');
        
        // Assert
        Assert.assertTrue(isDelimiter);
    }
    
    @Test
    public void testIsEscape_WithEscapeCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isEscape = lexer.isEscape('\\');
        
        // Assert
        Assert.assertTrue(isEscape);
    }
    
    @Test
    public void testIsQuoteChar_WithQuoteCharCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isQuoteChar = lexer.isQuoteChar('"');
        
        // Assert
        Assert.assertTrue(isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart_WithCommentStartCharacter() {
        // Arrange
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, null);
        
        // Act
        boolean isCommentStart = lexer.isCommentStart('#');
        
        // Assert
        Assert.assertTrue(isCommentStart);
    }
}