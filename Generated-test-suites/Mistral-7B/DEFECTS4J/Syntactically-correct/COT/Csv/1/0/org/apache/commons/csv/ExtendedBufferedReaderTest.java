package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test constructor ExtendedBufferedReader
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testConstructor() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNotNull(reader);
    }

    /**
     * Test method read
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int result = reader.read();
        Assert.assertEquals('a', result);
    }

    /**
     * Test method readAgain
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\nc"));;
        reader.read();
        reader.read();
        int result = reader.readAgain();
        Assert.assertEquals('b', result);
    }

    /**
     * Test method read with empty char array
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadEmpty() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int result = reader.read(new char[0], 0, 0);
        Assert.assertEquals(0, result);
    }

    /**
     * Test method read with valid length
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadValidLength() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd"));
        char[] result = new char[3];
        int length = reader.read(result, 0, 3);
        Assert.assertEquals(3, length);
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c'}, result);
    }

    /**
     * Test method readLine
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\nc\rdef"));
        String line = reader.readLine();
        Assert.assertEquals("ab\nc", line);
        Assert.assertEquals('r', reader.readAgain());
    }

    /**
     * Test method lookAhead
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd"));
        int result = reader.lookAhead();
        Assert.assertEquals('a', result);
    }

    /**
     * Test method getLineNumber
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab\nc\rdef\r"));
        int result = reader.getLineNumber();
        Assert.assertEquals(3, result);
    }

    /**
     * Test method read with null char array
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullCharArray() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read(null, 0, 0);
    }

    /**
     * Test method read with negative offset
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        char[] result = new char[1];
        reader.read(result, -1, 1);
    }

    /**
     * Test method read with negative length
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        char[] result = new char[1];
        reader.read(result, 0, -1);
    }

    /**
     * Test method read with length greater than available data
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadGreaterLength() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        char[] result = new char[5];
        int length = reader.read(result, 0, 5);
        Assert.assertEquals(0, length);
    }

    /**
     * Test method readLine with empty line
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testReadLineEmptyLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        String line = reader.readLine();
        Assert.assertEquals(null, line);
    }

    /**
     * Test method readLine with null reader
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NullPointerException.class)
    public void testReadLineNullReader() throws Exception {
        ExtendedBufferedReader reader = null;
        String line = reader.readLine();
    }
}