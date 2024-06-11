
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    private CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };

    @Test
    public void testTranslate() {
        assertEquals("foo", translator.translate("foo"));
        assertEquals("bar", translator.translate("bar"));
        assertNull(translator.translate(null));
    }

    @Test
    public void testWith() {
        assertEquals(translator, translator.with());
        assertEquals(translator, translator.with(translator));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex('A'));
        assertEquals("61", CharSequenceTranslator.hex('a'));
        assertEquals("5A", CharSequenceTranslator.hex('Z'));
        assertEquals("7A", CharSequenceTranslator.hex('z'));
    }

    // Missing some test cases for other methods.
}
