package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertNull(record.get("name3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("name1");
    }
    
    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMismatchedSizes() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(!record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(!record.isMapped("name3"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(!record.isSet("name3"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        int i = 0;
        for (String value : record) {
            assertEquals(values[i], value);
            i++;
        }
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[value1, value2, value3]", record.toString());
    }
}