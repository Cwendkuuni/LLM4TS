package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;&#x41;&#x41;&#xA9;&#x1F4;&#xFF01;&#xFFFF;";
        int index = 0;

        for (int i = 0; i < input.length(); i++) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, index + 2));
                }

                @Override
                public int length() {
                    return 1;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, writer);

            index += length;
        }

        assertEquals("AÂ&amp;", writer.toString());
    }

    @Test
    public void testTranslate_ValidDecEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;&#10;&#13;&#160;&#32;&#128;&#65535;";
        int index = 0;

        for (int i = 0; i < input.length(); i++) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, index + 2));
                }

                @Override
                public int length() {
                    return 1;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, writer);

            index += length;
        }

        assertEquals("&amp;\n \u0080\uffff", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;&#xX;&#123;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, index + 2));
                }

                @Override
                public int length() {
                    return 1;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, writer);
        });

        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;&#123;";
        int index = 0;

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return new String(input.subSequence(index, index + 2));
                }

                @Override
                public int length() {
                    return 1;
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, null);
        });
    }

    @Test
    public void testTranslate_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.subSequence(index, index + 2));
            }

            @Override
            public int length() {
                return 1;
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertNull(length);
        assertEquals("", writer.toString());
    }
}