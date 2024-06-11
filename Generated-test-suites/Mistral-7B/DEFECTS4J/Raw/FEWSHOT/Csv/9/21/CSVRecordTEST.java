package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1L);
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(10));
        assertNull(record.get(-1));
        assertNull(record.get(Enum.class));
        assertFalse(record.isMapped("foo"));
        assertFalse(record.isSet("foo"));
        assertFalse(record.isSet("bar"));
    }

    @Test
    public void testCSVRecordWithValues() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), null, null, 1L);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
        assertThrows(NullPointerException.class, () -> record.get(null));
        assertThrows(IllegalArgumentException.class, () -> record.get(-1));
        assertThrows(IllegalArgumentException.class, () -> record.get(Enum.class));
        assertFalse(record.isMapped("foo"));
        assertFalse(record.isSet("foo"));
        assertFalse(record.isSet("bar"));
        assertFalse(record.isSet("baz"));
        assertFalse(record.isSet("quux"));
    }

    @Test
    public void testCSVRecordWithMapping() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, null, 1L);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertNull(record.get("header4"));
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header2"));
        assertFalse(record.isSet("header3"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testCSVRecordWithMappingAndComment() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, "comment", 1L);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertEquals("comment", record.getComment());
        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertNull(record.get("header4"));
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header2"));
        assertFalse(record.isSet("header3"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testCSVRecordWithMappingAndNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(new String[0], mapping, null, 1L);
        assertEquals(0, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.get(0));
        assertNull(record.get(1));
        assertNull(record.get(10));
        assertNull(record.get(-1));
        assertNull(record.get(Enum.class));
        assertFalse(record.isMapped("foo"));
        assertFalse(record.isSet("foo"));
        assertFalse(record.isSet("bar"));
        assertFalse(record.isSet("baz"));
    }

    @Test
    public void testCSVRecordGetWithNullMapping() {
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertThrows(IllegalStateException.class, () -> record.get("foo"));
    }

    @Test
    public void testCSVRecordGetWithInvalidName() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, null, 1L);
        assertThrows(IllegalArgumentException.class, () -> record.get("quux"));
    }

    @Test
    public void testCSVRecordGetWithInconsistentRecord() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, null, 1L);
        assertThrows(IllegalArgumentException.class, () -> record.get("header4"));
    }

    @Test
    public void testCSVRecordGetWithOutOfBoundsIndex() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, null, 1L);
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testCSVRecordGetWithNullValue() {
        List<String> values = Arrays.asList("foo", null, "baz");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), mapping, null, 1L);
        assertNull(record.get(1));
    }

    @Test
    public void testCSVRecordPutIn() {
        List<String> values = Arrays.asList("foo", "bar", "baz");
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values.toArray(new String[0]), null, null, 1L);
        map.putAll(record.putIn(map));
        assertEquals(Collections.singletonMap("foo", "foo"), map);
        assertEquals(Collections.singletonMap("bar", "bar"), record.putIn(map));
        assertEquals(Collections.singletonMap("baz", "baz"), record.putIn(map));
    }

    @Test
    public void testCSVRecordPutInWithEmptyMapping() {
        String[] values = new String[0];
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        map.putAll(record.putIn(map));
        assertEquals(Collections.emptyMap(), map);
    }
}