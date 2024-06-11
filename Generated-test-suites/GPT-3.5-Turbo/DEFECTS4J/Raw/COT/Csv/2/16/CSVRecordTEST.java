import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {
    
    // CSVRecord instance used for testing
    private CSVRecord record;
    
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertNull(record.get("name3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1"};
        record = new CSVRecord(values, null, null, 1);
        
        record.get("name");
    }
    
    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"value1"};
        record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMatchingSize() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMismatchingSize() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        record = new CSVRecord(values, null, null, 1);
        
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2"};
        record = new CSVRecord(values, null, null, 1);
        
        assertArrayEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1"};
        record = new CSVRecord(values, null, "comment", 1);
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1"};
        record = new CSVRecord(values, null, null, 2);
        
        assertEquals(2, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        record = new CSVRecord(values, null, null, 1);
        
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[value1, value2]", record.toString());
    }
}