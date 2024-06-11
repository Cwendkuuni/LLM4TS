package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.DefaultCSVParser;
import org.apache.commons.csv.TextPosition;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
    }

    // Test for constructor
    @Test
    public void testExtendedBufferedReader() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        assertNotNull(reader);
    }

    // Test for read method
    @Test
    public void testRead() throws IOException {
        outputStream.write('A');
        int result = reader.read();
        assertEquals('A', result);
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        reader.close();
        int result = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    // Test for readAgain method
    @Test
    public void testReadAgain() throws IOException {
        outputStream.write('A');
        outputStream.write('B');
        reader.read();
        reader.read();
        int result = reader.readAgain();
        assertEquals('B', result);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAgainBeforeRead() throws IOException {
        int result = reader.readAgain();
    }

    // Test for read method with array
    @Test
    public void testReadArray() throws IOException {
        outputStream.write('A');
        outputStream.write('B');
        outputStream.write('C');
        char[] buf = new char[3];
        int length = reader.read(buf, 0, buf.length);
        assertEquals(3, length);
        assertEquals('A', buf[0]);
        assertEquals('B', buf[1]);
        assertEquals('C', buf[2]);
    }

    @Test
    public void testReadArrayWithLessCharsThanBufferSize() throws IOException {
        outputStream.write('A');
        outputStream.write('B');
        char[] buf = new char[5];
        int length = reader.read(buf, 0, buf.length);
        assertEquals(2, length);
        assertEquals('A', buf[0]);
        assertEquals('B', buf[1]);
    }

    @Test
    public void testReadArrayWithEndOfStream() throws IOException {
        reader.close();
        char[] buf = new char[1];
        int length = reader.read(buf, 0, buf.length);
        assertEquals(0, length);
    }

    // Test for readLine method
    @Test
    public void testReadLine() throws IOException {
        outputStream.write("This is a test".getBytes());
        String result = reader.readLine();
        assertEquals("This is a test", result);
    }

    @Test
    public void testReadLineEndOfStream() throws IOException {
        reader.close();
        String result = reader.readLine();
        assertNull(result);
    }

    @Test
    public void testReadLineWithBackslashN() throws IOException {
        outputStream.write("This is a test\n".getBytes());
        String result = reader.readLine();
        assertEquals("This is a test", result);
        assertEquals('\n', reader.lastChar);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithBackslashNR() throws IOException {
        outputStream.write("This is a test\r".getBytes());
        String result = reader.readLine();
        assertEquals("This is a test", result);
        assertEquals('\r', reader.lastChar);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithBackslashNRandN() throws IOException {
        outputStream.write("This is a test\r\n".getBytes());
        String result = reader.readLine();
        assertEquals("This is a test", result);
        assertEquals('\r', reader.lastChar);
        assertEquals(1, reader.getLineNumber());
        result = reader.readLine();
        assertEquals("", result);
        assertEquals('\n', reader.lastChar);
        assertEquals(2, reader.getLineNumber());
    }

    // Test for lookAhead method
    @Test
    public void testLookAhead() throws IOException {
        outputStream.write('A');
        int result = reader.lookAhead();
        assertEquals('A', result);
    }

    @Test
    public void testLookAheadEndOfStream() throws IOException {
        reader.close();
        int result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    // Test for getLineNumber method
    @Test
    public void testGetLineNumber() throws IOException {
        outputStream.write("This is a test\n".getBytes());
        reader.readLine();
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(3, result);
    }

    @Test
    public void testGetLineNumberAfterClosedReader() throws IOException {
        reader.close();
        int result = reader.getLineNumber();
        assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testPeekBeforeOpenReader() throws IOException {
        reader.peek();
    }

    @Test(expected = IOException.class)
    public void testReadBeforeOpenReader() throws IOException {
        reader.read();
    }

    @Test(expected = IOException.class)
    public void testReadLineBeforeOpenReader() throws IOException {
        reader.readLine();
    }

    @Test(expected = IOException.class)
    public void testLookAheadBeforeOpenReader() throws IOException {
        reader.lookAhead();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() throws IOException {
        reader.read(new char[0], 3, -1);
    }
}