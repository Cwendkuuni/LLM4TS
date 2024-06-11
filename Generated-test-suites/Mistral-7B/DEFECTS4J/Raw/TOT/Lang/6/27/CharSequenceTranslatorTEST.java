package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for basic translation.
     */
    @Test
    public void testBasicTranslation() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "Hello, World!";
        String translated = translator.translate(original);
        assertEquals(original, translated);
    }

    /**
     * Test for translation with error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslationWithError() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IllegalArgumentException("Error occurred");
            }
        };
        String input = "Test input";
        translator.translate(input);
    }

    /**
     * Test for translation with empty input.
     */
    @Test
    public void testTranslationWithEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String translated = translator.translate(String.Empty);
        assertEquals(String.Empty, translated);
    }

    /**
     * Test for translation with null input.
     */
    @Test
    public void testTranslationWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String translated = translator.translate(null);
        assertNull(translated);
    }

    /**
     * Test for translation with custom translators.
     */
    @Test
    public void testTranslationWithCustomTranslators() {
        CharSequenceTranslator translator = new AggregateTranslator(
                new IdentityTranslator(),
                new LowerCaseTranslator(),
                new HexTranslator()
        );
        String original = "Hello, World!";
        String translated = translator.translate(original);
        assertEquals("hello, 0x486f, 0x6c6c, 0x6f2c, 0x2057, 0x6f, 0x72, 0x6c, 0x64, 0x21", translated);
    }
}