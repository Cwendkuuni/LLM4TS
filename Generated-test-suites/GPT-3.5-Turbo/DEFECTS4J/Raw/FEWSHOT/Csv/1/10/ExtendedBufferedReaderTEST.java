
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        assertEquals('a', br.read());
        assertEquals('b', br.read());
        assertEquals('c', br.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read());
        reader.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        assertEquals('a', br.read());
        assertEquals('a', br.readAgain());
        assertEquals('b', br.read());
        reader.close();
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("abc\n123\r\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        char[] buf = new char[2];
        assertEquals(2, br.read(buf, 0, 2));
        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals(2, br.read(buf, 0, 2));
        assertEquals('c', buf[0]);
        assertEquals('\n', buf[1]);
        assertEquals(2, br.read(buf, 0, 2));
        assertEquals('1', buf[0]);
        assertEquals('2', buf[1]);
        assertEquals(-1, br.read(buf, 0, 2));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        reader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("abc\ndef\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        assertEquals("abc", br.readLine());
        assertEquals(1, br.getLineNumber());
        assertEquals('f', br.readAgain());
        assertEquals("def", br.readLine());
        assertEquals(2, br.getLineNumber());
        assertNull(br.readLine());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.readAgain());
        reader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        assertEquals('a', br.lookAhead());
        assertEquals('a', br.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.lookAhead());
        reader.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("abc\ndef\r\n");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        assertEquals(0, br.getLineNumber());
        br.readLine();
        assertEquals(1, br.getLineNumber());
        br.readLine();
        assertEquals(2, br.getLineNumber());
        br.readLine();
        assertEquals(-1, br.getLineNumber());
        reader.close();
    }

}
