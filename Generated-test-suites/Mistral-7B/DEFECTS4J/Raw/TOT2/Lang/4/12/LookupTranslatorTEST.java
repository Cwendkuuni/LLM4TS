package org.junit.tests;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorConstruction() {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator.lookupMap);
    }

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "hello world test";
        int index = 0;
        translator.translate(input, index, writer);
        assertEquals("world test", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNull(translator.lookupMap);
    }

    @Test
    public void testKeyNotFound() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "unknown";
        int index = 0;
        translator.translate(input, index, writer);
        assertTrue(writer.toString().isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testShortInputSequence() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "hello";
        int index = 5;
        translator.translate(input, index, writer);
        assertTrue(writer.toString().isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongInputSequence() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "hellotest";
        int index = 2;
        translator.translate(input, index, writer);
        assertTrue(writer.toString().isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongerInputSequence() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = "hellotestworld";
        int index = 2;
        translator.translate(input, index, writer);
        assertTrue(writer.toString().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = null;
        CharSequence input = "hellotestworld";
        int index = 2;
        try {
            translator.translate(input, index, writer);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IOException.class)
    public void testIOExceptionDuringTranslation() throws IOException {
        CharSequence[][] lookup = {{"hello", "world"}, {"test", "result"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                if (c == 'w') {
                    throw new IOException("Test IOException");
                }
                super.write(c);
            }
        };
        CharSequence input = "hellotestworld";
        int index = 2;
        try {
            translator.translate(input, index, writer);
        } catch (IOException e) {
            // expected
        }
    }
}