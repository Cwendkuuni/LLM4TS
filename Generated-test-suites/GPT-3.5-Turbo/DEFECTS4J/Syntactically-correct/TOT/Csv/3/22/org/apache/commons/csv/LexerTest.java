import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        Mockito.when(mockedReader.getLineNumber()).thenReturn(10L);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        Assert.assertEquals(10L, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_ValidEscapeSequence() throws IOException {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        Mockito.when(mockedReader.read()).thenReturn('n');
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        int result = lexer.readEscape();
        Assert.assertEquals('\n', result);
    }

    @Test(expected = IOException.class)
    public  void testReadEscape_InvalidEscapeSequence() throws IOException{
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        Mockito.when(mockedReader.read()).thenReturn('x');
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_NoTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test");
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testIsWhitespace_WhitespaceCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isWhitespace(' ');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespace_NonWhitespaceCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isWhitespace('a');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_StartOfLineCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isStartOfLine('\r');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_NonStartOfLineCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isStartOfLine('a');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_EndOfFileCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isEndOfFile(-1);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_NonEndOfFileCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isEndOfFile('a');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsDelimiter_DelimiterCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char delimiter = ',';
        Mockito.when(csvFormat.getDelimiter()).thenReturn(delimiter);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isDelimiter(delimiter);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDelimiter_NonDelimiterCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char delimiter = ',';
        Mockito.when(csvFormat.getDelimiter()).thenReturn(delimiter);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isDelimiter(';');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEscape_EscapeCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char escape = '\\';
        Mockito.when(csvFormat.getEscape()).thenReturn(escape);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isEscape(escape);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEscape_NonEscapeCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char escape = '\\';
        Mockito.when(csvFormat.getEscape()).thenReturn(escape);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isEscape('/');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_QuoteCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char quoteChar = '"';
        Mockito.when(csvFormat.getQuoteChar()).thenReturn(quoteChar);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isQuoteChar(quoteChar);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_NonQuoteCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char quoteChar = '"';
        Mockito.when(csvFormat.getQuoteChar()).thenReturn(quoteChar);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isQuoteChar('\'');
        Assert.assertFalse(result);
    }

    @Test
    public void testIsCommentStart_CommentStartCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char commentStart = '#';
        Mockito.when(csvFormat.getCommentStart()).thenReturn(commentStart);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isCommentStart(commentStart);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCommentStart_NonCommentStartCharacter() {
        ExtendedBufferedReader mockedReader = Mockito.mock(ExtendedBufferedReader.class);
        CSVFormat csvFormat = Mockito.mock(CSVFormat.class);
        char commentStart = '#';
        Mockito.when(csvFormat.getCommentStart()).thenReturn(commentStart);
        Lexer lexer = new Lexer(csvFormat, mockedReader);
        boolean result = lexer.isCommentStart('/');
        Assert.assertFalse(result);
    }
}