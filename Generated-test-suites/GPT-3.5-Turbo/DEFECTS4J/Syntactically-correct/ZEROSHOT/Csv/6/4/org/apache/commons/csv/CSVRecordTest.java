import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        
        // Test with valid enum value
        assertEquals(null, record.get(TestEnum.TEST1));
        
        // Test with null enum value
        assertEquals(null, record.get(null));
        
        // Test with invalid enum value
        assertEquals(null, record.get(TestEnum.TEST3));
    }
    
    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[] {"a", "b", "c"}, null, null, 0);
        
        // Test with valid index
        assertEquals("b", record.get(1));
        
        // Test with invalid index
        try {
            record.get(3);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetStringMappingNull() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        
        // Test with null mapping
        try {
            record.get("name");
            fail("IllegalStateException should be thrown");
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetStringMappingValid() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, createMapping(), null, 0);
        
        // Test with valid name
        assertEquals("value1", record.get("name1"));
        
        // Test with invalid name
        try {
            record.get("name3");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        
        // Test with comment
        assertEquals("comment", record.getComment());
        
        // Test without comment
        CSVRecord recordWithoutComment = new CSVRecord(null, null, null, 0);
        assertEquals(null, recordWithoutComment.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 10);
        
        // Test with record number
        assertEquals(10, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        // Test with consistent record
        CSVRecord record1 = new CSVRecord(new String[] {"value1", "value2"}, createMapping(), null, 0);
        assertTrue(record1.isConsistent());
        
        // Test with inconsistent record
        CSVRecord record2 = new CSVRecord(new String[] {"value1"}, createMapping(), null, 0);
        assertFalse(record2.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, createMapping(), null, 0);
        
        // Test with mapped name
        assertTrue(record.isMapped("name1"));
        
        // Test with unmapped name
        assertFalse(record.isMapped("name3"));
    }
    
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, createMapping(), null, 0);
        
        // Test with set name
        assertTrue(record.isSet("name1"));
        
        // Test with unset name
        assertFalse(record.isSet("name3"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        
        // Test with size 2
        assertEquals(2, record.size());
    }
    
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, createMapping(), null, 0);
        
        // Test with mapping
        assertEquals(createMapping(), record.toMap());
        
        // Test without mapping
        CSVRecord recordWithoutMapping = new CSVRecord(new String[] {"value1", "value2"}, null, null, 0);
        assertEquals(new HashMap<String, String>(), recordWithoutMapping.toMap());
    }
    
    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        return mapping;
    }
}

enum TestEnum {
    TEST1,
    TEST2,
    TEST3
}