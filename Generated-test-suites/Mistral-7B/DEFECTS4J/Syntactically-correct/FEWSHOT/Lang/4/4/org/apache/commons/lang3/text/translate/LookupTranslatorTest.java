package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][][]{});
        StringWriter writer = new StringWriter();

        translator.translate("", 0, writer);

        assertEquals("", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithSingleCharacterLookup() {
        CharSequence[][] lookup = new CharSequence[][]{{"a", "z"}, {"b", "y"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("ab", 0, writer);

        assertEquals("zy", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithMultiCharacterLookup() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("foobaz", 0, writer);

        assertEquals("quxbar", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithOutOfBoundsIndex() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("foobaz", 3, writer);

        assertEquals("", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithPartialMatch() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"foobaz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("foobaz", 0, writer);

        assertEquals("qux", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithLongestSequence() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"foobarbaz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("foobarbaz", 0, writer);

        assertEquals("qux", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithShortestSequence() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"foo", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("foo", 0, writer);

        assertEquals("qux", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithEmptyInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate("", 0, writer);

        assertEquals("", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithNullInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate(null, 0, writer);

        assertNull(writer.toString());
    }
}