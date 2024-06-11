import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical use case
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        String result = translator.translate("abc");
        assertEquals("abc", result);

        // Edge case: input is null
        result = translator.translate(null);
        assertNull(result);

        // Edge case: input contains special characters
        result = translator.translate("123#@!&");
        assertEquals("123#@!&", result);

        // Error scenario: if IOException is thrown
        translator = new IOExceptionCharSequenceTranslator();
        try {
            result = translator.translate("abc");
            fail("IOException was expected but not thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testTranslateWithIndex() {
        // Typical use case
        CharSequenceTranslator translator = new SampleCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("abc", 0, writer);
        assertEquals(3, result);
        assertEquals("abc", writer.toString());

        // Edge case: index out of bounds
        writer = new StringWriter();
        result = translator.translate("abc", 5, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());

        // Error scenario: if IOException is thrown
        translator = new IOExceptionCharSequenceTranslator();
        writer = new StringWriter();
        try {
            result = translator.translate("abc", 0, writer);
            fail("IOException was expected but not thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testHex() {
        // Typical use case
        String hexString = CharSequenceTranslator.hex(65);
        assertEquals("41", hexString);

        // Edge case: codepoint is a special character
        hexString = CharSequenceTranslator.hex(8364); // Euro symbol
        assertEquals("20AC", hexString);
    }
}