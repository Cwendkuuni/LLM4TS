package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String testString = "Hello, world!";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c = ebr.read();
        Assert.assertEquals('H', c);

        c = ebr.read();
        Assert.assertEquals('e', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('o', c);

        c = ebr.read();
        Assert.assertEquals(',', c);

        c = ebr.read();
        Assert.assertEquals(' ', c);

        c = ebr.read();
        Assert.assertEquals('w', c);

        c = ebr.read();
        Assert.assertEquals('o', c);

        c = ebr.read();
        Assert.assertEquals('r', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('d', c);

        c = ebr.read();
        Assert.assertEquals('!', c);

        c = ebr.read();
        Assert.assertEquals(-1, c);

        ebr.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        String testString = "Hello, world!";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('H', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('e', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('l', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('l', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('o', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals(',', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals(' ', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('w', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('o', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('r', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('l', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('d', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals('!', c);

        c = ebr.read();
        c = ebr.readAgain();
        Assert.assertEquals(-1, c);

        ebr.close();
    }

    @Test
    public void testReadCharArray() throws IOException {
        String testString = "Hello, world!";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        char[] buf = new char[5];
        int len = ebr.read(buf, 0, 5);
        Assert.assertEquals(5, len);
        Assert.assertEquals('H', buf[0]);
        Assert.assertEquals('e', buf[1]);
        Assert.assertEquals('l', buf[2]);
        Assert.assertEquals('l', buf[3]);
        Assert.assertEquals('o', buf[4]);

        len = ebr.read(buf, 0, 5);
        Assert.assertEquals(5, len);
        Assert.assertEquals(',', buf[0]);
        Assert.assertEquals(' ', buf[1]);
        Assert.assertEquals('w', buf[2]);
        Assert.assertEquals('o', buf[3]);
        Assert.assertEquals('r', buf[4]);

        len = ebr.read(buf, 0, 5);
        Assert.assertEquals(2, len);
        Assert.assertEquals('l', buf[0]);
        Assert.assertEquals('d', buf[1]);

        len = ebr.read(buf, 0, 5);
        Assert.assertEquals(-1, len);

        ebr.close();
    }

    @Test
    public void testReadLine() throws IOException {
        String testString = "Hello, world!\nThis is a test";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        String line = ebr.readLine();
        Assert.assertEquals("Hello, world!", line);

        line = ebr.readLine();
        Assert.assertEquals("This is a test", line);

        line = ebr.readLine();
        Assert.assertNull(line);

        ebr.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        String testString = "Hello, world!";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c = ebr.lookAhead();
        Assert.assertEquals('H', c);

        c = ebr.read();
        Assert.assertEquals('H', c);

        c = ebr.lookAhead();
        Assert.assertEquals('e', c);

        c = ebr.read();
        Assert.assertEquals('e', c);

        c = ebr.lookAhead();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.lookAhead();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.lookAhead();
        Assert.assertEquals('o', c);

        c = ebr.read();
        Assert.assertEquals('o', c);

        c = ebr.lookAhead();
        Assert.assertEquals(',', c);

        c = ebr.read();
        Assert.assertEquals(',', c);

        c = ebr.lookAhead();
        Assert.assertEquals(' ', c);

        c = ebr.read();
        Assert.assertEquals(' ', c);

        c = ebr.lookAhead();
        Assert.assertEquals('w', c);

        c = ebr.read();
        Assert.assertEquals('w', c);

        c = ebr.lookAhead();
        Assert.assertEquals('o', c);

        c = ebr.read();
        Assert.assertEquals('o', c);

        c = ebr.lookAhead();
        Assert.assertEquals('r', c);

        c = ebr.read();
        Assert.assertEquals('r', c);

        c = ebr.lookAhead();
        Assert.assertEquals('l', c);

        c = ebr.read();
        Assert.assertEquals('l', c);

        c = ebr.lookAhead();
        Assert.assertEquals('d', c);

        c = ebr.read();
        Assert.assertEquals('d', c);

        c = ebr.lookAhead();
        Assert.assertEquals('!', c);

        c = ebr.read();
        Assert.assertEquals('!', c);

        c = ebr.lookAhead();
        Assert.assertEquals(-1, c);

        c = ebr.read();
        Assert.assertEquals(-1, c);

        ebr.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String testString = "Hello, world!\nThis is a test\n";
        Reader reader = new StringReader(testString);
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int lineNumber = ebr.getLineNumber();
        Assert.assertEquals(0, lineNumber);

        ebr.readLine();
        lineNumber = ebr.getLineNumber();
        Assert.assertEquals(1, lineNumber);

        ebr.readLine();
        lineNumber = ebr.getLineNumber();
        Assert.assertEquals(2, lineNumber);

        ebr.readLine();
        lineNumber = ebr.getLineNumber();
        Assert.assertEquals(2, lineNumber);

        ebr.close();
    }

}