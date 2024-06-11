import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate("input", writer);
        assertEquals(5, result);
        assertEquals("output", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        int result = translator.translate(null, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("output");
            return input.length();
        }

    }

    @Test
    public void testTranslateWithNullInputString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateWithEmptyInputString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslateWithNonEmptyInputString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("input");
        assertEquals("output", result);
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new TestIOExceptionTranslator();
        assertThrows(IOException.class, () -> translator.translate("input"));
    }

    private static class TestIOExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }

    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(65);
        assertEquals("41", result);
    }
}