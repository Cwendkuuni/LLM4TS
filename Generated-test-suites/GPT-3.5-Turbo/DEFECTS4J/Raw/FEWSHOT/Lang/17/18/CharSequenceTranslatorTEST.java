package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {
    
    @Test
    public void testTranslate() {
        // Test with null input
        assertNull(CharSequenceTranslator.translate(null));
        
        // Test with empty input
        assertEquals("", CharSequenceTranslator.translate(""));
        
        // Test with non-empty input
        assertEquals("ABC", CharSequenceTranslator.translate("ABC"));
    }
    
    @Test
    public void testTranslateIndexOutofBounds() {
        // Test with input index out of bounds
        try {
            CharSequenceTranslator.translate("ABC", 5, new StringWriter());
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslateWriterNull() {
        // Test with null Writer
        try {
            CharSequenceTranslator.translate("ABC", 0, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testTranslateWriter() {
        // Test with valid input and Writer
        try {
            StringWriter writer = new StringWriter();
            assertEquals(3, CharSequenceTranslator.translate("ABC", 0, writer));
            assertEquals("ABC", writer.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testWith() {
        // Test with empty translators array
        CharSequenceTranslator translator = CharSequenceTranslator.with();
        assertEquals("ABC", translator.translate("ABC"));
        
        // Test with non-empty translators array
        translator = CharSequenceTranslator.with(new DummyTranslator());
        assertEquals("DEF", translator.translate("ABC"));
    }
    
    @Test
    public void testHex() {
        // Test with valid codepoint
        assertEquals("41", CharSequenceTranslator.hex(65));
        
        // Test with invalid codepoint
        assertEquals("0", CharSequenceTranslator.hex(-1));
    }
}

class DummyTranslator extends CharSequenceTranslator {
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write("DEF");
        return 0;
    }
}