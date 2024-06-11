package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = {"cat", "dog"};
        CharSequence[] seq2 = {"fo", "bar"};
        CharSequence[][] lookup = {seq1, seq2};
        LookupTranslator translator = new LookupTranslator(lookup);
        
        StringWriter writer = new StringWriter();
        
        assertEquals(3, translator.translate("cat is a pet", 0, writer));
        assertEquals("dog is a pet", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        assertEquals(0, translator.translate("bat is a pet", 0, writer));
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        
        assertEquals(2, translator.translate("foo", 0, writer));
        assertEquals("bar", writer.toString());
    }

}