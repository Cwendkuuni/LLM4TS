import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0; // implementation for testing purposes
            }
        };
        
        // test null input
        assertNull(translator.translate(null));
        
        // test empty input
        assertEquals("", translator.translate(""));
        
        // test non-empty input
        assertEquals("test", translator.translate("test"));
    }
    
    @Test
    public void testTranslate_withWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        
        // test null input
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
        
        // test empty input
        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
        
        // test non-empty input
        writer = new StringWriter();
        translator.translate("test", writer);
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0; // implementation for testing purposes
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0; // implementation for testing purposes
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        // test that both translators are present in the merged translator
        assertEquals(translator1, mergedTranslator);
        assertEquals(translator2, ((AggregateTranslator) mergedTranslator).getTranslators()[1]);
    }
    
    @Test
    public void testHex() {
        // test conversion of codepoint to hexadecimal
        assertEquals("41", CharSequenceTranslator.hex(65));
        
        // test upper case conversion
        assertEquals("41", CharSequenceTranslator.hex(97));
    }
}