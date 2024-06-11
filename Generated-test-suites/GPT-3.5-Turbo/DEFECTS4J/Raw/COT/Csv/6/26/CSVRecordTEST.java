
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void getEnumValue_ValidEnum_ReturnsValue() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, null, 0);

        // Act
        String result = record.get(TestEnum.VALUE1);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void getIndexedValue_ValidIndex_ReturnsValue() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 0);

        // Act
        String result = record.get(0);

        // Assert
        Assert.assertEquals("Value1", result);
    }

    @Test(expected = IllegalStateException.class)
    public void getValueByName_NoMapping_ThrowsIllegalStateException() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, null, 0);

        // Act
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValueByName_MappingNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);

        // Act
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValueByName_IndexOutOfBounds_ThrowsIllegalArgumentException() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, mapping, null, 0);

        // Act
        record.get("name1");
    }

    @Test
    public void getComment_ValidComment_ReturnsComment() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 0);

        // Act
        String result = record.getComment();

        // Assert
        Assert.assertEquals("This is a comment", result);
    }

    @Test
    public void getRecordNumber_ValidNumber_ReturnsNumber() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, null, 10);

        // Act
        long result = record.getRecordNumber();

        // Assert
        Assert.assertEquals(10, result);
    }

    @Test
    public void isConsistent_MappingIsNull_ReturnsTrue() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, null, 0);

        // Act
        boolean result = record.isConsistent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isConsistent_MappingSizeEqualsValuesLength_ReturnsTrue() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, null, 0);

        // Act
        boolean result = record.isConsistent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isConsistent_MappingSizeNotEqualsValuesLength_ReturnsFalse() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, null, 0);

        // Act
        boolean result = record.isConsistent();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void isMapped_ColumnIsMapped_ReturnsTrue() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);

        // Act
        boolean result = record.isMapped("name1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isMapped_ColumnIsNotMapped_ReturnsFalse() {
        // Arrange
        CSVRecord record = new CSVRecord(null, null, null, 0);

        // Act
        boolean result = record.isMapped("name");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void isSet_ColumnIsMappedAndHasValue_ReturnsTrue() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(new String[]{"Value1"}, mapping, null, 0);

        // Act
        boolean result = record.isSet("name1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isSet_ColumnIsMappedButDoesNotHaveValue_ReturnsFalse() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(new String[0], mapping, null, 0);

        // Act
        boolean result = record.isSet("name1");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void iterator_ValidRecord_ReturnsIterator() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 0);

        // Act
        Iterator<String> iterator = record.iterator();
        String result = iterator.next();

        // Assert
        Assert.assertEquals("Value1", result);
    }

    @Test
    public void putIn_ValidMap_PopulatesMap() {
        // Arrange
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, null, 0);

        // Act
        Map<String, String> result = record.putIn(map);

        // Assert
        Assert.assertEquals("Value1", result.get("name1"));
        Assert.assertEquals("Value2", result.get("name2"));
    }

    @Test
    public void size_ValidRecord_ReturnsSize() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 0);

        // Act
        int result = record.size();

        // Assert
        Assert.assertEquals(2, result);
    }

    @Test
    public void toMap_ValidRecord_ReturnsMap() {
        // Arrange
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, mapping, null, 0);

        // Act
        Map<String, String> result = record.toMap();

        // Assert
        Assert.assertEquals("Value1", result.get("name1"));
        Assert.assertEquals("Value2", result.get("name2"));
    }

    @Test
    public void toString_ValidRecord_ReturnsString() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 0);

        // Act
        String result = record.toString();

        // Assert
        Assert.assertEquals("[Value1, Value2]", result);
    }

    @Test
    public void values_ValidRecord_ReturnsValues() {
        // Arrange
        CSVRecord record = new CSVRecord(new String[]{"Value1", "Value2"}, null, null, 0);

        // Act
        String[] result = record.values();

        // Assert
        Assert.assertArrayEquals(new String[]{"Value1", "Value2"}, result);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}
