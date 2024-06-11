import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        Assert.assertEquals(0, lexer.getLineNumber());
        
        // Test when lineNumber is greater than 0
        in.setLineNumber(5);
        Assert.assertEquals(5, lexer.getLineNumber());
    }

    @Test
    public void testMapNullToDisabled() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        char expected = Lexer.DISABLED;
        
        Character c1 = null;
        Assert.assertEquals(expected, lexer.mapNullToDisabled(c1));
        
        Character c2 = 'a';
        Assert.assertEquals('a', lexer.mapNullToDisabled(c2));
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        // TODO: Add test cases for readEscape() method
    }

    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        StringBuilder buffer = new StringBuilder("test  ");
        lexer.trimTrailingSpaces(buffer);
        
        Assert.assertEquals("test", buffer.toString());
        
        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);
        
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = Lexer.LF;
        boolean result1 = lexer.readEndOfLine(c1);
        Assert.assertTrue(result1);
        
        int c2 = Lexer.CR;
        in.setLookAhead(Lexer.LF);
        boolean result2 = lexer.readEndOfLine(c2);
        Assert.assertTrue(result2);
        
        int c3 = 'a';
        boolean result3 = lexer.readEndOfLine(c3);
        Assert.assertFalse(result3);
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = Lexer.TAB;
        Assert.assertTrue(lexer.isWhitespace(c1));
        
        int c2 = Lexer.LF;
        Assert.assertFalse(lexer.isWhitespace(c2));
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = Lexer.LF;
        Assert.assertTrue(lexer.isStartOfLine(c1));
        
        int c2 = Lexer.CR;
        Assert.assertTrue(lexer.isStartOfLine(c2));
        
        int c3 = 'a';
        Assert.assertFalse(lexer.isStartOfLine(c3));
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = Lexer.END_OF_STREAM;
        Assert.assertTrue(lexer.isEndOfFile(c1));
        
        int c2 = 'a';
        Assert.assertFalse(lexer.isEndOfFile(c2));
    }

    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = ',';
        Assert.assertTrue(lexer.isDelimiter(c1));
        
        int c2 = 'a';
        Assert.assertFalse(lexer.isDelimiter(c2));
    }

    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = '\\';
        Assert.assertTrue(lexer.isEscape(c1));
        
        int c2 = 'a';
        Assert.assertFalse(lexer.isEscape(c2));
    }

    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = '\"';
        Assert.assertTrue(lexer.isQuoteChar(c1));
        
        int c2 = 'a';
        Assert.assertFalse(lexer.isQuoteChar(c2));
    }

    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        int c1 = '#';
        Assert.assertTrue(lexer.isCommentStart(c1));
        
        int c2 = 'a';
        Assert.assertFalse(lexer.isCommentStart(c2));
    }

    @Test
    public void testNextToken() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        // TODO: Add test cases for nextToken() method
    }
}