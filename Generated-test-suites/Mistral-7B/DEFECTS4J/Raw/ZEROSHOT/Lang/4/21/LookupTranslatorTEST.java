package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private String input;
    private String expectedOutput;

    public LookupTranslatorTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: input = {0}, expectedOutput = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Hello World", "Hello There"},
                {"12345", "1 Two 2 Three 4 Five"},
                {"", ""},
                {"abcd", null},
                {"ababab", "a a a a a b b b b"},
                {"abcdabcdabcd", "a b c d a b c d a b c d"},
                {"", ""},
                {"abcd1234abcd", "a b c d a 1 2 3 4 a b c d"}
        });
    }

    @Before
    public void setUp() {
        CharSequence[][] lookup = {{"Hello", "Hello There"}, {"1 Two", "1"}, {"Three", "2"}, {"Five", "3"}, {"World", ""}, {"ab", "a"}, {"a", "a"}, {"b", "b"}};
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslate() throws Exception {
        StringWriter out = new StringWriter();
        int actualLength = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, out);
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput.substring(0, actualLength));
        assertEquals(expectedOutput.length(), actualLength);
    }

    @Test
    public void testTranslateEmptyInput() throws Exception {
        StringWriter out = new StringWriter();
        int actualLength = translator.translate(CharSequenceUtils.subSequence("", 0, 0), 0, out);
        String actualOutput = out.toString();
        assertNull(actualOutput);
        assertEquals(0, actualLength);
    }

    @Test
    public void testTranslateInvalidInput() throws Exception {
        StringWriter out = new StringWriter();
        int actualLength = translator.translate(CharSequenceUtils.subSequence("abcd1234abcd", 5, 6), 0, out);
        String actualOutput = out.toString();
        assertNull(actualOutput);
        assertEquals(0, actualLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        translator.translate(null, 0, new StringWriter());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateIndexOutOfBounds() throws Exception {
        StringWriter out = new StringWriter();
        translator.translate(CharSequenceUtils.subSequence("abcd1234abcd", 11, 11), 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateIndexOutOfBoundsWithLongerInput() throws Exception {
        StringWriter out = new StringWriter();
        translator.translate(CharSequenceUtils.subSequence("abcd1234abcd", 14, 14), 0, out);
    }

    @Test(expected = IOException.class)
    public void testTranslateIOException() throws Exception {
        StringWriter out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        translator.translate(CharSequenceUtils.subSequence("abcd", 0, 4), 0, out);
    }
}