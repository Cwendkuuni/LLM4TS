package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the default translator.
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                for (int i = index; i < input.length(); i++) {
                    out.write(input.charAt(i));
                }
                return input.length() - index;
            }
        };

        String original = "This is a test.";
        String translated = translator.translate(original);
        assertEquals("This is a test.", translated);
    }

    /**
     * Test for the {@link AggregateTranslator}.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator uppercase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = Character.codePointAt(input, index);
                out.write(Character.toUpperCase(c));
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        CharSequenceTranslator lowercase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = Character.codePointAt(input, index);
                out.write(Character.toLowerCase(c));
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(uppercase, lowercase);

        String original = "This Is A Test.";
        String translatedUpper = translator.translate(original).toUpperCase();
        String translatedLower = translator.translate(original).toLowerCase();

        assertEquals("THIS IS A TEST.", translatedUpper);
        assertEquals("this is a test.", translatedLower);
    }

    /**
     * Test for throwing an IOException when a Writer fails.
     */
    @Test(expected = RuntimeException.class)
    public void testIOException() throws IOException {
        Writer writer = new StringWriter() {
            @Override
            public void write(int c, IOException e) throws IOException {
                throw e;
            }
        };

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.isSurrogate(Character.codePointAt(input, index)) ? 2 : 1;
            }
        };

        String original = "This is a test.";
        translator.translate(original, writer);
    }

    /**
     * Test for handling empty input.
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String original = "";
        String translated = translator.translate(original);
        assertEquals("", translated);
    }

    /**
     * Test for handling null input.
     */
    @Test
    public void testNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                assertEquals(input, null);
                fail("Should not be called with null input");
                return 0;
            }
        };

        String original = null;
        try {
            translator.translate(original);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}