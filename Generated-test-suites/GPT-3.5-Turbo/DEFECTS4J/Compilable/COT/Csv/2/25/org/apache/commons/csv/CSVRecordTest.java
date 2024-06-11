package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    
    @Before
    public void setUp() {
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
    public void testGetByIndex() {
        String expected = "value1";
        String actual = csvRecord.get(0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetByName() {
        String expected = "value2";
        String actual = csvRecord.get("column2");
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingProvided() {
        CSVRecord recordWithoutMapping = new CSVRecord(csvRecord.values(), null, 
                csvRecord.getComment(), csvRecord.getRecordNumber());
        recordWithoutMapping.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsConsistent_NoMappingProvided() {
        CSVRecord recordWithoutMapping = new CSVRecord(csvRecord.values(), null, 
                csvRecord.getComment(), csvRecord.getRecordNumber());
        assertTrue(recordWithoutMapping.isConsistent());
    }
    
    @Test
    public void testIsMapped_MappedColumn() {
        assertTrue(csvRecord.isMapped("column1"));
    }
    
    @Test
    public void testIsMapped_UnmappedColumn() {
        assertFalse(csvRecord.isMapped("column4"));
    }
    
    @Test
    public void testIsSet_MappedAndHasValue() {
        assertTrue(csvRecord.isSet("column2"));
    }
    
    @Test
    public void testIsSet_MappedButNoValue() {
        assertFalse(csvRecord.isSet("column3"));
    }
    
    @Test
    public void testIsSet_UnmappedColumn() {
        assertFalse(csvRecord.isSet("column4"));
    }
    
    @Test
    public void testIterator() {
        String[] expected = {"value1", "value2", "value3"};
        int index = 0;
        for (String value : csvRecord) {
            assertEquals(expected[index], value);
            index++;
        }
    }
    
    @Test
    public void testValues() {
        String[] expected = {"value1", "value2", "value3"};
        String[] actual = csvRecord.values();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetComment() {
        String expected = "This is a comment";
        String actual = csvRecord.getComment();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRecordNumber() {
        long expected = 1;
        long actual = csvRecord.getRecordNumber();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSize() {
        int expected = 3;
        int actual = csvRecord.size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = "[value1, value2, value3]";
        String actual = csvRecord.toString();
        assertEquals(expected, actual);
    }
}