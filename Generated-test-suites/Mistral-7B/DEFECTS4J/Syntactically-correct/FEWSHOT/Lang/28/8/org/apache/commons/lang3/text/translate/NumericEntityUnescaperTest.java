package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidEntity() throws IOException {
        CharSequence input = "&amp;#10; &#x61; &#x41;foo &#x3C3; &#x03B4;bar &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(15, length);
    }

    @Test
    public void testTranslate_ValidEntityWithHex() throws IOException {
        CharSequence input = "&amp;#x41; &#x42; &#x61; &#x3C3; &#x03B4; &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("ABfoóπbar Å", output.toString());
        assertEquals(15, length);
    }

    @Test
    public void testTranslate_ValidEntityWithSingleDigit() throws IOException {
        CharSequence input = "&amp;#3; &#x3C3; &#x03B4;bar &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("3πbar Å", output.toString());
        assertEquals(7, length);
    }

    @Test
    public void testTranslate_EmptyInput() throws IOException {
        CharSequence input = "";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("", output.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_NullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(null, 0, mock(Writer.class));
        });
    }

    @Test
    public void testTranslate_NullWriter() {
        CharSequence input = "&amp;#10;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(input, 0, null);
        });
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        CharSequence input = "&amp;#Invalid;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("", output.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_EntityWithInvalidCharacterAfterHash() throws IOException {
        CharSequence input = "&amp;#10; &#x61x; &#x41; &#x3C3; &#x03B4;bar &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithInvalidCharacterBeforeHash() throws IOException {
        CharSequence input = "&ampx;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithTrailingHash() throws IOException {
        CharSequence input = "&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar &#xA7;&#x3B;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithTrailingHashAndExtraCharacters() throws IOException {
        CharSequence input = "&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar &#xA7;&#x3B;x";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithLeadingHash() throws IOException {
        CharSequence input = "#&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar &#xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithLeadingHashAndExtraCharacters() throws IOException {
        CharSequence input = "#&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar &#xA7;x";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithLeadingAndTrailingHashes() throws IOException {
        CharSequence input = "##&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar ##&xA7;";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }

    @Test
    public void testTranslate_EntityWithLeadingAndTrailingHashesAndExtraCharacters() throws IOException {
        CharSequence input = "##&amp;#10; &#x61; &#x41; &#x3C3; &#x03B4;bar ##&xA7;x";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);

        assertEquals("amfoóπbar Å", output.toString());
        assertEquals(14, length);
    }
}