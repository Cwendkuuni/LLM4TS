import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test basic get method by Enum
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        
        assertEquals("value1", record.get(TestEnum.ONE));
        assertEquals("value2", record.get(TestEnum.TWO));
    }
    
    // Test basic get method by index
    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    // Test basic get method by name
    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        assertEquals("value1", record.get("column1"));
    }
    
    // Test error scenario for get method by name
    @Test(expected = IllegalArgumentException.class)
    public void testGetNameError() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        record.get("column2");
    }
    
    // Test basic getComment method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "This is a comment", 1);
        
        assertEquals("This is a comment", record.getComment());
    }
    
    // Test basic getRecordNumber method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{}, null, null, 5);
        
        assertEquals(5, record.getRecordNumber());
    }
    
    // Test isConsistent method
    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    // Test isMapped method
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
    }

    // Test isSet method
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        assertTrue(record.isSet("column1"));
        assertFalse(record.isSet("column2"));
    }
    
    // Test size method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        
        assertEquals(2, record.size());
    }
    
    // Test toMap method
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        
        Map<String, String> resultMap = record.toMap();
        
        assertEquals("value1", resultMap.get("column1"));
    }

    private enum TestEnum {
        ONE,
        TWO
    }
}