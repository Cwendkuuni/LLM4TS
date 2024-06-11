package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    @Test
    public void testTranslate_withNullInput_returnsNull() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_withEmptyInput_returnsEmptyString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslate_withNonEmptyInput_returnsTranslatedString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("hello");
        assertEquals("hello", result);
    }

    @Test(expected = RuntimeException.class)
    public void testTranslate_withIOExceptionInWriter_throwsRuntimeException() {
        CharSequenceTranslator translator = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        translator.translate("hello");
    }

    @Test
    public void testTranslate_withTranslatorConcatenation_returnsConcatenatedTranslation() {
        CharSequenceTranslator translator1 = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("1");
                return 1;
            }
        };

        CharSequenceTranslator translator2 = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("2");
                return 1;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String result = mergedTranslator.translate("hello");
        assertEquals("12", result);
    }

    @Test
    public void testHex_withCharacter_returnsExpectedHexRepresentation() {
        String hexResult = CharSequenceTranslator.hex(65); // 'A'
        assertEquals("41", hexResult);
    }

    @Test
    public void testHex_withCharacterBelow16_returnsExpectedHexRepresentationWithLeadingZero() {
        String hexResult = CharSequenceTranslator.hex(7);
        assertEquals("07", hexResult);
    }

    @Test
    public void testHex_withCharacterAbove255_returnsExpectedHexRepresentation() {
        String hexResult = CharSequenceTranslator.hex(1000);
        assertEquals("3E8", hexResult);
    }

}