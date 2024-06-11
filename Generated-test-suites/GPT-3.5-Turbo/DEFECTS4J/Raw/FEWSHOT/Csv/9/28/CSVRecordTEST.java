package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    private CSVRecord csvRecord;
    
    @Before
    public void setup() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    @Test
    public void testGetUsingEnum() {
        assertEquals("value1", csvRecord.get(CSVRecordTestEnum.COLUMN1));
        assertEquals("value2", csvRecord.get(CSVRecordTestEnum.COLUMN2));
        assertEquals("value3", csvRecord.get(CSVRecordTestEnum.COLUMN3));
    }
    
    @Test
    public void testGetUsingIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }
    
    @Test
    public void testGetUsingName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value"}, null, null, 1);
        record.get("column");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidName() {
        csvRecord.get("invalidColumn");
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
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("invalidColumn"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("invalidColumn"));
    }
    
    @Test
    public void testIterator() {
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("value1", csvRecord.iterator().next());
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "newValue");
        csvRecord.putIn(map);
        assertEquals("newValue", map.get("column1"));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testToList() {
        assertNotNull(csvRecord.toList());
        assertEquals(3, csvRecord.toList().size());
        assertEquals("value1", csvRecord.toList().get(0));
        assertEquals("value2", csvRecord.toList().get(1));
        assertEquals("value3", csvRecord.toList().get(2));
    }
    
    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(new String[] {"value1", "value2", "value3"}, csvRecord.values());
    }
    
    private enum CSVRecordTestEnum {
        COLUMN1,
        COLUMN2,
        COLUMN3
    }
}