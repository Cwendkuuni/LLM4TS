package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequence;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        // Add other assertions as needed
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "appplebanana";
        Writer out = new StringWriter();
        int result = translator.translate(new PlainCharSequence(input), 0, out);
        String expected = "applepeel";
        assertEquals(expected, out.toString().substring(0, result));
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        translator.translate(null, 0, out);
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        int result = translator.translate(CharSequence.EMPTY, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateIndexOutOfBounds() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        int result = translator.translate(new PlainCharSequence("appplebanana"), 10, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullLookup() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);
        Writer out = new StringWriter();
        String input = "appplebanana";
        int result = translator.translate(new PlainCharSequence(input), 0, out);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate(new PlainCharSequence("appplebanana"), 0, null);
    }

    @Test
    public void testTranslateShortInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        int result = translator.translate(new PlainCharSequence("ap"), 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateLongInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "appplebananapplebananaappplebanana";
        Writer out = new StringWriter();
        int result = translator.translate(new PlainCharSequence(input), 0, out);
        String expected = "applepealapplepealapplepeal";
        assertEquals(expected, out.toString().substring(0, result));
    }

    @Test
    public void testTranslateMissingSequence() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        int result = translator.translate(new PlainCharSequence("applpe"), 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithIoException() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "peel"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Test IO exception");
            }
        };
        int result = translator.translate(new PlainCharSequence("appplebanana"), 0, out);
        assertEquals(0, result);
    }
}