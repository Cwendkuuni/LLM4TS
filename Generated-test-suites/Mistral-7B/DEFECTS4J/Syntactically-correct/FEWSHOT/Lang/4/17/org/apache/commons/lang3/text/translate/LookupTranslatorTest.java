package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class LookupTranslatorTest {

    private LookupTranslator translator;
    private CharSequence[][] lookup;
    private CharSequence input;
    private int index;
    private String expectedOutput;

    public LookupTranslatorTest(CharSequence[][] lookup, CharSequence input, int index, String expectedOutput) {
        this.lookup = lookup;
        this.input = input;
        this.index = index;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index} of input: {input}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, "input", 0, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("a"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 2, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 2, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 3, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText(""), 0, ""},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText(""), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 2, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 3, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("a"), 0, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("a"), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("ab"), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 2, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 3, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText("b")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd")}, new StringText("abcd"), 0, "cd"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd")}, new StringText("abcd"), 1, "ab"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd")}, new StringText("abcd"), 2, "c"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd")}, new StringText("abcd"), 3, "d"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("a"), new StringText(""), new StringText("b"), new StringText(""), new StringText("c")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText(""), new StringText("b"), new StringText(""), new StringText("c")}, new StringText("ab"), 1, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText(""), new StringText("b"), new StringText(""), new StringText("c")}, new StringText("abcd"), 0, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText(""), new StringText("b"), new StringText(""), new StringText("c")}, new StringText("abcd"), 1, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText(""), new StringText("b"), new StringText(""), new StringText("c")}, new StringText("abcd"), 2, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText(""), new StringText(""), new StringText("c")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText(""), new StringText(""), new StringText("c")}, new StringText("ab"), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText(""), new StringText(""), new StringText("c")}, new StringText("abcd"), 0, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText(""), new StringText(""), new StringText("c")}, new StringText("abcd"), 1, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText(""), new StringText(""), new StringText("c")}, new StringText("abcd"), 2, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 0, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 1, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 2, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 3, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("a"), 0, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("a"), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("ab"), 0, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("ab"), 1, null},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 0, "d"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 1, "c"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 2, "b"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 3, "a"},
                {new CharSequence[][]{new StringText("a"), new StringText("b"), new StringText("c"), new StringText("d")}, new StringText("abcd"), 4, null},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 0, "cd"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 1, "ab"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 2, "ef"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 3, "gh"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 4, "ij"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("ij"), 0, "ij"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("ij"), 1, null},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 0, "cd"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 1, "ab"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 2, "ef"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 3, "gh"},
                {new CharSequence[][]{new StringText("ab"), new StringText("cd"), new StringText("ef"), new StringText("gh"), new StringText("ij")}, new StringText("abcd"), 4, "ij"}
        });
    }

    @Test
    public void testTranslate() throws IOException {
        translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        assertEquals(expectedOutput, translator.translate(input, index, writer));
        writer.flush();
        assertEquals(expectedOutput, writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullInput() throws IOException {
        translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        translator.translate(null, 0, writer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithInvalidIndex() throws IOException {
        translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        CharSequence input = new StringText("abcd");
        translator.translate(input, input.length(), writer);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullOutput() throws IOException {
        translator = new LookupTranslator(lookup);
        translator.translate(input, index, null);
    }

    @Test
    public void testEmptyLookupTable() throws IOException {
        translator = new LookupTranslator(Collections.emptyMap().toCharArray());
        StringWriter writer = new StringWriter();
        assertEquals("", translator.translate(new StringText(""), 0, writer));
        writer.flush();
    }

    private static class StringText implements CharSequence {
        private final String string;

        public StringText(String string) {
            this.string = string;
        }

        @Override
        public int length() {
            return string.length();
        }

        @Override
        public char charAt(int index) {
            return string.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringText(string.substring(start, end));
        }

        @Override
        public String toString() {
            return string;
        }
    }
}