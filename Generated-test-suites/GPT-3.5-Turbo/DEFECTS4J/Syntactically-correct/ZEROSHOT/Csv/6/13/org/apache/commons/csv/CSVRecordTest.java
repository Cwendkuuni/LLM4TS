import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get(TestEnum.VALUE1));
        Assert.assertEquals("value2", record.get(TestEnum.VALUE2));
        Assert.assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));
        Assert.assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoHeaderMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        record.get("name1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        CSVRecord record = createCSVRecord();
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidIndex() {
        CSVRecord record = createCSVRecord();
        record.get(3);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        String[] values = {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isMapped("name2"));
        Assert.assertTrue(record.isMapped("name3"));
    }

    @Test
    public void testIsNotMapped() {
        CSVRecord record = createCSVRecord();
        Assert.assertFalse(record.isMapped("invalid"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.isSet("name1"));
        Assert.assertTrue(record.isSet("name2"));
        Assert.assertTrue(record.isSet("name3"));
    }

    @Test
    public void testIsNotSet() {
        CSVRecord record = createCSVRecord();
        Assert.assertFalse(record.isSet("invalid"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = createCSVRecord();
        Assert.assertTrue(record.iterator().hasNext());
        Assert.assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals(3, record.toList().size());
        Assert.assertEquals("value1", record.toList().get(0));
        Assert.assertEquals("value2", record.toList().get(1));
        Assert.assertEquals("value3", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        CSVRecord record = createCSVRecord();
        Map<String, String> map = record.toMap();
        Assert.assertEquals(3, map.size());
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = createCSVRecord();
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }

    private CSVRecord createCSVRecord() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        return new CSVRecord(values, mapping, "This is a comment", 1);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }

}