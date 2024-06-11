package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.test.util.TestUtil;
import org.junit.Test;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReaderConstructor() {
        // test constructor
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader);
    }

    @Test
    public void testRead() throws IOException {
        // test read method
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int result = reader.read();
        assertEquals(TestUtil.TO_INT_CHAR(input.charAt(0)), result);
        assertEquals(TestUtil.TO_INT_CHAR('\n'), reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        reader.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        // test readAgain method
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.read();
        reader.read();
        int result = reader.readAgain();
        assertEquals(TestUtil.TO_INT_CHAR('\n'), result);

        reader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        // test readLine method
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String result = reader.readLine();
        assertEquals(input, result);
        assertEquals(1, reader.getLineNumber());
        assertNull(reader.readLine());

        reader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        // test lookAhead method
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int result = reader.lookAhead();
        assertEquals(TestUtil.TO_INT_CHAR(input.charAt(0)), result);

        reader.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        // test getLineNumber method
        String input = "test\ntest\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.readLine();
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(2, result);

        reader.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeLength() throws IOException {
        // test exception when passing negative length to read method
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.read(new char[1], 0, -1);

        reader.close();
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        // test read method with zero length
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int result = reader.read(new char[0], 0, 0);
        assertEquals(0, result);

        reader.close();
    }

    @Test
    public void testReadWithLengthBiggerThanAvailableData() throws IOException {
        // test read method with length bigger than available data
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int result = reader.read(new char[2], 0, 1);
        assertEquals(1, result);

        reader.close();
    }

    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        // test readLine method with empty line
        String input = "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String result = reader.readLine();
        assertNull(result);
        assertEquals(0, reader.getLineNumber());

        reader.close();
    }

    @Test
    public void testReadLineWithMultipleLineBreaks() throws IOException {
        // test readLine method with multiple line breaks
        String input = "test\n\r\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        String result = reader.readLine();
        assertEquals(input.substring(0, input.indexOf("\r\n")), result);
        assertEquals(1, reader.getLineNumber());

        result = reader.readLine();
        assertEquals(input.substring(input.indexOf("\r\n") + 2), result);
        assertEquals(2, reader.getLineNumber());

        reader.close();
    }

    @Test
    public void testLookAheadWithEndOfStream() throws IOException {
        // test lookAhead method with end of stream
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.mark(1);
        reader.read();
        reader.reset();

        int result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);

        reader.close();
    }

    @Test
    public void testLookAheadWithAvailableCharacter() throws IOException {
        // test lookAhead method with available character
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        reader.mark(1);
        reader.read();
        reader.reset();

        int result = reader.lookAhead();
        assertEquals(TestUtil.TO_INT_CHAR('\n'), result);

        reader.close();
    }

    @Test
    public void testLookAheadWithNoAvailableCharacter() throws IOException {
        // test lookAhead method with no available character
        String input = "test\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        int result = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);

        reader.close();
    }

    private static void assertNotNull(Object object) {
        if (null == object) {
            throw new AssertionError("Expected object to not be null");
        }
    }
}