package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class LookupTranslatorTest {

    /**
     * Test LookupTranslator constructor
     */
    @Test
    public void testLookupTranslatorConstructor() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" }, { "baz", "qux" }, { "qux", "corge" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(3, translator.getShortest());
        assertEquals(3, translator.getLongest());
    }

    /**
     * Test empty lookup table
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]);
        assertEquals(0, translator.getShortest());
        assertEquals(0, translator.getLongest());
    }

    /**
     * Test translate method with empty input
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" }, { "baz", "qux" }, { "qux", "corge" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(Character.MIN_VALUE, 0, out));
        assertEquals("", out.toString());

        out = new StringWriter();
        assertEquals(0, translator.translate(Character.MAX_VALUE, 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with input smaller than shortest lookup sequence
     */
    @Test
    public void testTranslateInputSmallerThanShortest() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("foo"), 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with input equal to shortest lookup sequence
     */
    @Test
    public void testTranslateInputEqualShortest() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("foo1"), 0, out));
        assertEquals("bar1", out.toString());
    }

    /**
     * Test translate method with input larger than longest lookup sequence
     */
    @Test
    public void testTranslateInputLargerThanLongest() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("foobar123"), 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with input matching a lookup sequence
     */
    @Test
    public void testTranslateInputMatchingLookupSequence() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("foo3"), 0, out));
        assertEquals("bar3", out.toString());
    }

    /**
     * Test translate method with input partially matching multiple lookup sequences
     */
    @Test
    public void testTranslateInputPartiallyMatchingMultipleLookupSequences() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("foobar1"), 0, out));
        assertEquals("bar1", out.toString());
    }

    /**
     * Test translate method with input partially matching no lookup sequences
     */
    @Test
    public void testTranslateInputPartiallyMatchingNoLookupSequences() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("qux"), 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInputNull() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        translator.translate(null, 0, out);
    }

    /**
     * Test translate method with negative index
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateNegativeIndex() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        translator.translate(new String("foo"), -1, out);
    }

    /**
     * Test translate method with input longer than longest lookup sequence with partial match
     */
    @Test
    public void testTranslateInputLongerThanLongestWithPartialMatch() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("foobar1"), 0, out));
        assertEquals("bar1", out.toString());
    }

    /**
     * Test translate method with input longer than longest lookup sequence with no match
     */
    @Test
    public void testTranslateInputLongerThanLongestWithNoMatch() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo3", "bar3" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("quxbar123"), 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with overlapping lookup sequences
     */
    @Test
    public void testTranslateInputWithOverlappingLookupSequences() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] { { "foo1", "bar1" }, { "foo2", "bar2" }, { "foo1foo2", "bar1bar2" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(6, translator.translate(new String("foo1foo2"), 0, out));
        assertEquals("bar1bar2", out.toString());
    }
}