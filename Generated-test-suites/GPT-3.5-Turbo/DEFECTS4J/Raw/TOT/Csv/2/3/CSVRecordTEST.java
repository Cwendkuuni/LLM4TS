import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment.";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByIndex_ExistingIndex_ReturnsValue() {
        String result = csvRecord.get(1);
        assertEquals("value2", result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_OutOfBoundsIndex_ThrowsException() {
        csvRecord.get(3);
    }

    @Test
    public void testGetByName_ExistingName_ReturnsValue() {
        String result = csvRecord.get("column2");
        assertEquals("value2", result);
    }

    @Test
    public void testGetByName_NonExistingName_ReturnsNull() {
        String result = csvRecord.get("column5");
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoHeaderMapping_ReturnsException() {
        CSVRecord csvRecordWithoutMapping = new CSVRecord(csvRecord.values(), null, csvRecord.getComment(), csvRecord.getRecordNumber());
        csvRecordWithoutMapping.get("column1");
    }

    @Test
    public void testIsConsistent_ConsistentRecord_ReturnsTrue() {
        boolean result = csvRecord.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistent_InconsistentRecord_ReturnsFalse() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        csvRecord = new CSVRecord(csvRecord.values(), inconsistentMapping, csvRecord.getComment(), csvRecord.getRecordNumber());
        boolean result = csvRecord.isConsistent();
        assertFalse(result);
    }

    @Test
    public void testIsMapped_ExistingName_ReturnsTrue() {
        boolean result = csvRecord.isMapped("column2");
        assertTrue(result);
    }

    @Test
    public void testIsMapped_NonExistingName_ReturnsFalse() {
        boolean result = csvRecord.isMapped("column5");
        assertFalse(result);
    }

    @Test
    public void testIsSet_MappedColumnWithValue_ReturnsTrue() {
        boolean result = csvRecord.isSet("column2");
        assertTrue(result);
    }

    @Test
    public void testIsSet_MappedColumnWithoutValue_ReturnsFalse() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column2", 1);
        csvRecord = new CSVRecord(csvRecord.values(), inconsistentMapping, csvRecord.getComment(), csvRecord.getRecordNumber());
        boolean result = csvRecord.isSet("column2");
        assertFalse(result);
    }

    @Test
    public void testIterator_ReturnsIteratorOverValues() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(csvRecord.size(), count);
    }

    @Test
    public void testValues_ReturnsValuesArray() {
        String[] result = csvRecord.values();
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    @Test
    public void testGetComment_CommentAvailable_ReturnsComment() {
        String result = csvRecord.getComment();
        assertEquals("This is a comment.", result);
    }

    @Test
    public void testGetComment_NoCommentAvailable_ReturnsNull() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, null, recordNumber);
        String result = csvRecord.getComment();
        assertNull(result);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        long result = csvRecord.getRecordNumber();
        assertEquals(1, result);
    }

    @Test
    public void testSize_ReturnsValuesSize() {
        int result = csvRecord.size();
        assertEquals(3, result);
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        String result = csvRecord.toString();
        assertEquals("[value1, value2, value3]", result);
    }
}