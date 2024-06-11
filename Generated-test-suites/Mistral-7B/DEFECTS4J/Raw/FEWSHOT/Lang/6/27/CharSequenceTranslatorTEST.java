package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new HexTranslator();
        String input = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String expectedOutput = "4142434445464748494a4b4c4d4e4f50515253545556575859";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWithAggregate() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregatedTranslator = identityTranslator.with(hexTranslator);
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String expectedOutput = "6162636465666768696a6b6c6d6e6f70717273747576777879"; // hex of 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
        String output = aggregatedTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence sequence = "test";
        Writer out = null;
        translator.translate(sequence, out);
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "";
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    @Test
    public void testTranslateWithNoConsumption() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = Character.toString('x');
        StringWriter out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals(1, consumed);
        String output = out.toString();
        assertEquals("x", output);
    }

    @Test
    public void testTranslateWithSingleCharacter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = Character.toString('x');
        StringWriter out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals(1, consumed);
        String output = out.toString();
        assertEquals("x", output);
    }

    @Test
    public void testTranslateWithMultipleCharacters() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abc";
        StringWriter out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals(3, consumed);
        String output = out.toString();
        assertEquals("abc", output);
    }

    @Test
    public void testTranslateWithSurrogatePair() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "👋";
        StringWriter out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals(2, consumed);
        String output = out.toString();
        assertEquals("👋", output);
    }

    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return c.length;
        }
    }

}