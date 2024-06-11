package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.TestUtils;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setUp() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}, {"java", "python"}};
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslateHello() throws IOException {
        String input = "Hello World";
        String expectedOutput = "Hi World";
        String output = TestUtils.toString(new StringWriter(), new StringBuilder(), () -> translator.translate(CharSequence.class.cast(input), 0, null));
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWorld() throws IOException {
        String input = "Hello World";
        String expectedOutput = "Hi World";
        String output = TestUtils.toString(new StringWriter(), new StringBuilder(), () -> translator.translate(CharSequence.class.cast(input), 6, null));
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateJava() throws IOException {
        String input = "The quick brown fox jumps over the lazy dog. java is a programming language.";
        String expectedOutput = "The quick brown fox jumps over the lazy dog. python is a programming language.";
        String output = TestUtils.toString(new StringWriter(), new StringBuilder(), () -> translator.translate(CharSequence.class.cast(input), input.lastIndexOf("java"), null));
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateEmpty() throws IOException {
        String input = "The quick brown fox jumps over the lazy dog.";
        String output = TestUtils.toString(new StringWriter(), new StringBuilder(), () -> translator.translate(CharSequence.class.cast(input), input.length(), null));
        assertNull(output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() throws IOException {
        String input = null;
        translator.translate(CharSequence.class.cast(input), 0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateNegativeIndex() throws IOException {
        String input = "The quick brown fox jumps over the lazy dog.";
        translator.translate(CharSequence.class.cast(input), -1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateBeyondLastIndex() throws IOException {
        String input = "The quick brown fox jumps over the lazy dog.";
        translator.translate(CharSequence.class.cast(input), input.length() + 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullLookup() throws IOException {
        CharSequence[][] lookup = null;
        LookupTranslator translator = new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateEmptyLookup() throws IOException {
        CharSequence[][] lookup = {};
        LookupTranslator translator = new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidLookup() throws IOException {
        CharSequence[][] lookup = {{new String("Hello"), new String("Hi")}, {new String("World"), 1}};
        LookupTranslator translator = new LookupTranslator(lookup);
    }
}