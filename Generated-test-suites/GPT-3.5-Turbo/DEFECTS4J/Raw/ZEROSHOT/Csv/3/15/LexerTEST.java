import static org.junit.Assert.*;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testLexerInitialization() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertEquals(format, lexer.format);
        assertEquals(in, lexer.in);
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(format.getEscape(), lexer.escape);
        assertEquals(format.getQuoteChar(), lexer.quoteChar);
        assertEquals(format.getCommentStart(), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }
    
    @Test
    public void testMapNullToDisabled() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertNull(lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }
    
    @Test
    public void testGetLineNumber() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertEquals(in.getLineNumber(), lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        // TODO: Add test cases for readEscape() method
        
        //assertEquals(expectedResult, lexer.readEscape());
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        StringBuilder buffer = new StringBuilder("hello    ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello", buffer.toString());
        
        StringBuilder buffer2 = new StringBuilder("    hello    ");
        lexer.trimTrailingSpaces(buffer2);
        assertEquals("    hello", buffer2.toString());
        
        StringBuilder buffer3 = new StringBuilder("hello");
        lexer.trimTrailingSpaces(buffer3);
        assertEquals("hello", buffer3.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        // TODO: Add test cases for readEndOfLine() method
        
        //assertEquals(expectedResult, lexer.readEndOfLine(c));
    }
    
    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\r'));
        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('a'));
    }
    
    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertFalse(lexer.isStartOfLine(','));
        assertFalse(lexer.isStartOfLine('a'));
    }
    
    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(','));
        assertFalse(lexer.isEndOfFile('a'));
    }
    
    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
        assertFalse(lexer.isDelimiter('a'));
    }
    
    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('/'));
        assertFalse(lexer.isEscape('a'));
    }
    
    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isQuoteChar('\"'));
        assertFalse(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar('a'));
    }
    
    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('!'));
        assertFalse(lexer.isCommentStart('a'));
    }
    
    @Test
    public void testNextToken() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        // TODO: Add test cases for nextToken() method
        
        //assertEquals(expectedResult, lexer.nextToken(reusableToken));
    }

}