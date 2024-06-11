import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    
    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "", 1L);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "This is a comment", 1L);
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "", 5L);
        assertEquals(5L, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertTrue(record1.isConsistent());
        
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, mapping, "", 1L);
        assertFalse(record2.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "", 1L);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }
    
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "", 1L);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertEquals(2, record1.size());
        
        CSVRecord record2 = new CSVRecord(new String[]{}, null, "", 1L);
        assertEquals(0, record2.size());
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "existingValue");
        
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "", 1L);
        record.putIn(map);
        
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertEquals(Arrays.asList("value1", "value2"), record.toList());
    }
    
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "", 1L);
        Map<String, String> map = record.toMap();
        
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1L);
        assertArrayEquals(new String[]{"value1", "value2"}, record.values());
    }
}

enum TestEnum {
    VALUE1, VALUE2;
}