import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testGet() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertEquals("Value3", record.get(2));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("column");
    }
    
    @Test
    public void testGetWithMapping() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("Value1", record.get("column1"));
        assertEquals("Value2", record.get("column2"));
    }
    
    @Test
    public void testGetWithNonExistingColumn() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertEquals(null, record.get("column3"));
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistent() {
        String[] values = {"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
    }
    
    @Test
    public void testIsNotMapped() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isMapped("column3"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
    }
    
    @Test
    public void testIsNotSet() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(record.isSet("column3"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        Iterator<String> iterator = record.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals("Value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("Value3", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testValues() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        String[] result = record.values();
        
        assertEquals(values, result);
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, "Comment", 1);
        
        assertEquals("Comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"Value1", "Value2", "Value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[Value1, Value2, Value3]", record.toString());
    }
}