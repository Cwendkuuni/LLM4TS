package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.get(""));
        assertNull(record.get(0));
        assertFalse(record.isMapped(""));
        assertFalse(record.isSet(""));
        assertNull(record.iterator().next());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));

        List<String> list = record.toList();
        Iterator<String> it = record.iterator();

        assertArrayEquals(values, list.toArray());
        assertSame(it, list.iterator());
        assertEquals(values[0], it.next());
        assertEquals(values[1], it.next());
        assertEquals(values[2], it.next());
        assertNull(it.hasNext());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = {"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertNull(record.getComment());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertNull(record.get("header1"));
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertNull(record.iterator().next());
    }

    @Test
    public void testCSVRecordWithMismatchedMapping() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertThrows(IllegalArgumentException.class, () -> record.get("header3"));
    }

    @Test
    public void testCSVRecordWithNegativeIndex() {
        String[] values = {"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    public void testCSVRecordWithNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertNull(record.getComment());
        assertNull(record.get("header1"));
        assertNull(record.get("header2"));
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isMapped("header2"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header2"));
        assertNull(record.iterator().next());
    }

    @Test
    public void testCSVRecordWithEmptyMapping() {
        String[] values = {"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertNull(record.getComment());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertNull(record.get("header1"));
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertNull(record.iterator().next());
    }
}