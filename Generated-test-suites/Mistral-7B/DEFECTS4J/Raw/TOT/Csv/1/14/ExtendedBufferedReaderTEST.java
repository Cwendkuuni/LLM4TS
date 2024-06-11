package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * ExtendedBufferedReader Test
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test read method
     * Read a single character from the reader and check if the returned value is correct
     * and if the line counter is incremented
     */
    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        Assert.assertEquals('H', reader.read());
        Assert.assertEquals(1, reader.getLineNumber());

        Assert.assertEquals('W', reader.read());
        Assert.assertEquals(1, reader.getLineNumber());

        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(2, reader.getLineNumber());

        Assert.assertEquals('World', reader.readLine());
        Assert.assertEquals(3, reader.getLineNumber());

        Assert.assertEquals(END_OF_STREAM, reader.read());
        Assert.assertEquals(3, reader.getLineNumber());
    }

    /**
     * Test readAgain method
     * Read a single character, save it for later, and check if the saved value is the same as the next character read
     */
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        // read the first character
        int firstChar = extendedReader.read();
        int expectedNextChar = 'E';

        // save the first character for later comparison
        int savedChar = extendedReader.readAgain();

        // check if the saved character is the same as the next character
        int nextChar = extendedReader.read();
        Assert.assertEquals(expectedNextChar, nextChar);
        Assert.assertEquals(savedChar, expectedNextChar);
    }

    /**
     * Test read method with different line breaks
     * Test if the read method can handle different line terminator combinations (CRLF, LF, CR)
     */
    @Test
    public void testReadWithDifferentLineBreaks() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld\r\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String line1 = extendedReader.readLine();
        Assert.assertEquals("Hello", line1);
        Assert.assertEquals(1, extendedReader.getLineNumber());

        String line2 = extendedReader.readLine();
        Assert.assertEquals("World", line2);
        Assert.assertEquals(2, extendedReader.getLineNumber());

        int lastChar = extendedReader.read();
        Assert.assertEquals(END_OF_STREAM, lastChar);
        Assert.assertEquals(2, extendedReader.getLineNumber());
    }

    /**
     * Test read method with empty line
     * Test if the read method can handle empty lines
     */
    @Test
    public void testReadWithEmptyLine() throws IOException {
        StringReader reader = new StringReader("\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String line = extendedReader.readLine();
        Assert.assertNull(line);
        Assert.assertEquals(0, extendedReader.getLineNumber());

        int lastChar = extendedReader.read();
        Assert.assertEquals(END_OF_STREAM, lastChar);
        Assert.assertEquals(0, extendedReader.getLineNumber());
    }

    /**
     * Test lookAhead method
     * Test if the lookAhead method can peek at the next character without consuming it
     */
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello World!");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int lookAheadChar = extendedReader.lookAhead();
        Assert.assertEquals('H', lookAheadChar);

        String line1 = extendedReader.readLine();
        Assert.assertEquals("Hello", line1);
        Assert.assertEquals(1, extendedReader.getLineNumber());

        int lookAheadChar2 = extendedReader.lookAhead();
        Assert.assertEquals('W', lookAheadChar2);

        String line2 = extendedReader.readLine();
        Assert.assertEquals("World!", line2);
        Assert.assertEquals(2, extendedReader.getLineNumber());

        int lastChar = extendedReader.read();
        Assert.assertEquals(END_OF_STREAM, lastChar);
        Assert.assertEquals(2, extendedReader.getLineNumber());
    }

    /**
     * Test read method with invalid input
     * Test if the read method can handle invalid input
     */
    @Test(expected = IOException.class)
    public void testReadWithInvalidInput() throws IOException {
        StringReader reader = new StringReader("Hello\0World");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        // read the first character
        int firstChar = extendedReader.read();
        Assert.assertEquals('H', firstChar);

        // read the next character, should throw an IOException
        extendedReader.read();
    }

    /**
     * Test readLine method with null input
     * Test if the readLine method can handle null input
     */
    @Test(expected = NullPointerException.class)
    public void testReadLineWithNullInput() {
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(null);
        extendedReader.readLine();
    }
}