import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        String result = record.get(Enum.TEST);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.get(1);
        Assert.assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithNoMappingFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key", 1);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        String result = record.getComment();
        Assert.assertEquals("comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 2);
        long result = record.getRecordNumber();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testIsConsistentWithNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        boolean result = record.isConsistent();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isConsistent();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistentWithMappingAndDifferentSize() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isConsistent();
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMappedWithNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        boolean result = record.isMapped("name");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMappedWithMappingAndNotFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        boolean result = record.isMapped("name");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsMappedWithMappingAndFound() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        boolean result = record.isMapped("name");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSetWithMappingAndValue() {
        String[] values = {"value"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isSet("name");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSetWithMappingAndNoValue() {
        String[] values = {};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        boolean result = record.isSet("name");
        Assert.assertFalse(result);
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertTrue(record.iterator().hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.putIn(new HashMap<>());
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals(null, map.get("name3"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        int result = record.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String result = record.toString();
        Assert.assertEquals("[value1, value2]", result);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        String[] result = record.values();
        Assert.assertArrayEquals(values, result);
    }

    private enum Enum {
        TEST
    }
}