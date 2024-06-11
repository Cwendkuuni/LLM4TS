package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.apache.commons.lang3.text.translate.RegexpTranslator;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * JUnit test file for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    /**
     * Tests the abstract method translate(CharSequence, int, Writer)
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        Writer out = new StringWriter();

        int count = translator.translate(input, 0, out);
        assertEquals(input.length(), count);
        assertEquals(input, out.toString());

        translator = new RegexpTranslator("[^\\w]", "X");
        out = new StringWriter();
        count = translator.translate(input, 0, out);
        assertEquals(input.length(), count);
        assertEquals("Xllo, Xllo!", out.toString());
    }

    /**
     * Tests the method translate(CharSequence)
     */
    @org.junit.Test
    public void testTranslateCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(input, output);

        translator = new RegexpTranslator("[^\\w]", "X");
        output = translator.translate(input);
        assertEquals("Xllo, Xllo!", output);
    }

    /**
     * Tests the method translate(CharSequence, Writer)
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslateCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        Writer out = new StringWriter();

        int count = translator.translate(input, out);
        assertEquals(input.length(), count);
        assertEquals(input, out.toString());

        translator = new RegexpTranslator("[^\\w]", "X");
        out = new StringWriter();
        count = translator.translate(input, out);
        assertEquals(input.length(), count);
        assertEquals("Xllo, Xllo!", out.toString());
    }

    /**
     * Tests the method translate(CharSequence, Writer) with null input
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new StringWriter();
        translator.translate(null, out);
    }

    /**
     * Tests the method translate(CharSequence, Writer) with null Writer
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        translator.translate(input, null);
    }

    /**
     * Tests the method with(CharSequenceTranslator...)
     */
    @org.junit.Test
    public void testWith() {
        CharSequenceTranslator t1 = new IdentityTranslator();
        CharSequenceTranslator t2 = new RegexpTranslator("[^\\w]", "X");
        CharSequenceTranslator translator = t1.with(t2);

        String input = "Hello, World!";
        String output = translator.translate(input);
        assertEquals("Xllo, Xllo!", output);
    }

    /**
     * Tests the static method hex(int)
     */
    @org.junit.Test
    public void testHex() {
        assertEquals("48", CharSequenceTranslator.hex(0x48));
        assertEquals("25", CharSequenceTranslator.hex(0x25));
    }
}