import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(MyEnum.VALUE1));
        assertEquals("value2", record.get(MyEnum.VALUE2));
    }
    
    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetNameWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("column3");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        record.get("column2");
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isConsistent());
        
        CSVRecord inconsistentRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column3"));
    }
    
    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value1");
        map.put("column2", "value2");
        
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        record.putIn(map);
        assertEquals(4, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals("[value1, value2]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertArrayEquals(new String[]{"value1", "value2"}, record.values());
    }

}