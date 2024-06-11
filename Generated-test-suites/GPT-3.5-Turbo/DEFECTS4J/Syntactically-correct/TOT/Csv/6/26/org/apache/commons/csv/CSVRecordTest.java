import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;
    
    @Before
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<String, Integer>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        comment = "This is a comment";
        recordNumber = 1;
        
        record = new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
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
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);
        csvRecord.get("column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        record.get("invalidColumn");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        record.get("column1");
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
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMappingNull() {
        CSVRecord csvRecord = new CSVRecord(values, null, comment, recordNumber);
        assertTrue(csvRecord.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
    }
    
    @Test
    public void testIsMappedWithInvalidName() {
        assertFalse(record.isMapped("invalidColumn"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
    }
    
    @Test
    public void testIsSetWithInvalidName() {
        assertFalse(record.isSet("invalidColumn"));
    }
    
    @Test
    public void testIterator() {
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
    public void testSize() {
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }
    
    @Test
    public void testValues() {
        assertArrayEquals(values, record.values());
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}