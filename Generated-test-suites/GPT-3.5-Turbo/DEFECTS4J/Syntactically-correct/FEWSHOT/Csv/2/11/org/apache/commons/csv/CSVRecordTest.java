package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        record.get(2);
    }
    
    @Test
    public void testGet_ValidColumnName() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }
    
    @Test
    public void testGet_InvalidColumnName() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertNull(record.get("column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGet_NoMappingSpecified() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        record.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord consistentRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        CSVRecord inconsistentRecord = new CSVRecord(new String[] {"value1"}, null, null, 1);
        
        assertTrue(consistentRecord.isConsistent());
        assertFalse(inconsistentRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped_WhenMappingExists() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
    }
    
    @Test
    public void testIsMapped_WhenMappingNotExists() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertFalse(record.isMapped("column3"));
    }
    
    @Test
    public void testIsSet_WhenMappedAndValueExists() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
    }
    
    @Test
    public void testIsSet_WhenMappedAndValueNotExists() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertFalse(record.isSet("column3"));
    }
    
    @Test
    public void testIsSet_WhenNotMapped() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertFalse(record.isSet("column1"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(2, count);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("[value1, value2]", record.toString());
    }
}