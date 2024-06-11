import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // custom translate implementation
                return 0;
            }
        };
        
        // test input with null
        assertNull(translator.translate(null));
        
        // test input with empty string
        assertEquals("", translator.translate(""));
        
        // test input with non-empty string
        assertEquals("abc", translator.translate("abc"));
    }
    
    @Test
    public void testWith() throws Exception {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals(2, ((AggregateTranslator) mergedTranslator).getTranslators().length);
    }
    
    @Test
    public void testHex() throws Exception {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
    }
}