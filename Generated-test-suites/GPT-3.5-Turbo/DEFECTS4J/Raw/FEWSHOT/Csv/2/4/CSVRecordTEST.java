package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testCSVRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertNotNull(record);
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.getMapping());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }
    
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        
        record.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"value1", "value2", "value3"};
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        Iterator<String> iterator = record.iterator();
        
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1L;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("[value1, value2, value3]", record.toString());
    }
}