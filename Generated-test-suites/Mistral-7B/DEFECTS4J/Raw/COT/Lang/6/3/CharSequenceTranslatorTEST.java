package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World!";
        String expected = "48656c6c6f20576f726c6421";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = "48656c6c6f20576f726c6421";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = null;
        assertNull(translator.translate(input));
    }

    @Test
    public void testTranslateNullOutput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "Hello World!";
        Writer output = null;
        assertThrows(IllegalArgumentException.class, () -> translator.translate(input, output));
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new AnotherCharSequenceTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        CharSequence input = "Hello World!";
        String expected = "48656c6c6f20576f726c6421XX"; // 'X' is the unique symbol for AnotherCharSequenceTranslator
        String actual = aggregateTranslator.translate(input);
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testIOException() throws IOException {
        CharSequenceTranslator translator = new IoExceptionCharSequenceTranslator();
        CharSequence input = "Hello World!";
        translator.translate(input); // IOException should be thrown here
    }

}

class MyCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write(hex(Character.codePointAt(input, index)));
        return Character.charCount(c);
    }
}

class AnotherCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write("XX".toCharArray()[0]);
        return Character.charCount(c);
    }
}

class IoExceptionCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException();
    }
}