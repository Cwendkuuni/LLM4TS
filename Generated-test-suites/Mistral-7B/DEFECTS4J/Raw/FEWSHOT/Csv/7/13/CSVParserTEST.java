package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Perform some assertions on the parsed CSV data
        for (CSVRecord record : records) {
            // Assert that the record has the correct number of columns
            assertEquals(5, record.size());

            // Assert that the values in the record are as expected
            String[] expectedValues = new String[]{"Col1", "Col2", "Col3", "Col4", "Col5"};
            assertArrayEquals(expectedValues, record.toArray(new String[0]));
        }
    }

    @Test
    public void testParseString() throws Exception {
        String csvData = "Col1,Col2,Col3,Col4,Col5\n" +
                "Value1,Value2,Value3,Value4,Value5\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Perform some assertions on the parsed CSV data
        for (CSVRecord record : records) {
            // Assert that the record has the correct number of columns
            assertEquals(5, record.size());

            // Assert that the values in the record are as expected
            String[] expectedValues = new String[]{"Value1", "Value2", "Value3", "Value4", "Value5"};
            assertArrayEquals(expectedValues, record.toArray(new String[0]));
        }
    }

    @Test
    public void testParseURL() throws Exception {
        URL url = new URL("http://example.com/csvFile.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.RFC4180, Charset.forName("UTF-8"));
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Perform some assertions on the parsed CSV data
        for (CSVRecord record : records) {
            // Assert that the record has the correct number of columns
            assertEquals(5, record.size());

            // Assert that the values in the record are as expected
            String[] expectedValues = new String[]{"Col1", "Col2", "Col3", "Col4", "Col5"};
            assertArrayEquals(expectedValues, record.toArray(new String[0]));
        }
    }

    @Test
    public void testIterateRecords() throws Exception {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);

        // Perform some assertions on each record
        for (CSVRecord record : parser) {
            assertEquals(5, record.size());
            String[] expectedValues = new String[]{"Col1", "Col2", "Col3", "Col4", "Col5"};
            assertArrayEquals(expectedValues, record.toArray(new String[0]));
        }

        parser.close();
    }

    @Test
    public void testGetRecords() throws Exception {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Perform some assertions on the parsed CSV data
        for (CSVRecord record : records) {
            assertEquals(5, record.size());
            String[] expectedValues = new String[]{"Col1", "Col2", "Col3", "Col4", "Col5"};
            assertArrayEquals(expectedValues, record.toArray(new String[0]));
        }
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        File file = new File("testCSVFile.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Perform some assertions on the parsed CSV data
        for (CSVRecord record : records) {
            assertEquals(0, record.getRecordNumber());
        }

        // Assert that the last record has the correct record number
        CSVParser parser2 = CSVParser.parse(file, CSVFormat.RFC4180);
        CSVRecord lastRecord = parser2.nextRecord();
        assertEquals(records.size(), lastRecord.getRecordNumber());
        parser2.close();
    }
}