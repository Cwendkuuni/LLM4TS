package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        String csvData = "col1,col2,col3\n" + "1,2,3\n" + "4,5,6\n" + "7,8,9";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
    }

    @Test
    public void testHasNextRecord() throws IOException {
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertTrue(parser.hasNext());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"1", "2", "3"}, record.getValues());
        }
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(3, parser.getRecordNumber());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRecordAfterEndOfStream() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(3, headerMap.size());
        assertArrayEquals(new String[]{"col1", "col2", "col3"},
                headerMap.keySet().toArray(new String[headerMap.size()]));
    }

    @Test
    public void testGetRecordWithHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        CSVRecord record = parser.nextRecord();
        assertArrayEquals(new String[]{"1"}, record.getValues(headerMap));
    }

    @Test
    public void testGetRecordWithEmptyHeaderMap() throws IOException {
        parser = CSVParser.parse(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testGetRecordWithoutHeader() throws IOException {
        CSVParser parserWithoutHeader = CSVParser.parse(new StringReader("1,2,3\n4,5,6\n7,8,9"), CSVFormat.DEFAULT
                .withHeaderExcluded());
        CSVRecord record = parserWithoutHeader.nextRecord();
        assertArrayEquals(new String[]{"1"}, record.getValues());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCsv() throws IOException {
        new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseEmptyCsv() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }

    @Test
    public void testParseExcelCsv() throws IOException {
        String csvData = "col1,col2,col3\n" + "1,2,3\n" + "4,5,6\n" + "7,8,9\n" + "@\"test\"";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.EXCEL);
        CSVRecord record = parser.nextRecord();
        String[] values = record.getValues();
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, values);
        assertArrayEquals(new String[]{"1", "2", "3"}, record.getValues(parser.getHeaderMap()));
        assertArrayEquals(new String[]{"1"}, record.getValues(parser.getHeaderMap()).subList(1, 3).toArray());
        assertEquals("test", record.getComment());
    }
}