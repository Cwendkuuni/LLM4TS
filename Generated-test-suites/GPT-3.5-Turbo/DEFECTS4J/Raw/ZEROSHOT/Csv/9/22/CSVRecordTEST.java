import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGet_withValidEnum_shouldReturnCorrectValue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        String result = record.get(TestEnum.TEST_ENUM);

        assertEquals("value1", result);
    }

    @Test
    public void testGet_withValidIndex_shouldReturnCorrectValue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        String result = record.get(1);

        assertEquals("value2", result);
    }

    @Test
    public void testGet_withValidName_shouldReturnCorrectValue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        String result = record.get("name2");

        assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGet_withNoMapping_shouldThrowIllegalStateException() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_withInvalidName_shouldThrowIllegalArgumentException() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_withInvalidIndex_shouldThrowIllegalArgumentException() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        record.get(2);
    }

    @Test
    public void testGetComment_withComment_shouldReturnCorrectComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);

        String result = record.getComment();

        assertEquals("This is a comment", result);
    }

    @Test
    public void testGetComment_withNoComment_shouldReturnNull() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        String result = record.getComment();

        assertNull(result);
    }

    @Test
    public void testGetRecordNumber_withValidRecordNumber_shouldReturnCorrectRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);

        long result = record.getRecordNumber();

        assertEquals(5, result);
    }

    @Test
    public void testIsConsistent_withMappingAndMatchingSize_shouldReturnTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void testIsConsistent_withMappingAndMismatchingSize_shouldReturnFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, generateMapping(), null, 1);

        boolean result = record.isConsistent();

        assertFalse(result);
    }

    @Test
    public void testIsConsistent_withNoMappingAndAnySize_shouldReturnTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);

        boolean result = record.isConsistent();

        assertTrue(result);
    }

    @Test
    public void testIsMapped_withMappedColumn_shouldReturnTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isMapped("name2");

        assertTrue(result);
    }

    @Test
    public void testIsMapped_withUnmappedColumn_shouldReturnFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isMapped("name3");

        assertFalse(result);
    }

    @Test
    public void testIsSet_withMappedAndExistingColumn_shouldReturnTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isSet("name2");

        assertTrue(result);
    }

    @Test
    public void testIsSet_withMappedAndNonExistingColumn_shouldReturnFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isSet("name3");

        assertFalse(result);
    }

    @Test
    public void testIsSet_withUnmappedColumn_shouldReturnFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        boolean result = record.isSet("name1");

        assertFalse(result);
    }

    @Test
    public void testIterator_shouldReturnIteratorOverValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        Iterator<String> iterator = record.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn_withMapping_shouldPopulateMapWithValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, generateMapping(), null, 1);

        HashMap<String, String> map = record.putIn(new HashMap<String, String>());

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }

    @Test
    public void testSize_withValues_shouldReturnCorrectSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        int result = record.size();

        assertEquals(2, result);
    }

    private Map<String, Integer> generateMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        return mapping;
    }

    private enum TestEnum {
        TEST_ENUM
    }
}