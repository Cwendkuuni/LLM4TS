package org.apache.commons.lang3.text.translate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        String expected = "Hello World!";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "𠮷𠮷";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        String expected = "𠮷𠮷";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithMultipleCodePoints() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello 你好！";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        String expected = "Hello 你好！";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = (cs, out) -> {
            for (int i = 0; i < cs.length(); i++) {
                int codePoint = Character.codePointAt(cs, i);
                String hex = CharSequenceTranslator.hex(codePoint);
                out.write(hex.toCharArray());
                out.write(1);
            }
        };
        CharSequenceTranslator translator = identity.with(hex);
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        String expected = "48656c6c6f20576f726c6421";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = null;
        StringWriter writer = new StringWriter();
        Assertions.assertThrows(NullPointerException.class, () -> translator.translate(input, writer));
    }

    @Test
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        Writer writer = null;
        Assertions.assertThrows(NullPointerException.class, () -> translator.translate(input, writer));
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            int consumed = 1;
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return consumed;
        }
    }

}