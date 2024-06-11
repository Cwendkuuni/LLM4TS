package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertNull(record.get("column1"));
        assertNull(record.get("column2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
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
    public void testIsMapped() {
        CSVRecord mappedRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        CSVRecord unmappedRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(mappedRecord.isMapped("column1"));
        assertFalse(mappedRecord.isMapped("column3"));
        assertFalse(unmappedRecord.isMapped("column1"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord setRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        CSVRecord unsetRecord = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(setRecord.isSet("column1"));
        assertFalse(setRecord.isSet("column3"));
        assertFalse(unsetRecord.isSet("column1"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 1);
        assertArrayEquals(new String[] {"value1", "value2"}, record.values());
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
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