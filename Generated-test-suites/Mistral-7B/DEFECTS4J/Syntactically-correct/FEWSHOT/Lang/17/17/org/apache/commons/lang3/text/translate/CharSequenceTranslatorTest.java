package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
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
        assertEquals("", translator.translate(CharSequences.EMPTY));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("a", translator.translate(CharSequences.of("a")));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ab", translator.translate(CharSequences.of("a", "b")));
    }

    @Test
    public void testTranslateWriterNull() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertThrows(IllegalArgumentException.class, () -> translator.translate(CharSequences.of("a"), null));
    }

    @Test
    public void testTranslateWriterEmpty() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("", translator.translate(CharSequences.EMPTY, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWriterSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("a", translator.translate(CharSequences.of("a"), writer));
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslateWriterMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("ab", translator.translate(CharSequences.of("a", "b"), writer));
        assertEquals("ab", writer.toString());
    }

    @Test
    public void testTranslateWithNullTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.with((CharSequenceTranslator[]) null).translate(CharSequences.of("a")));
    }

    @Test
    public void testTranslateWithEmptyTranslatorArray() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequenceTranslator merged = translator.with();
        assertEquals("a", merged.translate(CharSequences.of("a")));
        assertSame(translator, merged);
    }

    @Test
    public void testTranslateWithSingleTranslator() {
        CharSequenceTranslator translator1 = new ReplacingTranslator("a", "z");
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator merged = translator1.with(translator2);
        assertEquals("z", merged.translate(CharSequences.of("a")));
    }

    @Test
    public void testTranslateWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new ReplacingTranslator("a", "z");
        CharSequenceTranslator translator2 = new ReplacingTranslator("e", "x");
        CharSequenceTranslator merged = translator1.with(translator2);
        assertEquals("zx", merged.translate(CharSequences.of("ae")));
    }

    @Test
    public void testTranslateWithStringInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ab", translator.translate(CharSequences.of("ab")));
        assertEquals("ab", translator.translate(CharSequences.of("ab").toString()));
    }

    @Test
    public void testTranslateWithStringWriterInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequences.of("ab"), writer);
        assertEquals("ab", writer.toString());
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = CharSequences.of("\uD800\uDC00");
        int pos = 0;
        int consumed = translator.translate(input, pos, writer);
        assertEquals(2, consumed);
        assertEquals("\uD800\uDC00", input.subSequence(0, pos));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithSurrogatePairAndWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = CharSequences.of("\uD800\uDC00");
        int pos = 0;
        int consumed = translator.translate(input, pos, writer);
        assertEquals(2, consumed);
        assertEquals("\uD800\uDC00", input.subSequence(0, pos));
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslateWithSurrogatePairAndMultipleTranslators() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ReplacingTranslator("\uD800", "surrogateStart");
        CharSequenceTranslator translator3 = new ReplacingTranslator("\uDC00", "surrogateEnd");
        CharSequenceTranslator merged = translator1.with(translator2, translator3);
        CharSequence input = CharSequences.of("\uD800\uDC00");
        StringWriter writer = new StringWriter();
        int pos = 0;
        int consumed = merged.translate(input, pos, writer);
        assertEquals(2, consumed);
        assertEquals("\uD800\uDC00", input.subSequence(0, pos));
        assertEquals("surrogateStartsurrogateEnd", writer.toString());
    }

    @Test
    public void testTranslateWithLocale() {
        CharSequenceTranslator translator = new HexTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("41", translator.translate(CharSequences.of("A"), Locale.US, writer));
        assertEquals("5B", translator.translate(CharSequences.of("["), Locale.US, writer));
        assertEquals("83", translator.translate(CharSequences.of("ç"), Locale.US, writer));
        assertEquals("C3", translator.translate(CharSequences.of("ç"), Locale.FRANCE, writer));
        assertEquals("41", translator.translate(CharSequences.of("A"), Locale.FRANCE, writer));
        assertEquals("5B", translator.translate(CharSequences.of("["), Locale.FRANCE, writer));
        assertEquals("83", translator.translate(CharSequences.of("ç"), Locale.FRANCE, writer));
        assertEquals("C3", translator.translate(CharSequences.of("ç"), Locale.US, writer));
    }

    @Test
    public void testTranslateWithWriterIOException() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        writer.write("error");
        assertThrows(RuntimeException.class, () -> translator.translate(CharSequences.of("a"), writer));
    }

    @Test
    public void testTranslateWithIOException() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        StringWriter writer = new StringWriter();
        assertThrows(RuntimeException.class, () -> translator.translate(CharSequences.of("a"), writer));
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class ReplacingTranslator extends CharSequenceTranslator {
    private final char from;
    private final char to;

    public ReplacingTranslator(char from, char to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        char c = Character.codePointAt(input, index);
        if (c == this.from) {
            out.write(this.to);
            return Character.charCount(this.to);
        }
        out.write(c);
        return Character.charCount(c);
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        char c = Character.codePointAt(input, index);
        if (Character.isDigit(c)) {
            out.write(c);
            return Character.charCount(c);
        }
        if (Character.isLetter(c) && Character.isUpperCase(c)) {
            out.write(hex(Character.toIntCode(c) - 55));
            return 1;
        }
        out.write(hex(Character.toIntCode(c) - 97));
        return 1;
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : this.translators) {
            consumed += translator.translate(input, index, out);
            if (consumed >= input.length()) {
                break;
            }
            index += consumed;
        }
        return consumed;
    }
}

class CharSequences {
    public static CharSequence EMPTY = Character.toString(0);

    public static CharSequence of(char... cs) {
        return new String(cs);
    }

    public static CharSequence of(String s) {
        return s;
    }
}