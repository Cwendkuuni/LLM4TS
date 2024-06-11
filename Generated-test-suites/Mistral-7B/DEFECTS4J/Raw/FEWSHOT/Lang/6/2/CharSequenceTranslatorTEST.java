package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Tests for the CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        String input = "Hello, 世界! 你好！";
        CharSequenceTranslator translator = new HexTranslator();
        String output = translator.translate(input);
        String expected = "48656C6C6F2C E4BD8D4F 496D616765";
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithAggregate() {
        String input = "Hello, 世界! 你好！";
        CharSequenceTranslator translator = new HexTranslator().with(new ToUpperCaseTranslator());
        String output = translator.translate(input);
        String expected = "48656C6C6F2C E4BD8D4F 496D616765".toUpperCase(Locale.ENGLISH);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new HexTranslator();
        String output = translator.translate(null);
        assertNull(output);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new HexTranslator();
        String output = translator.translate(CharSequence.class.cast(""));
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithWriter() {
        String input = "Hello, 世界! 你好！";
        StringWriter stringWriter = new StringWriter();
        CharSequenceTranslator translator = new HexTranslator();
        translator.translate(input, stringWriter);
        String output = stringWriter.toString();
        String expected = "48656C6C6F2C E4BD8D4F 496D616765";
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        String input = "Hello, 世界! 你好！";
        CharSequenceTranslator translator = new HexTranslator();
        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithEmptyWriter() {
        String input = "Hello, 世界! 你好！";
        Writer writer = new StringWriter(0);
        CharSequenceTranslator translator = new HexTranslator();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals("", output);
    }

    @Test
    public void testAggregateTranslator() {
        String input = "Hello, 世界! 你好！";
        CharSequenceTranslator translator = new HexTranslator().with(new ReverseTranslator()).with(new ToUpperCaseTranslator());
        String output = translator.translate(input);
        String expected = "！你好！ 世界的 68656C6C 6F2C 68656C6C";
        assertEquals(expected, output);
    }
}

class ToUpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (Character.isUpperCase(Character.codePointAt(input, index))) {
            return 0;
        }
        out.write(Character.toUpperCase(Character.codePointAt(input, index)));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class ReverseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int length = input.length();
        if (index >= length) {
            return 0;
        }
        int offset = length - index - 1;
        out.write(Character.codePointAt(input, offset));
        return Character.charCount(Character.codePointAt(input, offset));
    }
}