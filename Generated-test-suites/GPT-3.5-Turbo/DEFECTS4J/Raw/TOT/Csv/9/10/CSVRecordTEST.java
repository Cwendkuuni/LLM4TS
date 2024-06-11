import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {
    
    // Test case for get(Enum<?> e)
    @Test
    public void testGetEnum() {
        CSVRecord record = createCSVRecord();
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    // Test case for get(int i)
    @Test
    public void testGetByIndex() {
        CSVRecord record = createCSVRecord();
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    // Test case for get(String name)
    @Test
    public void testGetByName() {
        CSVRecord record = createCSVRecordWithMapping();
        assertEquals("value1", record.get("Col1"));
        assertEquals("value2", record.get("Col2"));
    }
    
    // Test case for getComment()
    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecordWithComment();
        assertEquals("This is a comment", record.getComment());
    }
    
    // Test case for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecordWithRecordNumber();
        assertEquals(1L, record.getRecordNumber());
    }
    
    // Test case for isConsistent()
    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord();
        assertTrue(record.isConsistent());
    }
    
    // Test case for isMapped(String name)
    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecordWithMapping();
        assertTrue(record.isMapped("Col1"));
        assertTrue(record.isMapped("Col2"));
        assertFalse(record.isMapped("Col3"));
    }
    
    // Test case for isSet(String name)
    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecordWithMapping();
        assertTrue(record.isSet("Col1"));
        assertTrue(record.isSet("Col2"));
        assertFalse(record.isSet("Col3"));
    }
    
    // Test case for iterator()
    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord();
        assertTrue(record.iterator().hasNext());
    }
    
    // Test case for putIn(M map)
    @Test
    public void testPutIn() {
        CSVRecord record = createCSVRecordWithMapping();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("Col1"));
        assertEquals("value2", map.get("Col2"));
        assertNull(map.get("Col3"));
    }
    
    // Test case for size()
    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        assertEquals(2, record.size());
    }
    
    // Test case for toString()
    @Test
    public void testToString() {
        CSVRecord record = createCSVRecord();
        assertEquals("[value1, value2]", record.toString());
    }
    
    // Utility method to create a CSVRecord
    private CSVRecord createCSVRecord() {
        String[] values = new String[] { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 0L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    // Utility method to create a CSVRecord with header mapping
    private CSVRecord createCSVRecordWithMapping() {
        String[] values = new String[] { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Col1", 0);
        mapping.put("Col2", 1);
        String comment = null;
        long recordNumber = 0L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    // Utility method to create a CSVRecord with a comment
    private CSVRecord createCSVRecordWithComment() {
        String[] values = new String[] { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "This is a comment";
        long recordNumber = 0L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    // Utility method to create a CSVRecord with record number
    private CSVRecord createCSVRecordWithRecordNumber() {
        String[] values = new String[] { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1L;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    // Enum for testing get(Enum<?> e)
    private enum TestEnum {
        VALUE1, VALUE2
    }

}