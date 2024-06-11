package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}, {"c", "3"}, {"d", "4"}, {"e", "5"}});

        // Single character lookup
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals(1, translator.translate("b", 0, null));
        assertEquals(1, translator.translate("c", 0, null));
        assertEquals(1, translator.translate("d", 0, null));
        assertEquals(1, translator.translate("e", 0, null));

        // Multiple character lookup
        assertEquals(2, translator.translate("ab", 0, null));
        assertEquals(2, translator.translate("bc", 0, null));
        assertEquals(2, translator.translate("cd", 0, null));
        assertEquals(2, translator.translate("de", 0, null));
        assertEquals(2, translator.translate("abcde", 0, null));

        // Unknown character lookup
        assertEquals(0, translator.translate("f", 0, null));
        assertEquals(0, translator.translate("z", 0, null));
        assertEquals(0, translator.translate("abcdefg", 0, null));
    }
}