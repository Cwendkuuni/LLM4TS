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
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals("1", record.get(TestEnum.ONE));
        assertEquals("2", record.get(TestEnum.TWO));
        assertEquals("3", record.get(TestEnum.THREE));
    }
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertEquals("3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        assertEquals("1", record.get("One"));
        assertEquals("2", record.get("Two"));
        assertEquals("3", record.get("Three"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        record.get("One");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        record.get("Four");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByOutOfBoundsName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2"}, mapping, "", 1);
        
        record.get("Three");
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "This is a comment", 1);
        
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        record.values[2] = null;
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2"}, null, "", 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        assertTrue(record.isMapped("One"));
        assertTrue(record.isMapped("Two"));
        assertTrue(record.isMapped("Three"));
        assertFalse(record.isMapped("Four"));
    }
    
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        assertTrue(record.isSet("One"));
        assertTrue(record.isSet("Two"));
        assertTrue(record.isSet("Three"));
        assertFalse(record.isSet("Four"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        int count = 0;
        for (String value : record) {
            count++;
        }
        
        assertEquals(3, count);
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        record.putIn(map);
        
        assertEquals(3, map.size());
        assertEquals("1", map.get("One"));
        assertEquals("2", map.get("Two"));
        assertEquals("3", map.get("Three"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals(3, record.toList().size());
        assertEquals("1", record.toList().get(0));
        assertEquals("2", record.toList().get(1));
        assertEquals("3", record.toList().get(2));
    }
    
    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("One", 0);
        mapping.put("Two", 1);
        mapping.put("Three", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, mapping, "", 1);
        
        map = record.toMap();
        
        assertEquals(3, map.size());
        assertEquals("1", map.get("One"));
        assertEquals("2", map.get("Two"));
        assertEquals("3", map.get("Three"));
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals("[1, 2, 3]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"1", "2", "3"}, null, "", 1);
        
        assertEquals(3, record.values().length);
        assertEquals("1", record.values()[0]);
        assertEquals("2", record.values()[1]);
        assertEquals("3", record.values()[2]);
    }
    
    private enum TestEnum {
        
        ONE,
        TWO,
        THREE;
        
    }
    
}