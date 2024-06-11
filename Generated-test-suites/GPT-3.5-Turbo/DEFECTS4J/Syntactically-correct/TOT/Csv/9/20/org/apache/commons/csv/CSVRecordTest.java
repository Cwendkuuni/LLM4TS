import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.*;

public class CSVRecordTest {

    private static final String[] VALUES = { "value1", "value2" };
    private static final long RECORD_NUMBER = 1;
    private static final String COMMENT = "This is a comment.";
    private CSVRecord record;

    @Before
    public void setup() {
        record = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
    }

    @Test
    public void testGetEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }

    @Test
    public void testGetIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetName() {
        assertNull(record.get("name")); // Edge case: record with no header mapping
        try {
            record.get("value3"); // Error scenario: unmapped name
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for value3 not found, expected one of []", e.getMessage());
        }
        try {
            record.get("value2"); // Error scenario: inconsistent record
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'value2' is 1 but CSVRecord only has 2 values!", e.getMessage());
        }
        assertNull(record.get("comment")); // Edge case: no column value, returns null
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("name")); // Edge case: record with no header mapping
        assertFalse(record.isMapped("value3"));
        assertTrue(record.isMapped("value1"));
        assertTrue(record.isMapped("value2"));
    }

    @Test
    public void testIsSet() {
        assertFalse(record.isSet("name")); // Edge case: record with no header mapping
        assertFalse(record.isSet("value3"));
        assertTrue(record.isSet("value1"));
        assertTrue(record.isSet("value2"));
    }

    @Test
    public void testIterator() {
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        CSVRecordTest.TestMap<String, String> map = new CSVRecordTest.TestMap<>();
        record.putIn(map);
        assertTrue(map.containsKey("value1"));
        assertTrue(map.containsKey("value2"));
        assertFalse(map.containsKey("value3"));
        assertEquals("value1", map.get("value1"));
        assertEquals("value2", map.get("value2"));
        assertNull(map.get("value3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(VALUES[0], record.toList().get(0));
        assertEquals(VALUES[1], record.toList().get(1));
    }

    @Test
    public void testToMap() {
        CSVRecordTest.TestMap<String, String> map = (CSVRecordTest.TestMap<String, String>) record.toMap();
        assertTrue(map.containsKey("value1"));
        assertTrue(map.containsKey("value2"));
        assertFalse(map.containsKey("value3"));
        assertEquals("value1", map.get("value1"));
        assertEquals("value2", map.get("value2"));
        assertNull(map.get("value3"));
    }

    @Test
    public void testValues() {
        assertArrayEquals(VALUES, record.values());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }

    private static class TestMap<K, V> extends HashMap<K, V> {
        @Override
        public V put(K key, V value) {
            if (key.toString().equals("value3")) {
                throw new UnsupportedOperationException("Test exception");
            }
            return super.put(key, value);
        }
    }

}