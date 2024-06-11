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
    public void testGetByEnum() {
        // Test case for getting a value by Enum
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        assertEquals("1", csvRecord.get(TestEnum.ONE));
        assertEquals("2", csvRecord.get(TestEnum.TWO));
        assertEquals("3", csvRecord.get(TestEnum.THREE));
    }
    
    @Test
    public void testGetByIndex() {
        // Test case for getting a value by index
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        assertEquals("1", csvRecord.get(0));
        assertEquals("2", csvRecord.get(1));
        assertEquals("3", csvRecord.get(2));
    }
    
    @Test
    public void testGetByName() {
        // Test case for getting a value by name
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        mapping.put("three", 2);
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 1);
        assertEquals("1", csvRecord.get("one"));
        assertEquals("2", csvRecord.get("two"));
        assertEquals("3", csvRecord.get("three"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        // Test case for getting a value by name with no mapping provided
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        csvRecord.get("one");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameNotFound() {
        // Test case for getting a value by name with mapping but name not found
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 1);
        csvRecord.get("two");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        // Test case for getting a value by name with mapping and index out of bounds
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"1"}, mapping, null, 1);
        csvRecord.get("one");
    }
    
    @Test
    public void testGetComment() {
        // Test case for getting comment
        CSVRecord csvRecord = new CSVRecord(new String[]{}, null, "This is a comment", 1);
        assertEquals("This is a comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        // Test case for getting record number
        CSVRecord csvRecord = new CSVRecord(new String[]{}, null, null, 5);
        assertEquals(5, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        // Test case for checking if record is consistent
        CSVRecord csvRecord1 = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        assertTrue(csvRecord1.isConsistent());
        
        CSVRecord csvRecord2 = new CSVRecord(new String[]{"1", "2"}, null, null, 1);
        assertFalse(csvRecord2.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        // Test case for checking if column is mapped
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 1);
        assertTrue(csvRecord.isMapped("one"));
        assertFalse(csvRecord.isMapped("two"));
    }
    
    @Test
    public void testIsSet() {
        // Test case for checking if column is mapped and has a value
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        CSVRecord csvRecord1 = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 1);
        assertTrue(csvRecord1.isSet("one"));
        assertTrue(csvRecord1.isSet("two"));
        assertFalse(csvRecord1.isSet("three"));
        
        CSVRecord csvRecord2 = new CSVRecord(new String[]{"1"}, mapping, null, 1);
        assertTrue(csvRecord2.isSet("one"));
        assertFalse(csvRecord2.isSet("two"));
    }
    
    @Test
    public void testIterator() {
        // Test case for iterating over the record values
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(csvRecord.size(), count);
    }
    
    @Test
    public void testSize() {
        // Test case for getting number of values in the record
        CSVRecord csvRecord1 = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        assertEquals(3, csvRecord1.size());
        
        CSVRecord csvRecord2 = new CSVRecord(null, null, null, 1);
        assertEquals(0, csvRecord2.size());
    }
    
    @Test
    public void testToMap() {
        // Test case for converting the record to a map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        mapping.put("three", 2);
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, mapping, null, 1);
        Map<String, String> map = csvRecord.toMap();
        assertEquals("1", map.get("one"));
        assertEquals("2", map.get("two"));
        assertEquals("3", map.get("three"));
    }
    
    @Test
    public void testValues() {
        // Test case for getting values as an array
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        String[] values = csvRecord.values();
        assertEquals(3, values.length);
        assertEquals("1", values[0]);
        assertEquals("2", values[1]);
        assertEquals("3", values[2]);
    }
    
    @Test
    public void testToString() {
        // Test case for getting string representation of the record
        CSVRecord csvRecord = new CSVRecord(new String[]{"1", "2", "3"}, null, null, 1);
        assertEquals("[1, 2, 3]", csvRecord.toString());
    }
    
    enum TestEnum {
        ONE, TWO, THREE;
    }
}