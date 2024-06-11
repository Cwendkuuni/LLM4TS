import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class CSVRecordTest {
    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();
    private final String comment = "This is a comment";
    private final long recordNumber = 1L;
    
    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String result = record.get(TestEnum.TEST);
        
        assertEquals("value1", result);
    }
    
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String result = record.get(1);
        
        assertEquals("value2", result);
    }
    
    @Test
    public void testGetByName() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String result = record.get("name");
        
        assertEquals("value1", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNullNameWithoutMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        
        record.get("name");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        record.get("invalid");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        record.get(3);
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String result = record.getComment();
        
        assertEquals("This is a comment", result);
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        long result = record.getRecordNumber();
        
        assertEquals(1L, result);
    }
    
    @Test
    public void testIsConsistent() {
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isConsistent();
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInconsistent() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isConsistent();
        
        assertFalse(result);
    }
    
    @Test
    public void testIsMapped() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isMapped("name");
        
        assertTrue(result);
    }
    
    @Test
    public void testIsNotMapped() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isMapped("age");
        
        assertFalse(result);
    }
    
    @Test
    public void testIsSet() {
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isSet("name");
        
        assertTrue(result);
    }
    
    @Test
    public void testIsNotSet() {
        mapping.put("name", 3);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        boolean result = record.isSet("name");
        
        assertFalse(result);
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        int count = 0;
        for (String value : record) {
            count++;
        }
        
        assertEquals(3, count);
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        int size = record.size();
        
        assertEquals(3, size);
    }
    
    @Test
    public void testToMap() {
        mapping.put("name", 0);
        mapping.put("age", 1);
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        Map<String, String> result = record.toMap();
        
        assertEquals(2, result.size());
        assertEquals("value1", result.get("name"));
        assertEquals("value2", result.get("age"));
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String result = record.toString();
        
        assertEquals("[value1, value2, value3]", result);
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        
        String[] result = record.values();
        
        assertArrayEquals(values, result);
    }
    
    private enum TestEnum {
        TEST
    }
}