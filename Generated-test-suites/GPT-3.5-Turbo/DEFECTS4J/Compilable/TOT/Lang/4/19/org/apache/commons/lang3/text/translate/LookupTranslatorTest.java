package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_WithValidTranslation_ShouldReturnTranslatedOutput() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "abc", "123" },
            new CharSequence[] { "def", "456" }
        );
        StringWriter out = new StringWriter();
        int index = 0;
        int translatedLength = translator.translate("abc def", index, out);
        assertEquals("123", out.toString());
        assertEquals(3, translatedLength);
    }
    
    @Test
    public void testTranslate_WithNoTranslationAvailable_ShouldReturnZero() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "abc", "123" },
            new CharSequence[] { "def", "456" }
        );
        StringWriter out = new StringWriter();
        int index = 0;
        int translatedLength = translator.translate("xyz", index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }
    
    @Test(expected = NullPointerException.class)
    public void testTranslate_WithNullInput_ShouldThrowNullPointerException() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "abc", "123" },
            new CharSequence[] { "def", "456" }
        );
        StringWriter out = new StringWriter();
        int index = 0;
        translator.translate(null, index, out);
    }
    
    @Test
    public void testTranslate_WithIndexOutOfRange_ShouldReturnZero() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "abc", "123" },
            new CharSequence[] { "def", "456" }
        );
        StringWriter out = new StringWriter();
        int index = 5; // Out of range
        int translatedLength = translator.translate("abc def", index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }
    
    @Test
    public void testTranslate_WithEmptyInput_ShouldReturnZero() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] { "abc", "123" },
            new CharSequence[] { "def", "456" }
        );
        StringWriter out = new StringWriter();
        int index = 0;
        int translatedLength = translator.translate("", index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

}