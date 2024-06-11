import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.TEST_ENUM));

        // Test edge case
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 2);
        assertNull(record.get(TestEnum.TEST_ENUM));
    }

    @Test
    public void testGetByIndex() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));

        // Test edge case
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 2);
        try {
            record.get(2);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Pass
        }
    }

    @Test
    public void testGetByName() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value2", record.get("name2"));

        // Test edge case
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 2);
        try {
            record.get("name3");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetComment() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "Test comment", 1);
        assertEquals("Test comment", record.getComment());

        // Test edge case
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 2);
        assertNull(record.getComment());
    }

    // Other test cases for the remaining methods

}

enum TestEnum {
    TEST_ENUM
}