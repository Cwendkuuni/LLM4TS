
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        String value = record.get(MyEnum.VALUE1);
        
        // Assert
        assertEquals("value1", value);
    }
    
    @Test
    public void testGetInt() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        String value = record.get(2);
        
        // Assert
        assertEquals("value3", value);
    }
    
    @Test
    public void testGetString() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "vvalue3"}, mapping, null, 1);
        
        // Act
        String value = record.get("column2");
        
        // Assert
        assertEquals("value2", value);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetString_NoMapping() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        String value = record.get("column2");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetString_MappingNotFound() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        String value = record.get("column4");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetString_IndexOutOfBounds() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        String value = record.get("column3");
    }
    
    @Test
    public void testGetComment() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        
        // Act
        String comment = record.getComment();
        
        // Assert
        assertEquals("This is a comment", comment);
    }
    
    @Test
    public void testGetRecordNumber() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 5);
        
        // Act
        long recordNumber = record.getRecordNumber();
        
        // Assert
        assertEquals(5, recordNumber);
    }
    
    @Test
    public void testIsConsistent_True() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        boolean isConsistent = record.isConsistent();
        
        // Assert
        assertTrue(isConsistent);
    }
    
    @Test
    public void testIsConsistent_False() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        
        // Act
        boolean isConsistent = record.isConsistent();
        
        // Assert
        assertFalse(isConsistent);
    }
    
    @Test
    public void testIsMapped_True() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        boolean isMapped = record.isMapped("column2");
        
        // Assert
        assertTrue(isMapped);
    }
    
    @Test
    public void testIsMapped_False() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        boolean isMapped = record.isMapped("column2");
        
        // Assert
        assertFalse(isMapped);
    }
    
    @Test
    public void testIsSet_True() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        boolean isSet = record.isSet("column2");
        
        // Assert
        assertTrue(isSet);
    }
    
    @Test
    public void testIsSet_False() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        boolean isSet = record.isSet("column2");
        
        // Assert
        assertFalse(isSet);
    }
    
    @Test
    public void testIterator() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        Iterator<String> iterator = record.iterator();
        List<String> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        
        // Assert
        assertEquals(Arrays.asList("value1", "value2", "value3"), values);
    }
    
    @Test
    public void testPutIn() {
        // Arrange
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        Map<String, String> result = record.putIn(map);
        
        // Assert
        assertEquals(map, result);
    }
    
    @Test
    public void testSize() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        int size = record.size();
        
        // Assert
        assertEquals(3, size);
    }
    
    @Test
    public void testToMap() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        
        // Act
        Map<String, String> result = record.toMap();
        
        // Assert
        Map<String, String> expected = new HashMap<>();
        expected.put("column1", "value1");
        expected.put("column2", "value2");
        expected.put("column3", "value3");
        assertEquals(expected, result);
    }
    
    @Test
    public void testToString() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        String result = record.toString();
        
        // Assert
        assertEquals("[value1, value2, value3]", result);
    }
    
    @Test
    public void testValues() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        
        // Act
        String[] values = record.values();
        
        // Assert
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
    }
    
    private enum MyEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
    
}
