package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertNull(record.getComment());
        assertEquals(0, record.size());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
        assertNull(record.get("name"));
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(2));
        assertEquals(0, record.values().length);
        assertTrue(Arrays.equals(record.values(), new String[0]));
        assertEquals(record.toString(), "[]");
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertEquals("name1", record.get("name1"));
        assertEquals("name2", record.get("name2"));
        assertEquals("name3", record.get("name3"));
        assertEquals("name1", record.get(0));
        assertEquals("name2", record.get(1));
        assertEquals("name3", record.get(2));
        assertNull(record.getComment());
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isSet("name3"));
        List<String> valuesList = record.toList();
        assertEquals(3, valuesList.size());
        assertTrue(Arrays.equals(valuesList.toArray(), values));
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("name1", map.get("name1"));
        assertEquals("name2", map.get("name2"));
        assertEquals("name3", map.get("name3"));
        assertEquals(record.toString(), Arrays.toString(values));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNoMapping() {
        String[] values = {"name1", "name2"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);
        record.get("name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = {"name1", "name2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        record.get(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidName() {
        String[] values = {"name1", "name2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        record.get("invalidName");
    }
}