import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello World!";
        String expected = "H3ll0 W0rld!";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = null;
        assertNull(translator.translate(input));
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello World!";
        String expected = "H3ll0 W0rld!";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            assertEquals(expected, writer.toString());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello World!";
        Translator.translate(input, null);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MyTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "Hello World!";
        String expected = "H3ll0 W0rld!";
        String result = mergedTranslator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("20AC", CharSequenceTranslator.hex(8364));
    }
}