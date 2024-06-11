import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {
    
    @Test
    public void testConstructorWithValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals(values.length, record.size());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testConstructorWithNullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testGetByEnum() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("value1", record.get(TestEnum.ONE));
        assertEquals("value2", record.get(TestEnum.TWO));
        assertEquals("value3", record.get(TestEnum.THREE));
    }
    
    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }
    
    @Test
    public void testGetNonExistingColumnName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        try {
            record.get("column2");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for column2 not found, expected one of [column1]", e.getMessage());
        }
    }
    
    @Test
    public void testGetNonExistingColumnIndex() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        try {
            record.get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '1' is 1 but CSVRecord only has 1 values!", e.getMessage());
        }
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 1;
        
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        assertEquals(values.length, record.size());
    }

}

enum TestEnum {
    ONE,
    TWO,
    THREE;
}