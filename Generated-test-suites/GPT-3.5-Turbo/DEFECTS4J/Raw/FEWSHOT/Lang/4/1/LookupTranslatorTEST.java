
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator();
    }

    @Test
    public void testConstructorWithLookupMap() {
        CharSequence[][] lookup = { { "a", "A" }, { "b", "B" }, { "c", "C" } };
        lookupTranslator = new LookupTranslator(lookup);

        assertEquals("A", lookupTranslator.lookupMap.get("a"));
        assertEquals("B", lookupTranslator.lookupMap.get("b"));
        assertEquals("C", lookupTranslator.lookupMap.get("c"));
    }

    @Test
    public void testConstructorWithNullLookupMap() {
        lookupTranslator = new LookupTranslator(null);

        assertEquals(0, lookupTranslator.lookupMap.size());
    }

    @Test
    public void testConstructorWithEmptyLookupMap() {
        CharSequence[][] lookup = {};
        lookupTranslator = new LookupTranslator(lookup);

        assertEquals(0, lookupTranslator.lookupMap.size());
    }

    @Test
    public void testConstructorWithNullLookupArray() {
        CharSequence[][] lookup = { null };
        lookupTranslator = new LookupTranslator(lookup);

        assertEquals(0, lookupTranslator.lookupMap.size());
    }

    @Test
    public void testTranslateWithValidInput() throws IOException {
        CharSequence input = "abcde";
        Writer out = mock(Writer.class);

        lookupTranslator.translate(input, 0, out);

        verify(out, times(1)).write(null);
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        CharSequence input = "";
        Writer out = mock(Writer.class);

        int result = lookupTranslator.translate(input, 0, out);

        assertEquals(0, result);
        verifyZeroInteractions(out);
    }

    @Test
    public void testShortest() {
        CharSequence[] lookup1 = { "a", "A" };
        CharSequence[] lookup2 = { "ab", "AB" };
        CharSequence[] lookup3 = { "abc", "ABC" };
        CharSequence[][] lookup = { lookup1, lookup2, lookup3 };
        lookupTranslator = new LookupTranslator(lookup);

        assertEquals(1, lookupTranslator.shortest);
    }

    @Test
    public void testLongest() {
        CharSequence[] lookup1 = { "a", "A" };
        CharSequence[] lookup2 = { "ab", "AB" };
        CharSequence[] lookup3 = { "abc", "ABC" };
        CharSequence[][] lookup = { lookup1, lookup2, lookup3 };
        lookupTranslator = new LookupTranslator(lookup);

        assertEquals(3, lookupTranslator.longest);
    }
}
