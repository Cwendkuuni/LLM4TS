
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                } else if (input.charAt(index) == 'b') {
                    out.write('a');
                    return 1;
                }
                return 0;
            }
        };

        assertEquals("ba", translator.translate("ab"));
        assertEquals("b", translator.translate("a"));
        assertEquals("a", translator.translate("b"));
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        try {
            translator.translate("test", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            fail("Expected IllegalArgumentException to be thrown");
        }
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
              if (input.charAt(index) == 'a') {
                out.write('b');
                return 1;
              }
              return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
              if (input.charAt(index) == 'b') {
                out.write('c');
                return 1;
              }
              return 0;
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        assertEquals("cbc", mergedTranslator.translate("abc"));
        assertEquals("bc", mergedTranslator.translate("ac"));
        assertEquals("c", mergedTranslator.translate("a"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
    }

}
