import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Test typical case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.ONE));
        
        // Test edge case
        CSVRecord emptyRecord = new CSVRecord(null, null, null, 1);
        assertNull(emptyRecord.get(TestEnum.ONE));
        
        // Test exception handling
        try {
            record.get(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetByIndex() {
        // Test typical case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value2", record.get(1));
        
        // Test edge case
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 1);
        try {
            emptyRecord.get(0);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetByName() {
        // Test typical case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value2", record.get("column2"));
        
        // Test edge case
        CSVRecord emptyRecord = new CSVRecord(new String[]{}, null, null, 1);
        try {
            emptyRecord.get("column1");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Test exception handling
        try {
            record.get("invalidColumn");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
            recordWithoutMapping.get("column1");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    // More test cases for other public methods of CSVRecord can be added here

}