import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.JavaUnicodeUnescaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = mock(CharSequenceTranslator.class, CALLS_REAL_METHODS);
    }

    @Test
    public void testTranslate_withWriterAndIndex() throws IOException {
        // Arrange
        CharSequence input = "Hello";
        int index = 0;
        Writer out = new StringWriter();

        // Act
        int consumed = translator.translate(input, index, out);

        // Assert
        verify(translator, times(1)).translate(input, index, out);
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslate_withInput() {
        // Arrange
        CharSequence input = "Hello";

        // Act
        String translated = translator.translate(input);

        // Assert
        verify(translator, times(1)).translate(input);
        assertNull(translated);
    }

    @Test
    public void testTranslate_withInputAndWriter() throws IOException {
        // Arrange
        CharSequence input = "Hello";
        Writer out = new StringWriter();

        // Act
        translator.translate(input, out);

        // Assert
        verify(translator, times(1)).translate(input, out);
    }

    @Test
    public void testWith_translators() {
        // Arrange
        CharSequenceTranslator[] translators = {
                new LookupTranslator(new String[][]{{"one", "1"}, {"two", "2"}}),
                new JavaUnicodeEscaper(),
                new JavaUnicodeUnescaper()
        };

        // Act
        CharSequenceTranslator mergedTranslator = translator.with(translators);

        // Assert
        verify(translator, times(1)).with(translators);
        assertEquals(AggregateTranslator.class, mergedTranslator.getClass());
    }

    @Test
    public void testHex() {
        // Arrange
        int codepoint = 65;

        // Act
        String hexValue = CharSequenceTranslator.hex(codepoint);

        // Assert
        assertEquals("41", hexValue);
    }
}