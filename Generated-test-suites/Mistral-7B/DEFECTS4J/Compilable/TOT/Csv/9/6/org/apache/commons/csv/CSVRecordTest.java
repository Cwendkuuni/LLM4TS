package org.apache.commons.csv;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        // Given a CSV record with valid values
        String csv = "Name,Age,City\nAlice,25,New York\nBob,30,Los Angeles\n";
        List<CSVRecord> records = parseCSV(csv);
        CSVRecord record = records.get(0);

        // When retrieving values by name
        assertEquals("Alice", record.get("Name"));
        assertEquals("25", record.get("Age"));
        assertEquals("New York", record.get("City"));

        // When retrieving values by index
        assertEquals("Alice", record.get(0));
        assertEquals("25", record.get(1));
        assertEquals("New York", record.get(2));

        // When retrieving values by enum
        assertEquals("Alice", record.get(CSVRecordTest.Name.NAME));
        assertEquals("25", record.get(CSVRecordTest.Age.AGE));
        assertEquals("New York", record.get(CSVRecordTest.City.CITY));

        // When checking if a column is mapped
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));

        // When checking if a column has a value
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));

        // When checking if the record is consistent
        assertTrue(record.isConsistent());

        // Given a CSV record with empty values
        csv = "Name,Age,City\nAlice,,New York\nBob,30,,Los Angeles\n";
        records = parseCSV(csv);
        record = records.get(0);

        // When retrieving values by name with empty values
        assertEquals("", record.get("Name"));
        assertEquals("", record.get("Age"));
        assertEquals("New York", record.get("City"));

        // When checking if a column is mapped and has no value
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isSet("Name"));

        // Given a CSV record with a missing column
        csv = "Name,Age\nAlice,25\nBob,30";
        records = parseCSV(csv);
        record = records.get(0);

        // When retrieving a value with a missing column
        try {
            record.get("City");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Given a CSV record with invalid mapping
        csv = "Name,Age,City\nAlice,25,New York\nBob,30,Los Angeles\n";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 2);
        mapping.put("Age", 0);
        mapping.put("City", 3);
        records = parseCSV(csv, mapping);
        record = records.get(0);

        // When retrieving a value with an invalid index
        try {
            record.get(3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private List<CSVRecord> parseCSV(String csv) {
        return parseCSV(csv, null);
    }

    private List<CSVRecord> parseCSV(String csv, Map<String, Integer> mapping) {
        try (CSVParser parser = new CSVParser(new StringReader(csv), CSVFormat.DEFAULT)) {
            return parser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    enum Name {
        NAME
    }

    enum Age {
        AGE
    }

    enum City {
        CITY
    }
}