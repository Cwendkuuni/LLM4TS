
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class LookupTranslatorTest {
    
    private static final CharSequence[] LOOKUP_1 = { "a", "1" };
    private static final CharSequence[] LOOKUP_2 = { "ab", "12" };
    
    @Test
    public void testConstructor() {
        LookupTranslator translator = new LookupTranslator(LOOKUP_1, LOOKUP_2);
        assertEquals(2, translator.lookupMap.size());
        assertEquals("1", translator.lookupMap.get("a"));
        assertEquals("12", translator.lookupMap.get("ab"));
        assertEquals(1, translator.shortest);
        assertEquals(2, translator.longest);
    }
    
    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(LOOKUP_1, LOOKUP_2);
        
        Writer writer = new StringWriter();
        int result = translator.translate("abca", 0, writer);
        assertEquals(2, result);
        assertEquals("12c", writer.toString());
        
        writer = new StringWriter();
        result = translator.translate("xyz", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}
