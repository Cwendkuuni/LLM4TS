package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        // Test case 1: Lookup table is null
        LookupTranslator translator1 = new LookupTranslator(null);
        assertEquals(0, translator1.translate("foo", 0, null));

        // Test case 2: Translate input to output using lookup table
        LookupTranslator translator2 = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        assertEquals(0, translator2.translate("", 0, null));
        assertEquals(0, translator2.translate("Hello world!", 0, null));
        assertEquals(3, translator2.translate("foo", 0, null));
        assertEquals(3, translator2.translate("baz", 0, null));
        assertEquals(0, translator2.translate("qux", 0, null));
    }

    @Test
    public void testConstructor() {
        // Test case 1: Lookup table is null
        LookupTranslator translator1 = new LookupTranslator(null);
        assertEquals(0, translator1.longest);
        assertEquals(0, translator1.shortest);

        // Test case 2: Lookup table has non-zero length
        LookupTranslator translator2 = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}});
        assertEquals(3, translator2.longest);
        assertEquals(3, translator2.shortest);
    }
}