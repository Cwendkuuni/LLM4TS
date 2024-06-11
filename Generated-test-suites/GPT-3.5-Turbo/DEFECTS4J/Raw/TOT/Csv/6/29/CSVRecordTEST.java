import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    
    private CSVRecord csvRecord;
    
    @Before
    public void setup() {
        String[] values = {"1", "John", "Doe"};
        String comment = "This is a comment";
        long recordNumber = 1;
        
        csvRecord = new CSVRecord(values, null, comment, recordNumber);
    }
    
    // Test for get(Enum<?> e)
    @Test
    public void testGetEnum() {
        assertEquals("1", csvRecord.get(TestEnum.ONE));
    }
    
    // Test for get(int i)
    @Test
    public void testGetByIndex() {
        assertEquals("John", csvRecord.get(1));
    }
    
    // Test for get(String name)
    @Test
    public void testGetByName() {
        assertEquals("Doe", csvRecord.get("lastName"));
    }
    
    // Test for get(String name) when no header mapping is provided
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"1", "John", "Doe"}, null, null, 1);
        recordWithoutMapping.get("firstName");
    }
    
    // Test for get(String name) when name is not mapped
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNonexistentName() {
        csvRecord.get("age");
    }
    
    // Test for get(String name) when the record is inconsistent
    @Test(expected = IllegalArgumentException.class)
    public void testGetByInconsistentName() {
        csvRecord.get("firstName");
    }
    
    // Test for getComment()
    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }
    
    // Test for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }
    
    // Test for isConsistent()
    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }
    
    // Test for isMapped(String name)
    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("firstName"));
    }
    
    // Test for isMapped(String name) when no mapping is provided
    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"1", "John", "Doe"}, null, null, 1);
        assertFalse(recordWithoutMapping.isMapped("firstName"));
    }
    
    // Test for isSet(String name)
    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("firstName"));
    }
    
    // Test for isSet(String name) when name is not mapped
    @Test
    public void testIsSetWithNonexistentName() {
        assertFalse(csvRecord.isSet("age"));
    }
    
    // Test for iterator()
    @Test
    public void testIterator() { 
        assertTrue(csvRecord.iterator().hasNext());
        assertEquals("1", csvRecord.iterator().next());
    }
    
    // Test for putIn(M map)
    @Test
    public void testPutIn() {
        HashMap<String, String> map = new HashMap<>();
        csvRecord.putIn(map);
        assertTrue(map.containsKey("firstName"));
        assertEquals("John", map.get("firstName"));
    }
    
    // Test for size()
    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }
    
    // Test for toList()
    @Test
    public void testToList() {
        assertEquals("1", csvRecord.toList().get(0));
    }
    
    // Test for toMap()
    @Test
    public void testToMap() {
        HashMap<String, String> map = csvRecord.toMap();
        assertTrue(map.containsKey("firstName"));
        assertEquals("John", map.get("firstName"));
    }
    
    // Test for toString()
    @Test
    public void testToString() {
        assertEquals("[1, John, Doe]", csvRecord.toString());
    }
    
    // Test for values()
    @Test
    public void testValues() {
        assertArrayEquals(new String[]{"1", "John", "Doe"}, csvRecord.values());
    }
    
    
    enum TestEnum {
        ONE, TWO, THREE
    }
}