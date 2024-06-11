package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {
    private CSVRecord record;

    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();
    private final long recordNumber = 1L;
    private final String comment = "This is a comment.";

    @Before
    public void setup() {
        //Initialize CSVRecord for testing
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex_validIndex_returnsValue() {
        // Test getting a value by index
        assertEquals("value1", record.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_invalidIndex_throwsException() {
        // Test getting a value by an invalid index
        record.get(3);
    }

    @Test
    public void testGetByName_validName_returnsValue() {
        // Test getting a value by name
        assertEquals("value2", record.get("column2"));
    }

    @Test
    public void testGetByName_invalidName_returnsNull() {
        // Test getting a value by an invalid name
        assertNull(record.get("unknownColumn"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_missingMapping_throwsException() {
        // Test getting a value by name without header mapping
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        recordWithoutMapping.get("column1");
    }

    @Test
    public void testIsConsistent_validMappingAndValues_returnsTrue() {
        // Test checking consistency with valid mapping and values
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_missingMapping_returnsTrue() {
        // Test checking consistency with missing mapping
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(recordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsConsistent_invalidMapping_returnsFalse() {
        // Test checking consistency with invalid mapping
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("column1", 0);
        invalidMapping.put("column2", 1);
        CSVRecord recordWithInvalidMapping = new CSVRecord(values, invalidMapping, comment, recordNumber);
        assertFalse(recordWithInvalidMapping.isConsistent());
    }

    @Test
    public void testIsMapped_mappedColumn_returnsTrue() {
        // Test checking if a column is mapped
        assertTrue(record.isMapped("column2"));
    }

    @Test
    public void testIsMapped_unmappedColumn_returnsFalse() {
        // Test checking if an unmapped column is mapped
        assertFalse(record.isMapped("unknownColumn"));
    }

    @Test
    public void testIsSet_mappedAndSetColumn_returnsTrue() {
        // Test checking if a mapped and set column returns true
        assertTrue(record.isSet("column1"));
    }

    @Test
    public void testIsSet_mappedAndUnsetColumn_returnsFalse() {
        // Test checking if a mapped but unset column returns false
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIsSet_unmappedColumn_returnsFalse() {
        // Test checking if an unmapped column returns false
        assertFalse(record.isSet("unknownColumn"));
    }

    @Test
    public void testIterator_returnsIterator() {
        // Test getting an iterator
        assertNotNull(record.iterator());
    }

    @Test
    public void testGetComment_hasComment_returnsComment() {
        // Test getting the comment when comment exists
        assertEquals("This is a comment.", record.getComment());
    }

    @Test
    public void testGetComment_noComment_returnsNull() {
        // Test getting the comment when no comment exists
        CSVRecord recordWithoutComment = new CSVRecord(values, mapping, null, recordNumber);
        assertNull(recordWithoutComment.getComment());
    }

    @Test
    public void testGetRecordNumber_validRecordNumber_returnsRecordNumber() {
        // Test getting the record number
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testSize_returnsNumberOfValues() {
        // Test getting the number of values
        assertEquals(3, record.size());
    }

    @Test
    public void testToString_returnsStringRepresentation() {
        // Test converting record to string
        assertEquals("[value1, value2, value3]", record.toString());
    }
}