package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link CharSequenceTranslator} class
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello, \\u4E16\\u754C!";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello, 世界!";
        int codepointsConsumed = translator.translate(input, 0, out);
        assertEquals(input.length() * 2, codepointsConsumed);
        String actual = ((ByteArrayOutputStream) out).toString();
        String expected = "Hello, \\u4E16\\u754C!";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        Writer out = null;
        translator.translate("Hello", out);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "";
        String output = translator.translate(input);
        assertEquals(output, "");
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = null;
        String output = translator.translate(input);
        assertEquals(null, output);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "Hello, 世界!";
        String expected = "Hello, \\u4E16\\u754C!\\u0026\\u0023"; // Add extra characters for second translator
        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAggregateTranslatorWithNullTranslator() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator[] translators = new CharSequenceTranslator[2];
        translators[0] = translator1;
        translators[1] = null;
        CharSequenceTranslator aggregateTranslator = translator1.with(translators);
    }
}