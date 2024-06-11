
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("abc", translator.translate("abc"));
    }
    
    @Test
    public void testTranslate_withCustomTranslator() {
        CharSequenceTranslator customTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('A');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return customTranslator.translate(input, index, out);
            }
        };
        
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("Abc", translator.translate("abc"));
    }

    @Test
    public void testTranslate_withMultipleTranslators() {
        CharSequenceTranslator customTranslator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('A');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator customTranslator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'b') {
                    out.write('B');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int consumed = 0;
                consumed += customTranslator1.translate(input, index, out);
                if (consumed == 0) {
                    consumed += customTranslator2.translate(input, index, out);
                }
                return consumed;
            }
        };
        
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("ABc", translator.translate("abc"));
    }

    @Test
    public void testWith() {
        CharSequenceTranslator customTranslator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('A');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator customTranslator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'b') {
                    out.write('B');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator translator = customTranslator1.with(customTranslator2);
        
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("ABc", translator.translate("abc"));
    }

    @Test
    public void testHex() {
        assertEquals("A", CharSequenceTranslator.hex('A'));
        assertEquals("2F", CharSequenceTranslator.hex('/'));
        assertEquals("5A", CharSequenceTranslator.hex('Z'));
    }    
}
