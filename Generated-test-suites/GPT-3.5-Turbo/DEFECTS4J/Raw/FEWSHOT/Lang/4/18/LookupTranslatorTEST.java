
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] lookup1 = { "a", "A" };
        CharSequence[] lookup2 = { "b", "B" };
        LookupTranslator translator = new LookupTranslator(lookup1, lookup2);
        
        StringWriter writer = new StringWriter();
        assertEquals(1, translator.translate("a", 0, writer));
        assertEquals("A", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(1, translator.translate("b", 0, writer));
        assertEquals("B", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(0, translator.translate("c", 0, writer));
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(2, translator.translate("ab", 0, writer));
        assertEquals("AB", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(1, translator.translate("ba", 0, writer));
        assertEquals("B", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(2, translator.translate("abc", 0, writer));
        assertEquals("ABc", writer.toString());
    }
}
