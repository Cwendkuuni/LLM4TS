package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Tests for the {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";

        String expected = "48656c6c6f20576f726c6421";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an empty input.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslateEmptyInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "";

        String expected = "";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a null input.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = null;

        translator.translate(input, new StringWriter(512));
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a null Writer.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        Writer writer = null;

        translator.translate(input, writer);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslateWithIndex() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(output);

        int index = 6;
        int consumed = translator.translate(input, index, writer);
        assertEquals(6, consumed);
        assertEquals("World", new String(output.toByteArray()));
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an empty writer.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslateEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        assertNull(writer.toString());
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with an IOException.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateIOException() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter(512) {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Writer error");
            }
        };

        translator.translate(input, writer);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence)} method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslateMethod() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";

        String expected = "48656c6c6f20576f726c6421";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Tests the {@link CharSequenceTranslator#translate(CharSequence)} method with a null input.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testTranslateNullInputMethod() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = null;

        String actual = translator.translate(input);
        assertNull(actual);
    }

    /**
     * Tests the {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testWithMethod() throws Exception {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator expected = new AggregateTranslator(new CharSequenceTranslator[] { translator1, translator2 });

        CharSequenceTranslator actual = translator1.with(translator2);
        assertEquals(expected.getClass(), actual.getClass());
    }

    /**
     * Tests the {@link CharSequenceTranslator#hex(int)} method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testHexMethod() throws Exception {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
        assertEquals("44", CharSequenceTranslator.hex(68));
        assertEquals("45", CharSequenceTranslator.hex(69));
        assertEquals("46", CharSequenceTranslator.hex(70));
        assertEquals("47", CharSequenceTranslator.hex(71));
        assertEquals("48", CharSequenceTranslator.hex(72));
        assertEquals("49", CharSequenceTranslator.hex(73));
        assertEquals("4a", CharSequenceTranslator.hex(74));
        assertEquals("4b", CharSequenceTranslator.hex(75));
        assertEquals("4c", CharSequenceTranslator.hex(76));
        assertEquals("4d", CharSequenceTranslator.hex(77));
        assertEquals("4e", CharSequenceTranslator.hex(78));
        assertEquals("4f", CharSequenceTranslator.hex(79));
        assertEquals("50", CharSequenceTranslator.hex(80));
        assertEquals("51", CharSequenceTranslator.hex(81));
        assertEquals("52", CharSequenceTranslator.hex(82));
        assertEquals("53", CharSequenceTranslator.hex(83));
        assertEquals("54", CharSequenceTranslator.hex(84));
        assertEquals("55", CharSequenceTranslator.hex(85));
        assertEquals("56", CharSequenceTranslator.hex(86));
        assertEquals("57", CharSequenceTranslator.hex(87));
        assertEquals("58", CharSequenceTranslator.hex(88));
        assertEquals("59", CharSequenceTranslator.hex(89));
        assertEquals("5a", CharSequenceTranslator.hex(90));
        assertEquals("61", CharSequenceTranslator.hex(97));
        assertEquals("62", CharSequenceTranslator.hex(98));
        assertEquals("63", CharSequenceTranslator.hex(99));
        assertEquals("64", CharSequenceTranslator.hex(100));
        assertEquals("65", CharSequenceTranslator.hex(101));
        assertEquals("66", CharSequenceTranslator.hex(102));
        assertEquals("67", CharSequenceTranslator.hex(103));
        assertEquals("68", CharSequenceTranslator.hex(104));
        assertEquals("69", CharSequenceTranslator.hex(105));
        assertEquals("6a", CharSequenceTranslator.hex(106));
        assertEquals("6b", CharSequenceTranslator.hex(107));
        assertEquals("6c", CharSequenceTranslator.hex(108));
        assertEquals("6d", CharSequenceTranslator.hex(109));
        assertEquals("6e", CharSequenceTranslator.hex(110));
        assertEquals("6f", CharSequenceTranslator.hex(111));
        assertEquals("70", CharSequenceTranslator.hex(112));
        assertEquals("71", CharSequenceTranslator.hex(113));
        assertEquals("72", CharSequenceTranslator.hex(114));
        assertEquals("73", CharSequenceTranslator.hex(115));
        assertEquals("74", CharSequenceTranslator.hex(116));
        assertEquals("75", CharSequenceTranslator.hex(117));
        assertEquals("76", CharSequenceTranslator.hex(118));
        assertEquals("77", CharSequenceTranslator.hex(119));
        assertEquals("78", CharSequenceTranslator.hex(120));
        assertEquals("79", CharSequenceTranslator.hex(121));
        assertEquals("7a", CharSequenceTranslator.hex(122));
    }
}