package org.apache.commons.csv; // replace with the actual package name if different

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
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
 * Test class for CSVParser.
 */
public class CSVParserTest {

    // Test data
    private static final String CSV_STRING = "a;b\nc;d\n";
    private static final String CSV_FILE_PATH = "src/test/resources/testCSV.csv";

    private CSVParser parser;
    private CSVRecord record;

    @Before
    public void setup() throws IOException {
        // Initialize test parser with test string
        parser = new CSVParser(new StringReader(CSV_STRING), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseFile() throws IOException {
        // Test parsing a file
        CSVParser parserFromFile = CSVParser.parse(new File(CSV_FILE_PATH), CSVFormat.DEFAULT);
        List<CSVRecord> records = parserFromFile.getRecords();
        parserFromFile.close();

        assertEquals(2, records.size());
        assertCSVRecordEquals(new String[]{"a", "b"}, records.get(0).toArray());
        assertCSVRecordEquals(new String[]{"c", "d"}, records.get(1).toArray());
    }

    @Test
    public void testParseString() throws IOException {
        // Test parsing a string
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertCSVRecordEquals(new String[]{"a", "b"}, records.get(0).toArray());
        assertCSVRecordEquals(new String[]{"c", "d"}, records.get(1).toArray());
    }

    @Test
    public void testParseStringWithEmptyRecord() throws IOException {
        String emptyCSVString = "a";
        parser = new CSVParser(new StringReader(emptyCSVString), CSVFormat.DEFAULT);
        record = parser.nextRecord();

        assertNull(record);
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseStringWithEmptyFile() throws IOException {
        // Test parsing an empty file
        CSVParser parserEmptyFile = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        parserEmptyFile.getRecords();
    }

    @Test
    public void testParseStringWithComment() throws IOException {
        String csvStringWithComment = "a;b\nc;d# This is a comment";
        parser = new CSVParser(new StringReader(csvStringWithComment), CSVFormat.DEFAULT);
        record = parser.nextRecord();

        assertSame(CSVFormat.DEFAULT, parser.getFormat());
        assertCSVRecordEquals(new String[]{"a", "b"}, record.toArray());
        assertSame("This is a comment", record.getComment());
    }

    @Test(expected = IOException.class)
    public void testParseStringWithInvalidFormat() throws IOException {
        String csvStringWithInvalidFormat = "a;b\nc:d";
        parser = new CSVParser(new StringReader(csvStringWithInvalidFormat), CSVFormat.EXCEL);
    }

    @Test
    public void testParseStringWithCustomFormat() throws IOException {
        String csvStringWithCustomFormat = "a;b\nc;d";
        CSVFormat customFormat = new CSVFormat(',', '\n', null, null, null);
        parser = new CSVParser(new StringReader(csvStringWithCustomFormat), customFormat);
        record = parser.nextRecord();

        assertSame(customFormat, parser.getFormat());
        assertCSVRecordEquals(new String[]{"a", "b"}, record.toArray());
        assertNull(record.getComment());
    }

    @Test
    public void testParseStringWithNullFormat() throws IOException {
        // Test parsing a string with a null format
        try {
            parser = new CSVParser(new StringReader(CSV_STRING), null);
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    // Helper methods

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected array
     * @param actual the actual array
     */
    private void assertCSVRecordEquals(String[] expected) {
        assertCSVRecordEquals(expected, null);
    }

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected array
     * @param comment the expected comment
     */
    private void assertCSVRecordEquals(String[] expected, String comment) {
        assertCSVRecordEquals(expected, comment, null);
    }

    /**
     * Asserts that two arrays are equal.
     *
     * @param expected the expected array
     * @param comment the expected comment
     * @param record the actual record
     */
    private void assertCSVRecordEquals(String[] expected, String comment, CSVRecord record) {
        if (record == null) {
            assertNull(parser.nextRecord());
        } else {
            assertArrayEquals(expected, record.toArray());
            assertEquals(comment, record.getComment());
        }
    }

    /**
     * Asserts that the two arrays are equal.
     *
     * @param expected the expected array
     * @param actual the actual array
     */
    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (expected.getClass() != actual.getClass()) {
            fail("Expected: " + expected.getClass().getName() + ", but was: " + actual.getClass().getName());
        } else {
            assertArrayEquals((Object[]) expected, (Object[]) actual);
        }
    }

    /**
     * Asserts that the two arrays are equal.
     *
     * @param expected the expected array
     * @param actual the actual array
     */
    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Expected: " + Arrays.toString(expected) + ", but was: " + Arrays.toString(actual));
        }

        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                fail("Expected [" + i + "]=" + expected[i] + ", but was [" + i + "]=" + actual[i]);
            }
        }
    }
}