package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

public class LookupTranslatorTest {
    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hi", "hello"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(1, translator.translate(new String("foobar"), 0, null));
        assertEquals(5, translator.translate(new String("hihello"), 0, null));
        assertEquals(5, translator.translate(new String("hihello"), 4, null));
        assertEquals("bar", translator.translate(new String("foo"), 0, null));
        assertEquals("hello", translator.translate(new String("hi"), 0, null));
        assertNull(translator.translate(new String("qux"), 0, null));
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]);
        assertNull(translator.translate(new String(""), 0, null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        translator.translate(null, 0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        translator.translate(new String("foobar"), 3, null);
    }

    @Test
    public void testShortInput() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hi", "hello"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(1, translator.translate(new String("foo"), 0, null));
        assertEquals(5, translator.translate(new String("hihello"), 0, null));
        assertEquals(5, translator.translate(new String("hihello"), 4, null));
        assertEquals("bar", translator.translate(new String("foo"), 0, null));
        assertEquals("hello", translator.translate(new String("hi"), 0, null));
        assertEquals(0, translator.translate(new String("qux"), 0, null));
    }

    @Test
    public void testLongInput() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hi", "hello"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String longInput = "foobarfoobarfoobarfoobarfoo";
        assertEquals(3, translator.translate(new String(longInput), 0, null));
        assertEquals(3, translator.translate(new String(longInput), 12, null));
        assertEquals(3, translator.translate(new String(longInput), 21, null));
        assertEquals(0, translator.translate(new String(longInput), 24, null));
    }
}