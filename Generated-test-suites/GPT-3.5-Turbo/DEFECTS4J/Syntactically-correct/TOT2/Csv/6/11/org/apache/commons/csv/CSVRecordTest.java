import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals("value1", record.get(TestEnum.TEST_ENUM_1));
        
        // Edge case - Enum value not found
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertNull(record2.get(TestEnum.TEST_ENUM_3));
    }

    @Test
    public void testGetInt() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals("value1", record.get(0));
        
        // Edge case - Index out of bounds
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        try {
            record2.get(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetString() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals("value2", record.get("test2"));
        
        // Edge case - Name not mapped
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        try {
            record2.get("test3");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetComment() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals("Comment", record.getComment());
        
        // Edge case - No comment available
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertNull(record2.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertTrue(record.isConsistent());
        
        // Edge case - Inconsistent record size
        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, null, "Comment", 1);
        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertFalse(record.isMapped("test1"));
    }

    @Test
    public void testIsSet() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertTrue(record.isSet("test2"));
        
        // Edge case - Column not set
        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, null, "Comment", 1);
        assertFalse(record2.isSet("test1"));
    }

    @Test
    public void testIterator() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertTrue(record.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertNotNull(record.putIn(new HashMap<String, String>()));
    }

    @Test
    public void testSize() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertNotNull(record.toMap());
    }

    @Test
    public void testToString() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertNotNull(record.toString());
    }

    @Test
    public void testValues() {
        // Typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Comment", 1);
        assertNotNull(record.values());
    }

}