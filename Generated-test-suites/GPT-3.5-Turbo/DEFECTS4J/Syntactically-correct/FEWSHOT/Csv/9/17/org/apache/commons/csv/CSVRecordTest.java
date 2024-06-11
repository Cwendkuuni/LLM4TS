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
    private Map<String, Integer> mapping;
    private String[] values;

    @Before
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(ColumnEnum.COLUMN1));
        assertEquals("value2", record.get(ColumnEnum.COLUMN2));
        assertEquals("value3", record.get(ColumnEnum.COLUMN3));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithNoMapping() {
        record = new CSVRecord(values, null, "comment", 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidName() {
        record.get("invalid");
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        record = new CSVRecord(values, null, "comment", 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInvalidMapping() {
        mapping.put("column4", 3);
        record = new CSVRecord(values, mapping, "comment", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        assertEquals(values[0], record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertTrue(map.containsKey("column1"));
        assertEquals("value1", map.get("column1"));
        assertTrue(map.containsKey("column2"));
        assertEquals("value2", map.get("column2"));
        assertTrue(map.containsKey("column3"));
        assertEquals("value3", map.get("column3"));
        assertFalse(map.containsKey("column4"));
    }

    @Test
    public void testSize() {
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(values.length, record.toList().size());
        assertEquals(values[0], record.toList().get(0));
        assertEquals(values[1], record.toList().get(1));
        assertEquals(values[2], record.toList().get(2));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertTrue(map.containsKey("column1"));
        assertEquals("value1", map.get("column1"));
        assertTrue(map.containsKey("column2"));
        assertEquals("value2", map.get("column2"));
        assertTrue(map.containsKey("column3"));
        assertEquals("value3", map.get("column3"));
        assertFalse(map.containsKey("column4"));
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        assertEquals(values.length, record.values().length);
        assertEquals(values[0], record.values()[0]);
        assertEquals(values[1], record.values()[1]);
        assertEquals(values[2], record.values()[2]);
    }
    
    private enum ColumnEnum {
        COLUMN1,
        COLUMN2,
        COLUMN3
    }
}