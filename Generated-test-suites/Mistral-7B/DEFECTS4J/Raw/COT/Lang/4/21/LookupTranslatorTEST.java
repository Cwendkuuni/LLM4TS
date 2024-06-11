package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequencePair;
import org.junit.Test;

/**
 * Tests for {@link LookupTranslator} class
 *
 * @author John Doe
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNull(translator.getLookupMap());
    }

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        CharSequencePair[] lookup = new CharSequencePair[0];
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNull(translator.getLookupMap());
    }

    @Test
    public void testLookupTranslatorWithValidLookupTable() {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<CharSequence, CharSequence>() {
            {
                put("apple", "banana");
                put("cat", "dog");
            }
        };
        assertEquals(expectedLookupMap, translator.getLookupMap());
    }

    @Test
    public void testTranslateMethodWithEmptyInput() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.class.asSubclass(String.class).cast(""), 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateMethodWithValidInput() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(4, translator.translate(CharSequence.class.asSubclass(String.class).cast("applecat"), 0, out));
        assertEquals("bananadog", out.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslateMethodWithInputBeyondBoundaries() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        translator.translate(CharSequence.class.asSubclass(String.class).cast("abapplecat"), 2, out);
    }

    @Test
    public void testTranslateMethodWithInputShorterThanShortestKey() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.class.asSubclass(String.class).cast("apple"), 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateMethodWithInputLongerThanLongestKey() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(4, translator.translate(CharSequence.class.asSubclass(String.class).cast("applecatdog"), 0, out));
        assertEquals("bananadog", out.toString());
    }

    @Test
    public void testTranslateMethodWithInputLongerThanLongestKeyButNotLongerThanInputLength() throws Exception {
        CharSequencePair[][] lookup = new CharSequencePair[][] { { "apple", "banana" }, { "cat", "dog" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(4, translator.translate(CharSequence.class.asSubclass(String.class).cast("applecatdogfoo"), 0, out));
        assertEquals("bananadog", out.toString());
    }
}