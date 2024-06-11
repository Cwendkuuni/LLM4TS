package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator(new CharSequence[][]{ {"abc", "123"}, {"def", "456"}, {"xyz", "789"} });
    }

    @Test
    public void testTranslate_InputWithLookupValue_ReturnsTranslatedOutput() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 1;
        CharSequence input = "abc";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("123", translatedOutput);
    }

    @Test
    public void testTranslate_InputWithoutLookupValue_ReturnsZeroIndex() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 0;
        CharSequence input = "xyzpqr";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("", translatedOutput);
    }

    @Test
    public void testTranslate_InputWithMultipleLookupValues_ReturnsTranslatedOutput() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 3;
        CharSequence input = "def";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("456", translatedOutput);
    }

    @Test
    public void testTranslate_InputWithShortestLookupValue_ReturnsTranslatedOutput() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 1;
        CharSequence input = "xz";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("", translatedOutput);
    }

    @Test
    public void testTranslate_InputWithLongestLookupValue_ReturnsTranslatedOutput() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 3;
        CharSequence input = "abcpqr";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("123", translatedOutput);
    }

    @Test
    public void testTranslate_InputWithPartialLookupValue_ReturnsZeroIndex() throws IOException {
        Writer writer = new StringWriter();
        int index = 0;
        int expectedIndex = 0;
        CharSequence input = "zabc";
        int actualIndex = lookupTranslator.translate(input, index, writer);
        String translatedOutput = writer.toString();
        assertEquals(expectedIndex, actualIndex);
        assertEquals("", translatedOutput);
    }
}