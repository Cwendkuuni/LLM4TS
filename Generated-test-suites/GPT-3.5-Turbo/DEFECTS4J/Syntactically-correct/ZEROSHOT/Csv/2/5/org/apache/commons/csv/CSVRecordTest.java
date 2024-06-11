import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName_noMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertThrows(IllegalStateException.class, () -> {
            csvRecord.get("name");
        });
    }

    @Test
    public void testGetByName_validMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertEquals("value1", csvRecord.get("name1"));
        assertEquals("value2", csvRecord.get("name2"));
        assertEquals("value3", csvRecord.get("name3"));
    }

    @Test
    public void testGetByName_invalidMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertNull(csvRecord.get("name3"));
    }
    
    @Test
    public void testIsConsistent_noMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertTrue(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsConsistent_validMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_invalidMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped_validMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isMapped("name1"));
        assertTrue(csvRecord.isMapped("name2"));
        assertTrue(csvRecord.isMapped("name3"));
    }

    @Test
    public void testIsMapped_invalidMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(csvRecord.isMapped("name3"));
    }

    @Test
    public void testIsSet_validMapping_validValue() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertTrue(csvRecord.isSet("name1"));
        assertTrue(csvRecord.isSet("name2"));
        assertTrue(csvRecord.isSet("name3"));
    }

    @Test
    public void testIsSet_validMapping_invalidValue() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        
        assertFalse(csvRecord.isSet("name3"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        Iterator<String> iterator = csvRecord.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertArrayEquals(values, csvRecord.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, "comment", 1);
        
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 5);
        
        assertEquals(5, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
        
        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}