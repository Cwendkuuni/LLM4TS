package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter writer = new StringWriter();
        String input = "test";

        int result = translator.translate(input, 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testLookupTranslatorWithSingleKeyValuePair() {
        CharSequence key = "test";
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });
        StringWriter writer = new StringWriter();
        String input = "test";

        int result = translator.translate(input, 0, writer);
        assertEquals("replacement", writer.toString());
        assertEquals(key.length(), result);
    }

    @Test
    public void testLookupTranslatorWithMultipleKeyValuePairs() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });
        StringWriter writer = new StringWriter();
        String input = "test1test2";

        int result = translator.translate(input, 0, writer);
        assertEquals("replacement1replacement2", writer.toString());
        assertEquals(key1.length() + key2.length(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyKey() {
        CharSequence key = "";
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullValue() {
        CharSequence key = "test";
        CharSequence value = null;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });
    }

    @Test
    public void testLookupTranslatorWithInputBeyondLookupTable() {
        CharSequence key = "test";
        CharSequence value = "replacement";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });
        StringWriter writer = new StringWriter();
        String input = "testlonginput";

        int result = translator.translate(input, 0, writer);
        assertEquals("replacement", writer.toString());
        assertEquals(key.length(), result);
    }

    @Test
    public void testLookupTranslatorWithInputPartiallyMatched() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });
        StringWriter writer = new StringWriter();
        String input = "test1partialtest2";

        int result = translator.translate(input, 0, writer);
        assertEquals("replacement1" + value2.toString(), writer.toString());
        assertEquals(key1.length() + key2.length() - key1.length(), result);
    }

    @Test
    public void testLookupTranslatorWithInputMatchedAtBeginning() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });
        StringWriter writer = new StringWriter();
        String input = "test1test2";

        int index = key1.length();
        int result = translator.translate(input, index, writer);
        assertEquals("replacement1", writer.toString());
        assertEquals(value1.length(), result);
    }

    @Test
    public void testLookupTranslatorWithInputMatchedAtMiddle() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });
        StringWriter writer = new StringWriter();
        String input = "testothertest1test2";

        int index = key1.length() + 1;
        int result = translator.translate(input, index, writer);
        assertEquals("replacement1", writer.toString());
        assertEquals(value1.length(), result);
    }

    @Test
    public void testLookupTranslatorWithInputMatchedAtEnd() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replacement1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replacement2";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });
        StringWriter writer = new StringWriter();
        String input = "test1test2other";

        int index = input.length() - key2.length() - 1;
        int result = translator.translate(input, index, writer);
        assertEquals("replacement2", writer.toString());
        assertEquals(value2.length(), result);
    }
}