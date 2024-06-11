package org.apache.commons.csv; // Import necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.apache.commons.csv.util.Assertions;
import org.apache.commons.csv.util.Constants;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVParser class
 */
public class CSVParserTest {
    private static final String TEST_CSV_FILE = "testCSV.csv";
    private static final String TEST_CSV_STRING = "name,age,salary\nJohn Doe,30,50000\nJane Doe,25,45000\n";

    private CSVParser parser;
    private CSVRecord record;
    private CSVFormat format;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT.withHeader();
    }

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)}
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        parser = CSVParser.parse(new File(TEST_CSV_FILE), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
        assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}
     */
    @Test
    public void testParseString() {
        try {
            parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
            assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
            parser.close();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)}
     */
    @Test
    public void testParseURL() throws IOException {
        URL url = getClass().getResource("/testCSV.csv");
        parser = CSVParser.parse(url, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
        assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)}
     */
    @Test
    public void testParseReader() throws IOException {
        Reader reader = new StringReader(TEST_CSV_STRING);
        parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
        assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getHeaderMap()}
     */
    @Test
    public void testGetHeaderMap() {
        format = CSVFormat.DEFAULT;
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame(headerMap.size(), 3);
        assertEquals("name", headerMap.get(0).toString());
        assertEquals("age", headerMap.get(1).toString());
        assertEquals("salary", headerMap.get(2).toString());
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords()}
     */
    @Test
    public void testGetRecords() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
        assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(Collection)}
     */
    @Test
    public void testGetRecordsCollection() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        List<CSVRecord> records = new ArrayList<>();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        parser.getRecords(records);
        assertEquals(2, records.size());
        assertRecordsEqual(records.get(0), "name", "John Doe", "age", "30", "salary", "50000");
        assertRecordsEqual(records.get(1), "name", "Jane Doe", "age", "25", "salary", "45000");
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#iterator()}
     */
    @Test
    public void testIterator() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        List<CSVRecord> records = new ArrayList<>();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        Iterator<CSVRecord> it = parser.iterator();
        assertEquals(2, it.next().size());
        assertEquals("name", it.next().get(0));
        assertEquals("John Doe", it.next().get(1));
        assertEquals("age", it.next().get(0));
        assertEquals("30", it.next().get(1));
        assertEquals("salary", it.next().get(0));
        assertEquals("50000", it.next().get(1));
        assertFalse(it.hasNext());
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#nextRecord()}
     */
    @Test
    public void testNextRecord() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        assertEquals(2, record1.size());
        assertEquals("name", record1.get(0));
        assertEquals("John Doe", record1.get(1));
        assertEquals(2, record2.size());
        assertEquals("name", record2.get(0));
        assertEquals("Jane Doe", record2.get(1));
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#isClosed()}
     */
    @Test
    public void testIsClosed() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    /**
     * Test method for {@link CSVParser#close()}
     */
    @Test
    public void testClose() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getCurrentLineNumber()}
     */
    @Test
    public void testGetCurrentLineNumber() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()}
     */
    @Test
    public void testGetRecordNumber() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#getRecords(Collection)} with empty collection
     */
    @Test
    public void testGetRecordsEmptyCollection() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        List<CSVRecord> records = Collections.emptyList();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        parser.getRecords(records);
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#close()} with closed parser
     */
    @Test
    public void testCloseClosedParser() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(TEST_CSV_STRING), format);
        parser.close();
    }

    /**
     * Helper method to compare two records
     * @param record1
     * @param record2
     */
    private void assertRecordsEqual(CSVRecord record1, String... expected) {
        assertSame(record1.size(), expected.length);
        for (int i = 0; i < record1.size(); i++) {
            assertEquals(expected[i], record1.get(i));
        }
    }
}