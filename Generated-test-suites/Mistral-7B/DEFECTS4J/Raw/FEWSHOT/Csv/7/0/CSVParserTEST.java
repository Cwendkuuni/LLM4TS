package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new File("testData/sample.csv"), CSVFormat.DEFAULT).getRecords();
        assertEquals(10, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[] { "Name", "Age", "City" }, record.getHeaderMap().keySet().toArray(new String[3]));
            assertArrayEquals(new String[] { "John Doe", "30", "New York" }, record.toArray());
        }
    }

    @Test
    public void testParseString() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();
        assertEquals(2, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[] { "Name", "Age", "City" }, record.getHeaderMap().keySet().toArray(new String[3]));
            assertArrayEquals(new String[] { "John Doe", "30", "New York" }, record.toArray());
        }
    }

    @Test
    public void testParseUrl() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new InputStreamReader(CSVParserTest.class.getResourceAsStream("/testData/sample.csv")),
                CSVFormat.DEFAULT).getRecords();
        assertEquals(10, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[] { "Name", "Age", "City" }, record.getHeaderMap().keySet().toArray(new String[3]));
            assertArrayEquals(new String[] { "John Doe", "30", "New York" }, record.toArray());
        }
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        CSVFormat customFormat = new CSVFormat(',', '\n', "\r\n", null, null, null, null, null, null, false);
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name\nAge\nCity\nJohn Doe\n30\nNew York"), customFormat)
                .getRecords();
        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(i + 1, record.getRecordNumber());
            assertArrayEquals(new String[] { "Name" }, record.getHeaderMap().keySet().toArray(new String[1]));
            assertArrayEquals(new String[] { "John Doe" }, record.toArray());
        }
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new File("testData/empty.csv"), CSVFormat.DEFAULT).getRecords();
        assertEquals(0, records.size());
    }

    @Test
    public void testParseInvalidFile() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new File("testData/invalid.csv"), CSVFormat.DEFAULT);
        assertFalse(records.isPresent());
    }

    @Test
    public void testCloseParser() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();

        assertTrue(records.iterator().hasNext());
        CSVParser parser = (CSVParser) records.iterator().next();
        parser.close();

        assertFalse(records.iterator().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorAfterClose() throws IOException {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();

        assertTrue(records.iterator().hasNext());
        CSVParser parser = (CSVParser) records.iterator().next();
        parser.close();

        records.iterator().next();
    }

    @Test
    public void testGetHeaderMap() {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();

        Map<String, Integer> headerMap = records.get(0).getHeaderMap();
        assertEquals(3, headerMap.size());
        assertArrayEquals(new String[] { "Name", "Age", "City" }, headerMap.keySet().toArray(new String[3]));
    }

    @Test
    public void testGetRecords() {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();
        CSVRecord firstRecord = records.get(0);
        assertSame(firstRecord, records.iterator().next());
    }

    @Test
    public void testGetRecordsEmpty() {
        List<CSVRecord> records = CSVParser.parse(new File("testData/empty.csv"), CSVFormat.DEFAULT).getRecords();
        assertTrue(records.isEmpty());
    }

    @Test
    public void testGetRecordsNull() {
        List<CSVRecord> records = null;
        assertNull(records.getRecords());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorAfterEndOfRecords() {
        List<CSVRecord> records = CSVParser.parse(new StringReader("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles"),
                CSVFormat.DEFAULT).getRecords();

        assertTrue(records.iterator().hasNext());
        records.iterator().next();
        assertTrue(records.iterator().hasNext());
        records.iterator().next();

        Iterator<CSVRecord> iterator = records.iterator();
        iterator.remove(); // This should throw an UnsupportedOperationException, but instead it throws an IllegalStateException
    }
}