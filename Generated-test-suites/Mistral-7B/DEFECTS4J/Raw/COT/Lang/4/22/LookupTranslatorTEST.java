package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * LookupTranslatorTest
 *
 * @since 3.0
 * @version $Id$
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorConstruction() {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(1, translator.lookupMap.size());
        assertEquals("def", translator.lookupMap.get("abc"));
        assertEquals(3, translator.shortest);
        assertEquals(3, translator.longest);
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals(0, translator.lookupMap.size());
        assertNull(translator.lookupMap.get("abc"));
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
    }

    @Test
    public void testTranslateWithinBounds() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "abcdefg";
        int index = 0;
        int length = translator.translate(input, index, writer);
        assertEquals("def", writer.toString());
        assertEquals(3, length);
        index += length;
    }

    @Test
    public void testTranslateOutsideBounds() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "abcabcdefg";
        int index = 0;
        int length = translator.translate(input, index, writer);
        assertEquals("def", writer.toString());
        assertEquals(3, length);
        index += length;

        length = translator.translate(input, index, writer);
        assertNull(writer.toString());
        assertEquals(0, length);
    }

    @Test(expected = IOException.class)
    public void testTranslateNullInput() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        translator.translate(null, 0, writer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateIndexOutOfBounds() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "abcdefg";
        int index = input.length();
        int length = translator.translate(input, index, writer);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate("abcdefg".toCharArray(), 0, null);
    }

    @Test
    public void testTranslateEmptyLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter writer = new StringWriter();
        CharSequence input = "abcdefg";
        int index = 0;
        int length = translator.translate(input, index, writer);
        assertEquals(0, length);
    }

    @Test
    public void testTranslateNonMatchingLookup() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] {{"abc", "def"}, {"hij", "klm"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "abcdefg";
        int index = 0;
        int length = translator.translate(input, index, writer);
        assertEquals("def", writer.toString());
        assertEquals(3, length);
        index += length;

        length = translator.translate(input, index, writer);
        assertNull(writer.toString());
        assertEquals(0, length);

        input = "hijklmno";
        index = 0;
        length = translator.translate(input, index, writer);
        assertNull(writer.toString());
        assertEquals(0, length);
    }
}