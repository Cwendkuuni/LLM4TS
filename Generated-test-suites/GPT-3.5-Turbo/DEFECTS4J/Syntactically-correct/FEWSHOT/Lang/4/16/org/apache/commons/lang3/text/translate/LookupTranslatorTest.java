package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = {{ "a", "b" }, { "c", "d" }};
        LookupTranslator translator = new LookupTranslator(lookup);
        HashMap<CharSequence, CharSequence> lookupMap = translator.lookupMap;

        assertEquals("b", lookupMap.get("a"));
        assertEquals("d", lookupMap.get("c"));
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {{ "a", "b" }, { "c", "d" }};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        Writer writer = new StringWriter();

        int result = translator.translate("abc", 0, writer);
        assertEquals("b", writer.toString());
        assertEquals(1, result);

        writer = new StringWriter();
        result = translator.translate("xyz", 0, writer);
        assertEquals(0, writer.toString().length());
        assertEquals(0, result);
    }
}