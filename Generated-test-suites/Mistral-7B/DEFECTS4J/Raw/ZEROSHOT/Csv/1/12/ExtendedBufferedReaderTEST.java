package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ExtendedBufferedReader
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream("Line 1\r\nLine 2\r\nLine 3".getBytes());
        reader = new ExtendedBufferedReader(new InputStreamReader(is));
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        assertNull(reader.readLine());
        assertEquals(-1, reader.getLineNumber());
    }

    /**
     * Test read method
     */
    @Test
    public void testRead() throws IOException {
        int c = reader.read();
        assertEquals('L', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('i', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('n', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('e', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('\r', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('L', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('i', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('n', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('e', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('\r', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('L', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('i', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('n', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('g', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', (char) c);
        assertEquals(4, reader.getLineNumber());

        c = reader.read();
        assertEquals(-1, c);
    }

    /**
     * Test readAgain method
     */
    @Test
    public void testReadAgain() throws IOException {
        reader.mark(1);
        int c = reader.read();
        assertEquals('L', (char) c);
        assertEquals(1, reader.getLineNumber());

        int lastChar = reader.readAgain();
        assertEquals('L', (char) lastChar);
        assertEquals(1, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        assertEquals('i', (char) c);
        assertEquals(1, reader.getLineNumber());

        lastChar = reader.readAgain();
        assertEquals('i', (char) lastChar);
        assertEquals(1, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        assertEquals('n', (char) c);
        assertEquals(1, reader.getLineNumber());

        lastChar = reader.readAgain();
        assertEquals('n', (char) lastChar);
        assertEquals(1, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        assertEquals('\r', (char) c);
        assertEquals(2, reader.getLineNumber());

        lastChar = reader.readAgain();
        assertEquals('\r', (char) lastChar);
        assertEquals(2, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        assertEquals('L', (char) c);
        assertEquals(2, reader.getLineNumber());

        lastChar = reader.readAgain();
        assertEquals('L', (char) lastChar);
        assertEquals(2, reader.getLineNumber());
    }

    /**
     * Test readLine method
     */
    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("Line 1\r\n", line);
        assertEquals(1, reader.getLineNumber());

        line = reader.readLine();
        assertEquals("Line 2\r\n", line);
        assertEquals(2, reader.getLineNumber());

        line = reader.readLine();
        assertEquals("Line 3", line);
        assertEquals(3, reader.getLineNumber());

        line = reader.readLine();
        assertNull(line);
        assertEquals(3, reader.getLineNumber());
    }

    /**
     * Test lookAhead method
     */
    @Test
    public void testLookAhead() throws IOException {
        reader.mark(1);
        int c = reader.read();
        reader.reset();

        int lookAheadChar = reader.lookAhead();
        assertEquals('L', (char) lookAheadChar);
        assertEquals(1, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        reader.reset();

        lookAheadChar = reader.lookAhead();
        assertEquals('i', (char) lookAheadChar);
        assertEquals(1, reader.getLineNumber());

        reader.mark(1);
        c = reader.read();
        reader.reset();

        lookAheadChar = reader.lookAhead();
        assertEquals('n', (char) lookAheadChar);
        assertEquals(1, reader.getLineNumber());
    }

    /**
     * Test getLineNumber method
     */
    @Test
    public void testGetLineNumber() {
        assertEquals(-1, reader.getLineNumber());

        reader.readLine();
        assertEquals(1, reader.getLineNumber());

        reader.readLine();
        assertEquals(2, reader.getLineNumber());

        reader.readLine();
        assertEquals(3, reader.getLineNumber());

        reader.readLine();
        assertEquals(4, reader.getLineNumber());

        reader.readLine();
        assertEquals(4, reader.getLineNumber());
    }
}