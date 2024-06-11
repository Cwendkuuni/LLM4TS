
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Test case 1
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };
        assertEquals("bbb", translator.translate("aaa"));
        
        // Test case 2
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                if (input.charAt(index) == 'b') {
                    out.write('c');
                    return 1;
                }
                return 0;
            }
        };
        assertEquals("bcc", translator2.translate("aabaa"));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };
        assertEquals("", translator.translate(""));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };
        translator.translate("aaa", null);
    }
    
    @Test
    public void testTranslateWithNoTranslation() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        assertEquals("aaa", translator.translate("aaa"));
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'b') {
                    out.write('c');
                    return 1;
                }
                return 0;
            }
        };
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals("ccc", mergedTranslator.translate("aaa"));
    }
    
    @Test
    public void testHex() {
        assertEquals("43", CharSequenceTranslator.hex(67));
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
}
