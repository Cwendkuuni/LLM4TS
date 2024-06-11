package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    
    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        csvRecord = new CSVRecord(values, mapping, "comment", 1);
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertNull(csvRecord.get("column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1"}, null, "comment", 1);
        record.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        
        mapping.put("column3", 2);
        assertFalse(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertFalse(csvRecord.isMapped("column3"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertFalse(csvRecord.isSet("column3"));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
    
    @Test
    public void testGetComment() {
        assertEquals("comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}