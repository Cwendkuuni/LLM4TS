package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1L);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertNull(record.get(Enum.class, "headerName"));
        assertNull(record.get(0));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("headerName"));
        assertFalse(record.isSet("headerName"));
        assertNull(record.getRecordNumber());
        assertSame(record, record);
        assertSame(record.getClass(), CSVRecord.class);
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(3, record.size());
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value1", record.get(0));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isSet("header1"));
        assertSame(record.getRecordNumber(), 1L);
        assertNull(record.getComment());
        assertSame(record, record);
        assertSame(record.getClass(), CSVRecord.class);
    }

    @Test
    public void testGetNullValue() {
        String[] values = {"", "", ""};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(null, record.get("header1"));
        assertEquals(null, record.get(Enum.class, "header1"));
        assertEquals(null, record.get(0));
    }

    @Test
    public void testGetInvalidIndex() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testGetInvalidName() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("invalidName"));
        assertThrows(IllegalArgumentException.class, () -> record.get(Enum.class, "invalidName"));
    }

    @Test
    public void testGetInvalidEnum() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get(Enum.class, "Header1"));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment for record";
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(new String[] {"header1", "value1", "value2"}, mapping, comment, 1L);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 2L;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(new String[] {"header1", "value1", "value2"}, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());

        String[] values2 = {"header1", "value1", "value2", "value3"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("header1", 0);
        mapping2.put("header2", 1);
        mapping2.put("header3", 2);
        mapping2.put("header4", 3);
        CSVRecord record2 = new CSVRecord(values2, mapping2, null, 1L);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("header1"));
        assertFalse(record.isMapped("header3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("header1"));
        assertFalse(record.isSet("header3"));
    }

    @Test
    public void testToList() {
        String[] values = {"header1", "value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        List<String> list = record.toList();
        assertArrayEquals(values, list.toArray());
    }

    @Test
    public void testToMap() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = record.toMap();
        List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
        assertArrayEquals(values, entries.stream().map(e -> e.getValue()).toArray());
    }

    @Test
    public void testToString() {
        String[] values = {"header1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("[header1, value1, value2]", record.toString());
    }
}