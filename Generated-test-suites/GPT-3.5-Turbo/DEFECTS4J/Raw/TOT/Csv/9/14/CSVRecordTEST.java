import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        values = new String[] {"A", "B", "C"};
        mapping = new HashMap<>();
        mapping.put("v1", 0);
        mapping.put("v2", 1);
        mapping.put("v3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGet_EnumValueExist_ReturnsValue() {
        String value = csvRecord.get(TestEnum.V1);
        assertEquals("A", value);
    }

    @Test
    public void testGet_EnumValueNotExist_ReturnsNull() {
        String value = csvRecord.get(TestEnum.V4);
        assertNull(value);
    }

    @Test
    public void testGet_IndexWithinRange_ReturnsValue() {
        String value = csvRecord.get(1);
        assertEquals("B", value);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBound_ThrowsArrayIndexOutOfBoundsException() {
        csvRecord.get(4);
    }

    @Test
    public void testGet_ColumnMapped_ReturnsValue() {
        String value = csvRecord.get("v2");
        assertEquals("B", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_ColumnNotMapped_ThrowsIllegalArgumentException() {
        csvRecord.get("v4");
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_MappingNotProvided_ThrowsIllegalStateException() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("v1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_ColumnIndexOutOfBound_ThrowsIllegalArgumentException() {
        csvRecord.get("v3");
    }

    @Test
    public void testGetComment_CommentExist_ReturnsComment() {
        String value = csvRecord.getComment();
        assertEquals(comment, value);
    }

    @Test
    public void testGetComment_CommentNotExist_ReturnsNull() {
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        String value = record.getComment();
        assertNull(value);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long value = csvRecord.getRecordNumber();
        assertEquals(recordNumber, value);
    }

    @Test
    public void testIsConsistent_MappingNotProvided_ReturnsTrue() {
        CSVRecord record = new CSVRecord(values, null, null, recordNumber);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistent_MappingSizeMatchesValuesSize_ReturnsTrue() {
        boolean result = csvRecord.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistent_MappingSizeDoesNotMatchValuesSize_ReturnsFalse() {
        mapping.put("v4", 3);
        boolean result = csvRecord.isConsistent();
        assertFalse(result);
    }

    @Test
    public void testIsMapped_ColumnMapped_ReturnsTrue() {
        boolean result = csvRecord.isMapped("v1");
        assertTrue(result);
    }

    @Test
    public void testIsMapped_ColumnNotMapped_ReturnsFalse() {
        boolean result = csvRecord.isMapped("v4");
        assertFalse(result);
    }

    @Test
    public void testIsSet_ColumnMappedAndHasValue_ReturnsTrue() {
        boolean result = csvRecord.isSet("v2");
        assertTrue(result);
    }

    @Test
    public void testIsSet_ColumnMappedAndNoValue_ReturnsFalse() {
        boolean result = csvRecord.isSet("v3");
        assertFalse(result);
    }

    @Test
    public void testIsSet_ColumnNotMapped_ReturnsFalse() {
        boolean result = csvRecord.isSet("v4");
        assertFalse(result);
    }

    @Test
    public void testIterator_ReturnsIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn_PopulateEmptyMap_ReturnsPopulatedMap() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);
        assertEquals(3, result.size());
        assertEquals("A", result.get("v1"));
        assertEquals("B", result.get("v2"));
        assertEquals("C", result.get("v3"));
    }

    @Test
    public void testSize_ReturnsValuesSize() {
        int size = csvRecord.size();
        assertEquals(3, size);
    }

    @Test
    public void testToList_ReturnsList() {
        List<String> result = csvRecord.toList();
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
        assertEquals("C", result.get(2));
    }

    @Test
    public void testToMap_PopulateEmptyMap_ReturnsPopulatedMap() {
        Map<String, String> result = csvRecord.toMap();
        assertEquals(3, result.size());
        assertEquals("A", result.get("v1"));
        assertEquals("B", result.get("v2"));
        assertEquals("C", result.get("v3"));
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        String result = csvRecord.toString();
        assertEquals("[A, B, C]", result);
    }

    private enum TestEnum {
        V1,
        V2,
        V3,
        V4
    }
}