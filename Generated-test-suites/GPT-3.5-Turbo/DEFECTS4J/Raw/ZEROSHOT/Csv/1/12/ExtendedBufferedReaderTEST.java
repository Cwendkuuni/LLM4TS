package org.apache.commons.csv;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        int actual = bufferedReader.read();
        int expected = 'H';
        assertEquals(expected, actual);

        actual = bufferedReader.read();
        expected = 'e';
        assertEquals(expected, actual);

        actual = bufferedReader.readAgain();
        expected = 'e';
        assertEquals(expected, actual);

        bufferedReader.close();
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        char[] buf = new char[5];
        int len = bufferedReader.read(buf, 0, 5);

        String actual = new String(buf, 0, len);
        String expected = "Hello";
        assertEquals(expected, actual);

        bufferedReader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        String actual = bufferedReader.readLine();
        String expected = "Hello";
        assertEquals(expected, actual);

        actual = bufferedReader.readLine();
        expected = "World";
        assertEquals(expected, actual);

        bufferedReader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        int actual = bufferedReader.lookAhead();
        int expected = 'H';
        assertEquals(expected, actual);

        actual = bufferedReader.read();
        expected = 'H';
        assertEquals(expected, actual);

        bufferedReader.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        int actual = bufferedReader.getLineNumber();
        int expected = 0;
        assertEquals(expected, actual);

        bufferedReader.readLine();

        actual = bufferedReader.getLineNumber();
        expected = 1;
        assertEquals(expected, actual);

        bufferedReader.close();
    }
}