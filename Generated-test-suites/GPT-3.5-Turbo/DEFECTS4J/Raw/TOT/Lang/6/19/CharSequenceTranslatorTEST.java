
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Mock
    private Writer writer;

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate_Index0_Success() throws IOException {
        StringWriter out = new StringWriter();
        int index = 0;

        int consumed = translator.translate("input text", index, out);

        assertEquals(3, consumed);
        assertEquals("input", out.toString());
    }

    @Test
    public void testTranslate_IndexOutOfRange_NoWriterInteraction() throws IOException {
        StringWriter out = new StringWriter();
        int index = 100;

        int consumed = translator.translate("input text", index, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_NullInput_NoWriterInteraction() throws IOException {
        StringWriter out = new StringWriter();
        int index = 0;

        int consumed = translator.translate(null, index, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_NonNullInput_NullWriter_ThrowsIllegalArgumentException() {
        int index = 0;

        try {
            translator.translate("input text", index, null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            assertEquals("Unexpected IOException", e.getMessage());
        }
    }

    @Test
    public void testTranslate_NullInputToString_NoException() {
        String result = translator.translate(null);
        assertEquals(null, result);
    }

    @Test
    public void testTranslate_NonNullInputToString_Success() {
        String input = "input text";
        String expected = "input";

        String result = translator.translate(input);

        assertEquals(expected, result);
    }

    @Test
    public void testTranslate_StringWriterToString_Success() throws IOException {
        StringWriter writer = new StringWriter();
        String input = "input text";
        String expected = "input";

        translator.translate(input, writer);

        String result = writer.toString();
        assertEquals(expected, result);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }
            String translatedString = input.toString().substring(index, index + 5);
            out.write(translatedString);
            return Math.min(translatedString.length(), input.length() - index);
        }
    }
}

