package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for ExtendedBufferedReader.
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test read method with normal input.
     */
    @Test
    public void testReadNormal() throws IOException {
        String input = "Hello World\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int actual = reader.read();
        Assert.assertEquals('H', actual);

        int actual2 = reader.read();
        Assert.assertEquals('e', actual2);

        int actual3 = reader.read();
        Assert.assertEquals('l', actual3);

        int actual4 = reader.read();
        Assert.assertEquals('l', actual4);

        int actual5 = reader.read();
        Assert.assertEquals('o', actual5);

        int actual6 = reader.read();
        Assert.assertEquals(' ', actual6);

        int actual7 = reader.read();
        Assert.assertEquals('W', actual7);

        int actual8 = reader.read();
        Assert.assertEquals('o', actual8);

        int actual9 = reader.read();
        Assert.assertEquals('r', actual9);

        int actual10 = reader.read();
        Assert.assertEquals('l', actual10);

        int actual11 = reader.read();
        Assert.assertEquals('\n', actual11);

        int actual12 = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual12);
    }

    /**
     * Test readAgain method with normal input.
     */
    @Test
    public void testReadAgainNormal() throws IOException {
        String input = "Hello World\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        int actual = reader.read();
        Assert.assertEquals(' ', actual);

        int actual2 = reader.readAgain();
        Assert.assertEquals('W', actual2);

        int actual3 = reader.read();
        Assert.assertEquals('o', actual3);

        int actual4 = reader.read();
        Assert.assertEquals('r', actual4);

        int actual5 = reader.read();
        Assert.assertEquals('l', actual5);

        int actual6 = reader.read();
        Assert.assertEquals('\n', actual6);

        int actual7 = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual7);
    }

    /**
     * Test read method with empty input.
     */
    @Test
    public void testReadEmpty() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0][])));
        int actual = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    /**
     * Test read method with error input.
     */
    @Test(expected = IOException.class)
    public void testReadError() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0][]);
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        reader.read();
        reader.read();
        reader.read(); // should throw an exception
    }

    /**
     * Test lookAhead method with normal input.
     */
    @Test
    public void testLookAheadNormal() throws IOException {
        String input = "Hello World\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int actual = reader.lookAhead();
        Assert.assertEquals('H', actual);

        reader.read();

        int actual2 = reader.lookAhead();
        Assert.assertEquals('e', actual2);
    }

    /**
     * Test lookAhead method with error input.
     */
    @Test(expected = IOException.class)
    public void testLookAheadError() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0][])));
        reader.lookAhead();
    }

    /**
     * Test getLineNumber method with normal input.
     */
    @Test
    public void testGetLineNumberNormal() throws IOException {
        String input = "Line 1\nLine 2\nLine 3\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();

        int actual = reader.getLineNumber();
        Assert.assertEquals(3, actual);
    }

    /**
     * Test getLineNumber method with empty input.
     */
    @Test
    public void testGetLineNumberEmpty() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0][])));
        int actual = reader.getLineNumber();
        Assert.assertEquals(-1, actual);
    }
}