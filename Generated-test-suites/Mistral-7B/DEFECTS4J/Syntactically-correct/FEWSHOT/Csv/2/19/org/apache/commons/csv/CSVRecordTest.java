package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String[] values = new String[] { "name", "age", "city" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
        assertEquals("name", record.get("name"));
        assertEquals("age", record.get("age"));
        assertEquals("city", record.get("city"));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("city"));
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("city"));
        assertFalse(record.isSet("nonExistentColumn"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.get("name"));
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.get("name"));
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[] { "name", "age", "city" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
    }

    @Test
    public void testCSVRecordInvalidMappingSize() {
        String[] values = new String[] { "name", "age", "city" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        mapping.put("invalid", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordComment() {
        String[] values = new String[] { "name", "age", "city" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testCSVRecordRecordNumber() {
        String[] values = new String[] { "name", "age", "city" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

}