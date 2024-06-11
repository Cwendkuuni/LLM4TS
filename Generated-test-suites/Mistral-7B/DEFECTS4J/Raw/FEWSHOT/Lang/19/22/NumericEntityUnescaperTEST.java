package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateBasic() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();
        char[] result = new char[1];

        // Test case for single character entity
        String input = "&amp;";
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals(1, length);
        assertEquals('&', result[0]);

        // Test case for single character entity with semicolon
        input = "&amp;";
        result = new char[2];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(1, length);
        assertEquals('&', result[0]);
        assertEquals('\u0000', result[1]);

        // Test case for multiple character entity
        input = "&apos;";
        result = new char[2];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(1, length);
        assertEquals('\'' , result[0]);
        assertEquals('\u0000', result[1]);

        // Test case for multiple character entity with semicolon
        input = "&apos;";
        result = new char[3];
        length = unescaper.translate(new String(input), 0, out);
        assertEquals(2, length);
        assertEquals('\'' , result[0]);
        assertEquals('\u0000', result[1]);
        assertEquals('\u0000', result[2]);

        // Test case for hexidecimal entity
        input = "&hex;#x41;";
        result = new char[2];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(1, length);
        assertEquals('A' , result[0]);
        assertEquals('\u0000', result[1]);

        // Test case for hexidecimal entity with semicolon
        input = "&hex;#x41;";
        result = new char[3];
        length = unescaper.translate(new String(input), 0, out);
        assertEquals(2, length);
        assertEquals('A' , result[0]);
        assertEquals('\u0000', result[1]);
        assertEquals('\u0000', result[2]);

        // Test case for hexidecimal entity with leading zeros
        input = "&hex;#x0A";
        result = new char[2];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(1, length);
        assertEquals('\n' , result[0]);
        assertEquals('\u0000', result[1]);

        // Test case for hexidecimal entity with semicolon and leading zeros
        input = "&hex;#x0A;";
        result = new char[3];
        length = unescaper.translate(new String(input), 0, out);
        assertEquals(2, length);
        assertEquals('\n' , result[0]);
        assertEquals('\u0000', result[1]);
        assertEquals('\u0000', result[2]);

        // Test case for invalid hexidecimal entity
        input = "&hex;#xaa;";
        result = new char[1];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(0, length);
        assertEquals('\u0000', result[0]);

        // Test case for invalid decimal entity
        input = "&dec;#1000000;";
        result = new char[1];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(0, length);
        assertEquals('\u0000', result[0]);

        // Test case for invalid decimal entity with no semicolon
        input = "&dec;#1000000";
        result = new char[1];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(0, length);
        assertEquals('\u0000', result[0]);

        // Test case for empty entity
        input = "&nbsp;";
        result = new char[1];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(0, length);
        assertEquals('\u0000', result[0]);

        // Test case for empty entity with semicolon
        input = "&nbsp;";
        result = new char[2];
        length = unescaper.translate(new String(input), 0, out);
        assertEquals(1, length);
        assertEquals('\u0000', result[0]);
        assertEquals('\u0000', result[1]);
    }

    @Test
    public void testTranslateComplex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();
        char[] result = new char[10];

        // Test case for multiple entities in one sequence
        String input = "&amp;#41;&#x42;&#x1F6;#x01F4;";
        int length = unescaper.translate(new String(input), 0, out);
        assertEquals(6, length);
        assertArrayEquals(new char[]{'&', 'A', '\u0022', '\u00E2', '\u00F1', '\u00F4'}, result, 0, 6);

        // Test case for entities with multiple characters
        input = "&apos;&apos;&apos;&apos;";
        result = new char[4];
        length = unescaper.translate(new String(input), 0, new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                result[0] = (char) c;
            }

            @Override
            public void flush() throws java.io.IOException {
            }

            @Override
            public void close() throws java.io.IOException {
            }
        });
        assertEquals(4, length);
        assertArrayEquals(new char[]{'\'', '\'', '\'', '\''}, result, 0, 4);
    }

    @Test
    public void testTranslateWithNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        // Test case for null input
        char[] result = new char[1];
        int length = unescaper.translate(null, 0, out);
        assertNull(result);
        assertEquals(0, length);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream();

        // Test case for empty input
        char[] result = new char[1];
        int length = unescaper.translate("", 0, out);
        assertEquals(0, length);
        assertNull(result);
    }
}