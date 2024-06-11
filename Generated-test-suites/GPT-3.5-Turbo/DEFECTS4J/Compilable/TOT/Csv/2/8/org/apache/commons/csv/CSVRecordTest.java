package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String result = record.get(0);

        Assert.assertEquals("value1", result);
    }

    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String result = record.get("column2");

        Assert.assertEquals("value2", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInconsistentRecord() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("column3");
    }

    @Test
    public void testIsConsistentWithMatchingSizes() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean result = record.isConsistent();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistentWithEmptyMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        boolean result = record.isConsistent();

        Assert.assertTrue(result);
    }

    @Test
    public void testIsConsistentWithMismatchingSizes() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean result = record.isConsistent();

        Assert.assertFalse(result);
    }

    @Test
    public void testIsMappedWithMappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean result = record.isMapped("column2");

        Assert.assertTrue(result);
    }

    @Test
    public void testIsMappedWithUnmappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        boolean result = record.isMapped("column2");

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSetWithMappedAndSetColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean result = record.isSet("column2");

        Assert.assertTrue(result);
    }

    @Test
    public void testIsSetWithMappedButNotSetColumn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        boolean result = record.isSet("column3");

        Assert.assertFalse(result);
    }

    @Test
    public void testIsSetWithUnmappedColumn() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        boolean result = record.isSet("column2");

        Assert.assertFalse(result);
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        int count = 0;
        for (String value : record) {
            count++;
        }

        Assert.assertEquals(3, count);
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String[] result = record.values();

        Assert.assertArrayEquals(values, result);
    }

    @Test
    public void testGetCommentWithComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment", 1);

        String result = record.getComment();

        Assert.assertEquals("This is a comment", result);
    }

    @Test
    public void testGetCommentWithoutComment() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String result = record.getComment();

        Assert.assertNull(result);
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 10);

        long result = record.getRecordNumber();

        Assert.assertEquals(10, result);
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        int result = record.size();

        Assert.assertEquals(3, result);
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        String result = record.toString();

        Assert.assertEquals("[value1, value2, value3]", result);
    }
}