import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private static final String[] TEST_VALUES = {"value1", "value2", "value3"};
    private static final String[] TEST_HEADERS = {"header1", "header2", "header3"};

    private CSVRecord csvRecord;

    @Before
    public void setup() {
        csvRecord = new CSVRecord(TEST_VALUES, null, null, 1);
    }

    @Test
    public void testGetByEnum_ValidEnum_ShouldReturnCorrectValue() {
        assertEquals("value1", csvRecord.get(TestEnum.TEST_VALUE));
        assertEquals("value2", csvRecord.get(TestEnum.TEST_HEADER));
    }

    @Test
    public void testGetByEnum_InvalidEnum_ShouldThrowIllegalArgumentException() {
        try {
            csvRecord.get(TestEnum.INVALID_ENUM);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for INVALID_ENUM not found, expected one of [TEST_VALUE, TEST_HEADER]", e.getMessage());
        }
    }

    @Test
    public void testGetByIndex_ValidIndex_ShouldReturnCorrectValue() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByIndex_InvalidIndex_ShouldThrowArrayIndexOutOfBoundsException() {
        try {
            csvRecord.get(3);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '3' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }

    @Test
    public void testGetByName_ValidName_ShouldReturnCorrectValue() {
        assertEquals("value1", csvRecord.get("header1"));
        assertEquals("value2", csvRecord.get("header2"));
        assertEquals("value3", csvRecord.get("header3"));
    }

    @Test
    public void testGetByName_NoMappingSpecified_ShouldThrowIllegalStateException() {
        try {
            csvRecord.get("header");
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void testGetByName_InvalidName_ShouldThrowIllegalArgumentException() {
        try {
            csvRecord.get("invalid_header");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for invalid_header not found, expected one of [header1, header2, header3]", e.getMessage());
        }
    }

    @Test
    public void testGetComment_NoComment_ShouldReturnNull() {
        assertNull(csvRecord.getComment());
    }

    @Test
    public void testGetComment_WithComment_ShouldReturnComment() {
        csvRecord = new CSVRecord(TEST_VALUES, null, "Test Comment", 1);
        assertEquals("Test Comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber_ShouldReturnRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent_MappingIsNull_ShouldReturnTrue() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSizeDoesNotMatchValuesLength_ShouldReturnFalse() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSizeMatchesValuesLength_ShouldReturnTrue() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped_MappedColumn_ShouldReturnTrue() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        assertTrue(csvRecord.isMapped("header1"));
    }

    @Test
    public void testIsMapped_UnmappedColumn_ShouldReturnFalse() {
        assertFalse(csvRecord.isMapped("header"));
    }

    @Test
    public void testIsSet_MappedColumnWithValidIndex_ShouldReturnTrue() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        assertTrue(csvRecord.isSet("header1"));
    }

    @Test
    public void testIsSet_MappedColumnWithInvalidIndex_ShouldReturnFalse() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        assertFalse(csvRecord.isSet("header3"));
    }

    @Test
    public void testIsSet_UnmappedColumn_ShouldReturnFalse() {
        assertFalse(csvRecord.isSet("header"));
    }

    @Test
    public void testSize_ShouldReturnValuesLength() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testIterator_ShouldReturnIteratorOverValues() {
        assertArrayEquals(TEST_VALUES, csvRecord.toList().toArray());
    }

    @Test
    public void testPutIn_ValidMapping_ShouldPutValuesInMap() {
        csvRecord = new CSVRecord(TEST_VALUES, createMapping(), null, 1);
        Map<String, String> resultMap = csvRecord.putIn(new HashMap<>());
        assertEquals("value1", resultMap.get("header1"));
        assertEquals("value2", resultMap.get("header2"));
        assertEquals("value3", resultMap.get("header3"));
    }

    @Test
    public void testPutIn_NullValues_ShouldPutNullValuesInMap() {
        csvRecord = new CSVRecord(null, createMapping(), null, 1);
        Map<String, String> resultMap = csvRecord.putIn(new HashMap<>());
        assertNull(resultMap.get("header1"));
        assertNull(resultMap.get("header2"));
        assertNull(resultMap.get("header3"));
    }

    @Test
    public void testToString_ShouldReturnStringRepresentationOfValues() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        return mapping;
    }

    enum TestEnum {
        TEST_VALUE,
        TEST_HEADER,
        INVALID_ENUM
    }

}