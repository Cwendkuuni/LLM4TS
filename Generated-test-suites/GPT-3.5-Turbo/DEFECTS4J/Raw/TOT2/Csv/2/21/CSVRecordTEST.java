package org.apache.commons.csv;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 1);
        
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetByNameWithValidName() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value2", csvRecord.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithInvalidName() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 1);
        
        csvRecord.get("invalid");
    }

    @Test
    public void testIsConsistent() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isMapped("col2"));
    }

    @Test
    public void testIsSetWithMappedAndSetColumn() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isSet("col2"));
    }

    @Test
    public void testIsSetWithMappedAndUnsetColumn() {
        String[] values = { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(csvRecord.isSet("col3"));
    }

    @Test
    public void testIterator() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 1);
        
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testGetComment() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), "comment", 1);
        
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 5);
        
        assertEquals(5, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 1);
        
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = { "value1", "value2" };
        CSVRecord csvRecord = new CSVRecord(values, new HashMap<>(), null, 1);
        
        assertEquals("[value1, value2]", csvRecord.toString());
    }

}