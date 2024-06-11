package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[][] {
                {"a", "1"},
                {"b", "2"},
                {"c", "3"}
            }
        );
        
        Writer out = new StringWriter();
        assertEquals(1, translator.translate("a", 0, out));
        assertEquals("1", out.toString());
        out.getBuffer().setLength(0);
        
        assertEquals(1, translator.translate("b", 0, out));
        assertEquals("2", out.toString());
        out.getBuffer().setLength(0);
        
        assertEquals(1, translator.translate("c", 0, out));
        assertEquals("3", out.toString());
        out.getBuffer().setLength(0);
        
        assertEquals(0, translator.translate("d", 0, out));
        assertEquals("", out.toString());
        out.getBuffer().setLength(0);
    }
    
    
    @Test
    public void testConstructor_emptyLookup() {
        LookupTranslator translator = new LookupTranslator();
        Writer out = new StringWriter();
        assertNull(translator.translate("a", 0, out));
        assertEquals("", out.toString());
        out.getBuffer().setLength(0);
    }
}