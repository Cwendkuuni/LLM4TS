import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnumValue_Success() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        String result = csvRecord.get(TestEnum.VALUE1);

        // Assert
        Assert.assertEquals("value1", result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEnumValue_InvalidIndex() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);

        // Act
        csvRecord.get(TestEnum.VALUE2); // Accessing a non-existent index

        // Assert (Exception expected)
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByName_NoMappingSpecified() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);

        // Act
        csvRecord.get("column1");

        // Assert (Exception expected)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_MappingNotFound() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);

        // Act
        csvRecord.get("column1");

        // Assert (Exception expected)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByName_IndexOutOfBounds() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);

        // Act
        csvRecord.get("column0");

        // Assert (Exception expected)
    }

    @Test
    public void testGetComment_NoComment() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);

        // Act
        String result = csvRecord.getComment();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testGetRecordNumber() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 5);

        // Act
        long result = csvRecord.getRecordNumber();

        // Assert
        Assert.assertEquals(5, result);
    }

    @Test
    public void testIsConsistent_WithMapping() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);

        // Act
        boolean result = csvRecord.isConsistent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistent_WithoutMapping() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        boolean result = csvRecord.isConsistent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistent_WithIncorrectMappingSize() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, createMapping(), null, 1);

        // Act
        boolean result = csvRecord.isConsistent();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMapped_WithMapping() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);

        // Act
        boolean result = csvRecord.isMapped("column1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsMapped_WithoutMapping() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        boolean result = csvRecord.isMapped("column1");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSet_WithValue() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);

        // Act
        boolean result = csvRecord.isSet("column1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSet_WithoutValue() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);

        // Act
        boolean result = csvRecord.isSet("column2");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIterator_Success() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }

        // Assert
        Assert.assertEquals(2, count);
    }

    @Test
    public void testPutIn_Success() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
        Map<String, String> map = new HashMap<>();

        // Act
        Map<String, String> result = csvRecord.putIn(map);

        // Assert
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("value1", result.get("column1"));
        Assert.assertEquals("value2", result.get("column2"));
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        return mapping;
    }

    @Test
    public void testSize() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        int result = csvRecord.size();

        // Assert
        Assert.assertEquals(2, result);
    }

    @Test
    public void testToList() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        List<String> result = csvRecord.toList();

        // Assert
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("value1", result.get(0));
        Assert.assertEquals("value2", result.get(1));
    }

    @Test
    public void testToMap() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);

        // Act
        Map<String, String> result = csvRecord.toMap();

        // Assert
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("value1", result.get("column1"));
        Assert.assertEquals("value2", result.get("column2"));
    }

    @Test
    public void testToString() {
        // Arrange
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);

        // Act
        String result = csvRecord.toString();

        // Assert
        Assert.assertEquals("[\"value1\", \"value2\"]", result);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}