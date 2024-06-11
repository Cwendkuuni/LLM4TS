package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        // Create a test reader with sample input
        StringReader reader = new StringReader("Hello, World!");

        // Create an extended buffered reader using the test reader
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        // Call the read method and check the returned value
        Assert.assertEquals('H', br.read());

        // Close the buffered reader
        br.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        // Create a test reader with sample input
        StringReader reader = new StringReader("Hello, World!");

        // Create an extended buffered reader using the test reader
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        // Create a buffer to store the read characters
        char[] buffer = new char[5];

        // Call the read method with the buffer and check the returned value
        Assert.assertEquals(5, br.read(buffer, 0, 5));

        // Close the buffered reader
        br.close();
    }

    @Test
    public void testReadLine() throws IOException {
        // Create a test reader with sample input
        StringReader reader = new StringReader("Hello\nWorld\n");

        // Create an extended buffered reader using the test reader
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        // Call the readLine method and check the returned value
        Assert.assertEquals("Hello", br.readLine());

        // Close the buffered reader
        br.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        // Create a test reader with sample input
        StringReader reader = new StringReader("Hello, World!");

        // Create an extended buffered reader using the test reader
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        // Call the lookAhead method and check the returned value
        Assert.assertEquals('e', br.lookAhead());

        // Close the buffered reader
        br.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        // Create a test reader with sample input
        StringReader reader = new StringReader("Hello\nWorld");

        // Create an extended buffered reader using the test reader
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        // Call the getLineNumber method and check the returned value
        Assert.assertEquals(0, br.getLineNumber());

        // Call the readLine method to increment the line number
        br.readLine();

        // Call the getLineNumber method again and check the returned value
        Assert.assertEquals(1, br.getLineNumber());

        // Close the buffered reader
        br.close();
    }
}