
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_CharSequenceIntWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        
        StringWriter writer = new StringWriter();
        translator.translate("ABC", 0, writer);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_CharSequence() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        assertEquals(null, translator.translate(null));
        assertEquals("", translator.translate(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithNullWriter() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        translator.translate("ABC", null);
    }
    
    @Test
    public void testTranslate_WithNonNullWriter() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer writer = mock(Writer.class);
        translator.translate("ABC", writer);
        verify(translator).translate("ABC", writer);
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = mock(CharSequenceTranslator.class);
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);
        
        CharSequenceTranslator translator = translator1.with(translator2);
        
        assertEquals(AggregateTranslator.class, translator.getClass());
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

}
