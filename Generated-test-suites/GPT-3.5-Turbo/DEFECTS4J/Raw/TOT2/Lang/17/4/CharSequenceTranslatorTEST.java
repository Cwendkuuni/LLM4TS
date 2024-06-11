import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical use case
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "abc";
        String expected = "xyz";
        assertEquals(expected, translator.translate(input));
        
        // Edge cases
        input = null;
        expected = null;
        assertEquals(expected, translator.translate(input));

        input = "";
        expected = "";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithWriter() {
        // Typical use case
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "test";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            String result = writer.toString();
            assertEquals("Translated text", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Edge cases
        input = null;
        StringWriter writer2 = new StringWriter();
        try {
            translator.translate(input, writer2);
            String result2 = writer2.toString();
            assertEquals("", result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHex() {
        // Typical use case
        assertEquals("41", CharSequenceTranslator.hex(65));
        
        // Edge cases
        assertEquals("0", CharSequenceTranslator.hex(0));
    }
}