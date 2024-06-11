import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    private String[] values = {"value1", "value2", "value3"};
    private Map<String, Integer> mapping = new HashMap<>();
    private String comment = "Sample comment";
    private long recordNumber = 1;
    
    @Before
    public void setup() {
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("value1", csvRecord.get(Columns.COLUMN1));
        assertEquals("value2", csvRecord.get(Columns.COLUMN2));
        assertEquals("value3", csvRecord.get(Columns.COLUMN3));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        csvRecord.get("column4");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        Map<String, Integer> invalidMapping = new HashMap<>();
        invalidMapping.put("column1", 5);
        
        CSVRecord record = new CSVRecord(values, invalidMapping, comment, recordNumber);
        record.get("column1");
    }
    
    @Test
    public void testGetByName() {
        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }
    
    @Test
    public void testGetComment() {
        assertEquals("Sample comment", csvRecord.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
        
        mapping.put("column4", 3);
        assertFalse(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
    }
    
    @Test
    public void testIterator() {
        int i = 0;
        for (String value : csvRecord) {
            assertEquals(values[i++], value);
        }
    }
    
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(values, csvRecord.values());
    }
    
    private enum Columns {
        COLUMN1, COLUMN2, COLUMN3
    }   
}