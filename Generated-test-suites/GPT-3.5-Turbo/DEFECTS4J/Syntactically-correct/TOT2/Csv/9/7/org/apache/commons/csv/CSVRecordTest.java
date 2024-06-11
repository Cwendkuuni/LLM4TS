import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        String result = record.get(TestEnum.TEST1);

        // Assertion
        assertEquals("value1", result);
    }

    @Test
    public void testGetByIndex() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        String result = record.get(1);

        // Assertion
        assertEquals("value2", result);
    }

    @Test
    public void testGetByName() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        String result = record.get("column2");

        // Assertion
        assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        record.get("column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "comment", 1);

        // Execution
        record.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameOutOfBoundIndex() {
        // Setup
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);

        // Execution
        record.get("column1");
    }

    @Test
    public void testGetComment() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        String result = record.getComment();

        // Assertion
        assertEquals("comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 5);

        // Execution
        long result = record.getRecordNumber();

        // Assertion
        assertEquals(5, result);
    }

    @Test
    public void testIsConsistent() {
        // Setup
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);

        // Execution
        boolean result1 = record1.isConsistent();
        boolean result2 = record2.isConsistent();

        // Assertion
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsMapped() {
        // Setup
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "comment", 1);

        // Execution
        boolean result1 = record.isMapped("column1");
        boolean result2 = record.isMapped("column3");

        // Assertion
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testIsSet() {
        // Setup
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "comment", 1);

        // Execution
        boolean result1 = record.isSet("column1");
        boolean result2 = record.isSet("column3");

        // Assertion
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testSize() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        int result = record.size();

        // Assertion
        assertEquals(3, result);
    }

    @Test
    public void testToList() {
        // Setup
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);

        // Execution
        List<String> result = record.toList();

        // Assertion
        assertEquals(Arrays.asList("value1", "value2", "value3"), result);
    }

    @Test
    public void testToMap() {
        // Setup
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "comment", 1);

        // Execution
        Map<String, String> result = record.toMap();

        // Assertion
        assertEquals(2, result.size());
        assertEquals("value1", result.get("column1"));
        assertEquals("value2", result.get("column2"));
    }

}