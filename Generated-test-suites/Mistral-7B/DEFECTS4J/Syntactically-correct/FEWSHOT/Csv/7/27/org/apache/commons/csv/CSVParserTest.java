package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertFalse(parser.isClosed());
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "col1,col2,col3\n" + "value1,value2,value3\n" + "value4,value5,value6";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertFalse(parser.isClosed());
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://www.example.com/csv/sample.csv");
        CSVParser parser = CSVParser.parse(url, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertFalse(parser.isClosed());
        parser.close();
    }

    @Test
    public void testIterator() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        parser.close();

        List<String> expectedValues = Arrays.asList("value1", "value2", "value3", "value4", "value5", "value6");
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            List<String> values = record.getValues();
            List<String> expectedValuesForRecord = expectedValues.subList(i, expectedValues.size());
            assertArrayEquals(values.toArray(new String[values.size()]), expectedValuesForRecord.toArray(new String[expectedValuesForRecord.size()]));
        }
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertNull(headerMap);

        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        long recordNumber = parser.getRecordNumber();

        assertEquals(0, recordNumber);

        parser.nextRecord();
        parser.nextRecord();

        recordNumber = parser.getRecordNumber();

        assertEquals(2, recordNumber);

        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();

        List<CSVRecord> expectedRecords = Arrays.asList(
                new CSVRecord(new String[]{"col1", "col2", "col3"}, Collections.emptyMap(), null, 1),
                new CSVRecord(new String[]{"value1", "value2", "value3"}, Collections.emptyMap(), null, 2),
                new CSVRecord(new String[]{"value4", "value5", "value6"}, Collections.emptyMap(), null, 3)
        );

        assertArrayEquals(expectedRecords.toArray(new CSVRecord[expectedRecords.size()]), records.toArray(new CSVRecord[records.size()]));

        parser.close();
    }

    @Test
    public void testGetRecordsWithEmptyList() throws IOException {
        File csvData = new File("src/test/resources/csv/empty.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);

        List<CSVRecord> records = parser.getRecords();

        List<CSVRecord> expectedRecords = Collections.emptyList();

        assertArrayEquals(expectedRecords.toArray(new CSVRecord[expectedRecords.size()]), records.toArray(new CSVRecord[records.size()]));

        parser.close();
    }

    @Test
    public void testGetRecordsWithCustomFormat() throws IOException {
        File csvData = new File("src/test/resources/csv/sample.csv");
        CSVFormat customFormat = new CSVFormat(new String[]{"col1", "col2", "col3"}, '\t');
        CSVParser parser = CSVParser.parse(csvData, customFormat);

        List<CSVRecord> records = parser.getRecords();

        List<CSVRecord> expectedRecords = Arrays.asList(
                new CSVRecord(new String[]{"col1\tcol2\tcol3"}, Collections.emptyMap(), null, 1),
                new CSVRecord(new String[]{"value1\tvalue2\tvalue3"}, Collections.emptyMap(), null, 2),
                new CSVRecord(new String[]{"value4\tvalue5\tvalue6"}, Collections.emptyMap(), null, 3)
        );

        assertArrayEquals(expectedRecords.toArray(new CSVRecord[expectedRecords.size()]), records.toArray(new CSVRecord[records.size()]));

        parser.close();
    }
}