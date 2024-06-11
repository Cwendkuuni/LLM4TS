package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "a", "1" }, 
            { "b", "2" }, 
            { "c", "3" } });

        StringWriter writer = new StringWriter();

        // Test lookup translation of single character
        assertEquals(0, translator.translate("x", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);

        // Test lookup translation of single character
        assertEquals(1, translator.translate("a", 0, writer));
        assertEquals("1", writer.toString());
        writer.getBuffer().setLength(0);

        // Test lookup translation of multiple characters
        assertEquals(3, translator.translate("abc", 0, writer));
        assertEquals("123", writer.toString());
        writer.getBuffer().setLength(0);

        // Test lookup translation with index beyond input length
        assertEquals(1, translator.translate("abc", 1, writer));
        assertEquals("2", writer.toString());
        writer.getBuffer().setLength(0);
    }
}
