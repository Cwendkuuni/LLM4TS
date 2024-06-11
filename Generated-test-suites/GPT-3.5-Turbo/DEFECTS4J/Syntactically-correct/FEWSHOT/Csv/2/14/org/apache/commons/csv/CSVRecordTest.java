package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    CSVRecord csvRecord;
    String[] values;
    Map<String, Integer> mapping;
    String comment;
    long recordNumber;
    
    @Before
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment.";
        recordNumber = 1;
        
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
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
        assertEquals("value3", csvRecord.get("column3"));
        assertNull(csvRecord.get("column4"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        csvRecordWithoutMapping.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        
        CSVRecord inconsistentCsvRecord = new CSVRecord(new String[] {"value1", "value2"}, mapping, comment, recordNumber);
        assertFalse(inconsistentCsvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            assertEquals(values[count++], value);
        }
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(values, csvRecord.values());
    }
    
    @Test
    public void testGetComment() {
        assertEquals(comment, csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        assertEquals(values.length, csvRecord.size());
    }
    
    @Test
    public void testToString() {
        assertEquals(Arrays.toString(values), csvRecord.toString());
    }
}