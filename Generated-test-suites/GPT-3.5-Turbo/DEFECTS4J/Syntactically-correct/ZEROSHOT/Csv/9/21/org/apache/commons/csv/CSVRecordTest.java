import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
    
    private CSVRecord record;
    private CSVRecord emptyRecord;
    
    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        String[] emptyValues = {};
        record = new CSVRecord(values, null, null, 1);
        emptyRecord = new CSVRecord(emptyValues, null, null, 1);
    }
    
    @Test
    public void testGetByEnum() {
        String value = record.get(MyEnum.VALUE1);
        assertEquals("value1", value);
    }
    
    @Test
    public void testGetByIndex() {
        String value = record.get(0);
        assertEquals("value1", value);
    }
    
    @Test
    public void testGetByName() {
        String value = record.get("name1");
        assertEquals("value1", value);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        emptyRecord.get("name1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        record.get("name3");
    }
    
    @Test
    public void testGetComment() {
        String comment = record.getComment();
        assertNull(comment);
    }
    
    @Test
    public void testGetRecordNumber() {
        long recordNumber = record.getRecordNumber();
        assertEquals(1, recordNumber);
    }
    
    @Test
    public void testIsConsistent() {
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
        
        boolean emptyIsConsistent = emptyRecord.isConsistent();
        assertTrue(emptyIsConsistent);
    }
    
    @Test
    public void testIsMapped() {
        boolean isMapped = record.isMapped("name1");
        assertFalse(isMapped);
        
        boolean emptyIsMapped = emptyRecord.isMapped("name1");
        assertFalse(emptyIsMapped);
    }
    
    @Test
    public void testIsSet() {
        boolean isSet = record.isSet("name1");
        assertFalse(isSet);
        
        boolean emptyIsSet = emptyRecord.isSet("name1");
        assertFalse(emptyIsSet);
    }
    
    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = record.putIn(map);
        assertEquals(map, result);
        
        Map<String, String> emptyMap = new HashMap<>();
        Map<String, String> emptyResult = emptyRecord.putIn(emptyMap);
        assertEquals(emptyMap, emptyResult);
    }
    
    @Test
    public void testSize() {
        int size = record.size();
        assertEquals(3, size);
        
        int emptySize = emptyRecord.size();
        assertEquals(0, emptySize);
    }
    
    @Test
    public void testToString() {
        String result = record.toString();
        assertEquals("[value1, value2, value3]", result);
    }
    
    @Test
    public void testValues() {
        String[] values = record.values();
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
    }
    
    private enum MyEnum {
        VALUE1, VALUE2, VALUE3;
    }
}