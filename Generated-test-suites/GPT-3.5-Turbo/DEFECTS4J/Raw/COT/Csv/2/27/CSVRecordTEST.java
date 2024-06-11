
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        String result = record.get(1);
        
        assertEquals("value2", result);
    }
    
    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        String result = record.get("column2");
        
        assertEquals("value2", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        record.get("column2");
    }
    
    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        boolean result = record.isConsistent();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        boolean result = record.isConsistent();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsConsistentWithIncorrectMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        boolean result = record.isConsistent();
        
        assertFalse(result);
    }
    
    @Test
    public void testIsMappedWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        boolean result = record.isMapped("column2");
        
        assertTrue(result);
    }
    
    @Test
    public void testIsMappedWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        boolean result = record.isMapped("column2");
        
        assertFalse(result);
    }
    
    @Test
    public void testIsSetWithMappingAndValue() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        boolean result = record.isSet("column2");
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSetWithMappingAndNoValue() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        
        boolean result = record.isSet("column3");
        
        assertFalse(result);
    }
    
    @Test
    public void testIsSetWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        boolean result = record.isSet("column2");
        
        assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        int count = 0;
        for (String value : record) {
            count++;
        }
        
        assertEquals(3, count);
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        String[] result = record.values();
        
        assertArrayEquals(values, result);
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 0);
        
        String result = record.getComment();
        
        assertEquals("This is a comment", result);
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 10);
        
        long result = record.getRecordNumber();
        
        assertEquals(10, result);
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        
        int result = record.size();
        
        assertEquals(3, result);
    }

}
