import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test get(Enum<?>) method
    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", record.get(EnumTest.VALUE2));
    }

    // Test get(int) method
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value2", record.get(1));
    }

    // Test get(String) method with no header mapping
    @Test(expected=IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    // Test get(String) method with unmapped name
    @Test(expected=IllegalArgumentException.class)
    public void testGetByUnmappedName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    // Test get(String) method with inconsistent record
    @Test(expected=IllegalArgumentException.class)
    public void testGetByInconsistentName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 1);
        mapping.put("age", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        record.get("age");
    }

    // Test getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    // Test getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 10);
        assertEquals(10, record.getRecordNumber());
    }

    // Test isConsistent() method with consistent record
    @Test
    public void testIsConsistentTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    // Test isConsistent() method with inconsistent record
    @Test
    public void testIsConsistentFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    // Test isMapped() method with mapped name
    @Test
    public void testIsMappedTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isMapped("name"));
    }

    // Test isMapped() method with unmapped name
    @Test
    public void testIsMappedFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertFalse(record.isMapped("address"));
    }

    // Test isSet() method with set name
    @Test
    public void testIsSetTrue() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isSet("name"));
    }

    // Test isSet() method with unset name
    @Test
    public void testIsSetFalse() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertFalse(record.isSet("address"));
    }

    // Test iterator() method
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test toMap() method
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name"));
        assertEquals("value2", map.get("age"));
    }

    // Test size() method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    // Test values() method
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
}