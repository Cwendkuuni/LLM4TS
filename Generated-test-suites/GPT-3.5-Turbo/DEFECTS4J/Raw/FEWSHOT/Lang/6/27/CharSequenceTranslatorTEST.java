package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyTranslator();
        assertEquals("foo", translator.translate("foo"));
        assertEquals("bar", translator.translate("bar"));
        assertEquals("baz", translator.translate("baz"));
        assertNull(translator.translate(null));
    }

    private static class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = input.charAt(index);
            if (c == 'a') {
                out.write('f');
                return 1;
            } else if (c == 'b') {
                out.write('b');
                out.write('a');
                out.write('r');
                return 1;
            } else if (c == 'c') {
                out.write('b');
                out.write('a');
                out.write('z');
                return 1;
            } else {
                out.write(c);
                return 1;
            }
        }
    }
}