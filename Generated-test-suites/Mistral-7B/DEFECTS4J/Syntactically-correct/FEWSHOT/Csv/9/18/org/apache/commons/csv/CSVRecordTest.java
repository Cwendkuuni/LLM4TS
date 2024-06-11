package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEqual;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithNullValuesAndMapping() {
        String[] nullValues = null;
        Map<String, Integer> nullMapping = null;
        String comment = null;
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(nullValues, nullMapping, comment, recordNumber);

        assertNull(record.get(null));
        assertNull(record.get(1));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertFalse(record.isConsistent());
        assertEquals(0, record.size());

        record = new CSVRecord(new String[0], nullMapping, comment, recordNumber);

        assertNull(record.get(null));
        assertNull(record.get(1));
        assertNull(record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithEmptyValuesAndMapping() {
        String[] emptyValues = new String[0];
        Map<String, Integer> emptyMapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(emptyValues, emptyMapping, comment, recordNumber);

        assertNull(record.get(null));
        assertNull(record.get(1));
        assertNull(record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values[0], record.get(0));
        assertEquals(values[1], record.get(1));
        assertEquals(values[2], record.get(2));
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertEquals(values.length, record.size());

        assertFalse(record.isMapped("header4"));
        assertFalse(record.isSet("header4"));
        assertFalse(record.isSet("header3"));
        assertSame(2, record.get(1).length());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
    }

    @Test
    public void testCSVRecordGetValueByEnum() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", record.get(CSVRecord.Enum.HEADER_1));
        assertEquals("value2", record.get(CSVRecord.Enum.HEADER_2));
        assertEquals("value3", record.get(CSVRecord.Enum.HEADER_3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameWithEmptyMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get("header4");
    }

    @Test
    public void testCSVRecordGetValueByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));

        assertNull(record.get("header4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetValueByNameWithInconsistentRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header4", 3);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get("header3");
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testCSVRecordSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, record.size());
    }

    @Test
    public void testCSVRecordToList() {
        String[] values = {"value1", "value2", "value3"};

        List<String> list = Arrays.asList(values);

        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertIterableEqual(list, record.iterator());
    }

    @Test
    public void testCSVRecordToMap() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        Map<String, String> map = record.toMap();

        assertEquals(mapping.size(), map.size());
        for (Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(entry.getValue(), map.get(entry.getKey()).length());
        }
    }

    private enum Enum {
        HEADER_1,
        HEADER_2,
        HEADER_3
    }
}