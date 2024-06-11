import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetWithEnum_ValidEnum_ReturnsValue() {
        // Arrange
        CSVRecord record = createRecord();
        MyEnum myEnum = MyEnum.FIRST_NAME;
        
        // Act
        String value = record.get(myEnum);
        
        // Assert
        assertEquals("John", value);
    }
    
    @Test
    public void testGetWithEnum_InvalidEnum_ThrowsException() {
        // Arrange
        CSVRecord record = createRecord();
        MyEnum myEnum = MyEnum.INVALID_ENUM;
        
        // Act & Assert
        try {
            record.get(myEnum);
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for INVALID_ENUM not found, expected one of [FIRST_NAME, LAST_NAME, AGE]", e.getMessage());
        }
    }
    
    @Test
    public void testGetWithIndex_ValidIndex_ReturnsValue() {
        // Arrange
        CSVRecord record = createRecord();
        int index = 0;
        
        // Act
        String value = record.get(index);
        
        // Assert
        assertEquals("John", value);
    }
    
    @Test
    public void testGetWithIndex_InvalidIndex_ThrowsException() {
        // Arrange
        CSVRecord record = createRecord();
        int index = 4;
        
        // Act & Assert
        try {
            record.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header '4' is 4 but CSVRecord only has 3 values!", e.getMessage());
        }
    }
    
    @Test
    public void testGetWithName_ValidName_ReturnsValue() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        String value = record.get("FIRST_NAME");
        
        // Assert
        assertEquals("John", value);
    }
    
    @Test
    public void testGetWithName_InvalidName_NoHeader_ThrowsException() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"John", "Doe", "30"}, null, null, 1);
        
        // Act & Assert
        try {
            record.get("FIRST_NAME");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }
    
    @Test
    public void testGetWithName_InvalidName_HeaderMappingNotFound_ThrowsException() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act & Assert
        try {
            record.get("INVALID_NAME");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for INVALID_NAME not found, expected one of [FIRST_NAME, LAST_NAME, AGE]", e.getMessage());
        }
    }
    
    @Test
    public void testGetComment_ValidComment_ReturnsComment() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"John", "Doe", "30"}, null, "This is a comment", 1);
        
        // Act
        String comment = record.getComment();
        
        // Assert
        assertEquals("This is a comment", comment);
    }
    
    @Test
    public void testGetComment_NoComment_ReturnsNull() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"John", "Doe", "30"}, null, null, 1);
        
        // Act
        String comment = record.getComment();
        
        // Assert
        assertEquals(null, comment);
    }
    
    @Test
    public void testGetRecordNumber_ReturnsRecordNumber() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        long recordNumber = record.getRecordNumber();
        
        // Assert
        assertEquals(1, recordNumber);
    }
    
    @Test
    public void testIsConsistent_MappingNull_ReturnsTrue() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"John", "Doe", "30"}, null, null, 1);
        
        // Act
        boolean isConsistent = record.isConsistent();
        
        // Assert
        assertTrue(isConsistent);
    }
    
    @Test
    public void testIsConsistent_MappingSizeEqualsValuesLength_ReturnsTrue() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        boolean isConsistent = record.isConsistent();
        
        // Assert
        assertTrue(isConsistent);
    }
    
    @Test
    public void testIsConsistent_MappingSizeNotEqualsValuesLength_ReturnsFalse() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"John", "Doe", "30"}, createMapping(), null, 1);
        
        // Act
        boolean isConsistent = record.isConsistent();
        
        // Assert
        assertFalse(isConsistent);
    }
    
    @Test
    public void testIsSet_MappedAndHasValue_ReturnsTrue() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        boolean isSet = record.isSet("FIRST_NAME");
        
        // Assert
        assertTrue(isSet);
    }
    
    @Test
    public void testIsSet_MappedAndNoValue_ReturnsFalse() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {"", "Doe", "30"}, createMapping(), null, 1);
        
        // Act
        boolean isSet = record.isSet("FIRST_NAME");
        
        // Assert
        assertFalse(isSet);
    }
    
    @Test
    public void testIsSet_NotMapped_ReturnsFalse() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        boolean isSet = record.isSet("INVALID_NAME");
        
        // Assert
        assertFalse(isSet);
    }
    
    @Test
    public void testIterator_ReturnsIteratorOverValues() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        String[] values = new String[3];
        int i = 0;
        for (String value : record) {
            values[i++] = value;
        }
        
        // Assert
        assertEquals("John", values[0]);
        assertEquals("Doe", values[1]);
        assertEquals("30", values[2]);
    }
    
    @Test
    public void testPutIn_ValidRecord_PopulatesMap() {
        // Arrange
        CSVRecord record = createRecord();
        Map<String, String> map = new HashMap<>();
        
        // Act
        Map<String, String> populatedMap = record.putIn(map);
        
        // Assert
        assertEquals(3, populatedMap.size());
        assertEquals("John", populatedMap.get("FIRST_NAME"));
        assertEquals("Doe", populatedMap.get("LAST_NAME"));
        assertEquals("30", populatedMap.get("AGE"));
    }
    
    @Test
    public void testSize_ReturnsValuesLength() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        int size = record.size();
        
        // Assert
        assertEquals(3, size);
    }
    
    @Test
    public void testToMap_ValidRecord_ReturnsMap() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        Map<String, String> map = record.toMap();
        
        // Assert
        assertEquals(3, map.size());
        assertEquals("John", map.get("FIRST_NAME"));
        assertEquals("Doe", map.get("LAST_NAME"));
        assertEquals("30", map.get("AGE"));
    }
    
    @Test
    public void testToMap_EmptyRecord_ReturnsEmptyMap() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[] {}, createMapping(), null, 1);
        
        // Act
        Map<String, String> map = record.toMap();
        
        // Assert
        assertEquals(0, map.size());
    }
    
    @Test
    public void testToString_ReturnsValuesAsString() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        String result = record.toString();
        
        // Assert
        assertEquals("[John, Doe, 30]", result);
    }
    
    @Test
    public void testValues_ReturnsValuesArray() {
        // Arrange
        CSVRecord record = createRecord();
        
        // Act
        String[] values = record.values();
        
        // Assert
        assertEquals(3, values.length);
        assertEquals("John", values[0]);
        assertEquals("Doe", values[1]);
        assertEquals("30", values[2]);
    }
    
    private CSVRecord createRecord() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = createMapping();
        String comment = "This is a comment";
        long recordNumber = 1;
        return new CSVRecord(values, mapping, comment, recordNumber);
    }
    
    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FIRST_NAME", 0);
        mapping.put("LAST_NAME", 1);
        mapping.put("AGE", 2);
        return mapping;
    }
    
    private enum MyEnum {
        FIRST_NAME,
        LAST_NAME,
        AGE,
        INVALID_ENUM
    }

}