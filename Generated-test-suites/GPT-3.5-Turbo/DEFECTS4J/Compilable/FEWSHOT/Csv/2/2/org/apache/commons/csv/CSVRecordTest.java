package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNullMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
        
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
        
        mapping.put("column4", 3);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isMapped("column1"));
        
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isSet("column1"));
        
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(values.length, count);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        String[] result = record.values();
        
        assertEquals(values.length, result.length);
        assertEquals(values[0], result[0]);
        assertEquals(values[1], result[1]);
        assertEquals(values[2], result[2]);
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);
        
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 5);
        
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(values.length, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        String result = record.toString();
        
        assertEquals("[value1, value2, value3]", result);
    }

}