package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequence[] lookup1 = {{"a", "1"}, {"b", "2"}, {"c", "3"}, {"d", "4"}};
        CharSequence[] lookup2 = {{"x", "10"}, {"y", "20"}, {"z", "30"}};
        LookupTranslator translator1 = new LookupTranslator(lookup1);
        LookupTranslator translator2 = new LookupTranslator(lookup2);
        
        StringBuilder input = new StringBuilder();
        
        // Exact matches
        input.setLength(0);
        input.append("a");
        assertEquals(1, translator1.translate(input, 0, null));
        
        input.setLength(0);
        input.append("b");
        assertEquals(1, translator1.translate(input, 0, null));
        
        input.setLength(0);
        input.append("c");
        assertEquals(1, translator1.translate(input, 0, null));
        
        // No match
        input.setLength(0);
        input.append("x");
        assertEquals(0, translator1.translate(input, 0, null));
        
        input.setLength(0);
        input.append("y");
        assertEquals(0, translator1.translate(input, 0, null));
        
        input.setLength(0);
        input.append("z");
        assertEquals(0, translator1.translate(input, 0, null));
        
        input.setLength(0);
        input.append("d");
        assertEquals(0, translator1.translate(input, 0, null));
        
        // Multiple matches
        input.setLength(0);
        input.append("cabad");
        assertEquals(3, translator1.translate(input, 0, null));
        
        // Empty input
        input.setLength(0);
        assertEquals(0, translator1.translate(input, 0, null));
        
        // Null input
        input = null;
        assertEquals(0, translator1.translate(input, 0, null));
        
        // LookupTranslator with different lookup
        input.setLength(0);
        input.append("xxyz");
        assertEquals(3, translator2.translate(input, 0, null));
        
    }
}