package org.junit.test;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"Key1", "Value1"}, {"Key2", "Value2"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.lookupMap.size());
    }

    @Test
    public void testTranslateMethod() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "This is a test string";
        CharSequence[][] lookup = new CharSequence[][]{{"Test", "Replace"}, {"String", "With"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        int index = 5;

        when(out.write(String.valueOf("Replace"))).thenReturn(1);
        when(out.write(String.valueOf("With"))).thenReturn(2);

        int result = translator.translate(input, index, out);
        assertEquals(7, result);
    }

    @Test
    public void testTranslateMethodWithEmptyLookupTable() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "Test";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        int result = translator.translate(input, index, out);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateMethodWithNullInput() throws IOException {
        Writer out = mock(Writer.class);
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Key1", "Value1"}});
        int index = 0;
        CharSequence input = null;
        int result = translator.translate(input, index, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethodWithNegativeIndex() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "Test";
        int index = -1;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Test", "Replace"}});
        int result = translator.translate(input, index, out);
    }

    @Test
    public void testTranslateMethodWithInputSmallerThanShortestKey() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "Test";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"TestShortest", "Replace"}, {"TestLongest", "With"}});
        int result = translator.translate(input, index, out);
        assertNull(result);
    }

    @Test
    public void testTranslateMethodWithInputLargerThanLongestKey() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "TestLongestTest";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"TestShortest", "Replace"}, {"TestLongest", "With"}});
        int result = translator.translate(input, index, out);
        assertNull(result);
    }

    @Test
    public void testTranslateMethodWithMultipleMatches() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "TestStringTest";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"TestString", "Replace"}, {"TestLongest", "With"}});
        when(out.write(String.valueOf("Replace"))).thenReturn(1);
        when(out.write(String.valueOf("With"))).thenReturn(2);
        int result = translator.translate(input, index, out);
        assertEquals(4, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethodWithKeyNotPresent() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "TestAnother";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Test", "Replace"}, {"TestLongest", "With"}});
        int result = translator.translate(input, index, out);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateMethodWithNullWriter() throws IOException {
        CharSequence input = "Test";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Test", "Replace"}});
        int result = translator.translate(input, index, null);
    }

    @Test(expected = IOException.class)
    public void testTranslateMethodWithIOException() throws IOException, InterruptedException {
        Writer out = mock(Writer.class);
        when(out.write(String.valueOf("Replace"))).thenThrow(new IOException());
        CharSequence input = "Test";
        int index = 0;
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Test", "Replace"}});
        int result = translator.translate(input, index, out);
        assertNull(result);
    }
}