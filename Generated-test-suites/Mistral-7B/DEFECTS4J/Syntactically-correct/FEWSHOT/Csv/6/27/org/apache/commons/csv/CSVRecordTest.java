package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEqual;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testRecordWithValuesAndMapping() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(4, record.size());
        assertIterableEqual(Arrays.asList("one", "two", "three", "four"), record);
        assertSame("one", record.get(0));
        assertSame("two", record.get(1));
        assertSame("three", record.get(2));
        assertSame("four", record.get(3));
        assertEquals("one", record.get("header1"));
        assertEquals("two", record.get("header2"));
        assertEquals("three", record.get("header3"));
        assertEquals("four", record.get("header4"));
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());

        Map<String, String> map = record.toMap();
        assertIterableEqual(Arrays.asList("one", "two", "three", "four"), map.values());
    }

    @Test
    public void testRecordWithValuesButNoMapping() {
        String[] values = new String[] { "one", "two", "three", "four" };
        CSVRecord record = new CSVRecord(values, null, null, 0);

        assertEquals(4, record.size());
        assertIterableEqual(Arrays.asList("one", "two", "three", "four"), record);
        assertSame("one", record.get(0));
        assertSame("two", record.get(1));
        assertSame("three", record.get(2));
        assertSame("four", record.get(3));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordWithNoValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
    }

    @Test
    public void testGetWithInvalidName() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        try {
            record.get("invalidName");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for invalidName not found, expected one of [header1, header2, header3, header4]", e
                    .getMessage());
        }
    }

    @Test
    public void testRecordConsistency() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        mapping.put("header5", 4);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isConsistent());

        mapping.remove("header5");
        assertFalse(record.isConsistent());
    }

    @Test
    public void testRecordComment() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        String comment = "comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 0);

        assertEquals("comment", record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testRecordRecordNumber() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        long recordNumber = 5;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testRecordEquals() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 0);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 0);

        assertEquals(record1, record2);
        assertEquals(record2, record1);

        String[] values2 = new String[] { "one", "two", "three", "five" };
        CSVRecord record3 = new CSVRecord(values2, mapping, null, 0);

        assertNotEquals(record1, record3);
        assertNotEquals(record3, record1);
    }

    @Test
    public void testRecordHashCode() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        int hashCode = record.hashCode();

        String[] values2 = new String[] { "one", "two", "three", "four" };
        CSVRecord record2 = new CSVRecord(values2, mapping, null, 0);

        assertEquals(hashCode, record2.hashCode());

        String[] values3 = new String[] { "one", "two", "three", "five" };
        CSVRecord record3 = new CSVRecord(values3, mapping, null, 0);

        assertNotEquals(hashCode, record3.hashCode());
    }

    @Test
    public void testRecordToString() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("[one, two, three, four]", record.toString());
    }

    @Test
    public void testRecordValues() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertArrayEquals(values, record.values());
    }

    @Test
    public void testRecordIterator() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        List<String> list = Arrays.asList(record);

        Iterator<String> iterator = record.iterator();

        for (String value : list) {
            assertEquals(value, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRecordPutIn() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 0);
        map.putAll(record.putIn(map));

        assertEquals(4, map.size());
        assertArrayEquals(values, map.values().toArray());
    }

    @Test
    public void testRecordToMap() {
        String[] values = new String[] { "one", "two", "three", "four" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        mapping.put("header4", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> map = record.toMap();
        assertEquals(4, map.size());
        assertArrayEquals(values, map.values().toArray(new String[0]));
    }
}