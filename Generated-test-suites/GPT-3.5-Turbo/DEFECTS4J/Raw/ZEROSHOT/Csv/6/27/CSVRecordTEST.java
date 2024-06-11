import org.apache.commons.csv.CSVFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get(TestEnum.TEST1));
        Assert.assertEquals("value2", record.get(TestEnum.TEST2));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get("test1"));
        Assert.assertEquals("value2", record.get("test2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithUnknownName() {
        CSVRecord record = createCSVRecord();
        record.get("unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, createMapping(), null, 0);
        record.get("test2");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 100);
        Assert.assertEquals(100, record.getRecordNumber());
    }

    @Test
    public void testIsConsistentTrue() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentFalse() {
        CSVRecord record = new CSVRecord(new String[]{"value1"}, createMapping(), null, 0);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMappedTrue() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isMapped("test1"));
        Assert.assertTrue(record.isMapped("test2"));
    }

    @Test
    public void testIsMappedFalse() {
        CSVRecord record = createCSVRecord();
        Assert.assertFalse(record.isMapped("test"));
    }

    @Test
    public void testIsSetTrue() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isSet("test1"));
        Assert.assertTrue(record.isSet("test2"));
    }

    @Test
    public void testIsSetFalse() {
        CSVRecord record = createCSVRecord();
        Assert.assertFalse(record.isSet("test"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals(Arrays.asList("value1", "value2"), record.toList());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        Assert.assertEquals("value1", map.get("test1"));
        Assert.assertEquals("value2", map.get("test2"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals(2, record.size());
    }

    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.toMap();
        Assert.assertEquals("value1", map.get("test1"));
        Assert.assertEquals("value2", map.get("test2"));
    }

    @Test
    public void testToString() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("[value1, value2]", record.toString());
    }

    private CSVRecord createCSVRecord() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = createMapping();
        return new CSVRecord(values, mapping, null, 0);
    }

    private Map<String, Integer> createMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("test1", 0);
        mapping.put("test2", 1);
        return mapping;
    }

    private enum TestEnum {
        TEST1, TEST2
    }
}