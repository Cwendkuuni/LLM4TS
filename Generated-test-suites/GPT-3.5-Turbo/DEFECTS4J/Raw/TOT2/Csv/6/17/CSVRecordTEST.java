import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value1", record.get(TestEnum.TEST1));
        
        // Test edge case
        assertEquals(null, record.get(TestEnum.TEST3));
    }

    @Test
    public void testGetInt() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value2", record.get(1));
        
        // Test edge case
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            record.get(2);
        });
    }

    @Test
    public void testGetString() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals("value1", record.get("column1"));
        
        // Test exception handling
        assertThrows(IllegalStateException.class, () -> {
            record.get("column2");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            record.get("unknownColumn");
        });
    }

    @Test
    public void testIsSet() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
    }

    @Test
    public void testPutIn() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        // Test typical use case
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }

}