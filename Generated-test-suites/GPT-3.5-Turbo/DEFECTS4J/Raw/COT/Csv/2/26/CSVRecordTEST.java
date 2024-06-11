import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertNull(record.get("name1"));
        assertNull(record.get("name2"));
        assertNull(record.get("name3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoHeaderMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        record.get("name1");
    }
    
    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }
    
    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }
    
    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        StringBuilder sb = new StringBuilder();
        for (String value : record)
            sb.append(value);
        
        assertEquals("value1value2value3", sb.toString());
    }
    
    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(values, record.values());
    }
    
    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "Comment", 1);
        
        assertEquals("Comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(1, record.getRecordNumber());
    }
    
    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        
        assertEquals("[value1, value2, value3]", record.toString());
    }
}