import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class CSVRecordTest {
    
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "comment", 1L);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);
        
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
        
        assertNull(record.get("nonExistentColumn"));
    }
    
    @Test
    public void testIsConsistent() {
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);
        
        assertTrue(record.isConsistent());
        
        mapping.put("column3", 2);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);
        
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("nonExistentColumn"));
    }
    
    @Test
    public void testIsSet() {
        HashMap<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);
        
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("nonExistentColumn"));
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "comment", 1L);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "comment", 1L);
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "comment", 1L);
        assertEquals(1L, record.getRecordNumber());
    }

}