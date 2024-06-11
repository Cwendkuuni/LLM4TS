package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequence[] seq1 = {"apple", "fruit"};
        CharSequence[] seq2 = {"orange", "fruit"};
        CharSequence[] seq3 = {"banana", "fruit"};
        CharSequence[] seq4 = {"carrot", "vegetable"};
        CharSequence[][] lookup = {seq1, seq2, seq3, seq4};

        LookupTranslator lookupTranslator = new LookupTranslator(lookup);

        assertEquals("fruit", lookupTranslator.translate("apple", 0, null));
        assertEquals("fruit", lookupTranslator.translate("orange", 0, null));
        assertEquals("fruit", lookupTranslator.translate("banana", 0, null));
        assertEquals("vegetable", lookupTranslator.translate("carrot", 0, null));
        assertNull(lookupTranslator.translate("mango", 0, null));
    }
}