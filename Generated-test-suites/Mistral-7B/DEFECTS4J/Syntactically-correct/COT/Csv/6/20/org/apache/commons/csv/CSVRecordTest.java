package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidData() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        assertEquals("Name", record.get("Name"));
        assertEquals("John Doe", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("30", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertEquals("New York", record.get("City"));
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetValueByEnum() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        assertEquals("John Doe", record.get(CSVRecord.Name.class));
        assertEquals("30", record.get(CSVRecord.Age.class));
        assertEquals("New York", record.get(CSVRecord.City.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoHeaderMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        record.get("Name2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameMapping() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        record.get("Age2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndex() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        record.get(10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        String csvData = "Name,Age\nJohn Doe,30\nJane Doe,35";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        record.get(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMappingSize() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        record.mapping = new HashMap<>(3) {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
            put("Comment", 3);
        }};
        record.get("City");
    }

    @Test
    public void testGetComment() {
        String csvData = "Name,Age,City,Comment\nJohn Doe,30,New York,Hello\nJane Doe,35,Chicago,World";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        assertEquals("Hello", record.getComment());
        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        assertTrue(record.isConsistent());

        String csvDataMissingColumn = "Name,Age\nJohn Doe,30";
        List<CSVRecord> recordsMissingColumn = parseCSV(csvDataMissingColumn);
        CSVRecord recordMissingColumn = recordsMissingColumn.get(0);
        assertFalse(recordMissingColumn.isConsistent());

        String csvDataExtraColumn = "Name,Age,City,Comment\nJohn Doe,30,New York,Hello\nJane Doe,35,Chicago,World\n";
        List<CSVRecord> recordsExtraColumn = parseCSV(csvDataExtraColumn);
        CSVRecord recordExtraColumn = recordsExtraColumn.get(0);
        assertFalse(recordExtraColumn.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Comment"));
    }

    @Test
    public void testIsSet() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Comment"));

        String csvDataMissingValue = "Name,Age,City\nJohn Doe,30\nJane Doe,35,Chicago";
        List<CSVRecord> recordsMissingValue = parseCSV(csvDataMissingValue);
        CSVRecord recordMissingValue = recordsMissingValue.get(1);
        assertFalse(recordMissingValue.isSet("City"));
    }

    @Test
    public void testIterator() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        List<String> values = new ArrayList<>();
        for (String val : record) {
            values.add(val);
        }

        assertEquals(Arrays.asList("Name", "John Doe", "Age", "30", "City", "New York"), values);
    }

    @Test
    public void testPutIn() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        Map<String, String> map = new HashMap<>();
        record.putIn(map);

        assertEquals(Arrays.asList("Name", "John Doe", "Age", "30", "City", "New York"), map.values());
    }

    @Test
    public void testSize() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        assertEquals(6, record.size());
    }

    @Test
    public void testToString() {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,35,Chicago";
        List<CSVRecord> records = parseCSV(csvData);
        CSVRecord record = records.get(0);

        assertEquals("[Name, John Doe, Age, 30, City, New York]", record.toString());
    }

    private List<CSVRecord> parseCSV(String csvData) {
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try (CSVParser parser = new CSVParser(new StringReader(csvData), format)) {
            return parser.getRecords();
        }
    }
}