package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        int actual = reader.read();
        int expected = 'H';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'e';
        assertEquals(expected, actual);
        // read next line
        actual = reader.read();
        expected = 'W';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'o';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = '\n';
        assertEquals(expected, actual);
        actual = reader.read(); // read next line
        expected = 'W';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'o';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'r';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'l';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'd';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = '\n';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        int actual = reader.read();
        int expected = 'H';
        assertEquals(expected, actual);
        actual = reader.readAgain();
        expected = 'H';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'e';
        assertEquals(expected, actual);
        actual = reader.readAgain();
        expected = 'e';
        assertEquals(expected, actual);
        // read next line
        actual = reader.read();
        expected = 'W';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'o';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'r';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'l';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'd';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = '\n';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = -1;
        assertEquals(expected, actual);
        actual = reader.readAgain();
        expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        String actual = reader.readLine();
        String expected = "Hello";
        assertEquals(expected, actual);
        actual = reader.readLine();
        expected = "World";
        assertEquals(expected, actual);
        actual = reader.readLine();
        expected = null;
        assertEquals(expected, actual);
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        int actual = reader.lookAhead();
        int expected = 'H';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'H';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'e';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'e';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'W';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'W';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'o';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'o';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'r';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'r';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'l';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'l';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = 'd';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = 'd';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = '\n';
        assertEquals(expected, actual);
        actual = reader.read();
        expected = '\n';
        assertEquals(expected, actual);
        actual = reader.lookAhead();
        expected = -1;
        assertEquals(expected, actual);
        actual = reader.read();
        expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
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
        expected = -1;
        assertEquals(expected, actual);
    }
}