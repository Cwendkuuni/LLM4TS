
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        // Test case for get(int i) method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        // Test case for get(String name) method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNull(record.get("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        // Test case for get(String name) method without mapping
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        // Test case for isConsistent() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMapping() {
        // Test case for isConsistent() method with mapping
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name", 0), null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        // Test case for isConsistent() method with inconsistent mapping
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name", 0), null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Test case for isMapped() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name", 0), null, 1);
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("other"));
    }

    @Test
    public void testIsSet() {
        // Test case for isSet() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, Map.of("name", 0), null, 1);
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("other"));
    }

    @Test
    public void testIterator() {
        // Test case for iterator() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String[] expected = {"value1", "value2"};
        int i = 0;
        for (String value : record) {
            assertEquals(expected[i], value);
            i++;
        }
    }

    @Test
    public void testValues() {
        // Test case for values() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String[] expected = {"value1", "value2"};
        assertArrayEquals(expected, record.values());
    }

    @Test
    public void testGetComment() {
        // Test case for getComment() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Test case for getRecordNumber() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        // Test case for size() method
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }

}
