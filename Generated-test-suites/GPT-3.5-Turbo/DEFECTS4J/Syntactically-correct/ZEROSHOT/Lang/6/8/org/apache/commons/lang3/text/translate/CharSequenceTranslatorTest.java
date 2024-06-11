import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateWithNonEmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("abc123", translator.translate("abc123"));
    }

    @Test
    public void testTranslateWithSpecialCharacters() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("\\u0041\\u0053\\u0046\\u0020\\u0049\\u0073\\u0020\\u0047\\u0072\\u0065\\u0061\\u0074", 
                translator.translate("ASF Is Great"));
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        try {
            assertEquals("abc123", translator.translate("abc123", new StringWriter()));
        } catch (Exception e) {
            fail("An exception should not be thrown.");
        }
    }

    @Test
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        try {
            translator.translate("abc123", null);
            fail("An IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // this is expected
        } catch (Exception e) {
            fail("An IllegalArgumentException should be thrown.");
        }
    }

    class TestTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException();
            }
            out.write(input.toString());
            out.flush();
            return input.length();
        }
    }
}