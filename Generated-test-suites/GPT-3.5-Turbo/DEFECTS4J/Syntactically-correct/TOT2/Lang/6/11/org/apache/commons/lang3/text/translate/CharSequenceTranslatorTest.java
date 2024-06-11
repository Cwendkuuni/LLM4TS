import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new MockTranslator();

        StringWriter writer = new StringWriter();
        int result = translator.translate("input text", 0, writer);

        assertEquals(10, result);
        assertEquals("output text", writer.toString());
    }

    @Test
    public void testTranslateWithoutWriter() {
        CharSequenceTranslator translator = new MockTranslator();

        String result = translator.translate("input text");

        assertEquals("output text", result);
    }

    @Test
    public void testTranslateChars() throws Exception {
        CharSequenceTranslator translator = new MockTranslator();

        StringWriter writer = new StringWriter();
        translator.translate("input text", writer);

        assertEquals("output text", writer.toString());
    }

    @Test
    public void testWithTranslators() {
        CharSequenceTranslator translator = new MockTranslator();
        CharSequenceTranslator translator2 = new MockTranslator();

        CharSequenceTranslator result = translator.with(translator2);

        assertNotNull(result);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

    private static class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("output text");
            return 10;
        }
    }
}