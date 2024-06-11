package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
        assertNull(record.get("col4"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("col1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        int i = 0;
        for (String value : record) {
            assertEquals(values[i++], value);
        }
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertArrayEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        
        assertEquals(comment, record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        long recordNumber = 10;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        
        assertEquals(recordNumber, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals(values.length, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals(values.toString(), record.toString());
    }
}