package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertEquals("", translator.translate(new String(""), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithSingleCharacterLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{new String("a"), new String("b")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("b", translator.translate(new String("a"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("c"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithSingleCharacterKeyAndMultipleValuesLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{new String("a"), new String("b"), new String("c")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("b", translator.translate(new String("a"), 0, new StringBuffer()));
        assertEquals("c", translator.translate(new String("a"), 1, new StringBuffer()));
        assertEquals("", translator.translate(new String("d"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithMultiCharacterKeyAndSingleValueLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{new String("foo"), new String("bar")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foo"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("baz"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithMultiCharacterKeyAndMultipleValuesLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foo"), 0, new StringBuffer()));
        assertEquals("qux", translator.translate(new String("baz"), 0, new StringBuffer()));
        assertEquals("corge", translator.translate(new String("quux"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("corge"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithBigLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{};
        for (int i = 0; i < 1000; i++) {
            lookup = (CharSequence[][]) Array.newInstance(CharSequence[].class, 1, 2);
            lookup[0] = new StringBuilder().append("key", i).toCharArray();
            lookup[1] = new StringBuilder().append("value", i).toCharArray();
            LookupTranslator translator = new LookupTranslator(lookup);
            translator.translate(new String("input"), 0, new StringBuffer());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyKeyCharSequence() {
        CharSequence[][] lookup = new CharSequence[][]{new String(""), new String("value")};
        new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyValueCharSequence() {
        CharSequence[][] lookup = new CharSequence[][]{new String("key"), new String("")};
        new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithKeyLongerThanValue() {
        CharSequence[][] lookup = new CharSequence[][]{new String("key"), new String("value")};
        new LookupTranslator(lookup);
        lookup[0] = new StringBuilder().append("longer", "key").toString().toCharArray();
        new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithValueLongerThanKey() {
        CharSequence[][] lookup = new CharSequence[][]{new String("key"), new String("value")};
        new LookupTranslator(lookup);
        lookup[1] = new StringBuilder().append("longer", "value").toString().toCharArray();
        new LookupTranslator(lookup);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLookupTranslatorWithInvalidIndex() {
        CharSequence[][] lookup = new CharSequence[][]{new String("key"), new String("value")};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate(new String("input"), 1, new StringBuffer());
    }

    @Test
    public void testLookupTranslatorWithLongInputAndShortKey() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foobar"), 0, new StringBuffer()));
        assertEquals("qux", translator.translate(new String("bazqux"), 0, new StringBuffer()));
        assertEquals("corge", translator.translate(new String("quuxcorge"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("not_found"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithLongInputAndLongKey() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foobarfoo"), 0, new StringBuffer()));
        assertEquals("qux", translator.translate(new String("bazquxqux"), 0, new StringBuffer()));
        assertEquals("corge", translator.translate(new String("quuxcorgecorge"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("not_foundnot_found"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithLongInputAndMultipleMatches() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foobarfooqux"), 0, new StringBuffer()));
        assertEquals("qux", translator.translate(new String("bazquxquxqux"), 0, new StringBuffer()));
        assertEquals("corge", translator.translate(new String("quuxcorgecorgequx"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("not_foundnot_foundnot_found"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithLongInputAndMultipleMatchesAtDifferentPositions() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foobarquxfooqux"), 0, new StringBuffer()));
        assertEquals("qux", translator.translate(new String("bazquxquxquxqux"), 0, new StringBuffer()));
        assertEquals("corge", translator.translate(new String("quuxcorgecorgequxqux"), 0, new StringBuffer()));
        assertEquals("", translator.translate(new String("not_foundnot_foundnot_foundnot_found"), 0, new StringBuffer()));
    }

    @Test
    public void testLookupTranslatorWithLongInputAndNoMatches() {
        CharSequence[][] lookup = new CharSequence[][]{
            new String("foo"), new String("bar"),
            new String("baz"), new String("qux"),
            new String("quux"), new String("corge")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(new String("not_foundnot_foundnot_foundnot_foundnot_found"), 0, new StringBuffer()));
    }
}