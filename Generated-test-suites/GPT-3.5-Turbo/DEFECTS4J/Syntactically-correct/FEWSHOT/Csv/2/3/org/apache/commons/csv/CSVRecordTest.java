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
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
        assertNull(record.get("col4"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1); 
        
        record.get("col1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentInconsistentRecord() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentMappingNull() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1); 
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("col4"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        int count = 0;
        for (String value : record) {
            count++;
        }
        
        assertEquals(3, count);
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertArrayEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1); 
        
        assertEquals("[value1, value2, value3]", record.toString());
    }
}