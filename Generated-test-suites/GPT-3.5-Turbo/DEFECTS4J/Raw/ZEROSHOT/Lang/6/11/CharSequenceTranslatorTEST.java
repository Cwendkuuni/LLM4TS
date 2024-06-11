package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        assertNotNull(translator.translate((CharSequence) null));
        
        CharSequence input = "test";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        Writer writer = new StringWriter();
        try {
            translator.translate((CharSequence) null, writer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        CharSequence input = "test";
        try {
            translator.translate(input, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        CharSequenceTranslator merged = translator1.with(translator2);
        assertNotNull(merged);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals("41", result);
    }
}