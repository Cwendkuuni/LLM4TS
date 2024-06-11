package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.InvalidArgumentException;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input.
     */
    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate(null, new StringWriter()));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null writer.
     */
    @Test
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate(new String("test"), null));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        String expected = "";
        assertEquals(expected, translator.translate(CharSequence.EMPTY, writer));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with normal input.
     */
    @Test
    public void testTranslateNormalInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        String input = "test";
        String expected = "test";
        assertEquals(expected, translator.translate(new String(input), writer).toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with surrogate pair.
     */
    @Test
    public void testTranslateSurrogatePair() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        String input = "\ud800\udc00";
        String expected = "𠀀";
        assertEquals(expected, translator.translate(new String(input), writer).toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with IOException.
     */
    @Test
    public void testTranslateIOException() {
        CharSequenceTranslator translator = new IOExceptionTranslator();
        StringWriter writer = new StringWriter();
        String input = "test";
        assertThrows(IOException.class, () -> translator.translate(new String(input), writer));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null input.
     */
    @Test
    public void testTranslateNullInputFinal() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate(null));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty input.
     */
    @Test
    public void testTranslateEmptyInputFinal() {
        CharSequenceTranslator translator = new DummyTranslator();
        String result = translator.translate(CharSequence.EMPTY);
        String expected = "";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with normal input.
     */
    @Test
    public void testTranslateNormalInputFinal() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "test";
        String expected = "test";
        String result = translator.translate(new String(input));
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with invalid arguments.
     */
    @Test
    public void testTranslateInvalidArguments() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertThrows(InvalidArgumentException.class,
                () -> translator.translate(null, new StringWriter()));
        assertThrows(InvalidArgumentException.class,
                () -> translator.translate(new StringWriter(), null));
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with empty array.
     */
    @Test
    public void testWithEmptyArray() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequenceTranslator aggregated = translator.with();
        assertEquals(translator, aggregated);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with single translator.
     */
    @Test
    public void testWithSingleTranslator() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();
        CharSequenceTranslator aggregated = translator1.with(translator2);
        assertEquals(2, aggregated.getTranslators().length);
        assertEquals(translator1, aggregated.getTranslators()[0]);
        assertEquals(translator2, aggregated.getTranslators()[1]);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with multiple translators.
     */
    @Test
    public void testWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();
        CharSequenceTranslator translator3 = new DummyTranslator();
        CharSequenceTranslator aggregated = translator1.with(translator2, translator3);
        assertEquals(3, aggregated.getTranslators().length);
        assertEquals(translator1, aggregated.getTranslators()[0]);
        assertEquals(translator2, aggregated.getTranslators()[1]);
        assertEquals(translator3, aggregated.getTranslators()[2]);
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder result = new StringBuilder();
            int pos = 0;
            int len = input.length();
            while (pos < len) {
                int consumed = translate(input, pos, result, 0);
                pos += consumed;
            }
            return result.toString();
        }

        @Override
        protected void translate(CharSequence input, Writer out, int offset, int length) throws IOException {
            for (int i = offset, n = offset + length; i < n; i++) {
                out.write(Character.codePointAt(input, i));
            }
        }

        @Override
        public CharSequenceTranslator[] getTranslators() {
            return new CharSequenceTranslator[0];
        }
    }

    private static class IOExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }

        @Override
        public String translate(CharSequence input) {
            throw new RuntimeException("IOException in translate(CharSequence) method");
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            throw new IOException();
        }

        @Override
        public CharSequenceTranslator[] getTranslators() {
            return new CharSequenceTranslator[0];
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                if (consumed >= input.length()) {
                    break;
                }
            }
            return consumed;
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder result = new StringBuilder();
            translate(input, result);
            return result.toString();
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            int index = 0;
            int length = input.length();
            while (index < length) {
                CharSequenceTranslator translator = translators[0];
                int consumed = translator.translate(input, index, out);
                index += consumed;
                if (consumed == 0) {
                    translator = translators[1];
                    consumed = translator.translate(input, index, out);
                    index += consumed;
                }
            }
        }

        @Override
        protected void translate(CharSequence input, StringBuilder result, int offset, int length) {
            int index = 0;
            int length2 = length;
            while (index < length2) {
                CharSequenceTranslator translator = translators[0];
                String translation = translator.translate(input, index, null);
                result.append(translation);
                index += translation.length();
                if (index < length) {
                    translator = translators[1];
                    translation = translator.translate(input, index, null);
                    result.append(translation);
                    index += translation.length();
                }
            }
        }

        @Override
        public CharSequenceTranslator[] getTranslators() {
            return translators;
        }
    }
}