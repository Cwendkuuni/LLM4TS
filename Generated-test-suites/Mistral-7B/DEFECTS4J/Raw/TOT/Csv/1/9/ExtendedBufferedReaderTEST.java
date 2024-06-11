package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String testData = "Line1\rLine2\rLine3\rLine4\rLine5";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        int expectedLineCounter = 5;
        int expectedLastChar;

        for (int i = 0; i < 5; i++) {
            int actualChar = reader.read();
            int actualLineCounter = reader.getLineNumber();

            Assert.assertEquals("Unexpected last character: " + actualChar, testData.charAt(i), (char) actualChar);
            Assert.assertEquals("Unexpected line counter: " + actualLineCounter, expectedLineCounter, actualLineCounter);

            if (i < 4) {
                expectedLastChar = testData.charAt(i + 1);
                expectedLineCounter++;
            } else {
                expectedLastChar = ExtendedBufferedReader.END_OF_STREAM;
            }
        }

        Assert.assertEquals("Unexpected last character: " + reader.read(), expectedLastChar, reader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        String testData = "Line1\rLine2\rLine3\rLine4\rLine5";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        int expectedLastChar = 'L';

        for (int i = 0; i < 4; i++) {
            reader.read();
        }

        int actualChar = reader.lookAhead();

        Assert.assertEquals("Unexpected lookahead character: " + actualChar, expectedLastChar, actualChar);
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("Line1\rLine2\r".getBytes())));

        reader.read();
        reader.read();

        reader.read(); // should throw an IOException
    }

    @Test
    public void testReadLine() throws IOException {
        String testData = "Line1\rLine2\rLine3\rLine4\rLine5";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        String expectedLine1 = "Line1";
        String expectedLine2 = "Line2";
        String expectedLine3 = "Line3";
        String expectedLine4 = "Line4";

        String actualLine1 = reader.readLine();
        String actualLine2 = reader.readLine();
        String actualLine3 = reader.readLine();
        String actualLine4 = reader.readLine();

        Assert.assertEquals("Unexpected line1: " + actualLine1, expectedLine1, actualLine1);
        Assert.assertEquals("Unexpected line2: " + actualLine2, expectedLine2, actualLine2);
        Assert.assertEquals("Unexpected line3: " + actualLine3, expectedLine3, actualLine3);
        Assert.assertEquals("Unexpected line4: " + actualLine4, expectedLine4, actualLine4);

        Assert.assertEquals("Unexpected last character: " + reader.read(), ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadEmptyLine() throws IOException {
        String testData = "\r";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        String actualLine = reader.readLine();

        Assert.assertEquals("Unexpected line: " + actualLine, "", actualLine);
        Assert.assertEquals("Unexpected last character: " + reader.read(), ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
}