import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test get(Enum<?> e) method
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertEquals("A", record.get(TestEnum.A));
        assertEquals("B", record.get(TestEnum.B));
        assertEquals("C", record.get(TestEnum.C));
    }
    
    // Test get(int i) method
    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertEquals("A", record.get(0));
        assertEquals("B", record.get(1));
        assertEquals("C", record.get(2));
    }
    
    // Test get(String name) method - valid case
    @Test
    public void testGetByName_Valid() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertEquals("A", record.get("nameA"));
        assertEquals("B", record.get("nameB"));
        assertEquals("C", record.get("nameC"));
    }
    
    // Test get(String name) method - invalid case
    @Test(expected = IllegalStateException.class)
    public void testGetByName_Invalid() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        record.get("nameA");
    }
    
    // Test get(String name) method - inconsistent case
    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_Inconsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        record.get("nameD");
    }
    
    // Test getComment() method
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }
    
    // Test getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }
    
    // Test isConsistent() method - consistent case
    @Test
    public void testIsConsistent_Consistent() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertTrue(record.isConsistent());
    }
    
    // Test isConsistent() method - inconsistent case
    @Test
    public void testIsConsistent_Inconsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A","B"}, getMapping(), null, 1);
        assertFalse(record.isConsistent());
    }
    
    // Test isMapped(String name) method - mapped case
    @Test
    public void testIsMapped_Mapped() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertTrue(record.isMapped("nameA"));
        assertTrue(record.isMapped("nameB"));
        assertTrue(record.isMapped("nameC"));
    }
    
    // Test isMapped(String name) method - not mapped case
    @Test
    public void testIsMapped_NotMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertFalse(record.isMapped("nameD"));
    }
    
    // Test isSet(String name) method - mapped and has value case
    @Test
    public void testIsSet_MappedHasValue() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertTrue(record.isSet("nameA"));
        assertTrue(record.isSet("nameB"));
        assertTrue(record.isSet("nameC"));
    }
    
    // Test isSet(String name) method - mapped but does not have value case
    @Test
    public void testIsSet_MappedNoValue() {
        CSVRecord record = new CSVRecord(new String[]{"A","B"}, getMapping(), null, 1);
        assertFalse(record.isSet("nameB"));
    }
    
    // Test isSet(String name) method - not mapped case
    @Test
    public void testIsSet_NotMapped() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        assertFalse(record.isSet("nameD"));
    }
    
    // Test iterator() method
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
    }
    
    // Test putIn(Map<String, String> map) method
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, getMapping(), null, 1);
        Map<String, String> map = record.toMap();
        assertEquals("A", map.get("nameA"));
        assertEquals("B", map.get("nameB"));
        assertEquals("C", map.get("nameC"));
    }
    
    // Test size() method
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertEquals("[A, B, C]", record.toString());
    }
    
    // Test values() method
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"A","B","C"}, null, null, 1);
        assertArrayEquals(new String[]{"A","B","C"}, record.values());
    }
    
    
    // Helper method to generate mapping
    private static Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("nameA", 0);
        mapping.put("nameB", 1);
        mapping.put("nameC", 2);
        return mapping;
    }
    
    // Helper enum for testing get(Enum<?> e) method
    private enum TestEnum {
        A, B, C;
    }
}