package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() {
        LookupTranslator lookup = new LookupTranslator(
            new CharSequence[]{"abc", "123"},
            new CharSequence[]{"def", "456"},
            new CharSequence[]{"ghi", "789"}
        );

        StringBuilder sb = new StringBuilder();
        lookup.translate("abcdef", 0, sb);
        assertEquals("123456", sb.toString());

        sb.setLength(0);
        lookup.translate("abcdefghi", 3, sb);
        assertEquals("def789", sb.toString());

        sb.setLength(0);
        lookup.translate("xyz", 0, sb);
        assertEquals("", sb.toString());

        sb.setLength(0);
        lookup.translate("", 0, sb);
        assertEquals("", sb.toString());
    }
}