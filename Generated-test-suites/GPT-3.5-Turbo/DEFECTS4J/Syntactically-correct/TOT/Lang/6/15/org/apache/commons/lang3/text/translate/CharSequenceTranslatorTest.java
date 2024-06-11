import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
    
    private static class DummyTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // dummy implementation for testing purposes
            return 0;
        }
        
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        DummyTranslator translator = new DummyTranslator();
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        DummyTranslator translator = new DummyTranslator();
        assertEquals("", translator.translate(""));
    }
    
    @Test
    public void testTranslate_withInput_shouldReturnCorrectOutput() {
        DummyTranslator translator = new DummyTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // always translate 'a' to 'b'
                out.write('b');
                return 1;
            }
        };
        assertEquals("bbc", translator.translate("abc"));
        assertEquals("bbbbc", translator.translate("abac"));
        assertEquals("bbb", translator.translate("aaa"));
    }
    
    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        DummyTranslator translator = new DummyTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        try {
            translator.translate("abc");
            fail("RuntimeException should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }
    
    @Test
    public void testHex_withValidCodepoint_shouldReturnCorrectString() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
        assertEquals("7FFF", CharSequenceTranslator.hex(32767));
        assertEquals("CAFE", CharSequenceTranslator.hex(51966));
    }
    
    @Test
    public void testHex_withNegativeCodepoint_shouldReturnCorrectString() {
        assertEquals("FFFF8001", CharSequenceTranslator.hex(-32767));
        assertEquals("FFFFFFFF", CharSequenceTranslator.hex(-1));
    }
    
    @Test
    public void testHex_withZeroCodepoint_shouldReturnCorrectString() {
        assertEquals("0", CharSequenceTranslator.hex(0));
    }
    
}