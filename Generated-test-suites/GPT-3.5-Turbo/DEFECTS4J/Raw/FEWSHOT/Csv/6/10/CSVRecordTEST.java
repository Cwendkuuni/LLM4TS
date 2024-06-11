package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setup() {
        values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        mapping.put("value3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGet_ByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGet_ByName() {
        assertEquals("value1", record.get("value1"));
        assertEquals("value2", record.get("value2"));
        assertEquals("value3", record.get("value3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_ByName_NoMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_ByName_InvalidColumnName() {
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_ByName_IndexOutOfBounds() {
        record.get("value4");
    }

    @Test
    public void testGet_ByEnum() {
        EnumTest enumTest = EnumTest.VALUE1;
        assertEquals("value1", record.get(enumTest));
    }

    @Test
    public void testGetComment() {
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_WithMapping() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_NoMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("value1"));
        assertTrue(record.isMapped("value2"));
        assertTrue(record.isMapped("value3"));
        assertFalse(record.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("value1"));
        assertTrue(record.isSet("value2"));
        assertTrue(record.isSet("value3"));
        assertFalse(record.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(values.length, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertMapValues(map);
    }

    @Test
    public void testSize() {
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(Arrays.asList(values), record.toList());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertMapValues(map);
    }

    private void assertMapValues(Map<String, String> map) {
        assertEquals("value1", map.get("value1"));
        assertEquals("value2", map.get("value2"));
        assertEquals("value3", map.get("value3"));
    }

    @Test
    public void testToString() {
        assertEquals(Arrays.toString(values), record.toString());
    }

    @Test
    public void testValues() {
        assertEquals(values, record.values());
    }

    public enum EnumTest {
        VALUE1, VALUE2, VALUE3
    }
}