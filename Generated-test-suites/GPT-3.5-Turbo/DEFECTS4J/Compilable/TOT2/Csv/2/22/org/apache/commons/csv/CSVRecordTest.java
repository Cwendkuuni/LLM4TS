package org.apache.commons.csv;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        String result = record.get(1);
        
        assertEquals("B", result);
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        String result = record.get("column2");
        
        assertEquals("B", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"A", "B", "C"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("column2");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistent() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("column2"));
    }
    
    @Test
    public void testIsNotMapped() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isMapped("column2"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"A", "B", "C"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("column2"));
    }
    
    @Test
    public void testIsNotSet() {
        String[] values = {"A", "B"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isSet("column2"));
    }

    // Add more test cases for other methods as needed

}