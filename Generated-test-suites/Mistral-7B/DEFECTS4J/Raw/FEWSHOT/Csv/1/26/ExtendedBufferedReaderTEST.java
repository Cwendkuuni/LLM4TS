package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int c;
        for (int i = 0; i < 3; i++) {
            c = reader.read();
            assertEquals(i < 2 ? 'f' : '\n', c);
        }
        assertEquals('\n', reader.read());
        assertEquals('\r', reader.read());
        assertNull(reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        reader.read();
        reader.read();
        reader.read();
        reader.read();

        int c = reader.readAgain();
        assertEquals('f', c);

        c = reader.readAgain();
        assertEquals('f', c);

        c = reader.readAgain();
        assertEquals('b', c);

        c = reader.readAgain();
        assertEquals('\n', c);

        c = reader.readAgain();
        assertEquals('\r', c);

        c = reader.readAgain();
        assertNull(c);
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        String line1 = reader.readLine();
        assertEquals("foo", line1);
        assertEquals('f', reader.readAgain());

        String line2 = reader.readLine();
        assertEquals("bar", line2);
        assertEquals('\n', reader.readAgain());
        assertEquals('\r', reader.readAgain());
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int expected = 'f';
        int actual;

        reader.mark(1);
        actual = reader.read();
        reader.reset();
        assertEquals(expected, actual);

        expected = 'b';
        reader.mark(1);
        actual = reader.read();
        reader.read(); // consume \n
        reader.read(); // consume b
        reader.reset();
        assertEquals(expected, actual);

        expected = '\n';
        reader.mark(1);
        actual = reader.read();
        reader.read(); // consume \n
        reader.read(); // consume b
        reader.read(); // consume \r
        reader.read(); // consume \n
        reader.reset();
        assertEquals(expected, actual);

        expected = '\r';
        reader.mark(1);
        actual = reader.read();
        reader.read(); // consume \n
        reader.read(); // consume b
        reader.read(); // consume \r
        reader.read(); // consume \n
        reader.reset();
        assertEquals(expected, actual);

        assertNull(reader.read());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(2, reader.getLineNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeMark() throws IOException {
        String input = "foo\nbar\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        reader.mark(-1);
    }
}