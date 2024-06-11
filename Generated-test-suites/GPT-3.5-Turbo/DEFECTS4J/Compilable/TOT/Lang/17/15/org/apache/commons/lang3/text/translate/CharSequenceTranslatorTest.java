package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CharSequenceTranslatorTest {

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'a') {
                out.write('b');
                return 1;
            }
            return 0;
        }
    }

    @Test
    public void testTranslate_StringWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter(input.length() * 2);

        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        assertEquals("bbc", writer.toString());
    }

    @Test
    public void testTranslate_NullWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "abc";
        Writer writer = null;

        try {
            translator.translate(input, writer);
            fail("Expected IllegalArgumentException not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        StringWriter writer = new StringWriter();

        try {
            translator.translate(input, writer);
            assertNull(writer.toString());
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_InputWithTranslation() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "abc";
        String expectedOutput = "bbc";

        String result = translator.translate(input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testTranslate_InputWithoutTranslation() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "def";
        String expectedOutput = "def";

        String result = translator.translate(input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "";
        String expectedOutput = "";

        String result = translator.translate(input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testWith_MergedTranslator() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'b') {
                    out.write('c');
                    return 1;
                }
                return 0;
            }
        };
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "abc";
        String expectedOutput = "ccc";

        String result = mergedTranslator.translate(input);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";

        String result = CharSequenceTranslator.hex(codepoint);

        assertEquals(expectedOutput, result);
    }
}