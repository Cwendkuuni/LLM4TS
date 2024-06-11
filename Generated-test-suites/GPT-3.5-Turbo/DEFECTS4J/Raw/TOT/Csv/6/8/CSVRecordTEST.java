import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test for get(Enum<?> e) method
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    // Test for get(int i) method
    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    // Test for get(String name) method
    @Test
    public void testGetString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
    }

    // Test for get(String name) method when no header mapping is provided
    @Test(expected = IllegalStateException.class)
    public void testGetStringNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("col");
    }

    // Test for get(String name) method when the name is not mapped
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1), null, 1);
        record.get("col3");
    }

    // Test for get(String name) method when the index is out of bounds
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("col", 2), null, 1);
        record.get("col");
    }

    // Test for getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }

    // Test for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    // Test for isConsistent() method with consistent record
    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1, "col3", 2), null, 1);
        assertTrue(record.isConsistent());
    }

    // Test for isConsistent() method with inconsistent record
    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1), null, 1);
        assertFalse(record.isConsistent());
    }

    // Test for isMapped() method when name is mapped
    @Test
    public void testIsMappedTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1, "col3", 2), null, 1);
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
    }

    // Test for isMapped() method when name is not mapped
    @Test
    public void testIsMappedFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1), null, 1);
        assertFalse(record.isMapped("col3"));
    }

    // Test for isSet() method when name is mapped and has a value
    @Test
    public void testIsSetTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1, "col3", 2), null, 1);
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
    }

    // Test for isSet() method when name is mapped but does not have a value
    @Test
    public void testIsSetFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("col1", 0), null, 1);
        assertFalse(record.isSet("col2"));
    }

    // Test for iterator() method
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        String[] expectedValues = {"value1", "value2", "value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expectedValues[i++], value);
        }
    }

    // Test for putIn(Map<String, String> map) method
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1, "col3", 2), null, 1);
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    // Test for size() method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    // Test for toMap() method
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                Map.of("col1", 0, "col2", 1, "col3", 2), null, 1);
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    // Test for values() method
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        String[] expectedValues = {"value1", "value2", "value3"};
        assertArrayEquals(expectedValues, record.values());
    }

    // Enum for testing get(Enum<?> e) method
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}