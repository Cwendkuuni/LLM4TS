
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = new String[] {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"value"}, null, null, 1);
        csvRecord.get("col1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        csvRecord.get("col4");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        csvRecord.get("col3");
    }
    
    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        
        CSVRecord inconsistentRecord = new CSVRecord(new String[] {"value"}, null, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col1"));
        assertTrue(csvRecord.isMapped("col2"));
        assertTrue(csvRecord.isMapped("col3"));
        assertFalse(csvRecord.isMapped("col4"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col3"));
        assertFalse(csvRecord.isSet("col4"));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testToList() {
        assertTrue(csvRecord.toList().contains("value1"));
        assertTrue(csvRecord.toList().contains("value2"));
        assertTrue(csvRecord.toList().contains("value3"));
    }
    
    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }
    
    @Test
    public void testValues() {
        String[] values = csvRecord.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }
}
