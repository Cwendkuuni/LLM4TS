
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setUp() {
        // setUp lookup table for translation
        translator = new LookupTranslator(
                new CharSequence[][] {
                        {"a", "1"},
                        {"b", "2"},
                        {"c", "3"}
                });
    }

    @Test
    public void testTranslate_withValidInput() throws IOException {
        final String input = "abc";
        final StringWriter writer = new StringWriter();

        assertEquals(3, translator.translate(input, 0, writer));
        assertEquals("123", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidInput() throws IOException {
        final String input = "def";
        final StringWriter writer = new StringWriter();

        assertEquals(0, translator.translate(input, 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withPartialMatch() throws IOException {
        final String input = "abdef";
        final StringWriter writer = new StringWriter();

        assertEquals(2, translator.translate(input, 0, writer));
        assertEquals("12def", writer.toString());
    }

    @Test
    public void testTranslate_withCaseSensitiveMatch() throws IOException {
        final String input = "ABC";
        final StringWriter writer = new StringWriter();

        assertEquals(0, translator.translate(input, 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput() throws IOException {
        final String input = "";
        final StringWriter writer = new StringWriter();

        assertEquals(0, translator.translate(input, 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNullInput() throws IOException {
        final String input = null;
        final StringWriter writer = new StringWriter();

        assertEquals(0, translator.translate(input, 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withOutOfBoundsIndex() throws IOException {
        final String input = "abc";
        final StringWriter writer = new StringWriter();

        assertEquals(0, translator.translate(input, 10, writer));
        assertEquals("", writer.toString());
    }
}

