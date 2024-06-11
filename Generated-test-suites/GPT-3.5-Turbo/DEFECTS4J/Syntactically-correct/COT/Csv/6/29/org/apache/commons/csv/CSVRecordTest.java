import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    // Test get(Enum<?> e) method
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get(Enum.valueOf(CSVRecord.TestEnum.class, "VALUE1")));
        assertEquals("value2", record.get(Enum.valueOf(CSVRecord.TestEnum.class, "VALUE2")));
        assertEquals("value3", record.get(Enum.valueOf(CSVRecord.TestEnum.class, "VALUE3")));
    }
    
    // Test get(int i) method
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }
    
    // Test get(String name) method
    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }
    
    // Test getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }
    
    // Test getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    // Test isConsistent() method
    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertTrue(record1.isConsistent());
        
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, "", 1);
        assertFalse(record2.isConsistent());
    }
    
    // Test isMapped(String name) method
    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
    }
    
    // Test isSet(String name) method
    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertFalse(record.isSet("name1"));
        assertFalse(record.isSet("name2"));
        assertFalse(record.isSet("name3"));
    }
    
    // Test iterator() method
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String[] expected = {"value1", "value2", "value3"};
        int index = 0;
        for (String value : record) {
            assertEquals(expected[index++], value);
        }
    }
    
    // Test putIn(M map) method
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    // Test size() method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals(3, record.size());
    }
    
    // Test toMap() method
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
    
    // Test values() method
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
        String[] expected = {"value1", "value2", "value3"};
        assertArrayEquals(expected, record.values());
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
    
}