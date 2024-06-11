package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        String csvFile = "data.csv";
        File file = new File(csvFile);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        for (int i = 0; i < 5; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(3, values.length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(values[j]);
            }
        }

        parser.close();
    }

    @Test
    public void testParseString() throws Exception {
        String csvData = "col1,col2,col3\n1,2,3\n4,5,6\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        for (int i = 0; i < 3; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(3, values.length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(values[j]);
            }
        }

        parser.close();
    }

    @Test
    public void testParseURL() throws Exception {
        String csvUrl = "http://example.com/data.csv";
        CSVParser parser = CSVParser.parse(new URL(csvUrl), Charset.forName("UTF-8"), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        for (int i = 0; i < 5; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(3, values.length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(values[j]);
            }
        }

        parser.close();
    }

    @Test
    public void testIterateRecords() throws Exception {
        String csvFile = "data.csv";
        File file = new File(csvFile);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        parser.close();

        assertEquals(5, records.size());

        for (int i = 0; i < 5; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(3, values.length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(values[j]);
            }
        }
    }

    @Test
    public void testIterateRecordsWithHeader() throws Exception {
        String csvFile = "data_with_header.csv";
        File file = new File(csvFile);
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        parser.close();

        assertEquals(5, records.size());

        for (int i = 0; i < 5; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(3, values.length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(values[j]);
            }
            assertEquals("col" + (j + 1), record.getColumnName(j));
        }
    }

    @Test
    public void testInvalidCsv() throws Exception {
        String csvData = "col1,col2\n1,2,3\n4,\n5,6\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (int i = 0; i < 2; i++) {
            CSVRecord record = records.get(i);
            assertNotNull(record);
            assertEquals(i + 1, record.getRecordNumber());
            String[] values = record.getValues();
            assertNotNull(values);
            assertEquals(2 - i, values.length);
            for (int j = 0; j < values.length; j++) {
                assertNotNull(values[j]);
            }
        }

        parser.close();
    }
}