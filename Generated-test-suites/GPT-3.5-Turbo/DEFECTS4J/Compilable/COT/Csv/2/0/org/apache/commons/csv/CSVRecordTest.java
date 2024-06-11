package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("value1", record.get("column1"));
        Assert.assertEquals("value2", record.get("column2"));
        Assert.assertEquals("value3", record.get("column3"));
        Assert.assertNull(record.get("column4"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithNullMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithInconsistentMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(record.isMapped("column1"));
        Assert.assertTrue(record.isMapped("column2"));
        Assert.assertTrue(record.isMapped("column3"));
        Assert.assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertTrue(record.isSet("column1"));
        Assert.assertTrue(record.isSet("column2"));
        Assert.assertTrue(record.isSet("column3"));
        Assert.assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        int count = 0;
        for (String value : record) {
            Assert.assertEquals(values[count], value);
            count++;
        }
        Assert.assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = "This is a comment";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = null;
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }

}