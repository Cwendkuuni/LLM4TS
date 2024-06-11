package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("foo", record.get(TestEnum.FOO));
        assertEquals("bar", record.get(TestEnum.BAR));
    }

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
    }

    @Test
    public void testGetString() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        assertEquals("foo", record.get("foo"));
        assertEquals("bar", record.get("bar"));
    }

    @Test
    public void testGetString_noMapping() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        try {
            record.get("foo");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void testGetString_mappingNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("bar", 1);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        try {
            record.get("foo");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for foo not found, expected one of [bar]", e.getMessage());
        }
    }

    @Test
    public void testGetString_indexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        CSVRecord record = new CSVRecord(new String[]{"foo"}, mapping, null, 1);
        try {
            record.get("foo");
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'foo' is 0 but CSVRecord only has 1 values!", e.getMessage());
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{}, null, null, 2);
        assertEquals(2, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_true() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_false() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped_true() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        CSVRecord record = new CSVRecord(new String[]{}, mapping, null, 1);
        assertTrue(record.isMapped("foo"));
    }

    @Test
    public void testIsMapped_false() {
        CSVRecord record = new CSVRecord(new String[]{}, null, null, 1);
        assertFalse(record.isMapped("foo"));
    }

    @Test
    public void testIsSet_true() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        CSVRecord record = new CSVRecord(new String[]{"value"}, mapping, null, 1);
        assertTrue(record.isSet("foo"));
    }

    @Test
    public void testIsSet_false() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 1);
        CSVRecord record = new CSVRecord(new String[]{"value"}, mapping, null, 1);
        assertFalse(record.isSet("foo"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("foo", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        map = record.putIn(map);
        assertEquals("foo", map.get("foo"));
        assertEquals("bar", map.get("bar"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(Arrays.asList("foo", "bar"), record.toList());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, mapping, null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("foo", map.get("foo"));
        assertEquals("bar", map.get("bar"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals("[foo, bar]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        assertEquals(new String[]{"foo", "bar"}, record.values());
    }

    private enum TestEnum {
        FOO,
        BAR
    }
}