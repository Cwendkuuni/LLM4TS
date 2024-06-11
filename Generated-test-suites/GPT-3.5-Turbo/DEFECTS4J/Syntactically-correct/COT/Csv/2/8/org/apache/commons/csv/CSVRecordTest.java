import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    @Test
    public void testGet() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("a", record.get(0));
    }
    
    @Test
    public void testGetWithName() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("a", record.get("col1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithNameNoMapping() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("col1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("col2"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValues() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment.", 1);
        assertEquals("This is a comment.", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[a, b, c]", record.toString());
    }
}