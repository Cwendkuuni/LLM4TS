import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord record;
    
    @Before
    public void setUp() {
        String[] values = {"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        record = new CSVRecord(values, mapping, "Comment", 1);
    }
    
    @Test
    public void testGetByEnum() {
        assertEquals("Value1", record.get(TestEnum.VALUE1));
        assertEquals("Value2", record.get(TestEnum.VALUE2));
        assertEquals("Value3", record.get(TestEnum.VALUE3));
    }
    
    @Test
    public void testGetByIndex() {
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertEquals("Value3", record.get(2));
    }
    
    @Test
    public void testGetByName() {
        assertEquals("Value1", record.get("Column1"));
        assertEquals("Value2", record.get("Column2"));
        assertEquals("Value3", record.get("Column3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        record = new CSVRecord(new String[0], null, null, 1);
        record.get("Column1");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameMappingNotFound() {
        record.get("Column4");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIndexOutOfBounds() {
        record.get("Column3");
    }
    
    @Test
    public void testGetComment() {
        assertEquals("Comment", record.getComment());
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
    public void testIsConsistentNoMapping() {
        record = new CSVRecord(new String[0], null, null, 1);
        assertTrue(record.isConsistent());
    }
    
    @Test
    public void testIsConsistentMappingSizeMismatch() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        assertFalse(record.isConsistent());
    }
    
    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("Column1"));
        assertTrue(record.isMapped("Column2"));
        assertTrue(record.isMapped("Column3"));
    }
    
    @Test
    public void testIsMappedNotMapped() {
        assertFalse(record.isMapped("Column4"));
    }
    
    @Test
    public void testIsSet() {
        assertTrue(record.isSet("Column1"));
        assertTrue(record.isSet("Column2"));
        assertTrue(record.isSet("Column3"));
    }
    
    @Test
    public void testIsSetNotSet() {
        assertFalse(record.isSet("Column4"));
    }
    
    @Test
    public void testIterator() {
        String[] expected = {"Value1", "Value2", "Value3"};
        int i = 0;
        for (String value : record) {
            assertEquals(expected[i], value);
            i++;
        }
    }
    
    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("Column1", "Value1");
        map.put("Column2", "Value2");
        map.put("Column3", "Value3");
        assertEquals(map, record.putIn(new HashMap<String, String>()));
    }
    
    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }
    
    @Test
    public void testToList() {
        List<String> expected = Arrays.asList("Value1", "Value2", "Value3");
        assertEquals(expected, record.toList());
    }
    
    @Test
    public void testToMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("Column1", "Value1");
        expected.put("Column2", "Value2");
        expected.put("Column3", "Value3");
        assertEquals(expected, record.toMap());
    }
    
    @Test
    public void testToString() {
        String expected = "[Value1, Value2, Value3]";
        assertEquals(expected, record.toString());
    }
    
    @Test
    public void testValues() {
        String[] expected = {"Value1", "Value2", "Value3"};
        assertArrayEquals(expected, record.values());
    }
    
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }

}