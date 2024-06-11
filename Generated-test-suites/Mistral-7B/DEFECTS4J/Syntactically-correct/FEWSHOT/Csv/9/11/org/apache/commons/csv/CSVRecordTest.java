package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordEmpty() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertEquals(0, record.getRecordNumber());
        assertEquals(null, record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("test"));
        assertFalse(record.isSet("test"));
        assertEquals(0, record.size());
        assertEquals(0, record.values().length);
        assertNull(record.get("test"));
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"test1", "test2", "test3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(0, record.getRecordNumber());
        assertEquals(null, record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isSet("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isMapped("col3"));
        assertTrue(record.isSet("col3"));
        assertEquals(3, record.size());
        assertArrayEquals(values, record.values());
        assertEquals("test1", record.get("col1"));
        assertEquals("test2", record.get("col2"));
        assertEquals("test3", record.get("col3"));
    }

    @Test
    public void testCSVRecordNegativeIndex() {
        String[] values = new String[]{"test1", "test2", "test3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get("col4"));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"test1", "test2", "test3"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("test"));
        assertFalse(record.isSet("test"));
        assertEquals(3, record.size());
        assertArrayEquals(values, record.values());
        assertNull(record.get("test"));
    }

    @Test
    public void testCSVRecordGetString() {
        String[] values = new String[]{"test1", "test2", "test3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("test1,test2,test3", record.toString());
    }

    @Test
    public void testCSVRecordToMap() {
        String[] values = new String[]{"test1", "test2", "test3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("test1", map.get("col1"));
        assertEquals("test2", map.get("col2"));
        assertEquals("test3", map.get("col3"));
    }

}