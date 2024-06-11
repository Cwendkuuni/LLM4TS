package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private static final String NUMERIC_ENTITY_UNESCAPED = "\u00A1\u00A2\u00A3 &#x0393; &#[x03C0]; &#0377; &#[x1F000]; &#12345; &#[x10FFFF]; ";
    private static final byte[] NUMERIC_ENTITY_ESCAPED = NUMERIC_ENTITY_UNESCAPED.getBytes(StandardCharsets.UTF_8);
    private static final String HTML_ENTITY_UNESCAPED = "&#1;&#2;&#3; &#x393; &#{x3C0}; &#247; &#{x1F000}; &#12345; &#{x10FFFF}; ";
    private static final String HTML_ENTITY_ESCAPED = "&#1;&#2;#3; &amp;#x393; &amp;#{x3C0}; &amp;#247; &amp;#{x1F000}; &amp;#12345; &amp;#{x10FFFF}; ";

    @Test
    public void testTranslateNormalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        int translationCount = unescaper.translate(new String(NUMERIC_ENTITY_ESCAPED), 0, writer);
        assertEquals(NUMERIC_ENTITY_UNESCAPED.length(), translationCount);
        assertEquals(NUMERIC_ENTITY_UNESCAPED, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testTranslateHexCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        int translationCount = unescaper.translate(new String(NUMERIC_ENTITY_ESCAPED), 0, writer);
        assertEquals(NUMERIC_ENTITY_UNESCAPED.length(), translationCount);
        assertEquals(NUMERIC_ENTITY_UNESCAPED, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testTranslateInvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String invalidEntity = "&# %#####";
        int translationCount = unescaper.translate(new String(invalidEntity.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String invalidEntity = "&#[xABCDEF]";
        int translationCount = unescaper.translate(new String(NUMERIC_ENTITY_ESCAPED), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test
    public void testTranslateWithSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String entityWithSemicolon = "&#[x1F000];";
        int translationCount = unescaper.translate(new String(entityWithSemicolon.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(1, translationCount);
        assertEquals("\uD800", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testTranslateWithMissingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String entityWithoutSemicolon = "&#[x1F000]";
        int translationCount = unescaper.translate(new String(entityWithoutSemicolon.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test
    public void testTranslateWithInvalidCharacterBeforeNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String entityWithInvalidCharacterBeforeNumber = "&%#[x1F000];";
        int translationCount = unescaper.translate(new String(entityWithInvalidCharacterBeforeNumber.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test
    public void testTranslateWithInvalidCharacterAfterNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String entityWithInvalidCharacterAfterNumber = "&#[x1F000]%";
        int translationCount = unescaper.translate(new String(entityWithInvalidCharacterAfterNumber.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test
    public void testTranslateWithInvalidCharacterInNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        String entityWithInvalidCharacterInNumber = "&#[x1F0ABCDEF]";
        int translationCount = unescaper.translate(new String(entityWithInvalidCharacterInNumber.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, translationCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNegativeIndex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        int translationCount = unescaper.translate(new String(""), -1, writer);
        assertEquals(0, translationCount);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateOutOfBoundsIndex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Intended blank
            }

            @Override
            public void close() throws IOException {
                // Intended blank
            }
        };

        int translationCount = unescaper.translate(new String(""), 0, writer);
        assertEquals(0, translationCount);
    }
}