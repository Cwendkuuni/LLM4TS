package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"Alice", "Bob", "Charlie"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertEquals("Alice", record.get(0));
        assertEquals("Bob", record.get(1));
        assertEquals("Charlie", record.get(2));
        assertEquals("", record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));

        List<String> valuesList = record.toList();
        assertEquals(Arrays.asList("Alice", "Bob", "Charlie"), valuesList);

        Map<String, String> map = record.toMap();
        assertEquals(Arrays.asList("Name=Alice", "Age=Bob", "City=Charlie"), map.values());
    }

    @Test
    public void testCSVRecordWithoutValues() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        String[] values = new String[]{"Alice", "Bob", "Charlie"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(values.length + 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        String[] values = new String[]{"Alice", "Bob", "Charlie"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        String[] values = new String[]{"Alice", "Bob", "Charlie"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Unknown");
    }
}