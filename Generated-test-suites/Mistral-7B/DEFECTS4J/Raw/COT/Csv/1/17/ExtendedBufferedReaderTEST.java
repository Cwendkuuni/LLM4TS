package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test constructor ExtendedBufferedReader
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testExtendedBufferedReader() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        assertNotNull(ebr);
    }

    /**
     * Test method read
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\n".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int result = ebr.read();
        assertEquals('T', result);

        result = ebr.read();
        assertEquals('e', result);

        result = ebr.read();
        assertEquals('s', result);

        result = ebr.read();
        assertEquals('\n', result);

        result = ebr.read();
        assertEquals(-1, result);

        result = ebr.read();
        assertEquals(-1, result);
    }

    /**
     * Test method readAgain
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\n".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int result = ebr.read();
        int lastChar = ebr.readAgain();
        assertEquals('T', result);
        assertEquals('T', lastChar);

        result = ebr.read();
        lastChar = ebr.readAgain();
        assertEquals('e', result);
        assertEquals('e', lastChar);

        result = ebr.read();
        lastChar = ebr.readAgain();
        assertEquals('s', result);
        assertEquals('s', lastChar);

        result = ebr.read();
        lastChar = ebr.readAgain();
        assertEquals('\n', result);
        assertEquals('\n', lastChar);

        result = ebr.read();
        lastChar = ebr.readAgain();
        assertEquals(-1, result);
        assertEquals(-1, lastChar);
    }

    /**
     * Test method read
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReadArray() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\n".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        char[] buf = new char[3];
        int result = ebr.read(buf, 0, 3);
        assertEquals(3, result);
        assertEquals('T', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('s', buf[2]);

        result = ebr.read(buf, 0, 1);
        assertEquals('n', buf[0]);
        assertEquals(-1, result);
    }

    /**
     * Test method readLine
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\nTest2\n".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        String line1 = ebr.readLine();
        assertEquals("Test", line1);
        assertEquals(1, ebr.getLineNumber());

        String line2 = ebr.readLine();
        assertEquals("Test2", line2);
        assertEquals(2, ebr.getLineNumber());

        String line3 = ebr.readLine();
        assertNull(line3);
        assertEquals(-1, ebr.getLineNumber());
    }

    /**
     * Test method lookAhead
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\nTest2".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int lookAhead = ebr.lookAhead();
        assertEquals('T', lookAhead);

        lookAhead = ebr.lookAhead();
        assertEquals('e', lookAhead);

        lookAhead = ebr.lookAhead();
        assertEquals('s', lookAhead);

        lookAhead = ebr.lookAhead();
        assertEquals('\n', lookAhead);

        lookAhead = ebr.lookAhead();
        assertEquals('T', lookAhead);
    }

    /**
     * Test method getLineNumber
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("Test\nTest2\n".getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int lineNumber = ebr.getLineNumber();
        assertEquals(0, lineNumber);

        String line1 = ebr.readLine();
        assertEquals("Test", line1);
        assertEquals(1, ebr.getLineNumber());

        String line2 = ebr.readLine();
        assertEquals("Test2", line2);
        assertEquals(2, ebr.getLineNumber());

        String line3 = ebr.readLine();
        assertNull(line3);
        assertEquals(-1, ebr.getLineNumber());

        String line4 = ebr.readLine();
        assertNull(line4);
        assertEquals(-1, ebr.getLineNumber());
    }

}