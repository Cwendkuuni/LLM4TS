package org.apache.commons.csv; // import necessary classes here

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVParser class.
 */
public class CSVParserTest {

    private static final String TEST_CSV = "name,age\nAlice,30\nBob,25";

    private CSVParser parser;
    private CSVFormat format;

    @Before
    public void setUp() throws Exception {
        format = CSVFormat.DEFAULT.withHeader();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV.getBytes());
        parser = new CSVParser(new InputStreamReader(inputStream), format);
    }

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, format);
        // Test methods here
        // ...
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = TEST_CSV;
        CSVParser parser = CSVParser.parse(csvString, format);
        // Test methods here
        // ...
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "file:test.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), format);
        // Test methods here
        // ...
        parser.close();
    }

    @Test
    public void testParseReader() throws IOException {
        StringReader reader = new StringReader(TEST_CSV);
        CSVParser parser = new CSVParser(reader, format);
        // Test methods here
        // ...
        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertArrayEquals(new String[]{"name", "age"}, record1.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Alice", record1.get(0));
        assertEquals(30, record1.get(1).getIntValue());

        assertArrayEquals(new String[]{"name", "age"}, record2.getHeaderMap().keySet().toArray(new String[0]));
        assertEquals("Bob", record2.get(0));
        assertEquals(25, record2.get(1).getIntValue());

        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertSame("name", headerMap.get("name"));
        assertSame("age", headerMap.get("age"));

        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        long recordNumber = parser.getRecordNumber();
        assertEquals(0, recordNumber);

        parser.nextRecord();
        recordNumber = parser.getRecordNumber();
        assertEquals(1, recordNumber);

        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        parser.close();
        try {
            parser.nextRecord();
            fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testIterator() throws IOException {
        CSVRecordIterator iterator = parser.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            CSVRecord record = iterator.next();
            count++;
        }
        assertEquals(2, count);

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoMoreElements() throws IOException {
        CSVRecordIterator iterator = parser.iterator();
        iterator.next();
        iterator.next();
        iterator.next(); // Should throw NoSuchElementException
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        long lineNumber = parser.getCurrentLineNumber();
        assertEquals(1, lineNumber);

        parser.nextRecord();
        lineNumber = parser.getCurrentLineNumber();
        assertEquals(2, lineNumber);

        parser.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test(expected = IOException.class)
    public void testInvalidInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid input".getBytes());
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream), format);
        parser.nextRecord();
        fail("IOException expected");
    }

    @Test
    public void testCustomFormat() {
        CSVFormat customFormat = new CSVFormat(',', null, null, null, null, null, null, null, null);
        CSVParser parser = CSVParser.parse(new StringReader(TEST_CSV), customFormat);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        CSVParser.parse(new StringReader(TEST_CSV), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReader() {
        CSVParser parser = new CSVParser(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFile() {
        CSVParser.parse(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUrl() {
        CSVParser.parse(null, format, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCharset() {
        CSVParser.parse(new URL("test.csv"), null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCSVFormat() {
        CSVParser parser = new CSVParser(new StringReader(""), null);
    }

    @Test
    public void testEmptyCsv() throws IOException {
        String emptyCsv = "";
        CSVParser parser = new CSVParser(new StringReader(emptyCsv), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(Collections.emptyList(), records);
        parser.close();
    }
}