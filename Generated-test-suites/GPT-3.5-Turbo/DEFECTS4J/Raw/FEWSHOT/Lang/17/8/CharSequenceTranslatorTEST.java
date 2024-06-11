
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateCharSequenceIntWriter() throws IOException {
        CharSequenceTranslator translator = new DummyCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        
        int consumed = translator.translate("abc", 0, writer);
        assertEquals(3, consumed);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        CharSequenceTranslator translator = new DummyCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new DummyCharSequenceTranslator();
        translator.translate("abc", null);
    }

    @Test
    public void testTranslateNullCharSequence() {
        CharSequenceTranslator translator = new DummyCharSequenceTranslator();
        
        assertNull(translator.translate(null));
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new DummyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new DummyCharSequenceTranslator();
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
    
    private static class DummyCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
        
    }

}
