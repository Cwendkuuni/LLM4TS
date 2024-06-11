
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setup() {
        CharSequence[][] lookup = {
            {"horse", "dog"},
            {"cat", "mouse"},
            {"apple", "banana"}
        };
        lookupTranslator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslate() throws IOException {
        assertEquals(4, lookupTranslator.translate("horse", 0, new StringWriter()));
        assertEquals(3, lookupTranslator.translate("cat", 0, new StringWriter()));
        assertEquals(5, lookupTranslator.translate("apple", 0, new StringWriter()));
        assertEquals(0, lookupTranslator.translate("", 0, new StringWriter()));
        assertEquals(0, lookupTranslator.translate("dog", 0, new StringWriter()));
        assertEquals(0, lookupTranslator.translate("mouse", 0, new StringWriter()));
        assertEquals(0, lookupTranslator.translate("banana", 0, new StringWriter()));
        assertEquals(0, lookupTranslator.translate("orange", 0, new StringWriter()));
    }
}

