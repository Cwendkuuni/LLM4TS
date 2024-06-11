package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        // Typical use case
        String result = translator.translate("Hello World");
        assertEquals("Hello World", result);
        
        // Edge case
        result = translator.translate("");
        assertEquals("", result);
        
        // Error scenario
        try {
            translator.translate(null);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        
        // Typical use case
        translator.translate("Hello World", writer);
        String result = writer.toString();
        assertEquals("Hello World", result);
        
        // Edge case
        writer = new StringWriter();
        translator.translate("", writer);
        result = writer.toString();
        assertEquals("", result);
        
        // Error scenario
        try {
            translator.translate(null, writer);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        }
        
        try {
            translator.translate("Hello World", null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testTranslateWithIndex() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        
        // Typical use case
        int consumed = translator.translate("Hello World", 0, writer);
        String result = writer.toString();
        assertEquals("Hello World", result);
        assertEquals(11, consumed);
        
        // Edge case
        writer = new StringWriter();
        consumed = translator.translate("", 0, writer);
        result = writer.toString();
        assertEquals("", result);
        assertEquals(0, consumed);
        
        // Error scenario
        try {
            translator.translate(null, 0, writer);
            fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // expected
        }
        
        try {
            translator.translate("Hello World", 0, null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testHex() {
        // Typical use case
        String result = CharSequenceTranslator.hex(65);
        assertEquals("41", result);
        
        // Edge case
        result = CharSequenceTranslator.hex(0);
        assertEquals("0", result);
    }

    private class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                throw new NullPointerException();
            }
            if (out == null) {
                throw new IllegalArgumentException();
            }
            out.write(input.charAt(index));
            return 1;
        }
    }
}