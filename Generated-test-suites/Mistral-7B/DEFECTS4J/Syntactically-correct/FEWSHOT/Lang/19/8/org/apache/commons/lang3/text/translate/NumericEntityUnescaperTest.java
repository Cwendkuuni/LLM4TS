package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidEntities_Hex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        String input = "&x#{0x1F}abc&x#{0xFFFF}def";
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            int result = unescaper.translate(new String(input.toCharArray()), index, writer);
            index += result;
            assertEquals(input.charAt(index - result), writer.toString().charAt(0));
            writer.reset();
        }

        assertEquals(input.length(), index);
    }

    @Test
    public void testTranslate_ValidEntities_Dec() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        String input = "&#{0x1F}&#{100}&#{255}&#{65536}";
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            int result = unescaper.translate(new String(input.toCharArray()), index, writer);
            index += result;
            assertEquals(input.charAt(index - result), writer.toString().charAt(0));
            writer.reset();
        }

        assertEquals(input.length(), index);
    }

    @Test
    public void testTranslate_MissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new Writer() {
                @Override
                public void write(int c) throws IOException {
                    outputStream.write(c);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            };

            String input = "&x#{0x1F}";
            int index = 0;
            int length = input.length();

            for (int i = 0; i < length; i++) {
                unescaper.translate(new String(input.toCharArray()), index, writer);
                index++;
            }
        });
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NumberFormatException.class, () -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new Writer() {
                @Override
                public void write(int c) throws IOException {
                    outputStream.write(c);
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            };

            String input = "&foo";
            int index = 0;
            int length = input.length();

            for (int i = 0; i < length; i++) {
                unescaper.translate(new String(input.toCharArray()), index, writer);
                index++;
            }
        });
    }

    @Test
    public void testTranslate_EmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        String input = "";
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            int result = unescaper.translate(new String(input.toCharArray()), index, writer);
            index += result;
            assertNull(writer.toString());
        }

        assertEquals(input.length(), index);
    }

    @Test
    public void testTranslate_NullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertNull(unescaper.translate(null, 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        }));
    }
}