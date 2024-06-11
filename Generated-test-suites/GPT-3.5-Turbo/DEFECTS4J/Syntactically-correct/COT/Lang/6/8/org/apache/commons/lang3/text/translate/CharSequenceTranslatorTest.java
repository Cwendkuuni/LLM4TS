import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    /**
     * Test for the translate() method.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy implementation for testing
                out.write(input.charAt(index));
                return 1;
            }
        };

        // Test with null input
        assertEquals(null, translator.translate(null));

        // Test with empty input
        assertEquals("", translator.translate(""));

        // Test with single character input
        assertEquals("A", translator.translate("A"));

        // Test with multiple character input
        assertEquals("ABC", translator.translate("ABC"));
    }

    /**
     * Test for the translate() method with Writer parameter.
     */
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Dummy implementation for testing
                out.write(input.charAt(index));
                return 1;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }

    /**
     * Test for the with() method.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals(mergedTranslator.getClass(), AggregateTranslator.class);

        // Test that the mergedTranslator contains both translators
        AggregateTranslator aggregateTranslator = (AggregateTranslator) mergedTranslator;
        assertEquals(translator1, aggregateTranslator.getTranslators()[0]);
        assertEquals(translator2, aggregateTranslator.getTranslators()[1]);
    }

    /**
     * Test for the hex() method.
     */
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("2A", CharSequenceTranslator.hex(42));
        assertEquals("FE", CharSequenceTranslator.hex(254));
    }
}