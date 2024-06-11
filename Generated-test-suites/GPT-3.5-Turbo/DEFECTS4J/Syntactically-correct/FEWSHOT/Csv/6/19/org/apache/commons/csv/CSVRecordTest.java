import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    
    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("column2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 2);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("column1");
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), "Comment", 1);
        assertEquals("Comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertFalse(record1.isConsistent());
        
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record2.isConsistent());
        
        CSVRecord record3 = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        assertFalse(record3.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
    }
    
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        
        CSVRecord record1 = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        assertTrue(record1.isSet("column1"));
        
        CSVRecord record2 = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        assertFalse(record2.isSet("column2"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals("value1", record.iterator().next());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("column1", "value1");
        expectedMap.put("column2", "value2");
        
        assertEquals(expectedMap, record.toMap());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertEquals("[value1, value2]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<String, Integer>(), null, 1);
        assertArrayEquals(new String[]{"value1", "value2"}, record.values());
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }
}