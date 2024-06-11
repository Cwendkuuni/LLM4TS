import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByInvalidIndex() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get(3);
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("a", record.get("col1"));
        assertEquals("b", record.get("col2"));
        assertEquals("c", record.get("col3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("col1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values1 = {"a", "b", "c"};
        String[] values2 = {"a", "b"};
        Map<String, Integer> mapping1 = new HashMap<>();
        mapping1.put("col1", 0);
        mapping1.put("col2", 1);
        mapping1.put("col3", 2);
        Map<String, Integer> mapping2 = new HashMap<>();
        mapping2.put("col1", 0);
        mapping2.put("col2", 1);
        CSVRecord record1 = new CSVRecord(values1, mapping1, null, 1);
        CSVRecord record2 = new CSVRecord(values2, mapping2, null, 1);
        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("col4"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"a", "b", "c"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
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
        CSVRecord record = new CSVRecord(values, null, "Sample comment", 1);
        assertEquals("Sample comment", record.getComment());
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