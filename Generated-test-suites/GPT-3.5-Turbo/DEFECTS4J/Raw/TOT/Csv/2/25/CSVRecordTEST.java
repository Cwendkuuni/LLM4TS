import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Constructor test
    @Test
    public void testCSVRecordConstructor() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertNotNull(record);
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    // "get" method test by index
    @Test
    public void testCSVRecordGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        // Test for valid index
        assertEquals("value1", record.get(0));
        
        // Test for invalid index
        try {
            record.get(3);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
            assertEquals("Index 3 out of bounds for length 3", e.getMessage());
        }
    }

    // "get" method test by name
    @Test
    public void testCSVRecordGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Test for valid name
        assertEquals("value1", record.get("column1"));
        
        // Test for invalid name
        assertNull(record.get("column4"));
    }
    
    // "isConsistent" method test
    @Test
    public void testCSVRecordIsConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Test for consistent record
        assertTrue(record.isConsistent());
        
        // Test for inconsistent record
        mapping.put("column3", 2);
        assertFalse(record.isConsistent());
    }
    
    // "isMapped" method test
    @Test
    public void testCSVRecordIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        // Test for mapped column
        assertTrue(record.isMapped("column1"));
        
        // Test for unmapped column
        assertFalse(record.isMapped("column4"));
    }
    
    // "isSet" method test
    @Test
    public void testCSVRecordIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 3);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        // Test for set column
        assertTrue(record.isSet("column1"));
        
        // Test for unset column
        assertFalse(record.isSet("column3"));
        
        // Test for unmapped column
        assertFalse(record.isSet("column4"));
    }
    
    // "iterator" method test
    @Test
    public void testCSVRecordIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        int count = 0;
        for (String value : record) {
            assertEquals(values[count], value);
            count++;
        }
    }
    
    // "toString" method test
    @Test
    public void testCSVRecordToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String expected = "[value1, value2, value3]";
        assertEquals(expected, record.toString());
    }

}