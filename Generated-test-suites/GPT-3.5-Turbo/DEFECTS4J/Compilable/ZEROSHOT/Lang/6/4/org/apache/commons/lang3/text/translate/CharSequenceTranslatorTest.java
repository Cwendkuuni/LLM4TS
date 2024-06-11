package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateCharSequenceIntWriter() throws IOException {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            input.append("Hello World!");
        }
        
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new TestTranslator();
        int consumed = translator.translate(input.toString(), 0, writer);
        
        assertEquals(1300, consumed);
        assertEquals(input.toString().replace("Hello", "Test"), writer.toString());
    }

    @Test
    public void testTranslateCharSequence() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Hello World!";
        String translation = translator.translate(input);
        
        assertEquals("Test World!", translation);
    }

    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello World!", writer);
        
        assertEquals("Test World!", writer.toString());
    }

    @Test
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = null;
        try {
            translator.translate("Hello World!", writer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        String translation = translator.translate(input);
        
        assertNull(translation);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequenceTranslator otherTranslator = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator.with(otherTranslator);
        
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String hex = CharSequenceTranslator.hex(codepoint);
        
        assertEquals("41", hex);
    }

    private class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write("T");
                return 1;
            }
            return 0;
        }
        
    }

}