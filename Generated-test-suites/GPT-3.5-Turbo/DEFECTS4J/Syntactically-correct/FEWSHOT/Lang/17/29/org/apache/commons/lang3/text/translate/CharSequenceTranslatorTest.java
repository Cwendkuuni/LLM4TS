package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    private class TestTranslator extends CharSequenceTranslator {
        
        private int count;
        
        public TestTranslator(int count) {
            this.count = count;
        }
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return count;
        }
    }
    
    @Test
    public void testTranslateWithWriter() throws IOException {
        TestTranslator translator = new TestTranslator(2);
        StringWriter writer = new StringWriter();
        translator.translate("test", writer);
        assertEquals("test", writer.toString());
        
        writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        TestTranslator translator = new TestTranslator(2);
        translator.translate("test", null);
    }
    
    @Test
    public void testTranslateWithString() {
        TestTranslator translator = new TestTranslator(2);
        assertEquals("test", translator.translate("test"));
        
        assertEquals("", translator.translate(null));
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator(1);
        CharSequenceTranslator translator2 = new TestTranslator(2);
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertEquals("test", mergedTranslator.translate("test"));
        
        assertEquals("", mergedTranslator.translate(null));
    }
    
    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("20AC", CharSequenceTranslator.hex(8364));
    }
}