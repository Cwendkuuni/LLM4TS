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
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals("", record.comment);
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistentName"));
        assertSame("foo", record.get("name1"));
        assertSame("bar", record.get("name2"));
        assertSame("baz", record.get("name3"));
        assertNull(record.get("nonExistentName"));
        assertEquals(3, record.size());
        List<String> list = record.toList();
        assertArrayEquals(values, list.toArray());
        Map<String, String> map = record.toMap();
        assertEquals(mapping, map);
    }

    @Test
    public void testGetWithEnum() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertEquals("foo", record.get(Name.NAME1));
        assertEquals("bar", record.get(Name.NAME2));
        assertEquals("baz", record.get(Name.NAME3));
    }

    @Test
    public void testGetWithInvalidEnum() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertThrows(IllegalArgumentException.class, () -> record.get(Name.NAME4));
    }

    @Test
    public void testGetWithNullMapping() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, "", 0);

        assertThrows(IllegalStateException.class, () -> record.get("name1"));
    }

    @Test
    public void testGetWithInvalidIndex() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testGetWithInvalidIndexAndNullMapping() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, "", 0);

        assertThrows(IllegalStateException.class, () -> record.get(0));
    }

    @Test
    public void testGetComment() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 0);

        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMappingSize() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistentName"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"foo", "bar", "baz", "qux"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        mapping.put("name4", 3);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("name5"));
    }

    @Test
    public void testToString() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "", 0);

        assertEquals("[foo, bar, baz]", record.toString());
    }

    private enum Name {
        NAME1, NAME2, NAME3, NAME4
    }
}