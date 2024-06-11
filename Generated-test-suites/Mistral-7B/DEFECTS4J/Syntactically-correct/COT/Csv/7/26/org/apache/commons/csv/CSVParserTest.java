package org.apache.commons.csv; // Make sure to import all necessary classes

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CSVParser class
 */
public class CSVParserTest {

    /** The test CSV data */
    private static final String TEST_CSV_DATA = "a;b\nc;d\n1;2;3";

    /** The test CSV format */
    private CSVFormat testFormat;

    /** The test input stream */
    private ByteArrayInputStream testInputStream;

    /** The test reader */
    private Reader testReader;

    /** The test parser */
    private CSVParser testParser;

    /**
     * Set up test environment
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        testFormat = CSVFormat.DEFAULT;
        testInputStream = new ByteArrayInputStream(TEST_CSV_DATA.getBytes());
        testReader = new ByteArrayReader(TEST_CSV_DATA.getBytes());
    }

    /**
     * Test parse method with File
     *
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("testFile.csv"), testFormat);
        assertSame(parser.getClass(), CSVParser.class);

        // Test with valid input
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d"}, records.get(1).values());

        parser.close();
    }

    /**
     * Test parse method with String
     *
     * @throws IOException
     */
    @Test
    public void testParseString() throws IOException {
        CSVParser parser = CSVParser.parse(TEST_CSV_DATA, testFormat);
        assertSame(parser.getClass(), CSVParser.class);

        // Test with valid input
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d"}, records.get(1).values());

        parser.close();
    }

    /**
     * Test parse method with URL
     *
     * @throws IOException
     */
    @Test
    public void testParseURL() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(TEST_CSV_DATA.getBytes());
        URL testURL = new URL("testURL", null, 0, testInputStream);
        CSVParser parser = CSVParser.parse(testURL, CSVFormat.DEFAULT, Charset.forName("UTF-8"));
        assertSame(parser.getClass(), CSVParser.class);

        // Test with valid input
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d"}, records.get(1).values());

        parser.close();
    }

    /**
     * Test parse method with Reader
     *
     * @throws IOException
     */
    @Test
     public void testParseReader() throws IOException {
         CSVParser parser = new CSVParser(testReader, testFormat);
         assertSame(parser.getClass(), CSVParser.class);

         // Test with valid input
         List<CSVRecord> records = parser.getRecords();
         assertFalse(records.isEmpty());
         assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
         assertArrayEquals(new String[]{"c", "d"}, records.get(1).values());

         parser.close();
         testReader.close();
     }

    /**
     * Test getRecords method
     *
     * @throws IOException
     */
    @Test
    public void testGetRecords() throws IOException {
        CSVParser parser = new CSVParser(testReader, testFormat);
        List<CSVRecord> records = parser.getRecords();

        // Test with valid input
        assertFalse(records.isEmpty());
        assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
        assertArrayEquals(new String[]{"c", "d"}, records.get(1).values());

        parser.close();
        testReader.close();
    }

    /**
     * Test getRecords method with empty CSV data
     *
     * @throws IOException
     */
    @Test
    public void testGetRecordsEmpty() throws IOException {
        // Create an empty CSV format
        CSVParser parser = new CSVParser(new StringReader(""), testFormat);
        List<CSVRecord> records = parser.getRecords();

        // Test with empty CSV data
        assertTrue(records.isEmpty());

        parser.close();
    }

    /**
     * Test getRecords method with invalid CSV data
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testGetRecordsInvalid() throws IOException {
        // Create an invalid CSV format
        CSVParser parser = new CSVParser(new StringReader("a;b\nc"), testFormat);
        parser.getRecords();
    }

    /**
     * Test isClosed method
     *
     * @throws IOException
     */
    @Test
    public void testIsClosed() throws IOException {
        CSVParser parser = new CSVParser(testReader, testFormat);

        // Test isClosed method with closed parser
        parser.close();
        assertTrue(parser.isClosed());

        // Test isClosed method with opened parser
        assertFalse(parser.isClosed());

        parser.close();
    }

    /**
     * Test close method
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        CSVParser parser = new CSVParser(testReader, testFormat);
        parser.close();

        // Test close method with already closed parser
        parser.close();
    }

    /**
     * Test iterator method
     *
     * @throws IOException
     */
    @Test
    public void testIterator() throws IOException {
        CSVParser parser = new CSVParser(testReader, testFormat);
        Iterator<CSVRecord> iterator = parser.iterator();

        // Test iterator method with empty CSV data
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        assertTrue(iterator.hasNext());

        // Test iterator method with valid CSV data
        CSVRecord record = iterator.next();
        assertArrayEquals(new String[]{"a", "b"}, record.values());
        CSVRecord record2 = iterator.next();
        assertArrayEquals(new String[]{"c", "d"}, record2.values());
        assertFalse(iterator.hasNext());

        parser.close();
        testReader.close();
    }

    /**
     * Test nextRecord method with valid input
     *
     * @throws IOException
     */
    @Test
    public void testNextRecordWithValidInput() throws IOException {
        CSVParser parser = new CSVParser(testReader, testFormat);
        CSVRecord record = parser.nextRecord();
        assertArrayEquals(new String[]{"a", "b"}, record.values());
        CSVRecord record2 = parser.nextRecord();
        assertArrayEquals(new String[]{"c", "d"}, record2.values());
        assertNull(parser.nextRecord());

        parser.close();
        testReader.close();
    }

    /**
     * Test nextRecord method with empty input
     *
     * @throws IOException
     */
    @Test
    public void testNextRecordWithEmptyInput() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), testFormat);
        assertNull(parser.nextRecord());
        parser.close();
    }

    /**
     * Test nextRecord method with invalid input
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testNextRecordWithInvalidInput() throws IOException {
        CSVParser parser = new CSVParser(new StringReader("a;b\nc"), testFormat);
        parser.nextRecord();
    }
}