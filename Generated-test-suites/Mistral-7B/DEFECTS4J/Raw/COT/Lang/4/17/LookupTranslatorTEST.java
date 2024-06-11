package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Test cases for LookupTranslator class
 *
 * @author [Your Name]
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with null lookup table
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullLookupTable() {
        new LookupTranslator((CharSequence[][][]) null);
    }

    /**
     * Test constructor with empty lookup table
     */
    @Test
    public void testConstructorWithEmptyLookupTable() {
        CharSequence[][] lookupTable = new CharSequence[0][];
        LookupTranslator translator = new LookupTranslator(lookupTable);
        assertNull(translator.lookupMap);
        assertNull(translator.shortest);
        assertNull(translator.longest);
    }

    /**
     * Test constructor with valid lookup table
     */
    @Test
    public void testConstructorWithValidLookupTable() {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        assertEquals(2, translator.lookupMap.size());
        assertEquals("apple", translator.lookupMap.keySet().toArray()[0]);
        assertEquals("banana", translator.lookupMap.get("apple"));
        assertEquals("cat", translator.lookupMap.keySet().toArray()[1]);
        assertEquals("dog", translator.lookupMap.get("cat"));
        assertEquals(2, translator.shortest);
        assertEquals(4, translator.longest);
    }

    /**
     * Test translate method with valid input and index
     */
    @Test
    public void testTranslateWithValidInputAndIndex() throws Exception {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter out = new StringWriter();
        CharSequence input = "applecat";
        int index = 0;
        int expectedLength = 5;
        int actualLength = translator.translate(input, index, out);
        assertEquals(expectedLength, actualLength);
        assertEquals("banana dog", out.toString());
    }

    /**
     * Test translate method with input length less than the shortest lookup table sequence
     */
    @Test
    public void testTranslateWithInputLengthLessThanShortestLookupTableSequence() throws Exception {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter out = new StringWriter();
        CharSequence input = "app";
        int index = 0;
        int expectedLength = 2;
        int actualLength = translator.translate(input, index, out);
        assertEquals(expectedLength, actualLength);
        assertNull(out.toString());
    }

    /**
     * Test translate method with input length equal to the shortest lookup table sequence
     */
    @Test
    public void testTranslateWithInputLengthEqualToShortestLookupTableSequence() throws Exception {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter out = new StringWriter();
        CharSequence input = "apple";
        int index = 0;
        int expectedLength = 2;
        int actualLength = translator.translate(input, index, out);
        assertEquals(expectedLength, actualLength);
        assertEquals("banana", out.toString());
    }

    /**
     * Test translate method with input length greater than the longest lookup table sequence
     */
    @Test
    public void testTranslateWithInputLengthGreaterThanLongestLookupTableSequence() throws Exception {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter out = new StringWriter();
        CharSequence input = "applecatdog";
        int index = 0;
        int expectedLength = 6;
        int actualLength = translator.translate(input, index, out);
        assertEquals(expectedLength, actualLength);
        assertEquals("banana dogdog", out.toString());
    }

    /**
     * Test translate method with index out of bounds
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranslateWithIndexOutOfBounds() throws Exception {
        CharSequence[][] lookupTable = new CharSequence[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookupTable);
        StringWriter out = new StringWriter();
        CharSequence input = "applecat";
        int index = 3;
        translator.translate(input, index, out);
    }

    /**
     * Test translate method with null lookup table
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullLookupTable() throws Exception {
        StringWriter out = new StringWriter();
        LookupTranslator translator = new LookupTranslator(null);
        CharSequence input = "applecat";
        int index = 0;
        translator.translate(input, index, out);
    }
}