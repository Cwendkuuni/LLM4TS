import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 1L);
        
        String result = record.get(MyEnum.VALUE1);
        
        assertNull(result);
    }
    
    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        
        String result = record.get(1);
        
        assertEquals("value2", result);
    }
    
    @Test
    public void testGetStringWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        
        try {
            record.get("name");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for name not found, expected one of null", e.getMessage());
        }
    }
    
    @Test
    public void testGetStringWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                                        new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 1);
                                            put("name3", 2);
                                        }},
                                        null, 1L);
        
        String result = record.get("name2");
        
        assertEquals("value2", result);
    }
    
    @Test
    public void testGetStringWithMappingInvalidIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
                                        new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 2);
                                        }},
                                        null, 1L);
        
        try {
            record.get("name2");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'name2' is 2 but CSVRecord only has 2 values!", e.getMessage());
        }
    }
    
    @Test
    public void testGetStringWithMappingNullString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "null"}, 
                                        new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 1);
                                            put("name3", 2);
                                        }},
                                        null, 1L);
        
        String result = record.get("name3");
        
        assertNull(result);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 1L);
        
        String result = record.getComment();
        
        assertEquals("comment", result);
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 10L);
        
        long result = record.getRecordNumber();
        
        assertEquals(10L, result);
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        
        assertTrue(record1.isConsistent());
        assertFalse(record2.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 1);
                                            put("name3", 2);
                                        }},
                                        null, 1L);
        
        assertTrue(record.isMapped("name2"));
        assertFalse(record.isMapped("name4"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                                         new HashMap<String, Integer>() {{
                                             put("name1", 0);
                                             put("name2", 2);
                                         }},
                                         null, 1L);
        
        assertTrue(record.isSet("name2"));
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name3"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        
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
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                        new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 1);
                                            put("name3", 2);
                                        }},
                                        null, 1L);
        
        Map<String, String> map = new HashMap<>();
        
        record.putIn(map);
        
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1L);
        
        assertEquals(3, record1.size());
        assertEquals(2, record2.size());
    }
    
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"},
                                        new HashMap<String, Integer>() {{
                                            put("name1", 0);
                                            put("name2", 1);
                                            put("name3", 2);
                                        }},
                                        null, 1L);
        
        Map<String, String> map = record.toMap();
        
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        
        String result = record.toString();
        
        assertEquals("[value1, value2, value3]", result);
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1L);
        
        String[] result = record.values();
        
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }
    
    private enum MyEnum {
        VALUE1, VALUE2;
    }
}