package org.apache.commons.csv;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;
    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        values = new String[]{"a", "b", "c"};
        mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_ValidEnum_ReturnsValue() {
        assertEquals("a", csvRecord.get(TestEnum.COLUMN1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByEnum_InvalidEnum_ThrowsIllegalArgumentException() {
        csvRecord.get(TestEnum.INVALID_COLUMN);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        assertEquals("b", csvRecord.get(1));
    }

    @Test
    public void testGetByIndex_InvalidIndex_ReturnsValue() {
        assertNull(csvRecord.get(3));
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        assertEquals("c", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingSpecified_ThrowsIllegalStateException() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        csvRecordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsIllegalArgumentException() {
        csvRecord.get("invalid_column");
    }

    @Test
    public void testGetComment_ValidComment_ReturnsComment() {
        assertEquals(comment, csvRecord.getComment());
    }

    @Test
    public void testGetComment_NoComment_ReturnsNull() {
        CSVRecord csvRecordWithoutComment = new CSVRecord(values, mapping, null, recordNumber);
        assertNull(csvRecordWithoutComment.getComment());
    }

    @Test
    public void testGetRecordNumber_ValidRecordNumber_ReturnsRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent_MappingNotSpecified_ReturnsTrue() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(csvRecordWithoutMapping.isConsistent());
    }

    @Test
    public void testIsConsistent_ValidMappingAndMatchingRecordSize_ReturnsTrue() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_ValidMappingAndNotMatchingRecordSize_ReturnsFalse() {
        CSVRecord csvRecordWithMismatchedRecordSize = new CSVRecord(new String[]{"a", "b"}, mapping, comment, recordNumber);
        assertFalse(csvRecordWithMismatchedRecordSize.isConsistent());
    }

    @Test
    public void testIsMapped_MappedColumn_ReturnsTrue() {
        assertTrue(csvRecord.isMapped("column1"));
    }

    @Test
    public void testIsMapped_UnmappedColumn_ReturnsFalse() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet_MappedColumnWithValue_ReturnsTrue() {
        assertTrue(csvRecord.isSet("column1"));
    }

    @Test
    public void testIsSet_MappedColumnWithoutValue_ReturnsFalse() {
        assertFalse(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSet_UnmappedColumn_ReturnsFalse() {
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator_ReturnsIteratorOverValues() {
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("a", csvRecord.iterator().next());
    }

    @Test
    public void testPutIn_PutsAllValuesIntoMap() {
        Map<String, String> map = new HashMap<String, String>();
        csvRecord.putIn(map);
        assertEquals(3, map.size());
        assertEquals("a", map.get("column1"));
        assertEquals("b", map.get("column2"));
        assertEquals("c", map.get("column3"));
    }

    @Test
    public void testSize_ReturnsNumberOfValues() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString_ReturnsStringRepresentationOfValues() {
        assertEquals("[a, b, c]", csvRecord.toString());
    }

    @Test
    public void testValues_ReturnsValuesArray() {
        assertArrayEquals(values, csvRecord.values());
    }

    private enum TestEnum {
        COLUMN1,INVALID_COLUMN
    }
}