package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("Hi", translator.translate("Hello", 0, null));
        assertEquals("Earth", translator.translate("World", 0, null));
        assertEquals("Python", translator.translate("Java", 0, null));

        assertNull(translator.translate("Test", 0, null));
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});

        assertNull(translator.translate("Test", 0, null));
    }

    @Test
    public void testShortInputTranslation() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}, {"He", "Hello"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("Hello", translator.translate("He", 0, null));
    }

    @Test
    public void testLongInputTranslation() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}, {"Hello World", "Hello Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("Hello Earth", translator.translate("Hello World", 0, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        new LookupTranslator(lookup)[0] = null;
        LookupTranslator translator = new LookupTranslator(lookup);

        translator.translate("Test", 0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        translator.translate("Test", 3, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);

        translator.translate("Test", 0, null);
    }
}