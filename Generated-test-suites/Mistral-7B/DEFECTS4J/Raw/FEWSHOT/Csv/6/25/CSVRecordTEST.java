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
    public void testCSVRecordWithEmptyArray() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(values.length, record.size());
        assertSame(values, record.values());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetValueByEnum() {
        String[] values = new String[]{"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("value1", record.get(Enum.ONE));
    }

    @Test
    public void testGetValueByName() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("value1", record.get("A"));
        assertEquals("value2", record.get("B"));
        assertEquals("value3", record.get("C"));
    }

    @Test
    public void testGetValueByNameWithNullMapping() {
        String[] values = new String[]{"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertThrows(IllegalStateException.class, () -> record.get("A"));
    }

    @Test
    public void testGetValueByNameWithInvalidName() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertThrows(IllegalArgumentException.class, () -> record.get("D"));
    }

    @Test
    public void testGetValueByNameWithInvalidIndex() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertThrows(IllegalArgumentException.class, () -> record.get("C"),
                "Expected IndexOutOfBoundsException, but got IllegalArgumentException");
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));
        assertFalse(record.isMapped("D"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertFalse(record.isSet("C"));
        assertFalse(record.isSet("D"));
    }

    @Test
    public void testGetComment() {
        String comment = "Comment for record";
        CSVRecord record = new CSVRecord(null, null, comment, 1);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 12345L;
        CSVRecord record = new CSVRecord(null, null, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("A", 0);
        mapping2.put("B", 1);
        mapping2.put("D", 2);
        CSVRecord record2 = new CSVRecord(values, mapping2, null, 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);
        map.putAll(record.putIn(map));
        List<String> list = Arrays.asList(map.values().toArray(new String[0]));
        assertArrayEquals(values, list.toArray(new String[0]));
    }
}