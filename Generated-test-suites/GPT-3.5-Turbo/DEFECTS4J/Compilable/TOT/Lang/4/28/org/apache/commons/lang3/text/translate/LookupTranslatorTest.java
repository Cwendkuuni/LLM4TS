package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "bonjour"}, {"world", "monde"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("hello world", 0, writer);
        assertEquals("bonjour monde", writer.toString());
        assertEquals(11, result);
    }
    
    @Test
    public void testTranslate_emptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "bonjour"}, {"world", "monde"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_longestInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "bonjour"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("hello", 0, writer);
        assertEquals("bonjour", writer.toString());
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_noMatch() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "bonjour"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("world", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
}