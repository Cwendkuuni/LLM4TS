import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Create a CSVRecord instance
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Test the get method with Enum value
        assertEquals("value1", record.get(TestEnum.VALUE_1));
    }

    @Test
    public void testGetByIndex() {
        // Create a CSVRecord instance
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Test the get method with index
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        // Create a CSVRecord instance
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Test the get method with name
        assertEquals("value3", record.get("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        // Create a CSVRecord instance without mapping
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Test the get method with name when no mapping is provided
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        // Create a CSVRecord instance with mapping
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "comment", 1);

        // Test the get method with invalid name
        record.get("invalidName");
    }

    // More test cases for other methods should be added here

}

enum TestEnum {
    VALUE_1,
    VALUE_2,
    VALUE_3;
}