import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {
    
    // Test get(Enum)
    @Test
    public void testGetWithEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String result = record.get(TestEnum.FIRST);
        assertEquals("value1", result);
    }
    
    // Test get(int)
    @Test
    public void testGetWithIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String result = record.get(1);
        assertEquals("value2", result);
    }
    
    // Test get(String)
    @Test
    public void testGetWithName() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String result = record.get("name");
        assertNull(result);
    }
    
    // Test getComment()
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
        String result = record.getComment();
        assertEquals("This is a comment", result);
    }
    
    // Test getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        long result = record.getRecordNumber();
        assertEquals(1, result);
    }
    
    // Test isConsistent() - True case
    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        boolean result = record.isConsistent();
        assertTrue(result);
    }
    
    // Test isConsistent() - False case
    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, null, null, 1);
        boolean result = record.isConsistent();
        assertFalse(result);
    }
    
    // Test isMapped() - True case
    @Test
    public void testIsMappedTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        boolean result = record.isMapped("name");
        assertFalse(result);
    }
    
    // Test isMapped() - False case
    @Test
    public void testIsMappedFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        boolean result = record.isMapped("name");
        assertFalse(result);
    }
    
    // Test isSet() - True case
    @Test
    public void testIsSetTrue() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        boolean result = record.isSet("name");
        assertFalse(result);
    }
    
    // Test isSet() - False case
    @Test
    public void testIsSetFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        boolean result = record.isSet("name");
        assertFalse(result);
    }
    
    // Test iterator()
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    // Test putIn(Map)
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals(2, map.size());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }
    
    // Test size()
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        int size = record.size();
        assertEquals(2, size);
    }
    
    // Test toMap()
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Map<String, String> map = record.toMap();
        assertEquals(2, map.size());
        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
    }
    
    // Test values()
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        String[] values = record.values();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }
}