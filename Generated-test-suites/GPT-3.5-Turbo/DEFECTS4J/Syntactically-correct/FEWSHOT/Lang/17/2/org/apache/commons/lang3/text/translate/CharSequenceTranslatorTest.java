package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator merged = translator1.with(translator2);
        assertTrue(merged instanceof AggregateTranslator);
        AggregateTranslator aggrTranslator = (AggregateTranslator) merged;
        assertEquals(2, aggrTranslator.translators.length);
        assertEquals(translator1, aggrTranslator.translators[0]);
        assertEquals(translator2, aggrTranslator.translators[1]);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("2A", CharSequenceTranslator.hex(42));
        assertEquals("1F", CharSequenceTranslator.hex(31));
    }
}