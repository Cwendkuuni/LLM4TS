package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord record;

    @Before
    public void setUp() {
        String[] values = {"1", "John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("id", 0);
        mapping.put("firstName", 1);
        mapping.put("lastName", 2);
        record = new CSVRecord(values, mapping, null, 1);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("1", record.get(RecordEnum.ID));
        assertEquals("John", record.get(RecordEnum.FIRST_NAME));
        assertEquals("Doe", record.get(RecordEnum.LAST_NAME));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("1", record.get(0));
        assertEquals("John", record.get(1));
        assertEquals("Doe", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("1", record.get("id"));
        assertEquals("John", record.get("firstName"));
        assertEquals("Doe", record.get("lastName"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[] {}, null, null, 1);
        recordWithoutMapping.get("id");
    }
    
    @Test
    public void testGetByNameNotFound() {
        try {
            record.get("age");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Mapping for age not found"));
        }    
    }
    
    @Test
    public void testGetByNameIndexOutOfBoundsException() {
        try {
            record.get("address");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Index for header 'address' is"));
        }
    }
    
    @Test
    public void testGetComment() {
        assertNull(record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithNullMapping() {
        CSVRecord recordWithNullMapping = new CSVRecord(new String[] {"1", "John", "Doe"}, null, null,
                1);
        assertTrue(recordWithNullMapping.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithDifferentMappingSize() {
        Map<String, Integer> differentMapping = new HashMap<>();
        differentMapping.put("id", 0);
        differentMapping.put("firstName", 1);
        CSVRecord recordWithDifferentMapping = new CSVRecord(new String[] {"1", "John"}, differentMapping,
                null, 1);
        assertFalse(recordWithDifferentMapping.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("id"));
        assertTrue(record.isMapped("firstName"));
        assertTrue(record.isMapped("lastName"));
        assertFalse(record.isMapped("age"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(record.isSet("id"));
        assertTrue(record.isSet("firstName"));
        assertTrue(record.isSet("lastName"));
        assertFalse(record.isSet("age"));
    }
    
    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertEquals("1", iterator.next());
        assertEquals("John", iterator.next());
        assertEquals("Doe", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "0");
        map.put("firstName", "John");
        map.put("lastName", "Doe");
        
        assertEquals(map, record.putIn(new HashMap<>()));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        List<String> list = Arrays.asList("1", "John", "Doe");
        assertEquals(list, record.toList());
    }
    
    @Test
    public void testToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("firstName", "John");
        map.put("lastName", "Doe");
        
        assertEquals(map, record.toMap());
    }
    
    @Test
    public void testToString() {
        assertEquals("[1, John, Doe]", record.toString());
    }
    
    enum RecordEnum {
        ID, FIRST_NAME, LAST_NAME;
    }
}