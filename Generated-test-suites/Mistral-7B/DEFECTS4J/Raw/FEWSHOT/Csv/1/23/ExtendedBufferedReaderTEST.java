package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadAndLineCounter() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        int ch;
        StringBuilder lineBuilder = new StringBuilder();
        String line;

        for (int i = 0; i < input.length(); i++) {
            ch = reader.read();
            lineBuilder.append((char) ch);
            if (ch == '\n') {
                line = lineBuilder.toString();
                assertEquals(i / input.length() + 1, reader.getLineNumber());
                assertEquals(line, reader.readLine());
                lineBuilder.setLength(0);
            }
        }

        ch = reader.read();
        assertNull(ch);
        assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        int lookAhead = reader.lookAhead();
        assertSame(input.charAt(0), (char) lookAhead);
        assertEquals(0, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertSame(input.charAt(1), (char) lookAhead);
        assertEquals(1, reader.getLineNumber());

        reader.readLine();

        lookAhead = reader.lookAhead();
        assertSame(input.charAt(3), (char) lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertSame(input.charAt(4), (char) lookAhead);
        assertEquals(3, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertNull(lookAhead);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        int ch = reader.read();
        int lastChar = reader.readAgain();
        assertEquals(ch, lastChar);

        ch = reader.read();
        lastChar = reader.readAgain();
        assertEquals(ch, lastChar);

        ch = '\n';
        reader.read();
        lastChar = reader.readAgain();
        assertNull(lastChar);

        ch = reader.read();
        lastChar = reader.readAgain();
        assertEquals(ch, lastChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(stream));

        assertEquals(-1, reader.getLineNumber());

        reader.readLine();
        assertEquals(1, reader.getLineNumber());

        reader.readLine();
        assertEquals(2, reader.getLineNumber());

        reader.readLine();
        assertEquals(3, reader.getLineNumber());

        reader.readLine();
        assertFalse(reader.hasNextLine());
        assertEquals(-1, reader.getLineNumber());
    }
}