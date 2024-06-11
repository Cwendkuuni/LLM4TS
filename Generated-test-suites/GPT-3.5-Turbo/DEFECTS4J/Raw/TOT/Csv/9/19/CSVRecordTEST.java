import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test get(Enum) method
        Assert.assertEquals("value1", record.get(TestEnum.VALUE1));
        Assert.assertEquals("value2", record.get(TestEnum.VALUE2));
        Assert.assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test get(int) method
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test get(String) method
        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));
        Assert.assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutMapping() {
        // Create a test CSVRecord without header mapping
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Try to access column by name
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidMapping() {
        // Create a test CSVRecord with invalid mapping
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Try to access column with invalid name
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        // Create a test CSVRecord with invalid index
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Try to access column with invalid index
        record.get("name1").length();
    }

    @Test
    public void testGetComment() {
        // Create a test CSVRecord with comment
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);

        // Test getComment method
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        // Create a test CSVRecord with record number
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 10);

        // Test getRecordNumber method
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        // Create a test CSVRecord without mapping
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record1 = new CSVRecord(values, null, null, 1);

        // Create a test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 1);

        // Test isConsistent method
        Assert.assertTrue(record1.isConsistent());
        Assert.assertTrue(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        // Create a test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        // Test isMapped method
        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isMapped("name2"));
        Assert.assertTrue(record.isMapped("name3"));
        Assert.assertFalse(record.isMapped("invalidName"));
    }

    @Test
    public void testIsSet() {
        // Create a test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);

        // Test isSet method
        Assert.assertTrue(record.isSet("name1"));
        Assert.assertTrue(record.isSet("name2"));
        Assert.assertFalse(record.isSet("name3"));
        Assert.assertFalse(record.isSet("invalidName"));
    }

    @Test
    public void testPutIn() {
        // Create a test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);

        // Test putIn method
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertNull(map.get("name3"));
        Assert.assertNull(map.get("invalidName"));
    }

    @Test
    public void testSize() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test size method
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test toList method
        List<String> list = record.toList();
        Assert.assertEquals("value1", list.get(0));
        Assert.assertEquals("value2", list.get(1));
        Assert.assertEquals("value3", list.get(2));
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testToMap() {
        // Create a test CSVRecord with mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);

        // Test toMap method
        Map<String, String> map = record.toMap();
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertNull(map.get("name3"));
        Assert.assertNull(map.get("invalidName"));
    }

    @Test
    public void testToString() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test toString method
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        // Create a test CSVRecord
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        // Test values method
        Assert.assertArrayEquals(values, record.values());
    }

    enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}