package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithNoMappingAndNoComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithMappingAndNoComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
        assertSame("header1", record.get("header1"));
        assertSame("bar", record.get("header2"));
        assertSame("baz", record.get("header3"));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithMappingAndComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
        assertSame("foo", record.get("header1"));
        assertSame("bar", record.get("header2"));
        assertSame("baz", record.get("header3"));
        assertEquals(comment, record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGet_UnknownColumn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
    }

    @Test
    public void testGet_EmptyMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertThrows(IllegalStateException.class, () -> record.get("header1"));
    }

    @Test
    public void testGet_EmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(0));
    }

    @Test
    public void testGet_NullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
        assertNull(record.get(0));
    }

    @Test
    public void testIterate() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expected = Arrays.asList("foo", "bar", "baz");

        Iterator<String> iterator = record.iterator();

        assertIterableEquals(expected, iterator);
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "foo");
        expected.put("header2", "bar");
        expected.put("header3", "baz");

        map.putAll(record.putIn(map));

        assertEquals(expected, map);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "foo");
        expected.put("header2", "bar");
        expected.put("header3", "baz");

        Map<String, String> map = record.toMap();

        assertEquals(expected, map);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String expected = "CSVRecord[values=[foo, bar, baz], mapping=null, comment=null, recordNumber=1]";

        assertEquals(expected, record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals(values, record.values());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        int expected = values.length;

        assertEquals(expected, record.size());
    }
}