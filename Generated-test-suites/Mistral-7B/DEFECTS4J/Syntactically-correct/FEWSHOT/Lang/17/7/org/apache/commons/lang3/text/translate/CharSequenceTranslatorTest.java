package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator UNDER_TEST = new Translator();

    @Test
    public void testTranslate() {
        String input = "foo bar baz";
        String expected = "foo %62%61%6B%20%62%61%72%20%62%61%7A%6Z";
        String result = UNDER_TEST.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWriter() throws Exception {
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();
        UNDER_TEST.translate(input, writer);
        String result = writer.toString();
        String expected = "foo %62%61%6B%20%62%61%72%20%62%61%7A%6Z";
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateNullInput() {
        String result = UNDER_TEST.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateEmptyInput() {
        String result = UNDER_TEST.translate(StringUtils.EMPTY);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testTranslateEmptyWriter() throws Exception {
        StringWriter writer = new StringWriter();
        UNDER_TEST.translate(StringUtils.EMPTY, writer);
        assertEquals(StringUtils.EMPTY, writer.toString());
    }

    @Test
    public void testTranslateWriterNullInput() throws Exception {
        StringWriter writer = new StringWriter();
        UNDER_TEST.translate(null, writer);
        assertNull(writer.toString());
    }

    @Test
    public void testTranslateWith() throws Exception {
        CharSequenceTranslator translator1 = new Translator1();
        CharSequenceTranslator translator2 = new Translator2();
        CharSequenceTranslator mergedTranslator = UNDER_TEST.with(translator1, translator2);
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();
        mergedTranslator.translate(input, writer);
        String result = writer.toString();
        String expected = "foo %62%61%6B%20%62%61%72%20%62%61%7A%6Z %21%21%21%21%20%62%6F%6F%20%62%6F%6F%20%62%6F%6F%20%73%75%72%63%68%20%62%6F%6F%20%73%75%72%63%68%20%62%6F%6F";
        assertEquals(expected, result);
    }

    private static class Translator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c)) {
                int num = Character.digit(c, 16);
                if (num > 0) {
                    out.write(num);
                    return Character.charCount(c);
                }
            }
            out.write(c);
            return Character.charCount(c);
        }
    }

    private static class Translator1 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index == 0 && Character.isUpperCase(Character.codePointAt(input, index)) && Character.isAlphabetic(Character.codePointAt(input, index + 1))) {
                out.write('%');
                out.write('%');
                out.write('6' + (Character.codePointAt(input, index) - 'A' + 10));
                return 3;
            }
            return 1;
        }
    }

    private static class Translator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (Character.codePointAt(input, index) == ' ') {
                out.write('%');
                out.write('2');
                out.write('1');
                out.write(' ');
                return 3;
            }
            return Character.charCount(Character.codePointAt(input, index));
        }
    }

}

class TranslateTest {
    public static void main(String[] args) throws Exception {
        // Run the tests.
        org.junit.runner.JUnitCore.main("org.apache.commons.lang3.text.translate.CharSequenceTranslatorTest");
    }

}