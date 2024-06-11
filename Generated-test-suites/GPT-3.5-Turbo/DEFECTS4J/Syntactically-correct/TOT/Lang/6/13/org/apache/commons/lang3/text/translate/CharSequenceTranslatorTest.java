import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new EntityArrays();
    }

    @Test
    public void testTranslate_shouldTranslateInputToStringOutput() throws IOException {
        // Typical use case
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_shouldTranslateNullInputToStringOutput() throws IOException {
        // Edge case: Null input
        String input = null;
        assertNull(translator.translate(input));
    }

    @Test
    public void testTranslate_shouldTranslateEmptyInputToStringOutput() throws IOException {
        // Edge case: Empty input
        String input = "";
        String expectedOutput = "";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_shouldTranslateSpecialCharactersToStringOutput() throws IOException {
        // Edge case: Input with special characters
        String input = "Hello, \nWorld!";
        String expectedOutput = "Hello, \\nWorld!";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_shouldTranslateSurrogatePairToStringOutput() throws IOException {
        // Edge case: Input with surrogate pair
        String input = "Emoji: \uD83D\uDE00";
        String expectedOutput = "Emoji: \\uD83D\\uDE00";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_shouldThrowIllegalArgumentExceptionWhenWriterIsNull() throws IOException {
        // Error scenario: Writer is null
        String input = "Hello, World!";
        Writer writer = null;
        translator.translate(input, writer);
    }

    @Test
    public void testTranslate_shouldHandleIOExceptionByThrowingRuntimeException() {
        // Error scenario: IOException while writing to StringWriter
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        String input = "Hello, World!";
        try {
            translator.translate(input);
        } catch (RuntimeException e) {
            assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testHex_shouldReturnUpperCaseHexadecimalStringForCodepoint() {
        // Typical use case
        int codepoint = 65;
        String expectedOutput = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testHex_shouldReturnUpperCaseHexadecimalStringForUnicodeCodepoint() {
        // Typical use case: Unicode codepoint
        int codepoint = 128540;
        String expectedOutput = "1F604";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testHex_shouldReturnUpperCaseHexadecimalStringForMinimumCodepoint() {
        // Edge case: Minimum codepoint
        int codepoint = 0;
        String expectedOutput = "0";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testWith_shouldMergeMultipleTranslators() {
        // Typical use case
        CharSequenceTranslator translator1 = new EntityArrays();
        CharSequenceTranslator translator2 = new EntityArrays();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals(Arrays.asList(translator1, translator2), mergedTranslator.getTranslators());
    }

    @Test
    public void testWith_shouldMergeMultipleTranslatorsWhenTranslatorsArrayIsEmpty() {
        // Edge case: Empty translators array
        CharSequenceTranslator mergedTranslator = translator.with();
        assertEquals(Arrays.asList(translator), mergedTranslator.getTranslators());
    }

    @Test
    public void testWith_shouldMergeMultipleTranslatorsWhenTranslatorsArrayContainsNull() {
        // Edge case: Translators array contains null
        CharSequenceTranslator[] translators = {translator, null};
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        assertEquals(Arrays.asList(translator, null), mergedTranslator.getTranslators());
    }

    @Test
    public void testWith_shouldMergeMultipleTranslatorsWhenTranslatorsArrayContainsOneElement() {
        // Edge case: Translators array contains only one element
        CharSequenceTranslator[] translators = {translator};
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        assertEquals(Arrays.asList(translator, translator), mergedTranslator.getTranslators());
    }

    @Test
    public void testWith_shouldMergeMultipleTranslatorsWhenTranslatorsArrayContainsMultipleElements() {
        // Edge case: Translators array contains multiple elements
        CharSequenceTranslator[] translators = {new EntityArrays(), new AggregateTranslator(translator)};
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        assertEquals(Arrays.asList(translator, new EntityArrays(), new AggregateTranslator(translator)), mergedTranslator.getTranslators());
    }

    @Test
    public void testTranslate_withEmptyTranslator_shouldTranslateInputToStringOutput() throws IOException {
        // Typical use case: Empty translator
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testTranslate_withEmptyTranslator_shouldTranslateEmptyInputToStringOutput() throws IOException {
        // Typical use case: Empty translator with empty input
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "";
        String expectedOutput = "";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expectedOutput, writer.toString());
    }
}