import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetUsingEnum() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("A", record.get(TestEnum.TEST1));
    }
    
    @Test
    public void testGetUsingIndex() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("C", record.get(2));
    }
    
    @Test
    public void testGetUsingName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("B", record.get("1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("name");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        record.get("D");
    }
    
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, "This is a comment", 1);
        assertEquals("This is a comment", record.getComment());
    }
    
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }
    
    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMappingSizeMismatch() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(2), null, 1);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentWithMappingSizeMatch() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(3), null, 1);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsMappedWithMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(3), null, 1);
        assertTrue(record.isMapped("A"));
    }
    
    @Test
    public void testIsMappedWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record.isMapped("A"));
    }
    
    @Test
    public void testIsSetWithMappingAndValue() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(3), null, 1);
        assertTrue(record.isSet("A"));
    }
    
    @Test
    public void testIsSetWithMappingAndNullValue() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", null}, createMapping(3), null, 1);
        assertTrue(record.isSet("A"));
    }
    
    @Test
    public void testIsSetWithMappingAndNoValue() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", null}, createMapping(3), null, 1);
        assertFalse(record.isSet("B"));
    }
    
    @Test
    public void testIsSetWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertFalse(record.isSet("A"));
    }
    
    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        String[] expected = {"A", "B", "C"};
        int i = 0;
        for (String value : record) {
            assertEquals(expected[i], value);
            i++;
        }
    }
    
    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(3), null, 1);
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertTrue(map.containsKey("A"));
        assertEquals("A", map.get("A"));
    }
    
    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        List<String> list = record.toList();
        assertTrue(list.contains("A"));
        assertTrue(list.contains("B"));
        assertTrue(list.contains("C"));
    }
    
    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, createMapping(3), null, 1);
        Map<String, String> map = record.toMap();
        assertTrue(map.containsKey("A"));
        assertEquals("A", map.get("A"));
    }
    
    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        assertEquals("[A, B, C]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"A", "B", "C"}, null, null, 1);
        String[] expected = {"A", "B", "C"};
        assertArrayEquals(expected, record.values());
    }
    
    private Map<String, Integer> createMapping(int size) {
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < size; i++) {
            mapping.put(String.valueOf(i), i);
        }
        return mapping;
    }
}