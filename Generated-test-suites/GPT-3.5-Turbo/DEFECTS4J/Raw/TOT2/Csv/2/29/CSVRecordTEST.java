import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"John", "Doe", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        mapping.put("age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("John", record.get("firstName"));
        assertEquals("Doe", record.get("lastName"));
        assertEquals("30", record.get("age"));
        assertNull(record.get("email"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        mapping.put("age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping.remove("age");
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("firstName"));
        assertTrue(record.isMapped("lastName"));
        assertFalse(record.isMapped("age"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("firstName"));
        assertTrue(record.isSet("lastName"));
        assertFalse(record.isSet("age"));
    }

    // Additional test cases can be added for other methods

}