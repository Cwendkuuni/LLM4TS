package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("column1");
    }

    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMappedWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isMapped("column1"));
        Assert.assertTrue(record.isMapped("column2"));
        Assert.assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsMappedWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertFalse(record.isMapped("column1"));
        Assert.assertFalse(record.isMapped("column2"));
        Assert.assertFalse(record.isMapped("column3"));
    }

    @Test
    public void testIsSetWithMappingAndValue() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertTrue(record.isSet("column1"));
        Assert.assertTrue(record.isSet("column2"));
        Assert.assertTrue(record.isSet("column3"));
    }

    @Test
    public void testIsSetWithMappingWithoutValue() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Assert.assertFalse(record.isSet("column1"));
        Assert.assertFalse(record.isSet("column2"));
        Assert.assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIsSetWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertFalse(record.isSet("column1"));
        Assert.assertFalse(record.isSet("column2"));
        Assert.assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        int count = 0;
        for (String value : record) {
            count++;
        }

        Assert.assertEquals(values.length, count);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String[] recordValues = record.values();

        Assert.assertEquals(values.length, recordValues.length);
        for (int i = 0; i < values.length; i++) {
            Assert.assertEquals(values[i], recordValues[i]);
        }
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "comment", 1);

        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 10);

        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals(values.length, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
}