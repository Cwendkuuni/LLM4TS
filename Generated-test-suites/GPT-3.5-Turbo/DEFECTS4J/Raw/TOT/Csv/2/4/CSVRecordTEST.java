import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    
    private CSVRecord record;
    
    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
        assertNull(record.get("column4"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        record = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        record.get("column1");
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithEmptyValues() {
        String[] emptyValues = {};
        record = new CSVRecord(emptyValues, record.getMapping(), record.getComment(), record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithNullValues() {
        record = new CSVRecord(null, record.getMapping(), record.getComment(), record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithoutMapping() {
        record = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithInconsistentSize() {
        Map<String, Integer> inconsistentMapping = new HashMap<>();
        inconsistentMapping.put("column1", 0);
        inconsistentMapping.put("column2", 1);
        record = new CSVRecord(record.values(), inconsistentMapping, record.getComment(), record.getRecordNumber());
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
        assertFalse(record.isMapped("column4"));
    }
    
    @Test
    public void testIsMappedWithoutMapping() {
        record = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        assertFalse(record.isMapped("column1"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
        assertFalse(record.isSet("column4"));
    }
    
    @Test
    public void testIsSetWithoutMapping() {
        record = new CSVRecord(record.values(), null, record.getComment(), record.getRecordNumber());
        assertFalse(record.isSet("column1"));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(record.size(), count);
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    @Test
    public void testGetComment() {
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }
}