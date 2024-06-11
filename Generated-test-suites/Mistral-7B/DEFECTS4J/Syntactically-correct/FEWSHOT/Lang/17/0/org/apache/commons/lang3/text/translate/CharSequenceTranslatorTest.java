package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testHexTranslator() {
        HexTranslator hexTranslator = new HexTranslator();
        CharSequence input = "ABCDEF";
        String expected = "414243444546";
        String result = hexTranslator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testAggregateTranslator() {
        HexTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator1 = hexTranslator;
        CharSequenceTranslator translator2 = hexTranslator;
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        CharSequence input = "ABCDEF";
        String expected = "414243444546414243444546";
        String result = compositeTranslator.translate(input).toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateMethod() throws Exception {
        StringWriter writer = new StringWriter();
        CharSequence input = "Hello, 世界!";
        CharSequenceTranslator translator = new HexTranslator();
        translator.translate(input, 0, writer);
        String expected = "48656c6c6f2c e683b79921";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateMethodWithIOException() throws Exception {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        CharSequence input = "Hello, 世界!";
        CharSequenceTranslator translator = new HexTranslator();
        try {
            translator.translate(input, writer);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new HexTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateNullWriter() {
        CharSequence input = "Hello, 世界!";
        CharSequenceTranslator translator = new HexTranslator();
        try {
            translator.translate(input, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}