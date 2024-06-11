package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.io.CharArrayReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for ExtendedBufferedReader.
 */
public class ExtendedBufferedReaderTest {

    private CharArrayReader charArrayReader;
    private ExtendedBufferedReader extendedBufferedReader;

    @Before
    public void setUp() throws IOException {
        charArrayReader = new CharArrayReader(new char[]{'a', 'b', 'c', '\r', 'd', '\n', 'e', '\n', 'f', '\r', '\n'});
        extendedBufferedReader = new ExtendedBufferedReader(charArrayReader);
    }

    @After
    public void tearDown() throws IOException {
        extendedBufferedReader.close();
        charArrayReader.close();
    }

    /**
     * Test read method with normal character.
     */
    @Test
    public void testReadNormalCharacter() throws IOException {
        int readChar = extendedBufferedReader.read();
        Assert.assertEquals('a', readChar);
    }

    /**
     * Test read method with EOF character.
     */
    @Test
    public void testReadEndOfStream() throws IOException {
        for (int i = 0; i < 10; i++) {
            int readChar = extendedBufferedReader.read();
            Assert.assertNotEquals(-1, readChar);
        }
        int readChar = extendedBufferedReader.read();
        Assert.assertEquals(-1, readChar);
    }

    /**
     * Test readAgain method with normal character.
     */
    @Test
    public void testReadAgainNormalCharacter() throws IOException {
        int readChar = extendedBufferedReader.read();
        int readAgainChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(readChar, readAgainChar);
    }

    /**
     * Test readAgain method with EOF character.
     */
    @Test
    public void testReadAgainEndOfStream() throws IOException {
        int readChar = extendedBufferedReader.read();
        int readAgainChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(-1, readAgainChar);
    }

    /**
     * Test read method with various lengths and offsets.
     */
    @Test
    public void testReadVariousLengthAndOffset() throws IOException {
        char[] buffer = new char[4];
        int length = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, length);
        Assert.assertEquals('a', buffer[0]);
        Assert.assertEquals('b', buffer[1]);
        Assert.assertEquals('c', buffer[2]);
        Assert.assertEquals('\r', buffer[3]);
    }

    /**
     * Test readLine method with normal line.
     */
    @Test
    public void testReadLineNormalLine() throws IOException {
        String readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("abcd\r", readLine);
    }

    /**
     * Test readLine method with empty line.
     */
    @Test
    public void testReadLineEmptyLine() throws IOException {
        String readLine = extendedBufferedReader.readLine();
        Assert.assertEquals(null, readLine);
        Assert.assertEquals(-1, extendedBufferedReader.read());
        String readLine2 = extendedBufferedReader.readLine();
        Assert.assertEquals("ef\r\n", readLine2);
    }

    /**
     * Test readLine method with multiple lines.
     */
    @Test
    public void testReadLineMultipleLines() throws IOException {
        String readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("abcd\r", readLine);
        String readLine2 = extendedBufferedReader.readLine();
        Assert.assertEquals("ef\r\n", readLine2);
    }

    /**
     * Test lookAhead method with normal character.
     */
    @Test
    public void testLookAheadNormalCharacter() throws IOException {
        int lookAheadChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('a', lookAheadChar);
        int readChar = extendedBufferedReader.read();
        Assert.assertEquals('a', readChar);
    }

    /**
     * Test lookAhead method with EOF character.
     */
    @Test
    public void testLookAheadEndOfStream() throws IOException {
        int lookAheadChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals(-1, lookAheadChar);
    }

    /**
     * Test getLineNumber method.
     */
    @Test
    public void testGetLineNumber() throws IOException {
        int lineNumber = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(0, lineNumber);
        String readLine = extendedBufferedReader.readLine();
        Assert.assertEquals("abcd\r", readLine);
        int lineNumber2 = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(1, lineNumber2);
        String readLine2 = extendedBufferedReader.readLine();
        Assert.assertEquals("ef\r\n", readLine2);
        int lineNumber3 = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(-1, lineNumber3);
    }
}