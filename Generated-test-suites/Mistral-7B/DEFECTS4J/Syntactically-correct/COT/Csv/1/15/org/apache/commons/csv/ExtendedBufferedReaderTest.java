package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testConstruct() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test123\ntest456"));
        int actual = reader.read();
        Assert.assertEquals(49, actual); // '5' ASCII value
        int actual2 = reader.read();
        Assert.assertEquals(82, actual2); // 'R' ASCII value
        int actual3 = reader.read();
        Assert.assertEquals(10, actual3); // '\n' ASCII value
        int actual4 = reader.read();
        Assert.assertEquals(-1, actual4); // End of stream

        Assert.assertEquals(4, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test123\ntest456"));
        reader.read();
        reader.read();
        reader.read();
        int actual = reader.readAgain();
        Assert.assertEquals(10, actual); // '\n' ASCII value

        reader.mark(1);
        reader.read();
        Assert.assertEquals(56, reader.read()); // 'V' ASCII value
        reader.reset();
        Assert.assertEquals(10, reader.readAgain()); // '\n' ASCII value
    }

    @Test
    public void testReadArray() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test123\ntest456"));
        char[] buffer = new char[3];
        int actual = reader.read(buffer, 0, 3);
        Assert.assertEquals(3, actual);
        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('s', buffer[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArrayNegativeLength() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test123\ntest456"));
        reader.read(new char[0], 0, -1);
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test1\n test2\n test3\n"));
        String actual = reader.readLine();
        Assert.assertEquals("test1", actual);
        Assert.assertEquals(2, reader.getLineNumber());

        actual = reader.readLine();
        Assert.assertEquals(" test2", actual);
        Assert.assertEquals(3, reader.getLineNumber());

        actual = reader.readLine();
        Assert.assertEquals(" test3", actual);
        Assert.assertEquals(4, reader.getLineNumber());

        actual = reader.readLine();
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("test123\ntest456"));
        int actual = reader.lookAhead();
        Assert.assertEquals(-1, actual); // End of stream marker

        reader.mark(1);
        actual = reader.lookAhead();
        reader.reset();
        Assert.assertEquals(84, actual); // 'T' ASCII value

        reader.mark(1);
        reader.read();
        Assert.assertEquals(69, reader.lookAhead()); // 'E' ASCII value
        reader.reset();
    }
}