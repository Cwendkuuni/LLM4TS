package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "a,b\nc,d";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testParseExcelCSV() throws IOException {
        String csvData = "a,b\nc,d\n";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"a", "b"}, record.getValues());
        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(0, records.size());
    }

    @Test
    public void testParseMultilineCSV() throws IOException {
        String csvData = "col1\ncol1 value\ncol2\ncol2 value\n";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Map<String, Integer> headerMap = record.getHeaderMap();
        List<String> values = Arrays.asList(record.getValues());
        assertEquals(2, values.size());
        assertEquals("col1", headerMap.get("col1"));
        assertEquals("col1 value", values.get(0));
        assertEquals("col2", headerMap.get("col2"));
        assertEquals("col2 value", values.get(1));
        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testParseCustomHeaderCSV() throws IOException {
        String csvData = "Name,Age\nJohn,25\nMary,30";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT.withHeader("Name", "Age"));
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Map<String, Integer> headerMap = record1.getHeaderMap();
        List<String> values = Arrays.asList(record1.getValues());
        assertEquals(2, values.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));
        assertEquals("John", values.get(0));
        assertEquals("25", values.get(1));
        assertEquals(0, record1.getRecordNumber());

        CSVRecord record2 = records.get(1);
        values = Arrays.asList(record2.getValues());
        assertEquals("Mary", values.get(0));
        assertEquals("30", values.get(1));
        assertEquals(1, record2.getRecordNumber());
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyIterator() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
        iterator.next(); // throws NoSuchElementException
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvData = "a,b\nc,d\n--"; // Invalid CSV format with --
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNull(records);
    }
}