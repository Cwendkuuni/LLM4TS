package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    private String input;
    private CSVParser parser;

    @Before
    public void setUp() {
        input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5\r";
        reader = new ExtendedBufferedReader(new StringReader(input));
        parser = CSVParser.newParser();
        parser.setReader(reader);
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
    }

    @Test
    public void testRead() throws IOException {
        int actual = reader.read();
        Assert.assertEquals(-1, actual);

        input = "A";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.read();
        Assert.assertEquals('A'.code, actual);

        input = "AB\r";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.read();
        Assert.assertEquals('A'.code, actual);
        actual = reader.read();
        Assert.assertEquals('\r', actual);

        input = "AB\r\n";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.read();
        Assert.assertEquals('A'.code, actual);
        actual = reader.read();
        Assert.assertEquals('\r', actual);
        actual = reader.read();
        Assert.assertEquals('\n', actual);

        input = "ABC\r\n";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.read();
        Assert.assertEquals('A'.code, actual);
        actual = reader.read();
        Assert.assertEquals('B'.code, actual);
        actual = reader.read();
        Assert.assertEquals('C'.code, actual);
        actual = reader.read();
        Assert.assertEquals(-1, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        input = "A";
        reader = new ExtendedBufferedReader(new StringReader(input));
        reader.read();

        int actual = reader.readAgain();
        Assert.assertEquals('A'.code, actual);

        input = "AB\r";
        reader = new ExtendedBufferedReader(new StringReader(input));
        reader.read();

        actual = reader.readAgain();
        Assert.assertEquals('A'.code, actual);

        reader.read();

        actual = reader.readAgain();
        Assert.assertEquals('\r', actual);

        input = "AB\r\n";
        reader = new ExtendedBufferedReader(new StringReader(input));
        reader.read();

        actual = reader.readAgain();
        Assert.assertEquals('A'.code, actual);

        reader.read();

        actual = reader.readAgain();
        Assert.assertEquals('\r', actual);

        reader.read();

        actual = reader.readAgain();
        Assert.assertEquals('\n', actual);
    }

    @Test
    public void testReadLine() throws IOException {
        String actual = reader.readLine();
        Assert.assertNull(actual);

        input = "Line 1";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.readLine();
        Assert.assertEquals("Line 1", actual);
        Assert.assertEquals(1, reader.getLineNumber());

        input += "\rLine 2";
        reader.reset();
        reader.readLine();
        actual = reader.readLine();
        Assert.assertEquals("Line 2", actual);
        Assert.assertEquals(2, reader.getLineNumber());

        reader.reset();
        actual = reader.readLine();
        Assert.assertEquals("Line 1", actual);
        Assert.assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        reader.mark(1);
        int actual = reader.lookAhead();
        Assert.assertEquals(-1, actual);
        reader.reset();

        input = "A";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.lookAhead();
        Assert.assertEquals('A'.code, actual);

        input = "AB\r";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.lookAhead();
        Assert.assertEquals('A'.code, actual);

        reader.read();

        actual = reader.lookAhead();
        Assert.assertEquals('\r', actual);

        input = "AB\r\n";
        reader = new ExtendedBufferedReader(new StringReader(input));
        actual = reader.lookAhead();
        Assert.assertEquals('A'.code, actual);

        reader.read();

        actual = reader.lookAhead();
        Assert.assertEquals('\r', actual);

        reader.read();

        actual = reader.lookAhead();
        Assert.assertEquals('\n', actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadAfterEndOfStream() throws IOException {
        reader.mark(1);
        reader.read();
        reader.close();
        reader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Assert.assertEquals(0, reader.getLineNumber());

        input = "Line 1\rLine 2\rLine 3\rLine 4\rLine 5\r";
        reader = new ExtendedBufferedReader(new StringReader(input));
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        Assert.assertEquals(5, reader.getLineNumber());

        reader.reset();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());
    }

}