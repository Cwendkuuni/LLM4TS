import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        
        int result = translator.translate("test", 0, writer);
        
        assertEquals(4, result);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslate_StringInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        
        String result = translator.translate("test");
        
        assertEquals("test", result);
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        
        String result = translator.translate(null);
        
        assertNull(result);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        
        CharSequenceTranslator result = translator.with(new MockCharSequenceTranslator());
        
        assertNotNull(result);
    }

    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(65);
        
        assertEquals("41", result);
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.toString().substring(index));
            return input.length() - index;
        }
    }
}