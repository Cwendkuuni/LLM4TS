import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetIntIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals(null, record.get(-1));
        assertEquals(null, record.get(3));
    }

    @Test
    public void testGetStringName() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals(null, record.get("Address"));
        assertEquals(null, record.get("InvalidColumnName"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 0);
        CSVRecord record2 = new CSVRecord(new String[0], null, null, 0);
        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
    }

    // Other test cases for edge cases and exception handling scenarios

}