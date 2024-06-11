import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
        Enum<?> e = null;
        String result = csvRecord.get(e);
        Assert.assertNull(result);
    }

    @Test
    public void testGetByIndex() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String result = csvRecord.get(1);
        Assert.assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 0);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value"}, mapping, null, 0);
        csvRecord.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 0);
        csvRecord.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord csvRecord = new CSVRecord(null, null, "comment", 0);
        String result = csvRecord.getComment();
        Assert.assertEquals("comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord csvRecord = new CSVRecord(null, null, null, 5);
        long result = csvRecord.getRecordNumber();
        Assert.assertEquals(5, result);
    }

    @Test
    public void testIsConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        boolean result = csvRecord.isConsistent();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value"}, mapping, null, 0);
        boolean result = csvRecord.isMapped("name");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        CSVRecord csvRecord = new CSVRecord(new String[]{"value"}, mapping, null, 0);
        boolean result = csvRecord.isSet("name");
        Assert.assertTrue(result);
    }

    @Test
    public void testIterator() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String[] expected = {"value1", "value2"};
        int i = 0;
        for (String value : csvRecord) {
            Assert.assertEquals(expected[i++], value);
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "value");
        Map<String, String> result = new CSVRecord(new String[]{"value"}, null, null, 0).putIn(new HashMap<String, String>());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSize() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        int result = csvRecord.size();
        Assert.assertEquals(2, result);
    }

    @Test
    public void testToList() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String[] expected = {"value1", "value2"};
        List<String> result = csvRecord.toList();
        Assert.assertEquals(Arrays.asList(expected), result);
    }

    @Test
    public void testToMap() {
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "value");
        Map<String, String> result = new CSVRecord(new String[]{"value"}, null, null, 0).toMap();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String result = csvRecord.toString();
        Assert.assertEquals("[value1, value2]", result);
    }

    @Test
    public void testValues() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, null, 0);
        String[] expected = {"value1", "value2"};
        String[] result = csvRecord.values();
        Assert.assertEquals(expected, result);
    }

}