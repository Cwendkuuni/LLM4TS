import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    // Test the get(Enum) method
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String result = record.get(TestEnum.TEST_ENUM);
        assertEquals("value1", result);
    }

    // Test the get(int) method
    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String result = record.get(0);
        assertEquals("value1", result);
    }

    // Test the get(String) method with no mapping
    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        record.get("column1");
    }

    // Test the get(String) method with valid mapping
    @Test
    public void testGetValidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        String result = record.get("column1");
        assertEquals("value1", result);
    }

    // Test the get(String) method with invalid mapping
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        record.get("column4");
    }

    // Test the get(String) method with inconsistent record
    @Test(expected = IllegalArgumentException.class)
    public void testGetInconsistentRecord() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "", 1);
        record.get("column3");
    }

    // Test the getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        String result = record.getComment();
        assertEquals("This is a comment", result);
    }

    // Test the getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        long result = record.getRecordNumber();
        assertEquals(1, result);
    }

    // Test the isConsistent() method with consistent record
    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        boolean result = record.isConsistent();
        assertTrue(result);
    }

    // Test the isConsistent() method with inconsistent record
    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), "", 1);
        boolean result = record.isConsistent();
        assertFalse(result);
    }

    // Test the isMapped() method with mapped column
    @Test
    public void testIsMappedTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        boolean result = record.isMapped("column1");
        assertTrue(result);
    }

    // Test the isMapped() method with unmapped column
    @Test
    public void testIsMappedFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        boolean result = record.isMapped("column4");
        assertFalse(result);
    }

    // Test the isSet() method with set column
    @Test
    public void testIsSetTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        boolean result = record.isSet("column1");
        assertTrue(result);
    }

    // Test the isSet() method with unset column
    @Test
    public void testIsSetFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        boolean result = record.isSet("column3");
        assertFalse(result);
    }

    // Test the iterator() method
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test the putIn() method with an empty map
    @Test
    public void testPutInEmptyMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        Map<String, String> resultMap = record.putIn(new HashMap<String, String>());
        assertEquals("value1", resultMap.get("column1"));
        assertEquals("value2", resultMap.get("column2"));
        assertEquals("value3", resultMap.get("column3"));
    }

    // Test the putIn() method with an existing map
    @Test
    public void testPutInExistingMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        Map<String, String> existingMap = new HashMap<>();
        existingMap.put("column4", "value4");
        existingMap.put("column5", "value5");
        Map<String, String> resultMap = record.putIn(existingMap);
        assertEquals("value1", resultMap.get("column1"));
        assertEquals("value2", resultMap.get("column2"));
        assertEquals("value3", resultMap.get("column3"));
        assertEquals("value4", resultMap.get("column4"));
        assertEquals("value5", resultMap.get("column5"));
    }

    // Test the size() method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        int result = record.size();
        assertEquals(3, result);
    }

    // Test the toList() method
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        List<String> resultList = record.toList();
        assertEquals("value1", resultList.get(0));
        assertEquals("value2", resultList.get(1));
        assertEquals("value3", resultList.get(2));
    }

    // Test the toMap() method with an empty map
    @Test
    public void testToMapEmptyMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        Map<String, String> resultMap = record.toMap();
        assertEquals("value1", resultMap.get("column1"));
        assertEquals("value2", resultMap.get("column2"));
        assertEquals("value3", resultMap.get("column3"));
    }

    // Test the toMap() method with an existing map
    @Test
    public void testToMapExistingMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, createMapping(), "", 1);
        Map<String, String> existingMap = new HashMap<>();
        existingMap.put("column4", "value4");
        existingMap.put("column5", "value5");
        Map<String, String> resultMap = record.toMap();
        assertEquals("value1", resultMap.get("column1"));
        assertEquals("value2", resultMap.get("column2"));
        assertEquals("value3", resultMap.get("column3"));
        assertNull(resultMap.get("column4"));
        assertNull(resultMap.get("column5"));
    }

    // Test the toString() method
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String result = record.toString();
        assertEquals("[value1, value2, value3]", result);
    }

    // Test the values() method
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String[] result = record.values();
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        return mapping;
    }

    private enum TestEnum {
        TEST_ENUM
    }

}