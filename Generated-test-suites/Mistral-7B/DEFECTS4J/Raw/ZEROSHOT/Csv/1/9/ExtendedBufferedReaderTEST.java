package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        int expectedLastChar;
        int actualLastChar;
        int expectedLineNumber;
        int actualLineNumber;

        for (int i = 0; i < input.length(); i++) {
            expectedLastChar = (i < input.length() - 1 && input.charAt(i + 1) == '\n') ? '\n' : input.charAt(i);
            actualLastChar = reader.read();
            expectedLineNumber = i / (input.length() > 0 && input.charAt(i) == '\n' ? 1 : 0);
            actualLineNumber = reader.getLineNumber();

            Assert.assertEquals("Unexpected last character: " + (char) expectedLastChar, expectedLastChar, actualLastChar);
            Assert.assertEquals("Unexpected line number: " + expectedLineNumber, expectedLineNumber, actualLineNumber);
        }

        actualLastChar = reader.read();
        Assert.assertEquals("Unexpected last character: " + actualLastChar, ExtendedBufferedReader.END_OF_STREAM, actualLastChar);
        Assert.assertEquals("Unexpected line number: " + reader.getLineNumber(), input.length() / (input.length() > 0 && input.charAt(input.length() - 1) == '\n' ? 1 : 0), reader.getLineNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookAheadNegativeMark() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.mark(-1);
        reader.lookAhead();
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadAfterEndOfStream() throws Exception {
        String input = "Hello\nWorld\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.read();
        reader.read();
        reader.read();
        reader.lookAhead();

        reader.read(); // Consume EOF
        reader.lookAhead();
    }
}