package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    private class TestTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
        
    }
    
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "test";
        
        assertEquals(input, translator.translate(input));
    }
    
    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        
        assertEquals(null, translator.translate(null));
    }
    
    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        
        assertEquals("", translator.translate(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        translator.translate("test", null);
    }
    
    @Test
    public void testTranslateWithNullWriterAndNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter out = new StringWriter();
        translator.translate(null, out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter out = new StringWriter();
        String input = "test";
        
        translator.translate(input, out);
        
       assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWithWriterAndNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter out = new StringWriter();
        
        translator.translate(null, out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWithWriterAndEmptyInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter out = new StringWriter();
        
        translator.translate("", out);
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequenceTranslator[] translators = {translator};
        
        CharSequenceTranslator result = translator.with(translators);
        
        assertEquals(translator, result);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHexWithUpperCaseHex() {
        int codepoint = 97;
        String expected = "61";
        
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHexWithLowerCaseHex() {
        int codepoint = 48;
        String expected = "30";
        
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHexWithZeroCodepoint() {
        int codepoint = 0;
        String expected = "0";
        
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
}