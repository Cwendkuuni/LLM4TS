package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private CSVRecord record;
    private Map<String, Integer> mapping;

    @Before
    public void setUp() {
        String[] values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        String value = record.get(0);
        assertEquals("value1", value);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ThrowsException() {
        record.get(3);
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        String value = record.get("column2");
        assertEquals("value2", value);
    }

    @Test
    public void testGetByName_InvalidName_ReturnsNull() {
        String value = record.get("column4");
        assertNull(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingSpecified_ThrowsException() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, "comment", 1);
        record.get("column1");
    }

    @Test
    public void testIsConsistent_MappedRecord_ReturnsTrue() {
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_UnmappedRecord_ReturnsTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, "comment", 1);
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_MismatchedSize_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3", "value4"}, mapping, "comment", 1);
        boolean isConsistent = record.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMapped_MappedColumn_ReturnsTrue() {
        boolean isMapped = record.isMapped("column2");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMapped_UnmappedColumn_ReturnsFalse() {
        boolean isMapped = record.isMapped("column4");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSet_MappedAndSetColumn_ReturnsTrue() {
        boolean isSet = record.isSet("column2");
        assertTrue(isSet);
    }

    @Test
    public void testIsSet_MappedButNotSetColumn_ReturnsFalse() {
        boolean isSet = record.isSet("column3");
        assertFalse(isSet);
    }

    @Test
    public void testIsSet_UnmappedColumn_ReturnsFalse() {
        boolean isSet = record.isSet("column4");
        assertFalse(isSet);
    }

    @Test
    public void testIterator_ReturnsIteratorWithValues() {
        String[] expectedValues = new String[]{"value1", "value2", "value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i++], value);
        }
    }

    @Test
    public void testValues_ReturnsCopyOfValuesArray() {
        String[] values = record.values();
        values[0] = "updatedValue";
        assertNotEquals(values[0], record.get(0));
    }

    @Test
    public void testGetComment_WithComment_ReturnsComment() {
        String comment = record.getComment();
        assertEquals("comment", comment);
    }

    @Test
    public void testGetComment_WithoutComment_ReturnsNull() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        String comment = record.getComment();
        assertNull(comment);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testSize_ReturnsNumberOfValues() {
        int size = record.size();
        assertEquals(3, size);
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        String expectedString = "[value1, value2, value3]";
        String actualString = record.toString();
        assertEquals(expectedString, actualString);
    }
}