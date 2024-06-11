import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    CharSequenceTranslator translator;

    @Before
    public void setUp() {
        // Initialize translator
        translator = mock(CharSequenceTranslator.class);
    }

    @Test
    public void testTranslate() {
        // Test with non-null input
        String input = "test";
        String expectedResult = "translated";

        when(translator.translate(input)).thenReturn(expectedResult);

        assertEquals(expectedResult, translator.translate(input));
        verify(translator, times(1)).translate(input);

        // Test with null input
        input = null;

        assertNull(translator.translate(input));
        verify(translator, times(1)).translate(input);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        // Test with non-null input and non-null writer
        String input = "test";
        String expectedResult = "translated";

        Writer writer = mock(Writer.class);

        when(translator.translate(input, writer)).thenReturn(5);

        translator.translate(input, writer);
        verify(translator, times(1)).translate(input, writer);
        verify(writer, times(1)).write(expectedResult);

        // Test with non-null input and null writer
        input = "test";
        writer = null;

        translator.translate(input, writer);
        verify(translator, times(1)).translate(input, writer);

        // Test with null input and non-null writer
        input = null;
        writer = mock(Writer.class);

        translator.translate(input, writer);
        verify(translator, times(1)).translate(input, writer);

        // Test with null input and null writer
        input = null;
        writer = null;

        translator.translate(input, writer);
        verify(translator, times(1)).translate(input, writer);
    }

    @Test
    public void testWith() {
        // Test with empty array of translators
        CharSequenceTranslator[] emptyTranslators = new CharSequenceTranslator[0];

        assertEquals(translator, translator.with(emptyTranslators));

        // Test with non-empty array of translators
        CharSequenceTranslator[] translators = new CharSequenceTranslator[2];
        translators[0] = mock(CharSequenceTranslator.class);
        translators[1] = mock(CharSequenceTranslator.class);

        CharSequenceTranslator result = translator.with(translators);

        assertEquals(translator, result);
    }

    @Test
    public void testHex() {
        // Test with positive codepoint
        int codepoint = 65;
        String expectedResult = "41";

        assertEquals(expectedResult, CharSequenceTranslator.hex(codepoint));

        // Test with zero codepoint
        codepoint = 0;
        expectedResult = "0";

        assertEquals(expectedResult, CharSequenceTranslator.hex(codepoint));

        // Test with negative codepoint
        codepoint = -1;
        expectedResult = "FFFFFFFF";

        assertEquals(expectedResult, CharSequenceTranslator.hex(codepoint));
    }
}