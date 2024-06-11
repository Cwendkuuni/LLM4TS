import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("apple", record.get(0));
        assertEquals("banana", record.get(1));
        assertEquals("cherry", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("fruit", 0);
        mapping.put("color", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("apple", record.get("fruit"));
        assertNull(record.get("name"));
        assertEquals("cherry", record.get("color"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("fruit");
    }
    
    @Test
    public void testIsConsistentTrue() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentFalse() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("fruit", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMappedTrue() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("fruit", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isMapped("fruit"));
        assertFalse(record.isMapped("color"));
    }
    
    @Test
    public void testIsMappedFalse() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isMapped("fruit"));
        assertFalse(record.isMapped("color"));
    }
    
    @Test
    public void testIsSetTrue() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("fruit", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("fruit"));
        assertFalse(record.isSet("color"));
    }
    
    @Test
    public void testIsSetFalse() {
        String[] values = {"apple", "banana", "cherry"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isSet("fruit"));
        assertFalse(record.isSet("color"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        Iterator<String> iterator = record.iterator();
        assertEquals("apple", iterator.next());
        assertEquals("banana", iterator.next());
        assertEquals("cherry", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValues() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertArrayEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);
        
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 5);
        
        assertEquals(5, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"apple", "banana", "cherry"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[apple, banana, cherry]", record.toString());
    }
}