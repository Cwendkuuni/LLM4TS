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
    public void testCSVRecord_DefaultConstructor() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.comment);
        assertNull(record.mapping);
        assertArrayEquals(new String[0], record.values);
        assertEquals(-1, record.recordNumber);
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("test"));
        assertFalse(record.isSet("test"));
        assertNull(record.get("test"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_Constructor() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("comment", record.comment);
        assertEquals(mapping, record.mapping);
        assertArrayEquals(values, record.values());
        assertEquals(1, record.recordNumber);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));
        assertEquals("comment", record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertThrows(IllegalArgumentException.class, () -> record.get("not_mapped"));
        assertNull(record.get("header4"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testGet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        assertThrows(IllegalArgumentException.class, () -> record.get("not_mapped"));
        assertNull(record.get("header4"));
        assertNull(record.get(3));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testGetEnum() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("value1", record.get(EnumOne.HEADER_ONE));
        assertEquals("value2", record.get(EnumTwo.HEADER_TWO));
        assertEquals("value3", record.get(EnumThree.HEADER_THREE));
        assertThrows(IllegalArgumentException.class, () -> record.get(EnumFour.NOT_MAPPED));
        assertNull(record.get(EnumFive.HEADER_FIVE));
        assertNull(record.get(EnumSix.HEADER_SIX));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertTrue(record.isConsistent());
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("header1", 0);
        mapping2.put("header2", 1);
        mapping2.put("header3", 2);
        mapping2.put("header4", 3);
        CSVRecord record2 = new CSVRecord(values, mapping2, "comment", 1);
        assertFalse(record2.isConsistent());
        String[] values2 = {"value1", "value2"};
        Map<String, Integer> mapping3 = new HashMap<>();
        mapping3.put("header1", 0);
        mapping3.put("header2", 1);
        CSVRecord record3 = new CSVRecord(values2, mapping3, "comment", 1);
        assertFalse(record3.isConsistent());
        String[] values3 = {"value1", "value2", "value3", "value4"};
        Map<String, Integer> mapping4 = new HashMap<>();
        mapping4.put("header1", 0);
        mapping4.put("header2", 1);
        mapping4.put("header3", 2);
        CSVRecord record4 = new CSVRecord(values3, mapping4, "comment", 1);
        assertTrue(record4.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("not_mapped"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("not_mapped"));
        assertFalse(record.isSet("header4"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        List<String> valuesList = record.toList();
        Iterator<String> iterator = record.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String value = iterator.next();
            assertEquals(valuesList.get(i), value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> map2 = record.putIn(map);
        assertEquals(map, map2);
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals(3, record.size());
        String[] values2 = {"value1"};
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("header1", 0);
        CSVRecord record2 = new CSVRecord(values2, mapping2, "comment", 1);
        assertEquals(1, record2.size());
    }

    @Test
    public void testToList() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        List<String> valuesList = record.toList();
        assertEquals(Arrays.asList(values), valuesList);
    }

    @Test
    public void testToMap() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        Map<String, String> map = record.toMap();
        assertSame(map, record.putIn(map));
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    private enum EnumOne {
        HEADER_ONE
    }

    private enum EnumTwo {
        HEADER_TWO
    }

    private enum EnumThree {
        HEADER_THREE
    }

    private enum EnumFour {
        NOT_MAPPED
    }

    private enum EnumFive {
        HEADER_FIVE
    }

    private enum EnumSix {
        HEADER_SIX
    }
}