package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                out.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };

        // Test single character entities
        char[] expected = new char[]{'&', '#', '1', ';', 1};
        char[] result = new char[5];
        int length = unescaper.translate(new String(expected), 0, writer);
        result = out.toByteArray();
        assertArrayEquals(expected, result, 0, 5);
        assertEquals(5, length);

        expected = new char[]{'&', '#', 'x', '4', '1', ';', ' ', 'a', ' ', 't', ' ', '&', '#', 'x', '3', 'A', ';', ' ', 'h', 'e', 'l', 'l', 'o', ';'};
        result = new char[21];
        length = unescaper.translate(new String(expected), 0, writer);
        assertArrayEquals(expected, result, 0, 21);
        assertEquals(21, length);

        // Test multi-byte character entities
        expected = new char[]{'&', '#', 'x', 'D', '8', '0', ';'};
        result = new char[6];
        length = unescaper.translate(new String(expected), 0, writer);
        assertArrayEquals(expected, result, 0, 6);
        assertEquals(6, length);

        // Test entities with semicolon missing
        expected = new char[]{'&', '#', 'x', 'D', '8', '0'};
        result = new char[5];
        length = unescaper.translate(new String(expected), 0, writer);
        assertArrayEquals(expected, result, 0, 5);
        assertEquals(5, length);

        // Test invalid entities
        expected = new char[]{'&', '#', 'x', 'X'};
        result = new char[3];
        length = unescaper.translate(new String(expected), 0, writer);
        assertTrue(length == 0 || result.length == 0);
    }
}