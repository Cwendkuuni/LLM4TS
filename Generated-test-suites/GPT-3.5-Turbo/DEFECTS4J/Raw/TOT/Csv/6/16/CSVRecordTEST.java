import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    // Test case for get(Enum e) method
    @Test
    public void testGetWithEnum() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, getMapping(), "This is a comment", 1);
        Assert.assertEquals("value2", csvRecord.get(TestEnum.VALUE2));
        Assert.assertEquals("value1", csvRecord.get(TestEnum.VALUE1));
        Assert.assertEquals("value3", csvRecord.get(TestEnum.VALUE3));
    }

    // Test case for get(int i) method
    @Test
    public void testGetWithIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, getMapping(), "This is a comment", 1);
        Assert.assertEquals("value1", csvRecord.get(0));
        Assert.assertEquals("value2", csvRecord.get(1));
        Assert.assertEquals("value3", csvRecord.get(2));
    }

    // Test case for get(String name) method - Mapping exists
    @Test
    public void testGetWithNameWithMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals("value2", csvRecord.get("Column2"));
        Assert.assertEquals("value1", csvRecord.get("Column1"));
        Assert.assertEquals("value3", csvRecord.get("Column3"));
    }
    
    // Test case for get(String name) method - Mapping does not exist
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameWithoutMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
        csvRecord.get("Column4");
    }

    // Test case for getComment() method
    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals("This is a comment", csvRecord.getComment());
    }

    // Test case for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals(1, csvRecord.getRecordNumber());
    }

    // Test case for isConsistent() method - Mapping exists and record is consistent
    @Test
    public void testIsConsistentWithMappingsAndConsistentRecord() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertTrue(csvRecord.isConsistent());
    }

    // Test case for isConsistent() method - Mapping exists but record is inconsistent
    @Test
    public void testIsConsistentWithMappingsAndInconsistentRecord() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                getMapping(), "This is a comment", 1);
        Assert.assertFalse(csvRecord.isConsistent());
    }

    // Test case for isConsistent() method - Mapping does not exist
    @Test
    public void testIsConsistentWithoutMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"},
                null, "This is a comment", 1);
        Assert.assertTrue(csvRecord.isConsistent());
    }

    // Test case for isMapped() method - Column is mapped
    @Test
    public void testIsMappedWithMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertTrue(csvRecord.isMapped("Column1"));
        Assert.assertTrue(csvRecord.isMapped("Column2"));
        Assert.assertTrue(csvRecord.isMapped("Column3"));
    }

    // Test case for isMapped() method - Column is not mapped
    @Test
    public void testIsMappedWithoutMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "This is a comment", 1);
        Assert.assertFalse(csvRecord.isMapped("Column4"));
    }

    // Test case for isSet() method - Column is mapped and has a value
    @Test
    public void testIsSetWithMappingsAndValue() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertTrue(csvRecord.isSet("Column1"));
        Assert.assertTrue(csvRecord.isSet("Column2"));
        Assert.assertTrue(csvRecord.isSet("Column3"));
    }

    // Test case for isSet() method - Column is mapped but does not have a value
    @Test
    public void testIsSetWithMappingsButNoValue() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertFalse(csvRecord.isSet("Column2"));
    }

    // Test case for isSet() method - Column is not mapped
    @Test
    public void testIsSetWithoutMappings() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                null, "This is a comment", 1);
        Assert.assertFalse(csvRecord.isSet("Column4"));
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        Assert.assertEquals(3, count);
    }

    // Test case for putIn() method
    @Test
    public void testPutIn() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Map<String, String> map = csvRecord.putIn(new HashMap<>());
        Assert.assertEquals("value1", map.get("Column1"));
        Assert.assertEquals("value2", map.get("Column2"));
        Assert.assertEquals("value3", map.get("Column3"));
    }

    // Test case for size() method
    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals(3, csvRecord.size());
    }

    // Test case for toList() method
    @Test
    public void testToList() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals(3, csvRecord.toList().size());
    }

    // Test case for toMap() method
    @Test
    public void testToMap() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Map<String, String> map = csvRecord.toMap();
        Assert.assertEquals("value1", map.get("Column1"));
        Assert.assertEquals("value2", map.get("Column2"));
        Assert.assertEquals("value3", map.get("Column3"));
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2", "value3"},
                getMapping(), "This is a comment", 1);
        Assert.assertEquals("[value1, value2, value3]", csvRecord.toString());
    }

    // Helper method to provide column mappings
    private Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Column1", 0);
        mapping.put("Column2", 1);
        mapping.put("Column3", 2);
        return mapping;
    }

    // Helper enum for get(Enum e) method test
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

}