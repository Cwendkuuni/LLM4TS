import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1, "name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
        assertNull(record.get("nonExistent"));
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1, "name3", 2);
        CSVRecord consistentRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(consistentRecord.isConsistent());

        String[] inconsistentValues = {"value1", "value2"};
        CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, mapping, null, 1);
        
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1, "name3", 2);
        CSVRecord record = new CSVRecord(null, mapping, null, 1);
        
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistent"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = Map.of("name1", 0, "name2", 1, "name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("nonExistent"));
    }
    
    // Add more test cases for other methods as needed

}