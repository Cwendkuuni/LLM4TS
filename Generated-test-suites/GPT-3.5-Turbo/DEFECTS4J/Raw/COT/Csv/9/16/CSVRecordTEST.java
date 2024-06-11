
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String result = record.get(TestEnum.VALUE1);
        assertNull(result);
    }

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.get(1);
        assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithoutMappingNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("column1");
    }

    @Test
    public void testGetCommentNull() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String result = record.getComment();
        assertNull(result);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 0);
        String result = record.getComment();
        assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 10);
        long result = record.getRecordNumber();
        assertEquals(10, result);
    }

    @Test
    public void testIsConsistentNullMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistentEmptyMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistentWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistentWithMappingMismatch() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isConsistent();
        assertFalse(result);
    }

    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        boolean result = record.isMapped("column1");
        assertFalse(result);
    }

    @Test
    public void testIsMappedWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        boolean result = record.isMapped("column1");
        assertTrue(result);
    }

    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        boolean result = record.isSet("column1");
        assertFalse(result);
    }

    @Test
    public void testIsSetWithMappingOutOfBound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 1);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isSet("column1");
        assertFalse(result);
    }

    @Test
    public void testIsSetWithMappingAndValue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isSet("column1");
        assertTrue(result);
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutInEmptyMap() {
        String[] values = {"value1", "value2"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Map<String, String> result = record.putIn(map);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("column1"));
        assertEquals("value2", result.get("column2"));
    }

    @Test
    public void testPutInExistingMap() {
        String[] values = {"value1", "value2"};
        Map<String, String> map = new HashMap<>();
        map.put("column3", "value3");
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Map<String, String> result = record.putIn(map);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("column1"));
        assertEquals("value2", result.get("column2"));
        assertEquals("value3", result.get("column3"));
    }

    @Test
    public void testSizeEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        int result = record.size();
        assertEquals(0, result);
    }

    @Test
    public void testSizeNonEmpty() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        int result = record.size();
        assertEquals(3, result);
    }

    @Test
    public void testToMapEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        Map<String, String> result = record.toMap();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testToMapNonEmpty() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Map<String, String> result = record.toMap();
        assertEquals(2, result.size());
        assertEquals("value1", result.get("column1"));
        assertEquals("value2", result.get("column2"));
    }

    @Test
    public void testToStringEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String result = record.toString();
        assertEquals("[]", result);
    }

    @Test
    public void testToStringNonEmpty() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.toString();
        assertEquals("[value1, value2, value3]", result);
    }

    @Test
    public void testValuesEmpty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String[] result = record.values();
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testValuesNonEmpty() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String[] result = record.values();
        assertArrayEquals(values, result);
    }
}
