package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] lookup1 = {{"a", "A"}, {"b", "B"}};
        LookupTranslator translator1 = new LookupTranslator(lookup1);
        StringWriter writer1 = new StringWriter();
        assertEquals(1, translator1.translate("apple", 0, writer1));
        assertEquals("A", writer1.toString());

        CharSequence[][] lookup2 = {{"abc", "ABC"}, {"def", "DEF"}};
        LookupTranslator translator2 = new LookupTranslator(lookup2);
        StringWriter writer2 = new StringWriter();
        assertEquals(3, translator2.translate("abcdef", 0, writer2));
        assertEquals("ABC", writer2.toString());

        LookupTranslator translator3 = new LookupTranslator();
        StringWriter writer3 = new StringWriter();
        assertEquals(0, translator3.translate("", 0, writer3));
        assertEquals("", writer3.toString());
    }
}