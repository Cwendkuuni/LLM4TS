package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateStringWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo bar baz";
        String expected = "foo bar baz";
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testTranslateByteArrayOutputStream() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        translator.translate(input, 0, out);
        assertSame(input.toString().getBytes(), out.toByteArray());
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        translator.translate(input, 0, null);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();
        composite.translate(input, 0, writer);
        String expected = "foobarbaz"; // identity translation
        expected += " 666f6f 626161 626173"; // hex translation
        assertEquals(expected, writer.toString());
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return Character.charCount(c[0]);
        }
    }

    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(hex(c).toCharArray()[0]);
            out.write(hex(Character.codePointAt(input, index + 1)).toCharArray()[0]);
            return Character.charCount(c) + 2;
        }
    }

}