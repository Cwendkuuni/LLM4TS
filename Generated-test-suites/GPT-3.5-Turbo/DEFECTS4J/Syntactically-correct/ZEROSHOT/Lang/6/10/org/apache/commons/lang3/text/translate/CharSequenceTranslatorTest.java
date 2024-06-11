import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Custom translation logic here
                return 0;
            }
        };
        
        StringWriter writer = new StringWriter();
        int index = 0;
        int result = translator.translate("input", index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInputAndWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        StringWriter writer = new StringWriter();
        int index = 0;
        translator.translate(null, index, writer);
        
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate(null);
        
        assertNull(result);
    }
    
    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate("");
        
        assertEquals("", result);
    }
    
    @Test
    public void testTranslateWithNonEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        String result = translator.translate("input");
        
        assertEquals("input", result);
    }
    
    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        int index = 0;
        translator.translate("input", index, null);
        
        // No assertions, just testing if it throws IOException
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriterAndNullInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        int index = 0;
        translator.translate(null, index, null);
    }
    
    @Test
    public void testTranslateWithCustomTranslator() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 1;
            }
        };
        
        CharSequenceTranslator translator = translator1.with(translator2);
        
        assertEquals(translator1, translator.without(translator2));
        assertEquals(2, translator instanceof AggregateTranslator);
    }
    
    @Test
    public void testHex() {
        // Test with lowercase and uppercase letters
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
        assertEquals("61", CharSequenceTranslator.hex(97));
        assertEquals("62", CharSequenceTranslator.hex(98));
        assertEquals("63", CharSequenceTranslator.hex(99));
    }

}