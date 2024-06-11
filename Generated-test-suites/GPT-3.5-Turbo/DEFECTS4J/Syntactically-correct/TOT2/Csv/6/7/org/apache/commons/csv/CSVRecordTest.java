import static org.junit.Assert.*;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test getting value by Enum
        assertEquals("value1", csvRecord.get(TestEnum.ENUM_1));
    }

    @Test
    public void testGetIndex() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test getting value by index
        assertEquals("value2", csvRecord.get(1));
    }

    @Test
    public void testGetName() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test getting value by name
        assertEquals("value3", csvRecord.get("name"));
    }

    @Test
    public void testGetComment() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test getting comment
        assertEquals("Test comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test getting record number
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Create a CSVRecord instance with mapping
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "Test comment", 1);

        // Test consistency
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Create a CSVRecord instance with mapping
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "Test comment", 1);

        // Test if column is mapped
        assertTrue(csvRecord.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        // Create a CSVRecord instance with mapping
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "Test comment", 1);

        // Test if column is set
        assertTrue(csvRecord.isSet("name"));
    }

    @Test
    public void testIterator() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "Test comment", 1);

        // Test iterator
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
       // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "Test comment", 1);

        // Test put in map
        HashMap<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertEquals(2, map.size());
    }

    @Test
    public void testSize() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null,"Test comment", 1);

        // Test size
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToMap() {
        // Create a CSVRecord instance
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "Test comment", 1);

        // Test to map
        Map<String, String> map = csvRecord.toMap();
        assertEquals(2, map.size());
    }
}