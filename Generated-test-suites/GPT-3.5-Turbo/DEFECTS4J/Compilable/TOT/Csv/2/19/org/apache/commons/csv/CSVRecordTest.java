package org.apache.commons.csv;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private String[] values = {"John", "Doe"};
    private Map<String, Integer> mapping;
    
    @Before
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(0));
        assertEquals("Doe", csvRecord.get(1));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("John", csvRecord.get("firstName"));
        assertEquals("Doe", csvRecord.get("lastName"));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        CSVRecord csvRecordWithNoMapping = new CSVRecord(values, null, null, 1L);
        csvRecordWithNoMapping.get("firstName");
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithNoMapping() {
        CSVRecord csvRecordWithNoMapping = new CSVRecord(values, null, null, 1L);
        assertTrue(csvRecordWithNoMapping.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithDifferentSizeMapping() {
        mapping.put("middleName", 2);
        assertFalse(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("firstName"));
        assertTrue(csvRecord.isMapped("lastName"));
        assertFalse(csvRecord.isMapped("middleName"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("firstName"));
        assertTrue(csvRecord.isSet("lastName"));
        assertFalse(csvRecord.isSet("middleName"));
    }
    
    @Test
    public void testIterator() {
        assertNotNull(csvRecord.iterator());
        assertEquals("John", csvRecord.iterator().next());
        assertEquals("Doe", csvRecord.iterator().next());
    }
    
    @Test
    public void testGetComment() {
        assertNull(csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        assertEquals(2, csvRecord.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("[John, Doe]", csvRecord.toString());
    }
}