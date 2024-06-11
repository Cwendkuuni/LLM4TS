package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals("hello", translator.translate("hello"));
        assertEquals("world", translator.translate("world"));
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        Writer writer = new StringWriter();
        translator.translate("hello", writer);
        assertEquals("hello", writer.toString());
        writer.close();
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new MockTranslator();
        Writer writer = new StringWriter();
        int consumed = translator.translate("hello", 0, writer);
        assertEquals(0, consumed);
        assertEquals("hello", writer.toString());
        writer.close();
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new MockTranslator();
        assertEquals(translator, translator.with());
        assertEquals("hello", translator.with(new MockTranslator()).translate("hello"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("20", CharSequenceTranslator.hex(32));
    }

    //MockTranslator for testing
    private class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.toString());
            return 0;
        }
    }
}