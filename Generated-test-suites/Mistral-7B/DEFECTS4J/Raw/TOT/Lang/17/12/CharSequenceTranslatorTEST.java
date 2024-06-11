package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator composite = translator1.with(translator2);
        CharSequence input = "Hello World!";
        String expected = "HELLO WORLD!";
        String actual = composite.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyTranslator() {
        CharSequenceTranslator translator = new EmptyTranslator();
        CharSequence input = "";
        String expected = "";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testErrorWriter() {
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('a');
                return 1;
            }
        };
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSingleChar() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('A');
                return 1;
            }
        };
        CharSequence input = "A";
        String expected = "A";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMultiChar() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('H');
                out.write('e');
                out.write('l');
                out.write('l');
                out.write('o');
                return 5;
            }
        };
        CharSequence input = "Hello";
        String expected = "Hello";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testSurrogatePair() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('\uD800');
                out.write('\uDC00');
                return 2;
            }
        };
        CharSequence input = "\uD800\uDC00";
        String expected = "\uD800\uDC00";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String actual = translator.translate(CharSequence.class);
        assertNull(actual);
    }
}