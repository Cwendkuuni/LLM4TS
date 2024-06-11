import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("Hello, world!", 0, writer);
        
        assertEquals(0, consumed);
        assertEquals("Hello, world!", writer.toString());
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        
        String result = translator.translate(null);
        
        assertEquals(null, result);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        CharSequenceTranslator translator2 = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertEquals(mergedTranslator.getClass(), AggregateTranslator.class);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("2A", CharSequenceTranslator.hex(42));
    }

    @Test
    public void testTranslate_ExceptionHandling() {
        CharSequenceTranslator translator = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()) {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        
        StringWriter writer = new StringWriter();
        
        try {
            translator.translate("Hello, world!", 0, writer);
        } catch (IOException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

}