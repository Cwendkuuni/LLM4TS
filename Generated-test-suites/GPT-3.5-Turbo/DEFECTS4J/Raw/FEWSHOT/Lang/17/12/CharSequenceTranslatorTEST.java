package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {
    public static class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }
    
    @Test
    public void testTranslate_NullInput_ReturnsNull() {
        MockCharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslate_EmptyInput_ReturnsEmptyString() {
        MockCharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertEquals("", translator.translate(""));
    }
    
    @Test
    public void testTranslate_TranslatedOutput_Success() throws IOException {
        MockCharSequenceTranslator translator = new MockCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("TRANSLATED");
                return 1;
            }
        };
        
        StringWriter writer = new StringWriter();
        translator.translate("input", writer);
        assertEquals("TRANSLATED", writer.toString());
    }
    
    @Test
    public void testHex_InputNotCodepoint_ReturnsNull() {
        assertEquals(null, CharSequenceTranslator.hex('A'));
    }
    
    @Test
    public void testHex_InputCodepoint_ReturnsHexString() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
}