import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetWithMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Assert.assertTrue(record1.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        Assert.assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 0);
        Assert.assertFalse(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        Assert.assertTrue(record.isMapped("column1"));
        Assert.assertTrue(record.isMapped("column2"));
        Assert.assertTrue(record.isMapped("column3"));
        Assert.assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 0);
        Assert.assertTrue(record.isSet("column1"));
        Assert.assertTrue(record.isSet("column2"));
        Assert.assertTrue(record.isSet("column3"));
        Assert.assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        StringBuilder sb = new StringBuilder();
        for (String value : record) {
            sb.append(value);
        }
        Assert.assertEquals("value1value2value3", sb.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 0);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
}