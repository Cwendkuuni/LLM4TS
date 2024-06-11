
import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertNull(record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentInconsistentMapping() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
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
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);
        
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 5);
        
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(2, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[value1, value2]", record.toString());
    }
}

