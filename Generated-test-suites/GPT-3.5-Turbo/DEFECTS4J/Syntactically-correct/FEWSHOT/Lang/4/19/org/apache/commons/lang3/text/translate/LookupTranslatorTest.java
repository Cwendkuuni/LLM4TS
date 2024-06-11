package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "1"},
            {"ab", "2"},
            {"abc", "3"}
        });
        
        StringWriter out = new StringWriter();
        int result = translator.translate("abcde", 0, out);
        assertEquals(3, result);
        assertEquals("3", out.toString());
        
        out = new StringWriter();
        result = translator.translate("abcdefgh", 2, out);
        assertEquals(3, result);
        assertEquals("3", out.toString());
        
        out = new StringWriter();
        result = translator.translate("defghi", 2, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testConstructor() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "1"},
            {"ab", "2"},
            {"abc", "3"}
        });

        HashMap<CharSequence, CharSequence> expected = new HashMap<>();
        expected.put("a", "1");
        expected.put("ab", "2");
        expected.put("abc", "3");

        assertEquals(expected, translator.lookupMap);
        assertEquals(1, translator.shortest);
        assertEquals(3, translator.longest);
    }
}