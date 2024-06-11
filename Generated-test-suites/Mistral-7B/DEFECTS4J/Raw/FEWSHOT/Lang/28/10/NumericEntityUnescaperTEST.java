package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&xa0;&x1F&x7F;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(4, result);

        ByteArrayOutputStream output = (ByteArrayOutputStream) out;
        byte[] expected = {(byte) 194, (byte) 160, (byte) 191, (byte) 191};
        byte[] actual = output.toByteArray();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testTranslate_ValidDecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&#128;&#171;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(4, result);

        ByteArrayOutputStream output = (ByteArrayOutputStream) out;
        byte[] expected = {(byte) 194, (byte) 144, (byte) 191, (byte) 191};
        byte[] actual = output.toByteArray();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&xx;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new CharSequence() {
                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }

                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public CharSequence subSequence(int start, int end) {
                    return input.subSequence(start, end);
                }
            }, index, out);
        });
    }

    @Test
    public void testTranslate_EmptyEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithSpace() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "& #65;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(1, result);

        ByteArrayOutputStream output = (ByteArrayOutputStream) out;
        byte[] expected = {(byte) 'A'};
        byte[] actual = output.toByteArray();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testTranslate_EntityWithTrailingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&foo;&#128;&#171;;&#x7F;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(11, result);

        ByteArrayOutputStream output = (ByteArrayOutputStream) out;
        byte[] expected = {(byte) 'f', (byte) 'o', (byte) 194, (byte) 144, (byte) 191, (byte) 191, (byte) 127};
        byte[] actual = output.toByteArray();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testTranslate_EntityWithTrailingQuotes() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        String input = "&foo;&#128;&#171;&#x7F;";
        int index = 0;

        int result = unescaper.translate(new CharSequence() {
            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return input.subSequence(start, end);
            }
        }, index, out);

        assertEquals(12, result);

        ByteArrayOutputStream output = (ByteArrayOutputStream) out;
        byte[] expected = {(byte) 'f', (byte) 'o', (byte) 194, (byte) 144, (byte) 191, (byte) 191, (byte) 127};
        byte[] actual = output.toByteArray();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}