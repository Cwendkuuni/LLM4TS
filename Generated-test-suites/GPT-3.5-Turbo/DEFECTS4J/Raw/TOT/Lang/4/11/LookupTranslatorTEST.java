import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator(
                new CharSequence[]{"a", "b"},
                new CharSequence[]{"c", "d"}
        );
    }

    // Test cases for translate method

    @Test
    public void testTranslate_WithValidInputAndMatch_ShouldTranslateAndReturnLength() throws IOException {
        StringBuilder input = new StringBuilder("abc");
        StringWriter output = new StringWriter();

        int result = lookupTranslator.translate(input, 0, output);

        assertEquals("Translated string should be appended to output", "bd", output.toString());
        assertEquals("Length of translated string should be returned", 3, result);
    }

    @Test
    public void testTranslate_WithValidInputAndNoMatch_ShouldNotTranslateAndReturnZeroLength() throws IOException {
        StringBuilder input = new StringBuilder("xyz");
        StringWriter output = new StringWriter();

        int result = lookupTranslator.translate(input, 0, output);

        assertEquals("Output should remain empty", "", output.toString());
        assertEquals("Zero length should be returned when no match is found", 0, result);
    }

    @Test
    public void testTranslate_WithPartialMatchAtEndOfInput_ShouldTranslateAndReturnPartialLength() throws IOException {
        StringBuilder input = new StringBuilder("abcd");
        StringWriter output = new StringWriter();

        int result = lookupTranslator.translate(input, 0, output);

        assertEquals("Translated string should be appended to output", "bd", output.toString());
        assertEquals("Length of partially translated string should be returned", 3, result);
    }

    @Test
    public void testTranslate_WithPartialMatchAtBeginningOfInput_ShouldTranslateAndReturnPartialLength() throws IOException {
        StringBuilder input = new StringBuilder("abcde");
        StringWriter output = new StringWriter();

        int result = lookupTranslator.translate(input, 1, output);

        assertEquals("Translated string should be appended to output", "bd", output.toString());
        assertEquals("Length of partially translated string should be returned", 3, result);
    }

    @Test
    public void testTranslate_WithEmptyInput_ShouldNotTranslateAndReturnZeroLength() throws IOException {
        StringBuilder input = new StringBuilder("");
        StringWriter output = new StringWriter();

        int result = lookupTranslator.translate(input, 0, output);

        assertEquals("Output should remain empty", "", output.toString());
        assertEquals("Zero length should be returned for empty input", 0, result);
    }

    // Other method test cases

    @Test
    public void testConstructor_WithNullLookup_ShouldInitializeWithEmptyLookup() {
        LookupTranslator lookupTranslator = new LookupTranslator((CharSequence[][]) null);
        assertEquals("Lookup map should be empty", 0, lookupTranslator.getLookupMapSize());
    }

    @Test
    public void testConstructor_WithValidLookup_ShouldInitializeLookupMap() {
        CharSequence[] lookup1 = new CharSequence[]{"e", "f"};
        CharSequence[] lookup2 = new CharSequence[]{"g", "h"};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup1, lookup2);

        assertEquals("Lookup map should be populated with correct size", 2, lookupTranslator.getLookupMapSize());
        assertTrue("Lookup map should contain the keys and values correctly", lookupTranslator.hasLookup("e") && lookupTranslator.hasLookup("g")
                && lookupTranslator.getLookupValue("e").equals("f") && lookupTranslator.getLookupValue("g").equals("h"));
    }

    @Test
    public void testConstructor_WithEmptyLookup_ShouldInitializeLookupMapWithZeroSize() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[0][2]);
        assertEquals("Lookup map should be empty when initialized with an empty lookup", 0, lookupTranslator.getLookupMapSize());
    }

    @Test
    public void testConstructor_WithNullKeyInLookup_ShouldInitializeLookupMapIgnoringNullKey() {
        CharSequence[] lookup1 = new CharSequence[]{null, "f"};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup1);

        assertEquals("Lookup map should be empty as invalid lookup entries are ignored", 0, lookupTranslator.getLookupMapSize());
    }

    @Test
    public void testConstructor_WithNullValueInLookup_ShouldInitializeLookupMapIgnoringNullValue() {
        CharSequence[] lookup1 = new CharSequence[]{"e", null};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup1);

        assertEquals("Lookup map should be empty as invalid lookup entries are ignored", 0, lookupTranslator.getLookupMapSize());
    }

    @Test
    public void testConstructor_WithValidLookup_DetectShortestAndLongest() {
        CharSequence[] lookup1 = new CharSequence[]{"abc", "d"};
        CharSequence[] lookup2 = new CharSequence[]{"e", "fg"};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup1, lookup2);

        assertEquals("Shortest length should be correctly detected", 3, lookupTranslator.getShortestLength());
        assertEquals("Longest length should be correctly detected", 2, lookupTranslator.getLongestLength());
    }

    @Test
    public void testConstructor_WithNoLookup_DetectZeroShortestAndLongest() {
        LookupTranslator lookupTranslator = new LookupTranslator((CharSequence[][]) null);

        assertEquals("Shortest length should be zero when lookup is empty", 0, lookupTranslator.getShortestLength());
        assertEquals("Longest length should be zero when lookup is empty", 0, lookupTranslator.getLongestLength());
    }

    @Test
    public void testConstructor_WithLookupHavingEmptyTranslation_DetectZeroShortestAndLongest() {
        CharSequence[] lookup1 = new CharSequence[]{"a", ""};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup1);

        assertEquals("Shortest length should be zero when lookup value is empty", 0, lookupTranslator.getShortestLength());
        assertEquals("Longest length should be zero when lookup value is empty", 0, lookupTranslator.getLongestLength());
    }

    // Utility methods

    @Test
    public void testGetLookupMapSize_WithNonEmptyLookupMap_ShouldReturnCorrectSize() {
        assertEquals("Lookup map size should be correctly returned", 2, lookupTranslator.getLookupMapSize());
    }

    @Test
    public void testGetLookupMapSize_WithEmptyLookupMap_ShouldReturnZero() {
        LookupTranslator emptyLookupTranslator = new LookupTranslator((CharSequence[][]) null);
        assertEquals("Lookup map size should be zero when lookup map is empty", 0, emptyLookupTranslator.getLookupMapSize());
    }

    @Test
    public void testHasLookup_WithExistingKey_ShouldReturnTrue() {
        assertTrue("hasLookup should return true when lookup key exists", lookupTranslator.hasLookup("a"));
    }

    @Test
    public void testHasLookup_WithNonExistingKey_ShouldReturnFalse() {
        assertFalse("hasLookup should return false when lookup key does not exist", lookupTranslator.hasLookup("x"));
    }

    @Test
    public void testGetLookupValue_WithExistingKey_ShouldReturnCorrectValue() {
        assertEquals("getLookupValue should return correct value when lookup key exists", "b", lookupTranslator.getLookupValue("a"));
    }

    @Test
    public void testGetLookupValue_WithNonExistingKey_ShouldReturnNull() {
        assertNull("getLookupValue should return null when lookup key does not exist", lookupTranslator.getLookupValue("x"));
    }

}