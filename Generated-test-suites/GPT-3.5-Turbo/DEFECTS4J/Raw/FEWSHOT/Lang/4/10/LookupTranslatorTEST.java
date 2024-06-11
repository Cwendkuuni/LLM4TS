
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {
    
    private LookupTranslator translator;
    
    @Before
    public void setUp() {
        CharSequence[] lookup1 = { "a", "1" };
        CharSequence[] lookup2 = { "b", "2" };
        CharSequence[][] lookup = { lookup1, lookup2 };
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testConstructor() {
        HashMap<CharSequence, CharSequence> lookupMap = new HashMap<>();
        lookupMap.put("a", "1");
        lookupMap.put("b", "2");
        assertEquals(lookupMap, translator.lookupMap);
        assertEquals(1, translator.shortest);
        assertEquals(2, translator.longest);
    }
    
    @Test
    public void testTranslate() throws IOException {
        CharSequence input = "abc";
        int index = 0;
        Writer out = new StringWriter();
        int result = translator.translate(input, index, out);
        assertEquals(1, result);
        assertEquals("1", out.toString());
    }
    
    @Test
    public void testTranslate_shortestMatch() throws IOException {
        CharSequence input = "b";
        int index = 0;
        Writer out = new StringWriter();
        int result = translator.translate(input, index, out);
        assertEquals(1, result);
        assertEquals("2", out.toString());
    }
    
    @Test
    public void testTranslate_noMatch() throws IOException {
        CharSequence input = "def";
        int index = 0;
        Writer out = new StringWriter();
        int result = translator.translate(input, index, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

}
