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
    public void testGet_Enum() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    @Test
    public void testGet_Index() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGet_Name() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGet_Name_WithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        record.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGet_Name_InvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        record.get("column3");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGet_Name_IndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        record.get("column2");
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {}, null, "comment", 0);
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[] {}, mapping, null, 0);
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
    }
    
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[] {"value1"}, mapping, null, 0);
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        int count = 0;
        for (String value : record) {
            count++;
            switch (count) {
                case 1:
                    assertEquals("value1", value);
                    break;
                case 2:
                    assertEquals("value2", value);
                    break;
            }
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(2, record.toList().size());
        assertTrue(record.toList().contains("value1"));
        assertTrue(record.toList().contains("value2"));
    }
    
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals(2, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals("[value1, value2]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(2, record.values().length);
        assertEquals("value1", record.values()[0]);
        assertEquals("value2", record.values()[1]);
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2
    }

}