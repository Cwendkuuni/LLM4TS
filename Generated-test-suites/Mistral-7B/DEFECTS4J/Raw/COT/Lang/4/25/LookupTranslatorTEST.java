package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for LookupTranslator class
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "hello world";
        String expected = "hi planet";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(new String(input), 0, stringWriter);
        Assert.assertEquals(expected.length(), length);
        Assert.assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        String input = "test";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(new String(input), 0, stringWriter);
        Assert.assertEquals(0, length);
        Assert.assertEquals("", stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }

    @Test
    public void testEmptyInput() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(0, length);
        Assert.assertEquals("", stringWriter.toString());
    }

    @Test
    public void testInputBeyondLookupTable() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "helloworldtest";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(2, length);
        Assert.assertEquals("hi planet", stringWriter.toString());
    }

    @Test
    public void testInputWithMultipleMatches() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}, {"test", "sample"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "testworldhellotest";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(6, length);
        Assert.assertEquals("sample planet hi sample", stringWriter.toString());
    }

    @Test
    public void testInputWithPartialMatches() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "testhello";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(5, length);
        Assert.assertEquals("hi", stringWriter.toString());
    }

    @Test
    public void testInputWithMixedMatches() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}, {"test", "sample"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "helloworldtest";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(6, length);
        Assert.assertEquals("hi planet", stringWriter.toString());
    }

    @Test
    public void testInputWithEmptyKey() {
        CharSequence[][] lookup = {{"", "empty"}, {"hello", "hi"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "";
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(input, 0, stringWriter);
        Assert.assertEquals(5, length);
        Assert.assertEquals("empty", stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInputBeyondStringBounds() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "helloworldtest";
        int index = 10;
        StringWriter stringWriter = new StringWriter();

        int length = translator.translate(new String(input), index, stringWriter);
    }

    @Test(expected = IOException.class)
    public void testWriterNull() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "helloworldtest";
        int index = 0;

        int length = translator.translate(new String(input), index, null);
    }
}