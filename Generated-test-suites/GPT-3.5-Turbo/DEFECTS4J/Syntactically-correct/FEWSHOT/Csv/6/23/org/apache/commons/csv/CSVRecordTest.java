package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM1", 0);
        mapping.put("ENUM2", 1);
        mapping.put("ENUM3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertEquals("value1", record.get(Enum1.ENUM1));
        assertEquals("value2", record.get(Enum2.ENUM2));
        assertEquals("value3", record.get(Enum3.ENUM3));
    }
    
    @Test
    public void testGetByIndex() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertEquals("value1", record.get("NAME1"));
        assertEquals("value2", record.get("NAME2"));
        assertEquals("value3", record.get("NAME3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        record.get("NAME1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        record.get("NAME4");
    }
    
    @Test
    public void testGetComment() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, "comment", 0);
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 5);
        
        assertEquals(5, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithNullMapping() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithDifferentMappingSize() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithDifferentValuesSize() {
        String[] values = { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertTrue(record.isMapped("NAME1"));
        assertTrue(record.isMapped("NAME2"));
        assertTrue(record.isMapped("NAME3"));
        assertFalse(record.isMapped("NAME4"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        assertTrue(record.isSet("NAME1"));
        assertTrue(record.isSet("NAME2"));
        assertTrue(record.isSet("NAME3"));
        assertFalse(record.isSet("NAME4"));
    }
    
    @Test
    public void testIterator() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals("value1", record.iterator().next());
    }
    
    @Test
    public void testPutIn() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("NAME1", 0);
        mapping.put("NAME2", 1);
        mapping.put("NAME3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        Map<String, String> map = new HashMap<>();
        map.put("NAME4", "value4");
        
        record.putIn(map);
        
        assertEquals("value1", map.get("NAME1"));
        assertEquals("value2", map.get("NAME2"));
        assertEquals("value3", map.get("NAME3"));
        assertEquals("value4", map.get("NAME4"));
    }
    
    @Test
    public void testSize() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals("[value1, value2, value3]", record.toString());
    }
    
    @Test
    public void testValues() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        assertEquals(values, record.values());
    }
    
    enum Enum1 {
        ENUM1
    }
    
    enum Enum2 {
        ENUM2
    }
    
    enum Enum3 {
        ENUM3
    }
}