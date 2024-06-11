package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File testFile = new File("src/test/resources/CSVTestData.csv");
        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        // Check that we have read all records
        assertEquals(5, records.size());

        // Check contents of first record
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1", "C1"}, record1.toArray());
        assertEquals(0, record1.getRecordNumber());

        // Check contents of second record
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2", "C2"}, record2.toArray());
        assertEquals(1, record2.getRecordNumber());

        // Check that we can parse a record with empty values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"", "", ""}, record3.toArray());
        assertEquals(2, record3.getRecordNumber());

        // Check contents of fourth record
        CSVRecord record4 = records.get(4);
        assertArrayEquals(new String[]{"A5", "B5", "C5"}, record4.toArray());
        assertEquals(4, record4.getRecordNumber());

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String testString = "A;B;C\nA1,B1,C1\nA2,B2,C2\n\nA5,B5,C5";
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(testString.getBytes())), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        // Check that we have read all records
        assertEquals(5, records.size());

        // Check contents of first record
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A", "B", "C"}, record1.toArray());
        assertEquals(0, record1.getRecordNumber());

        // Check contents of second record
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A1", "B1", "C1"}, record2.toArray());
        assertEquals(1, record2.getRecordNumber());

        // Check that we can parse a record with empty values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"", "", ""}, record3.toArray());
        assertEquals(2, record3.getRecordNumber());

        // Check contents of fourth record
        CSVRecord record4 = records.get(4);
        assertArrayEquals(new String[]{"A5", "B5", "C5"}, record4.toArray());
        assertEquals(4, record4.getRecordNumber());

        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL testURL = new URL("http://example.com/data.csv");
        CSVParser parser = CSVParser.parse(testURL, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        // Check that we have read all records
        assertEquals(5, records.size());

        // Check contents of first record
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1", "C1"}, record1.toArray());
        assertEquals(0, record1.getRecordNumber());

        // Check contents of second record
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2", "C2"}, record2.toArray());
        assertEquals(1, record2.getRecordNumber());

        // Check that we can parse a record with empty values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"", "", ""}, record3.toArray());
        assertEquals(2, record3.getRecordNumber());

        // Check contents of fourth record
        CSVRecord record4 = records.get(4);
        assertArrayEquals(new String[]{"A5", "B5", "C5"}, record4.toArray());
        assertEquals(4, record4.getRecordNumber());

        parser.close();
    }

    @Test
    public void testParseIterator() throws IOException {
        File testFile = new File("src/test/resources/CSVTestData.csv");
        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }

        // Check that we have read all records
        assertEquals(5, records.size());

        // Check contents of first record
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"A1", "B1", "C1"}, record1.toArray());
        assertEquals(0, record1.getRecordNumber());

        // Check contents of second record
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"A2", "B2", "C2"}, record2.toArray());
        assertEquals(1, record2.getRecordNumber());

        // Check that we can parse a record with empty values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"", "", ""}, record3.toArray());
        assertEquals(2, record3.getRecordNumber());

        // Check contents of fourth record
        CSVRecord record4 = records.get(4);
        assertArrayEquals(new String[]{"A5", "B5", "C5"}, record4.toArray());
        assertEquals(4, record4.getRecordNumber());

        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        File testFile = new File("src/test/resources/CSVTestData.csv");
        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);

        List<CSVRecord> records1 = parser.getRecords();

        // Check that we have read all records
        assertEquals(5, records1.size());

        // Check contents of first record
        CSVRecord record1 = records1.get(0);
        assertArrayEquals(new String[]{"A1", "B1", "C1"}, record1.toArray());
        assertEquals(0, record1.getRecordNumber());

        // Check contents of second record
        CSVRecord record2 = records1.get(1);
        assertArrayEquals(new String[]{"A2", "B2", "C2"}, record2.toArray());
        assertEquals(1, record2.getRecordNumber());

        // Check that we can parse a record with empty values
        CSVRecord record3 = records1.get(2);
        assertArrayEquals(new String[]{"", "", ""}, record3.toArray());
        assertEquals(2, record3.getRecordNumber());

        // Check contents of fourth record
        CSVRecord record4 = records1.get(4);
        assertArrayEquals(new String[]{"A5", "B5", "C5"}, record4.toArray());
        assertEquals(4, record4.getRecordNumber());

        List<CSVRecord> records2 = parser.getRecords();

        // Check that we have read the same records again
        assertEquals(records1, records2);

        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        File testFile = new File("src/test/resources/CSVTestDataNoHeader.csv");
        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertTrue(headerMap.isEmpty());

        parser.close();

        testFile = new File("src/test/resources/CSVTestData.csv");
        parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);

        headerMap = parser.getHeaderMap();
        Map<String, Integer> expectedHeaderMap = new LinkedHashMap<>();
        expectedHeaderMap.put("A", 0);
        expectedHeaderMap.put("B", 1);
        expectedHeaderMap.put("C", 2);
        assertEquals(expectedHeaderMap, headerMap);

        parser.close();
    }
}