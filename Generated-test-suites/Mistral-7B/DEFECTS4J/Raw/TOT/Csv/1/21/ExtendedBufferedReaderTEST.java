package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() throws IOException {
        String testData = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n";
        reader = new ExtendedBufferedReader(new StringReader(testData));
    }

    /**
     * Test for the read method
     */
    @Test
    public void testRead() throws IOException {
        int readChar;
        for (int i = 0; i < 5; i++) {
            readChar = reader.read();
            assertEquals(String.format("Unexpected char at position %d", i), (char) ('L' + i), reader.readAgain());
            assertEquals(String.format("Unexpected char at position %d", i), (char) ('L' + i), (char) readChar);
        }

        readChar = reader.read();
        assertEquals(Integer.valueOf(-1), reader.readAgain());
        assertEquals(Integer.valueOf(-1), Integer.valueOf(readChar));
    }

    /**
     * Test for the readAgain method when readLine is called
     */
    @Test
    public void testReadAgainAfterReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("Line 1", line);
        assertEquals(Integer.valueOf('n'), reader.readAgain());
        assertEquals(Integer.valueOf('n'), reader.readAgain());
        assertEquals(Integer.valueOf('\r'), reader.readAgain());
    }

    /**
     * Test for the read method with empty buffer
     */
    @Test
    public void testReadEmptyBuffer() throws IOException {
        reader.read();
        reader.read();
        reader.read();
        int readChar = reader.read();
        assertEquals(Integer.valueOf(-1), reader.readAgain());
        assertEquals(Integer.valueOf(-1), Integer.valueOf(readChar));
    }

    /**
     * Test for the readAgain method when EOF is reached
     */
    @Test
    public void testReadAgainAtEOF() throws IOException {
        int readChar = reader.read();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        readChar = reader.read();
        assertEquals(Integer.valueOf(-1), reader.readAgain());
        assertEquals(Integer.valueOf(-1), Integer.valueOf(readChar));
    }

    /**
     * Test for the readLine method with empty buffer
     */
    @Test
    public void testReadLineEmptyBuffer() throws IOException {
        String line = reader.readLine();
        assertEquals(null, line);
        assertEquals(Integer.valueOf(-1), reader.readAgain());
    }

    /**
     * Test for the lookAhead method
     */
    @Test
    public void testLookAhead() throws IOException {
        int lookAheadChar = reader.lookAhead();
        assertEquals(Integer.valueOf('L'), Integer.valueOf(lookAheadChar));
        reader.read();
        lookAheadChar = reader.lookAhead();
        assertEquals(Integer.valueOf('I'), Integer.valueOf(lookAheadChar));
        reader.read();
        lookAheadChar = reader.lookAhead();
        assertEquals(Integer.valueOf('N'), Integer.valueOf(lookAheadChar));
        reader.read();
        lookAheadChar = reader.lookAhead();
        assertEquals(Integer.valueOf('E'), Integer.valueOf(lookAheadChar));
        reader.read();
        lookAheadChar = reader.lookAhead();
        assertEquals(Integer.valueOf(-1), Integer.valueOf(lookAheadChar));
    }

    /**
     * Test for the getLineNumber method
     */
    @Test
    public void testGetLineNumber() throws IOException {
        String line = reader.readLine();
        assertEquals("Line 1", line);
        assertEquals(Integer.valueOf(1), reader.getLineNumber());
        line = reader.readLine();
        assertEquals("Line 2", line);
        assertEquals(Integer.valueOf(2), reader.getLineNumber());
        line = reader.readLine();
        assertEquals("Line 3", line);
        assertEquals(Integer.valueOf(3), reader.getLineNumber());
        line = reader.readLine();
        assertEquals("Line 4", line);
        assertEquals(Integer.valueOf(4), reader.getLineNumber());
        line = reader.readLine();
        assertEquals("Line 5", line);
        assertEquals(Integer.valueOf(5), reader.getLineNumber());
        line = reader.readLine();
        assertEquals(null, line);
        assertEquals(Integer.valueOf(-1), reader.getLineNumber());
    }

    /**
     * Test for the read method with CSV parser
     */
    @Test
    public void testReadWithCSVParser() throws IOException {
        CSVParser parser = CSVParser.newParser();
        parser.setReader(reader);
        CSVRecord record;
        record = parser.getHeaderMap();
        assertNotNull(record);
        reader.readLine();
        record = parser.getRecord();
        assertEquals("Line 1", record.get("column1"));
        assertEquals("Line 1", record.get(0));
        reader.readLine();
        record = parser.getRecord();
        assertEquals("Line 2", record.get("column1"));
        assertEquals("Line 2", record.get(0));
        reader.readLine();
        record = parser.getRecord();
        assertEquals("Line 3", record.get("column1"));
        assertEquals("Line 3", record.get(0));
        reader.readLine();
        record = parser.getRecord();
        assertEquals("Line 4", record.get("column1"));
        assertEquals("Line 4", record.get(0));
        reader.readLine();
        record = parser.getRecord();
        assertEquals("Line 5", record.get("column1"));
        assertEquals("Line 5", record.get(0));
        reader.readLine();
        assertNull(parser.getRecord());
    }
}