package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testEmptyCSVRecord() {
        CSVRecord record = new CSVRecord(EMPTY_STRING_ARRAY, null, null, 0L);
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertEquals("baz", record.get("header3"));
        assertEquals(null, record.get("header4"));
        assertEquals("1", record.get(new Integer(0)).toString());
        assertEquals(1L, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertEquals(3, record.size());
        assertFalse(record.isMapped("nonExistentHeader"));
        assertArrayEquals(new String[] {"foo", "bar", "baz"}, record.values());
        assertSame(record, record.iterator().next());

        Map<String, String> map = record.toMap();
        assertEquals("foo", map.get("header1"));
        assertEquals("bar", map.get("header2"));
        assertEquals("baz", map.get("header3"));
    }

    @Test
    public void testCSVRecordWithNullValues() {
        String[] values = new String[] {"foo", null, "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("foo", record.get(0));
        assertNull(record.get(1));
        assertEquals("bar", record.get(2));
        assertEquals("bar", record.get("header3"));
        assertArrayEquals(new String[] {"foo", null, "bar"}, record.values());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testGetWithNonExistentName() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("nonExistentHeader"));
    }

    @Test
    public void testGetWithNullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithEmptyMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithMismatchingMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 3); // wrong index
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("header3"));
    }

    @Test
    public void testCSVRecordWithMissingHeader() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("header4"));
    }

    @Test
    public void testCSVRecordWithInconsistentSize() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 4); // wrong index
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("header3"));
    }

    @Test
    public void testCSVRecordWithTooManyHeaders() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 5); // wrong index
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("header4"));
    }

    @Test
    public void testCSVRecordWithNegativeIndex() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
    }
}