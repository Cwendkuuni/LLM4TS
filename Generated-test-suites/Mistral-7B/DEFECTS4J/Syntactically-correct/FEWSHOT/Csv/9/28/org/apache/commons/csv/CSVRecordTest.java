package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
    }

    @Test
    public void testGetByEnum() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get(Enum.ONE));
        assertEquals("bar", record.get(Enum.TWO));
        assertEquals("baz", record.get(Enum.THREE));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByEnumNoMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(Enum.ONE);
    }

    @Test
    public void testGetByIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertNull(record.get(3));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertNull(record.get("nonExistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("header4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidRecord() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("header1");
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment";
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 123;
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        mapping = null;
        assertFalse(record.isConsistent());

        values = new String[3];
        assertFalse(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        values = new String[4];
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("nonExistent"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("nonExistent"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expected = Arrays.asList("foo", "bar", "baz");

        Iterator<String> iterator = record.iterator();

        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> result = record.putIn(map);

        assertEquals(map, result);
        assertEquals("foo", map.get("header1"));
        assertEquals("bar", map.get("header2"));
        assertEquals("baz", map.get("header3"));
        assertNull(map.get("nonExistent"));
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expected = "[foo, bar, baz]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String[] expected = {"foo", "bar", "baz"};
        String[] actual = record.values();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmptyValues() {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isEmpty());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertTrue(record.iterator().isEmpty());
        assertTrue(record.toMap().isEmpty());
        assertTrue(record.isEmptyValues());
    }

    @Test
    public void testEmptyMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = Collections.emptyMap();

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isEmpty());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertTrue(record.iterator().isEmpty());
        assertTrue(record.toMap().isEmpty());
        assertTrue(record.isEmptyValues());
    }

    @Test
    public void testCloneRecord() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord originalRecord = new CSVRecord(values, mapping, null, 1);

        CSVRecord clonedRecord = originalRecord.clone();

        assertEquals(originalRecord, clonedRecord);
        assertSame(originalRecord.mapping, clonedRecord.mapping);
        assertSame(originalRecord.values, clonedRecord.values);
        assertSame(originalRecord.recordNumber, clonedRecord.recordNumber);
        assertSame(originalRecord.comment, clonedRecord.comment);
    }

}