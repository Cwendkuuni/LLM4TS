import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Constants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {
    @Test
    public void testLexerInstance() {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.getLineNumber()).thenReturn(1L);
        Lexer lexer = new Lexer(format, in);
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(mapNullToDisabled(format.getEscape()), lexer.escape);
        assertEquals(mapNullToDisabled(format.getQuoteChar()), lexer.quoteChar);
        assertEquals(mapNullToDisabled(format.getCommentStart()), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }

    // Add test cases for other methods with basic usage and edge cases

    @Test
    public void testReadEscape() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn('\\', 'r');
        Lexer lexer = new Lexer(null, in);
        assertEquals(CR, lexer.readEscape());
    }

    // Add test cases for other methods with edge cases and exception handling

    // ...

    private static Character mapNullToDisabled(Character c) {
        return c == null ? '\ufffe' : c;
    }
}