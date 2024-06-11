package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        HashMap<CharSequence, CharSequence> expectedMap = new HashMap<>();
        expectedMap.put("a", "1");
        expectedMap.put("b", "2");
        expectedMap.put("c", "3");
        assertEquals(expectedMap, translator.lookupMap);
        assertEquals(1, translator.shortest);
        assertEquals(1, translator.longest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {{"a", "1"}, {"b", "2"}, {"c", "3"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer writer = new StringWriter();
        
        assertEquals(1, translator.translate("a", 0, writer));
        assertEquals("1", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(1, translator.translate("b", 0, writer));
        assertEquals("2", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(1, translator.translate("c", 0, writer));
        assertEquals("3", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(0, translator.translate("d", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
    }
}