// Test file starts here

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() throws IOException {
        // Test case for the functionality of the translate method
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = "Hello World!";
        String expectedOutput = "48656C6C6F20576F726C6421";

        when(translator.translate(input, 0, out)).thenReturn(expectedOutput.length());

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);
        String actualOutput = aggregateTranslator.translate(input);

        assertEquals(expectedOutput, actualOutput);

        verify(translator, times(1)).translate(input, 0, out);
    }

    @Test
    public void testTranslateEmptyCharSequence() throws IOException {
        // Test case for empty CharSequence input
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = "";

        when(translator.translate(input, 0, out)).thenReturn(0);

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);
        String actualOutput = aggregateTranslator.translate(input);

        assertEquals("", actualOutput);

        verify(translator, never()).translate(input, 0, out);
    }

    @Test
    public void testTranslateSpecialCharacters() throws IOException {
        // Test case for CharSequence input containing special characters
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = "Hello 世界!";
        String expectedOutput = "48656C6C6F20E69786921";

        when(translator.translate(input, 0, out)).thenReturn(expectedOutput.length());

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);
        String actualOutput = aggregateTranslator.translate(input);

        assertEquals(expectedOutput, actualOutput);

        verify(translator, times(1)).translate(input, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullCharSequence() throws IOException {
        // Test case for CharSequence input that is null
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = null;

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);
        aggregateTranslator.translate(input);

        fail("Expected an IllegalArgumentException when passing a null CharSequence input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        // Test case for Writer output that is null
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        CharSequence input = "Hello World!";

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);
        aggregateTranslator.translate(input, null);

        fail("Expected an IllegalArgumentException when passing a null Writer output.");
    }

    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        // Test case for various error conditions during the translation process
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = "Hello World!";

        when(translator.translate(input, 0, out)).thenThrow(new IOException());

        AggregateTranslator aggregateTranslator = new AggregateTranslator(translator);

        aggregateTranslator.translate(input);

        fail("Expected an IOException when an error occurs during the translation process.");
    }
}