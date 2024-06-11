package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                //TODO: Implement translation logic
                throw new UnsupportedOperationException();
            }
        };
        
        String input = "input";
        String expected = "output";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        
        assertNull(translator.translate(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        
        translator.translate("input", null);
    }

    @Test(expected = RuntimeException.class)
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        
        translator.translate("input");
    }

}