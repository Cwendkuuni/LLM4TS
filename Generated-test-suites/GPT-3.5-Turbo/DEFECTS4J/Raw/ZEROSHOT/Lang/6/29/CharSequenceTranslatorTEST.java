package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        assertNull(translator.translate(null));
        
        String input = "hello";
        String expected = input;
        assertEquals(expected, translator.translate(input));
        
        input = "";
        expected = "";
        assertEquals(expected, translator.translate(input));
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
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertNotNull(mergedTranslator);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
        
        AggregateTranslator aggregateTranslator = (AggregateTranslator) mergedTranslator;
        assertEquals(2, aggregateTranslator.translators.length);
        assertSame(translator1, aggregateTranslator.translators[0]);
        assertSame(translator2, aggregateTranslator.translators[1]);
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
        
        codepoint = 63;
        expected = "3F";
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }

}