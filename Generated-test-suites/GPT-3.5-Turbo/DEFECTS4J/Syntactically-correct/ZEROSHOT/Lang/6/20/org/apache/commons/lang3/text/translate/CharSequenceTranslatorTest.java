import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals("Hello, World!", translator.translate("Hello, World!"));
    }
    
    @Test
    public void testTranslate_nullInput() {
        CharSequenceTranslator translator = new MockTranslator();
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslate_emptyInput() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals("", translator.translate(""));
    }
    
    @Test
    public void testTranslate_writer() throws Exception {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello, World!", writer);
        assertEquals("Hello, World!", writer.toString());
    }
    
    @Test
    public void testTranslate_writer_nullInput() throws Exception {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_writer_emptyInput() throws Exception {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_with() {
        CharSequenceTranslator translator1 = new MockTranslator();
        CharSequenceTranslator translator2 = new MockTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals("Hello, World!", mergedTranslator.translate("Hello, World!"));
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("4A", CharSequenceTranslator.hex(74));
        assertEquals("61", CharSequenceTranslator.hex(97));
    }
    
    private class MockTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) {
            try {
                out.write(input.charAt(index));
                return 1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}