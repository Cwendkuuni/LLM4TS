package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testTranslateSimpleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("a", translator.translate("abcd"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("abcd", translator.translate("abcd"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        translator.translate("abcd", writer);
        assertEquals("abcd", writer.toString());
    }

    @Test
    public void testTranslateCustomCharacter() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        translator.translate("abcd", writer);
        assertEquals("41424344", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator composite = identity.with(hex);
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        composite.translate("abcd", writer);
        assertEquals("41424344", writer.toString());
    }

    static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.toChars(Character.codePointAt(input, index))[0];
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    static class HexTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.toChars(Character.codePointAt(input, index))[0];
            out.write(CharSequenceTranslator.hex(Character.codePointAt(input, index)).toCharArray());
            return Character.charCount(c);
        }
    }
}