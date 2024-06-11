package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.test.basic.CharSequenceTestCase;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest extends CharSequenceTestCase {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, new StringWriter()));
    }

    @Test
    public void testLookupTranslatorWithSingleEntryLookupTable() {
        CharSequence key = "test";
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key, value}});
        StringWriter out = new StringWriter();

        assertEquals(value.toString(), translator.translate(key, 0, out).toString());
        assertNull(translator.translate(CharSequenceUtils.EMPTY, 0, out));
        assertNull(translator.translate("other", 0, out));
    }

    @Test
    public void testLookupTranslatorWithMultipleEntriesLookupTable() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key1, value1}, {key2, value2}});
        StringWriter out = new StringWriter();

        assertEquals(value1.toString(), translator.translate(key1, 0, out).toString());
        assertEquals(value2.toString(), translator.translate(key2, 0, out).toString());
        assertNull(translator.translate(CharSequenceUtils.EMPTY, 0, out));
        assertNull(translator.translate("other", 0, out));
    }

    @Test(expected = IOException.class)
    public void testLookupTranslatorWithNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"test", "replacement"}});
        translator.translate(CharSequenceUtils.EMPTY, 0, null);
    }

    @Test
    public void testLookupTranslatorWithOutOfBoundsInputIndex() {
        CharSequence key = "test";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key, "replacement"}});
        StringWriter out = new StringWriter();

        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 1, out).toString());
        assertNull(translator.translate(key, 1, out));
    }

    @Test
    public void testLookupTranslatorWithShortInputSequence() {
        CharSequence key = "test";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{key, "replacement"}});
        StringWriter out = new StringWriter();

        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, out).toString());
        assertNull(translator.translate(key, 1, out));
        assertEquals("replacement", translator.translate(key, 0, out).toString());
    }

    @Test
    public void testLookupTranslatorWithLongInputSequenceBeyondLookupTableSize() {
        CharSequence keyLong = new String(new char[100]);
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"test", "replacement"}});
        StringWriter out = new StringWriter();

        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, out).toString());
        assertNull(translator.translate(keyLong, 0, out));
    }

    @Test
    public void testLookupTranslatorWithLongInputSequenceWithinLookupTableSize() {
        CharSequence keyMedium = new String(new char[50]);
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{keyMedium, value}});
        StringWriter out = new StringWriter();

        assertEquals(value.toString(), translator.translate(keyMedium, 0, out).toString());
        assertNull(translator.translate(CharSequenceUtils.EMPTY, 0, out));
        assertNull(translator.translate("other", 0, out));
    }

    @Test
    public void testLookupTranslatorWithInputSequenceLongerThanLookupTableEntry() {
        CharSequence keyLong = new String(new char[51]);
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{new String(new char[50]), value}});
        StringWriter out = new StringWriter();

        assertEquals(value.toString(), translator.translate(keyLong.subSequence(0, 50), 0, out).toString());
        assertNull(translator.translate(CharSequenceUtils.EMPTY, 0, out));
        assertNull(translator.translate(keyLong, 0, out));
    }

    @Test
    public void testLookupTranslatorWithInputSequenceShorterThanLookupTableEntry() {
        CharSequence keyShort = new String(new char[20]);
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{new String(new char[30]), value}});
        StringWriter out = new StringWriter();

        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, out).toString());
        assertNull(translator.translate(keyShort, 0, out));
        assertEquals(value.toString(), translator.translate(keyShort.concat(new String(new char[10])), 0, out).toString());
    }

    @Test
    public void testLookupTranslatorWithInputSequencePartiallyMatchedWithLookupTableEntry() {
        CharSequence keyPartiallyMatched = new String(new char[25]);
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{new String(new char[30]), value}});
        StringWriter out = new StringWriter();

        assertEquals("", translator.translate(CharSequenceUtils.EMPTY, 0, out).toString());
        assertNull(translator.translate(keyPartiallyMatched, 0, out));
        assertEquals(value.toString(), translator.translate(keyPartiallyMatched.subSequence(0, 25), 0, out).toString());
    }

    @Test
    public void testLookupTranslatorWithInputSequenceMatchedWithMultipleLookupTableEntries() {
        CharSequence keyMatched = new String(new char[10]);
        CharSequence value1 = "replacement1";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{new String(new char[5]), value1}, {new String(new char[10]), value2}});
        StringWriter out = new StringWriter();

        assertEquals(value1.toString(), translator.translate(keyMatched.subSequence(0, 5), 0, out).toString());
        assertEquals(value2.toString(), translator.translate(keyMatched, 0, out).toString());
    }
}