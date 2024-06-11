package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertMapEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] emptyValues = new String[0];
        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord record = new CSVRecord(emptyValues, emptyMapping, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertNull(record.get(""));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped(""));
        assertFalse(record.isSet(""));
        assertTrue(Arrays.equals(emptyValues, record.values()));
        assertTrue(Arrays.equals(emptyValues, record.values()));
        assertMapEquals(emptyMapping, record.toMap());
        assertSame(record, record.clone());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertSame(values, record.values());
        assertSame(mapping, record.toMap().keySet());
        assertMapEquals(mapping, record.toMap());
        assertSame(record, record.clone());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithValuesAndComment() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, "comment", 0);
        assertEquals(3, record.size());
        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));
        assertEquals("baz", record.get("header3"));
        assertSame(values, record.values());
        assertSame(mapping, record.toMap().keySet());
        assertMapEquals(mapping, record.toMap());
        assertEquals("comment", record.getComment());
        assertTrue(Arrays.equals(values, record.values()));
        assertSame(record, record.clone());
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertThrows(IllegalStateException.class, () -> record.get("header1"));
    }

    @Test
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertThrows(IllegalArgumentException.class, () -> record.get(3));
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isConsistent());
        assertEquals(values.length, record.size());
        assertArrayEquals(values, record.values());
        assertMapEquals(new HashMap<>(), record.toMap());
        assertSame(record, record.clone());
    }

    @Test
    public void testCSVRecordWithConsistentMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithInconsistentMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header4", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }
}