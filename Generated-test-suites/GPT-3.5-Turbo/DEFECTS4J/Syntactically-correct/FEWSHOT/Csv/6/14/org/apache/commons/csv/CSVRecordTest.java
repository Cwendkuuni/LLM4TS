package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord createCSVRecord() {
        // Create a CSVRecord object for testing
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        return record;
    }
    
    @Test
    public void testGet() {
        CSVRecord record = createCSVRecord();
        
        // Test get() method with Enum parameter
        assertEquals("value1", record.get(SomeEnum.VALUE1));
        assertEquals("value2", record.get(SomeEnum.VALUE2));
        assertEquals("value3", record.get(SomeEnum.VALUE3));
        
        // Test get() method with index parameter
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        CSVRecord record = createCSVRecord();
        
        // Test get() method with name parameter
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
        
        // Test get() method with unmapped name parameter
        try {
            record.get("column4");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for column4 not found, expected one of [column1, column2, column3]", e.getMessage());
        }
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord();
        
        // Test getComment() method
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord();
        
        // Test getRecordNumber() method
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord();
        
        // Test isConsistent() method
        assertTrue(record.isConsistent());
        
        // Test isConsistent() method with inconsistent record
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecord();
        
        // Test isMapped() method with mapped name
        assertTrue(record.isMapped("column1"));
        
        // Test isMapped() method with unmapped name
        assertFalse(record.isMapped("column4"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecord();
        
        // Test isSet() method with mapped name and value
        assertTrue(record.isSet("column1"));
        
        // Test isSet() method with mapped name and no value
        assertFalse(record.isSet("column4"));
        
        // Test isSet() method with unmapped name
        assertFalse(record.isSet("column4"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        
        // Test size() method
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord();
        
        // Test toMap() method
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }
    
    @Test
    public void testValues() {
        CSVRecord record = createCSVRecord();
        
        // Test values() method
        String[] values = record.values();
        assertEquals(3, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
    
}