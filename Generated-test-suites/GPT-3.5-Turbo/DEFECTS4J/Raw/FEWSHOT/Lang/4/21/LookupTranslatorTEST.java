
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {
    
    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"a", "A"}, {"b", "B"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("A", translator.translate("a", 0, null));
        assertEquals("B", translator.translate("b", 0, null));
        assertEquals(0, translator.translate("", 0, null));
        assertEquals(0, translator.translate("c", 0, null));
    }
    
    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {{"a", "A"}, {"b", "B"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder output = new StringBuilder();
        assertEquals(1, translator.translate("a", 0, new StringWriter(output)));
        assertEquals("A", output.toString());
        output.setLength(0);
        assertEquals(1, translator.translate("b", 0, new StringWriter(output)));
        assertEquals("B", output.toString());
        output.setLength(0);
        assertEquals(0, translator.translate("", 0, new StringWriter(output)));
        assertEquals("", output.toString());
        output.setLength(0);
        assertEquals(0, translator.translate("c", 0, new StringWriter(output)));
        assertEquals("", output.toString());
    }
}
