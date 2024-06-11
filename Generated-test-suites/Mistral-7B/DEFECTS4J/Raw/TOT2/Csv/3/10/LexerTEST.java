
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

public class LexerTest {
    private Lexer lexer;
    private ExtendedBufferedReader mockReader;
    private CSVFormat format;

    @Before
    public void setup() {
        format = mock(CSVFormat.class);
        mockReader = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);

        when(format.getDelimiter()).thenReturn(';');
        when(format.getIgnoreSurroundingSpaces()).thenReturn(true);
        when(format.getIgnoreEmptyLines()).thenReturn(true);
        when(format.getEscape()).thenReturn('\\');
        when(format.getQuoteChar()).thenReturn('"');
        when(format.getCommentStart()).thenReturn('#');
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());

        when(mockReader.getLineNumber()).thenReturn(2);
        assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        when(mockReader.getCurrentCharacter()).thenReturn('\\');
        when(mockReader.getLastCharacter()).thenReturn('\n');
        assertEquals('\n', lexer.readEscape());

        when(mockReader.getCurrentCharacter()).thenReturn('\\');
        when(mockReader.getLastCharacter()).thenReturn('a');
        assertNull(lexer.readEscape());

        when(mockReader.getLastCharacter()).thenReturn('\u00ff');
        verify(mockReader, times(1)).read();
        assertEquals('\u00ff', lexer.readEscape());

        when(mockReader.getCurrentCharacter()).thenReturn('\\');
        when(mockReader.getLastCharacter()).thenReturn(LF);
        assertEquals(LF, lexer.readEscape());

        when(mockReader.getCurrentCharacter()).thenReturn('\\');
        when(mockReader.getLastCharacter()).thenReturn(CR);
        assertEquals(CR, lexer.readEscape());

        when(mockReader.getCurrentCharacter()).thenReturn('\u0000');
        when(mockReader.getLastCharacter()).thenReturn('\\');
        assertThrows(IOException.class, () -> lexer.readEscape());

        when(mockReader.getCurrentCharacter()).thenReturn('a');
        when(mockReader.getLastCharacter()).thenReturn('a');
        assertNull(lexer.readEscape());

        // Add the other test cases here...
    }

    // Add other test methods for the other abstract methods in the Lexer class here.
}
