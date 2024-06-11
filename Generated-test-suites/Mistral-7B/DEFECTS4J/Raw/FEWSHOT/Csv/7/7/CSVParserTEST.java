package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        String csvFilePath = "src/test/resources/CSVParserTestData.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"COL1", "COL2", "COL3"}, record.getHeader());
            assertEquals("ROW1_COL1", record.get("COL1"));
            assertEquals("ROW1_COL2", record.get("COL2"));
            assertEquals("ROW1_COL3", record.get("COL3"));
        }
    }

    @Test
    public void testParseString() throws Exception {
        String csvData = "COL1,COL2,COL3\nROW1_COL1,ROW1_COL2,ROW1_COL3\nCOL1,COL2,COL3\nROW2_COL1,ROW2_COL2,ROW2_COL3";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"COL1", "COL2", "COL3"}, record.getHeader());
            assertEquals("ROW1_COL1", record.get("COL1"));
            assertEquals("ROW1_COL2", record.get("COL2"));
            assertEquals("ROW1_COL3", record.get("COL3"));
            assertEquals("ROW2_COL1", record.get("COL1"));
            assertEquals("ROW2_COL2", record.get("COL2"));
            assertEquals("ROW2_COL3", record.get("COL3"));
        }
    }

    @Test
    public void testParseUrl() throws Exception {
        String csvUrl = "https://example.com/test-data.csv";
        CSVParser parser = CSVParser.parse(new URL(csvUrl), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"COL1", "COL2", "COL3"}, record.getHeader());
            assertEquals("ROW1_COL1", record.get("COL1"));
            assertEquals("ROW1_COL2", record.get("COL2"));
            assertEquals("ROW1_COL3", record.get("COL3"));
        }
    }

    @Test
    public void testParseWithCustomFormat() throws Exception {
        String csvData = "COL1,COL2,COL3\nROW1_COL1,ROW1_COL2,\"COL3 with quotes\"\nCOL1,COL2,COL3\nROW2_COL1,ROW2_COL2,ROW2_COL3";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("COL1", "COL2", "COL3")
                .setNullString("NA")
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"COL1", "COL2", "COL3"}, record.getHeader());
            assertEquals("ROW1_COL1", record.get("COL1"));
            assertEquals("ROW1_COL2", record.get("COL2"));
            assertEquals("COL3 with quotes", record.get("COL3"));
            assertEquals(null, record.get("COL3").isEmpty() ? record.get("COL3") : record.get("COL3").get(0));
            assertEquals(3, record.size());
            assertEquals("NA", record.get("COL3").get(2));
            assertEquals("NA", record.get("COL3").get(record.size() - 1));
        }
    }

    @Test
    public void testSkipLines() throws Exception {
        String csvData = "COL1,COL2,COL3\n--Skip this line--\nROW1_COL1,ROW1_COL2,ROW1_COL3\nROW2_COL1,ROW2_COL2,ROW2_COL3";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            assertArrayEquals(new String[]{"COL1", "COL2", "COL3"}, record.getHeader());
            assertEquals("ROW1_COL1", record.get("COL1"));
            assertEquals("ROW1_COL2", record.get("COL2"));
            assertEquals("ROW1_COL3", record.get("COL3"));
            assertEquals("ROW2_COL1", record.get("COL1"));
            assertEquals("ROW2_COL2", record.get("COL2"));
            assertEquals("ROW2_COL3", record.get("COL3"));
        }
    }

    @Test
    public void testEmptyCsvFile() throws Exception {
        String csvFilePath = "src/test/resources/emptyCSVFile.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws Exception {
        String csvData = "COL1,COL2,COL3\nROW1_COL1,ROW1_COL2,ROW1_COL3";
        CSVParser parser = new CSVParser(new StringReader(csvData));
        parser.setFormat(null);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testNullReader() throws Exception {
        CSVParser parser = new CSVParser(null);
        parser.getRecords();
    }

    @Test(expected = NullPointerException.class)
    public void testNullRecords() {
        CSVParser parser = new CSVParser(new StringReader("COL1,COL2,COL3"));
        parser.getRecords((List<CSVRecord>) null);
    }
}