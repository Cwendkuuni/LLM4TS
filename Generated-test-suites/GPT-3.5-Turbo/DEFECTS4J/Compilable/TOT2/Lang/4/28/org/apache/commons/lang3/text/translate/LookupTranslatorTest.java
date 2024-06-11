package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"hello", "bonjour"});
        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("hello world", 0, writer);
        assertEquals("bonjour", writer.toString());
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_edgeCase() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("abcd", 0, writer);
        assertEquals("123", writer.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_noMatch() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("hello world", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_emptyInput() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"empty", "result"});
        StringWriter writer = new StringWriter();
        int result = lookupTranslator.translate("", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test(expected = IOException.class)
    public void testTranslate_ioException() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"test", "exception"});
        StringWriter writer = new StringWriter();
        lookupTranslator.translate("test", 0, null); // Should throw IOException
    }
}