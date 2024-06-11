package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.get("name"));
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 0), null, 1);
        assertEquals("value1", record.get("name"));
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 1), null, 1);
        assertEquals("value2", record.get("name"));
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 2), null, 1);
        assertEquals("value3", record.get("name"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 0), null, 1);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 1), null, 1);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 2), null, 1);
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name", 0), null, 1);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isMapped("name"));
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 0), null, 1);
        assertTrue(record.isMapped("name"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertFalse(record.isSet("name"));
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, Map.of("name", 0), null, 1);
        assertTrue(record.isSet("name"));
        
        record = new CSVRecord(new String[]{"value1"}, Map.of("name", 0), null, 1);
        assertTrue(record.isSet("name"));
        
        record = new CSVRecord(new String[]{"value1"}, Map.of("name", 1), null, 1);
        assertFalse(record.isSet("name"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
        
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertNull(record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
        
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
        
        record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        assertEquals(1, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
}