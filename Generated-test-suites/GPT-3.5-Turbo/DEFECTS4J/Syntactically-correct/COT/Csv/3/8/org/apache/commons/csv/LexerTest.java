
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        // Test the getLineNumber() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }
    
    @Test
    public void testReadEscape() throws IOException {
        // Test the readEscape() method
        
        // Test case when next character is 'r'
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        in.setLastChar('r');
        int escape = lexer.readEscape();
        Assert.assertEquals('\r', escape);
        
        // Test case when next character is not a valid escape character
        in.setLastChar('x');
        try {
            lexer.readEscape();
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        // Test the trimTrailingSpaces() method
        StringBuilder buffer = new StringBuilder("test   ");
        
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        lexer.trimTrailingSpaces(buffer);
        
        Assert.assertEquals("test", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        // Test the readEndOfLine() method
        
        // Test case when current character is \n
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isEndOfLine = lexer.readEndOfLine('\n');
        Assert.assertTrue(isEndOfLine);
        
        // Test case when current character is \r and next character is \n
        in.setLookAhead('\n');
        isEndOfLine = lexer.readEndOfLine('\r');
        Assert.assertTrue(isEndOfLine);
        
        // Test case when current character is \r and next character is not \n
        in.setLookAhead('x');
        isEndOfLine = lexer.readEndOfLine('\r');
        Assert.assertFalse(isEndOfLine);
    }
    
    @Test
    public void testIsWhitespace() {
        // Test the isWhitespace() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isWhitespace = lexer.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
        
        isWhitespace = lexer.isWhitespace('\t');
        Assert.assertTrue(isWhitespace);
        
        isWhitespace = lexer.isWhitespace(',');
        Assert.assertFalse(isWhitespace);
    }
    
    @Test
    public void testIsStartOfLine() {
        // Test the isStartOfLine() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        Assert.assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine('\r');
        Assert.assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine(-1);
        Assert.assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine(' ');
        Assert.assertFalse(isStartOfLine);
    }
    
    @Test
    public void testIsEndOfFile() {
        // Test the isEndOfFile() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        Assert.assertTrue(isEndOfFile);
        
        isEndOfFile = lexer.isEndOfFile('\n');
        Assert.assertFalse(isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter() {
        // Test the isDelimiter() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isDelimiter = lexer.isDelimiter(',');
        Assert.assertTrue(isDelimiter);
        
        isDelimiter = lexer.isDelimiter('\t');
        Assert.assertFalse(isDelimiter);
    }
    
    @Test
    public void testIsEscape() {
        // Test the isEscape() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isEscape = lexer.isEscape('\\');
        Assert.assertTrue(isEscape);
        
        isEscape = lexer.isEscape('/');
        Assert.assertFalse(isEscape);
    }
    
    @Test
    public void testIsQuoteChar() {
        // Test the isQuoteChar() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isQuoteChar = lexer.isQuoteChar('"');
        Assert.assertTrue(isQuoteChar);
        
        isQuoteChar = lexer.isQuoteChar('\'');
        Assert.assertFalse(isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart() {
        // Test the isCommentStart() method
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(format, in);
        
        boolean isCommentStart = lexer.isCommentStart('#');
        Assert.assertTrue(isCommentStart);
        
        isCommentStart = lexer.isCommentStart('/');
        Assert.assertFalse(isCommentStart);
    }
}

