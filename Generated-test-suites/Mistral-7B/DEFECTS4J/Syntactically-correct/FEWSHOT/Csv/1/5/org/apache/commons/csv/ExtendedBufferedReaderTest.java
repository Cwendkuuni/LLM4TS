package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private static final String TEST_CONTENT = "line1\rline2\rline3\r--comment--\nline5";

    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c;
        for (int i = 0; i < TEST_CONTENT.length(); i++) {
            c = ebr.read();
            assertEquals(TEST_CONTENT.charAt(i), (char) c);
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c;
        for (int i = 0; i < TEST_CONTENT.length(); i++) {
            c = ebr.read();
            assertEquals(TEST_CONTENT.charAt(i), (char) c);
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());

        assertEquals(TEST_CONTENT.charAt(0), (char) ebr.readAgain());
        assertEquals(TEST_CONTENT.charAt(1), (char) ebr.readAgain());
        // ...
        assertEquals(TEST_CONTENT.charAt(TEST_CONTENT.length() - 1), (char) ebr.readAgain());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        String line;
        int lineNumber = 0;

        line = ebr.readLine();
        assertEquals("line1\r", line);
        lineNumber++;
        assertEquals(1, ebr.getLineNumber());

        line = ebr.readLine();
        assertEquals("line2\r", line);
        lineNumber++;
        assertEquals(2, ebr.getLineNumber());

        line = ebr.readLine();
        assertEquals("line3\r", line);
        lineNumber++;
        assertEquals(3, ebr.getLineNumber());

        line = ebr.readLine();
        assertNull(line);
        assertEquals(3, ebr.getLineNumber());

        line = ebr.readLine();
        assertNull(line);
        assertEquals(3, ebr.getLineNumber());

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.read());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int c;
        for (int i = 0; i < TEST_CONTENT.length(); i++) {
            if (i > 0 && TEST_CONTENT.charAt(i) == TEST_CONTENT.charAt(i - 1)) {
                continue; // Skip CR, LF, or CRLF
            }

            c = ebr.lookAhead();
            assertEquals(TEST_CONTENT.charAt(i), (char) c);
        }

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ebr.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        for (int i = 0; i < 5; i++) {
            String line = ebr.readLine();
            assertEquals(i + 1, ebr.getLineNumber());
        }

        assertEquals(5, ebr.getLineNumber());
    }

    @Test
    public void testMultipleReads() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        StringBuilder sb = new StringBuilder();
        int c;

        while ((c = ebr.read()) != ExtendedBufferedReader.END_OF_STREAM) {
            sb.append((char) c);
        }

        assertEquals(TEST_CONTENT, sb.toString());
        assertEquals(5, ebr.getLineNumber());
    }

    @Test
    public void testNullReader() {
        try {
            new ExtendedBufferedReader(null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}