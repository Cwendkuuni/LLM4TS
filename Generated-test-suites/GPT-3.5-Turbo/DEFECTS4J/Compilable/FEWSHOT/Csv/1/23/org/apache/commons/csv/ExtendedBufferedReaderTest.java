
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('h', bufferedReader.read());
        assertEquals('e', bufferedReader.read());
        assertEquals('l', bufferedReader.read());
        assertEquals('l', bufferedReader.read());
        assertEquals('o', bufferedReader.read());

        bufferedReader.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        bufferedReader.read();
        bufferedReader.read();
        bufferedReader.read();
        bufferedReader.read();
        bufferedReader.read();

        assertEquals(-1, bufferedReader.readAgain());

        bufferedReader.close();
    }

    @Test
    public void testReadArray() throws IOException {
        Reader reader = new StringReader("hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] buffer = new char[5];

        bufferedReader.read(buffer, 0, 5);

        assertEquals('h', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);

        bufferedReader.close();
    }

    @Test
    public void testReadAgainArray() throws IOException {
        Reader reader = new StringReader("hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] buffer = new char[5];

        bufferedReader.read(buffer, 0, 1);
        bufferedReader.read(buffer, 1, 1);
        bufferedReader.read(buffer, 2, 1);
        bufferedReader.read(buffer, 3, 1);
        bufferedReader.read(buffer, 4, 1);

        assertEquals(-1, bufferedReader.readAgain());

        bufferedReader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        assertEquals("hello", bufferedReader.readLine());
        assertEquals("world", bufferedReader.readLine());

        bufferedReader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("hello");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        assertEquals('e', bufferedReader.lookAhead());

        bufferedReader.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        bufferedReader.readLine();
        assertEquals(1, bufferedReader.getLineNumber());

        bufferedReader.readLine();
        assertEquals(2, bufferedReader.getLineNumber());

        bufferedReader.close();
    }
}
