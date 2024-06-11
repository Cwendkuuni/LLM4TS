package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "abcdef\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int actual = reader.read();
        int expected = 'a';
        assertEquals(expected, actual);

        actual = reader.read();
        expected = 'b';
        assertEquals(expected, actual);

        actual = reader.read();
        expected = 'c';
        assertEquals(expected, actual);

        actual = reader.read();
        expected = '\n';
        assertEquals(expected, actual);

        actual = reader.read();
        assertNull(actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "abc\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int actual = reader.read();
        int expected = 'a';
        assertEquals(expected, actual);

        actual = reader.readAgain();
        assertEquals(expected, actual);

        actual = reader.read();
        expected = 'b';
        assertEquals(expected, actual);

        actual = reader.readAgain();
        assertEquals(expected, actual);

        actual = reader.read();
        assertEquals('c', actual);

        actual = reader.read();
        assertNull(actual);
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "line1\nline2\nline3\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        String actual = reader.readLine();
        String expected = "line1";
        assertEquals(expected, actual);
        assertEquals('n', reader.read()); // consume \n

        actual = reader.readLine();
        expected = "line2";
        assertEquals(expected, actual);
        assertEquals('n', reader.read()); // consume \n

        actual = reader.readLine();
        expected = "line3";
        assertEquals(expected, actual);
        assertEquals('n', reader.read()); // consume \n

        actual = reader.readLine();
        assertNull(actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "abc\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int actual = reader.lookAhead();
        int expected = 'a';
        assertEquals(expected, actual);

        reader.read();

        actual = reader.lookAhead();
        expected = 'b';
        assertEquals(expected, actual);

        reader.read();
        reader.read();

        actual = reader.lookAhead();
        assertNull(actual);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "line1\nline2\nline3\n";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));

        int actual = reader.getLineNumber();
        int expected = 0;
        assertEquals(expected, actual);

        reader.readLine();

        actual = reader.getLineNumber();
        expected = 1;
        assertEquals(expected, actual);

        reader.readLine();

        actual = reader.getLineNumber();
        expected = 2;
        assertEquals(expected, actual);

        reader.readLine();

        actual = reader.getLineNumber();
        assertNull(actual);
    }
}