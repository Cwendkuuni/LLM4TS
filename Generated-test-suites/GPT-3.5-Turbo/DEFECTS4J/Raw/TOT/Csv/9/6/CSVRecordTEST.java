import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum_ValidEnum_ReturnsValue() {
        CSVRecord record = createCSVRecord();
        String value = record.get(TestEnum.TEST1);
        assertEquals("value1", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByEnum_InvalidEnum_ThrowsException() {
        CSVRecord record = createCSVRecord();
        record.get(TestEnum.INVALID);
    }

    @Test
    public void testGetByIndex_ValidIndex_ReturnsValue() {
        CSVRecord record = createCSVRecord();
        String value = record.get(0);
        assertEquals("value1", value);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndex_InvalidIndex_ThrowsException() {
        CSVRecord record = createCSVRecord();
        record.get(10);
    }

    @Test
    public void testGetByName_ValidName_ReturnsValue() {
        CSVRecord record = createCSVRecord();
        String value = record.get("column1");
        assertEquals("value1", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_MappingNotSpecified_ThrowsException() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidName_ThrowsException() {
        CSVRecord record = createCSVRecord();
        record.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_InvalidIndex_ThrowsException() {
        CSVRecord record = createCSVRecord();
        record.get("column2");
    }

    @Test
    public void testGetComment_NoComment_ReturnsNull() {
        CSVRecord record = createCSVRecord();
        String comment = record.getComment();
        assertNull(comment);
    }

    @Test
    public void testGetComment_WithComment_ReturnsComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, "This is a comment", 1);
        String comment = record.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        CSVRecord record = createCSVRecord();
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }

    @Test
    public void testIsConsistent_ValidRecord_ReturnsTrue() {
        CSVRecord record = createCSVRecord();
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    @Test
    public void testIsConsistent_InvalidRecord_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        boolean isConsistent = record.isConsistent();
        assertFalse(isConsistent);
    }

    @Test
    public void testIsMapped_MappedColumn_ReturnsTrue() {
        CSVRecord record = createCSVRecord();
        boolean isMapped = record.isMapped("column1");
        assertTrue(isMapped);
    }

    @Test
    public void testIsMapped_UnmappedColumn_ReturnsFalse() {
        CSVRecord record = createCSVRecord();
        boolean isMapped = record.isMapped("unknownColumn");
        assertFalse(isMapped);
    }

    @Test
    public void testIsSet_MappedColumnWithValue_ReturnsTrue() {
        CSVRecord record = createCSVRecord();
        boolean isSet = record.isSet("column1");
        assertTrue(isSet);
    }

    @Test
    public void testIsSet_MappedColumnWithNoValue_ReturnsFalse() {
        CSVRecord record = new CSVRecord(new String[]{}, createMapping(), null, 1);
        boolean isSet = record.isSet("column1");
        assertFalse(isSet);
    }

    @Test
    public void testIsSet_UnmappedColumn_ReturnsFalse() {
        CSVRecord record = createCSVRecord();
        boolean isSet = record.isSet("unknownColumn");
        assertFalse(isSet);
    }

    @Test
    public void testIterator_ReturnsIterator() {
        CSVRecord record = createCSVRecord();
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testPutIn_PopulatesMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
    }

    @Test
    public void testSize_ReturnsNumberOfValues() {
        CSVRecord record = createCSVRecord();
        int size = record.size();
        assertEquals(1, size);
    }

    @Test
    public void testToList_ReturnsList() {
        CSVRecord record = createCSVRecord();
        List<String> list = record.toList();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testToMap_ReturnsMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertEquals("value1", map.get("column1"));
    }

    private CSVRecord createCSVRecord() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = createMapping();
        return new CSVRecord(values, mapping, null, 1);
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        return mapping;
    }

    private enum TestEnum {
        TEST1,
        TEST2,
        TEST3
    }
}