package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFileWithHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new File("testData/header.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(1, records.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));

        assertEquals(2, record.size());
        assertEquals("John Doe", record.get(0));
        assertEquals(30, record.get(1).intValue());
    }

    @Test
    public void testParseFileWithoutHeader() throws IOException {
        CSVParser parser = CSVParser.parse(new File("testData/noHeader.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            assertEquals("value" + (i + 1), record.get(0));
            assertEquals(Integer.valueOf(i + 1), record.get(1).intValue());
        }
    }

    @Test
    public void testParseStringWithHeader() throws IOException {
        String csvData = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        CSVRecord record = records.get(0);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));

        assertEquals(2, record.size());
        assertEquals("John Doe", record.get(0));
        assertEquals(30, record.get(1).intValue());
    }

    @Test
    public void testParseStringWithoutHeader() throws IOException {
        String csvData = "value1,1\nvalue2,2\nvalue3,3";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            assertEquals(2, record.size());
            assertEquals("value" + (i + 1), record.get(0));
            assertEquals(Integer.valueOf(i + 1), record.get(1).intValue());
        }
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/data.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        // Replace this with actual testing code for the data at the URL
        assertEquals(0, records.size());
    }

    @Test(expected = IOException.class)
    public void testParseNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), new CSVFormat("INVALID_FORMAT"));
    }
}