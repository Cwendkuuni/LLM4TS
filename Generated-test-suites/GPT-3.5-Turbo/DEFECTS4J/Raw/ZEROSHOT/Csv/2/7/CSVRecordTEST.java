import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        
        try {
            record.get(3);
            fail("Exception not thrown");
        } catch(ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2", "name3"}, null, null, 1);
        
        assertNull(record.get("value1"));
        assertNull(record.get("value2"));
        assertNull(record.get("value3"));
        
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        mapping.put("value3", 2);
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        assertEquals("value1", record.get("value1"));
        assertEquals("value2", record.get("value2"));
        assertEquals("value3", record.get("value3"));
        
        try {
            record.get("value4");
            fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            record.get(null);
            fail("Exception not thrown");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertTrue(record.isConsistent());
        
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        mapping.put("value3", 2);
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        assertTrue(record.isConsistent());
        
        mapping.put("value4", 3);
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2", "name3"}, null, null, 1);
        
        assertFalse(record.isMapped("value1"));
        assertFalse(record.isMapped("value2"));
        assertFalse(record.isMapped("value3"));
        
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        mapping.put("value3", 2);
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        assertTrue(record.isMapped("value1"));
        assertTrue(record.isMapped("value2"));
        assertTrue(record.isMapped("value3"));
        
        assertFalse(record.isMapped("value4"));
        
        try {
            record.isMapped(null);
            fail("Exception not thrown");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"name1", "name2", "name3"}, null, null, 1);
        
        assertFalse(record.isSet("value1"));
        assertFalse(record.isSet("value2"));
        assertFalse(record.isSet("value3"));
        
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("value1", 0);
        mapping.put("value2", 1);
        mapping.put("value3", 2);
        record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        assertTrue(record.isSet("value1"));
        assertTrue(record.isSet("value2"));
        assertTrue(record.isSet("value3"));
        
        assertFalse(record.isSet("value4"));
        
        try {
            record.isSet(null);
            fail("Exception not thrown");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        Iterator<String> iterator = record.iterator();
        
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
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        assertEquals("[value1, value2, value3]", record.toString());
    }

}