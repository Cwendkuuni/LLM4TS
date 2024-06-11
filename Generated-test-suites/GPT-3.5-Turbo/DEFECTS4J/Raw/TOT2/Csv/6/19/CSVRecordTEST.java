import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "test comment", 1);
        assertEquals("value1", record.get(ColumnEnum.COLUMN_1));

        // Test edge case where enum does not exist (expecting IllegalArgumentException)
        try {
            record.get(ColumnEnum.INVALID_COLUMN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testGetInt() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "test comment", 1);
        assertEquals("value2", record.get(1));

        // Test edge case where index is out of bounds (expecting ArrayIndexOutOfBoundsException)
        try {
            record.get(5);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // pass
        }
    }

    @Test
    public void testGetString() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "test comment", 1);
        assertEquals("value3", record.get("COLUMN_3"));

        // Test edge case where name is not mapped (expecting IllegalArgumentException)
        try {
            record.get("INVALID_COLUMN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }

        // Test edge case where index is out of bounds (expecting IllegalArgumentException)
        try {
            record.get("COLUMN_4");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testIsConsistent() {
        // Test typical use case
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "test comment", 1);
        assertTrue(record1.isConsistent());

        // Test edge case where header is not provided (expecting true)
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "test comment", 1);
        assertTrue(record2.isConsistent());

        // Test edge case where header size does not match record size (expecting false)
        CSVRecord record3 = new CSVRecord(new String[]{"value1"}, null, "test comment", 1);
        assertFalse(record3.isConsistent());
    }

    // Additional test cases for other public methods can be added here

}