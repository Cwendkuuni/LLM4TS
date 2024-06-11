package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstructionWithValuesAndMapping() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertEquals(3, record.size());
        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
    }

    @Test
    public void testCSVRecordConstructionWithValuesAndNoMapping() {
        String[] values = {"name", "age", "city"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertEquals(3, record.size());
        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
    }

    @Test
    public void testCSVRecordGet() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
        assertNull(record.get(3));
        assertEquals("name", record.get("name"));
        assertEquals("age", record.get("age"));
        assertEquals("city", record.get("city"));
        assertNull(record.get("unknown"));
    }

    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isConsistent());
        mapping.put("unknown", 3);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordIsMapped() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("city"));
        assertFalse(record.isMapped("unknown"));
    }

    @Test
    public void testCSVRecordIsSet() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("city"));
        assertFalse(record.isSet("unknown"));
    }

    @Test
    public void testCSVRecordGetComment() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);

        assertEquals(comment, record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String[] values = {"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());
        assertEquals(1L, record.getRecordNumber());
    }
}