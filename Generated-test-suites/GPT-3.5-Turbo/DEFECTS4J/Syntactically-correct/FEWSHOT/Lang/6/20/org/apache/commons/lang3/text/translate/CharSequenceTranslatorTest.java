package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        // Test case with no translation required
        CharSequenceTranslator translator = new MyTranslator();
        assertEquals("hello world", translator.translate("hello world"));

        // Test case with translation required
        assertEquals("&lt;abc&gt;", translator.translate("<abc>"));

        // Test case with null input
        assertNull(translator.translate(null));
    }

    private static class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == '<') {
                out.write("&lt;");
                return 1;
            } else if (input.charAt(index) == '>') {
                out.write("&gt;");
                return 1;
            } else {
                return 0;
            }
        }
    }
}