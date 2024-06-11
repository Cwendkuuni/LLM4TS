package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        assertEquals("abc", translator.translate("abc"));
        assertEquals("", translator.translate(""));
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals("abc123", mergedTranslator.translate("abc 123"));
        assertEquals("", mergedTranslator.translate(""));
        assertNull(mergedTranslator.translate(null));
    }
    
    private class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == ' ') {
                out.write(" ");
                return 1;
            } else {
                out.write(input.charAt(index));
                return 1;
            }
        }
    }
    
    private class AnotherTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'a') {
                out.write("A");
                return 1;
            } else {
                out.write(input.charAt(index));
                return 1;
            }
        }
    }
}