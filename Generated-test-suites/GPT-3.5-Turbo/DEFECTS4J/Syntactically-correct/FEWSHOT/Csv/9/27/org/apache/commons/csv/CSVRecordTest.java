package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
        assertEquals("value2", record.get(TestEnum.TEST2));
        assertEquals("value3", record.get(TestEnum.TEST3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        assertEquals("value1", record.get("field1"));
        assertEquals("value2", record.get("field2"));
        assertEquals("value3", record.get("field3"));
    }

    @Test(expected=IllegalStateException.class)
    public void testGetNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        record.get("field1");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetNameNotMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        record.get("field4");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetNameIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        record.get("field3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Test when mapping is null
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());

        // Test when mapping size is equal to values length
        Map<String, Integer> mapping1 = new HashMap<>();
        mapping1.put("field1", 0);
        mapping1.put("field2", 1);
        mapping1.put("field3", 2);
        CSVRecord record1 = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping1, null, 1);
        assertTrue(record1.isConsistent());

        // Test when mapping size is not equal to values length
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("field1", 0);
        mapping2.put("field2", 1);
        CSVRecord record2 = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping2, null, 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        assertTrue(record.isMapped("field1"));
        assertTrue(record.isMapped("field2"));
        assertTrue(record.isMapped("field3"));
        assertFalse(record.isMapped("field4"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        assertTrue(record.isSet("field1"));
        assertTrue(record.isSet("field2"));
        assertTrue(record.isSet("field3"));
        assertFalse(record.isSet("field4"));
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("field1", 0);
        mapping.put("field2", 1);
        mapping.put("field3", 2);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        record.putIn(map);
        assertEquals("value1", map.get("field1"));
        assertEquals("value2", map.get("field2"));
        assertEquals("value3", map.get("field3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        String[] values = record.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
    
    private enum TestEnum {
        TEST1, TEST2, TEST3;
    }
}