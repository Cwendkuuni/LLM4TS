import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        String result = record.get(0);

        // Assert
        Assert.assertEquals("value1", result);
    }

    @Test
    public void testGetByName() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        String result = record.get("column1");

        // Assert
        Assert.assertEquals("value1", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isConsistent();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotConsistent() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isConsistent();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMapped() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isMapped("column1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotMapped() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isMapped("column2");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsSet() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isSet("column1");

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotSet() {
        // Arrange
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Act
        boolean result = record.isSet("column2");

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIterator() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        Iterator<String> iterator = record.iterator();
        List<String> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            resultList.add(iterator.next());
        }

        // Assert
        Assert.assertEquals(Arrays.asList("value1", "value2"), resultList);
    }

    @Test
    public void testValues() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        String[] result = record.values();

        // Assert
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testGetComment() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 0);

        // Act
        String result = record.getComment();

        // Assert
        Assert.assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 123);

        // Act
        long result = record.getRecordNumber();

        // Assert
        Assert.assertEquals(123, result);
    }

    @Test
    public void testSize() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        int result = record.size();

        // Assert
        Assert.assertEquals(2, result);
    }

    @Test
    public void testToString() {
        // Arrange
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        // Act
        String result = record.toString();

        // Assert
        Assert.assertEquals("[value1, value2]", result);
    }
}