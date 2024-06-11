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
    private static final String[] VALUES = new String[] {"Value1", "Value2"};
    private static final String COMMENT = "This is a comment";
    private static final long RECORD_NUMBER = 1L;
    
    @Before
    public void setUp() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        csvRecord = new CSVRecord(VALUES, mapping, COMMENT, RECORD_NUMBER);
    }
    
    @Test
    public void testGet_ByIndex_ValidIndex_ReturnsValue() {
        String result = csvRecord.get(0);
        assertEquals("Value1", result);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_ByIndex_InvalidIndex_ThrowsException() {
        csvRecord.get(2);
    }
    
    @Test
    public void testGet_ByName_MappedName_ReturnsValue() {
        String result = csvRecord.get("name");
        assertEquals("Value1", result);
    }
    
    @Test
    public void testGet_ByName_UnmappedName_ReturnsNull() {
        String result = csvRecord.get("unmapped");
        assertNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGet_ByName_NoMapping_ThrowsException() {
        CSVRecord csvRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        csvRecord.get("name");
    }
    
    @Test
    public void testIsConsistent_MatchingSizes_ReturnsTrue() {
        boolean result = csvRecord.isConsistent();
        assertTrue(result);
    }
    
    @Test
    public void testIsConsistent_DifferentSizes_ReturnsFalse() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"Value1"}, null, COMMENT, RECORD_NUMBER);
        boolean result = csvRecord.isConsistent();
        assertFalse(result);
    }
    
    @Test
    public void testIsMapped_MappedName_ReturnsTrue() {
        boolean result = csvRecord.isMapped("name");
        assertTrue(result);
    }
    
    @Test
    public void testIsMapped_UnmappedName_ReturnsFalse() {
        boolean result = csvRecord.isMapped("unmapped");
        assertFalse(result);
    }
    
    @Test
    public void testIsSet_MappedAndHasValue_ReturnsTrue() {
        boolean result = csvRecord.isSet("name");
        assertTrue(result);
    }
    
    @Test
    public void testIsSet_MappedAndNoValue_ReturnsFalse() {
        CSVRecord csvRecord = new CSVRecord(new String[] {"Value1", ""}, null, COMMENT, RECORD_NUMBER);
        boolean result = csvRecord.isSet("age");
        assertFalse(result);
    }
    
    @Test
    public void testIsSet_Unmapped_ReturnsFalse() {
        boolean result = csvRecord.isSet("unmapped");
        assertFalse(result);
    }
    
    @Test
    public void testIterator_ValidValues_ReturnsIteratorWithValues() {
        String[] expectedValues = {"Value1", "Value2"};
        int index = 0;
        for(String value : csvRecord) {
            assertEquals(expectedValues[index++], value);
        }
    }
    
    @Test
    public void testValues_ValidValues_ReturnsValues() {
        String[] result = csvRecord.values();
        assertEquals(2, result.length);
        assertEquals("Value1", result[0]);
        assertEquals("Value2", result[1]);
    }
    
    @Test
    public void testGetComment_HasComment_ReturnsComment() {
        String result = csvRecord.getComment();
        assertEquals(COMMENT, result);
    }
    
    @Test
    public void testGetComment_NoComment_ReturnsNull() {
        CSVRecord csvRecord = new CSVRecord(VALUES, null, null, RECORD_NUMBER);
        String result = csvRecord.getComment();
        assertNull(result);
    }
    
    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long result = csvRecord.getRecordNumber();
        assertEquals(RECORD_NUMBER, result);
    }
    
    @Test
    public void testSize_ReturnsCorrectSize() {
        int result = csvRecord.size();
        assertEquals(2, result);
    }
    
}