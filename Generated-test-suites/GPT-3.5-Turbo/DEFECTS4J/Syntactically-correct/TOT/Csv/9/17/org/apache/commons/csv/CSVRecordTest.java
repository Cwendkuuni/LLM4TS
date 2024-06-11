import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setup() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetByEnum_ReturnsCorrectValue() {
        assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex_ReturnsCorrectValue() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName_WithValidName_ReturnsCorrectValue() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_WithNoHeaderMapping_ThrowsIllegalStateException() {
        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_WithInvalidName_ThrowsIllegalArgumentException() {
        csvRecord.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_WithInvalidIndex_ThrowsIllegalArgumentException() {
        mapping.put("column4", 3);
        csvRecord.get("column4");
    }

    @Test
    public void testGetComment_ReturnsCorrectComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber_ReturnsCorrectRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent_WithMatchingSizes_ReturnsTrue() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_WithMismatchingSizes_ReturnsFalse() {
        values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped_WithMappedColumn_ReturnsTrue() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
    }

    @Test
    public void testIsMapped_WithUnmappedColumn_ReturnsFalse() {
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet_WithSetColumn_ReturnsTrue() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
    }

    @Test
    public void testIsSet_WithUnsetColumn_ReturnsFalse() {
        mapping.put("column4", 3);
        assertFalse(csvRecord.isSet("column4"));
    }

    @Test
    public void testIterator_ReturnsIterator() {
        assertNotNull(csvRecord.iterator());
    }

    @Test
    public void testPutIn_PutsValuesInMap() {
        Map<String, String> map = csvRecord.putIn(new HashMap<>());

        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize_ReturnsCorrectSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList_ReturnsCorrectList() {
        assertEquals(Arrays.asList("value1", "value2", "value3"), csvRecord.toList());
    }

    @Test
    public void testToMap_ReturnsCorrectMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString_ReturnsCorrectStringRepresentation() {
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    @Test
    public void testValues_ReturnsCorrectValuesArray() {
        assertArrayEquals(new String[] {"value1", "value2", "value3"}, csvRecord.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}