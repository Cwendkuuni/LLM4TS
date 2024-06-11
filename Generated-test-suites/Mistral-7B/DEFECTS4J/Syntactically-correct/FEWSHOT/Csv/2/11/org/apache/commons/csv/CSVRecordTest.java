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
    public void testCSVRecordWithEmptyArrayValues() {
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 0);
        assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"name1", "value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertArrayEquals(values, record.values());
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertEquals(values.length, record.size());

        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isSet("name1"));

        try {
            record.get("name1");
            throw new AssertionError("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCSVRecordWithMappingSizeMismatch() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name2", 0);
        mapping.put("name3", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertArrayEquals(values, record.values());
        assertEquals(null, record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertEquals(values.length, record.size());

        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithMappingSizeMatch() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertArrayEquals(values, record.values());
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertEquals(values.length, record.size());

        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isSet("name1"));
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertNull(record.get("name3"));
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("[name1, value1, value2]", record.toString());
    }

    @Test
    public void testCSVRecordGetRecordNumber() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordIterable() {
        String[] values = new String[]{"name1", "value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        Iterator<String> iterator = record.iterator();
        assertEquals("name1", iterator.next());
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }
}