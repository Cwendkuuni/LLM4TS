import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {
    
    private CSVRecord record;
    
    @Before
    public void setup() {
        record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }
    
    @Test
    public void testGetByName() {
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
    }
    
    @Test
    public void testGetComment() {
        assertNull(record.getComment());
        
        record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1L);
        
        assertEquals("comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
        
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
    }
    
    @Test
    public void testIsSet() {
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertFalse(record.isSet("name3"));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, record.size());
    }
    
    @Test
    public void testIterator() {
        assertTrue(record.iterator().hasNext());
        assertEquals("value1", record.iterator().next());
    }
    
    @Test
    public void testPutIn() {
        record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1L);
        
        assertEquals(createMap(), record.putIn(new HashMap<String, String>()));
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2]", record.toString());
    }
    
    @Test
    public void testValues() {
        assertEquals(new String[]{"value1", "value2"}, record.values());
    }
    
    
    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        
        return mapping;
    }
    
    private Map<String, String> createMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name1", "value1");
        map.put("name2", "value2");
        
        return map;
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}