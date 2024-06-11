package org.apache.commons.csv;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        Assert.assertEquals(null, record.get(TestEnum.TEST));
    }

    @Test
    public void testGetInt() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertEquals("value1", record.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingNotFound() {
        CSVRecord record = new CSVRecord(new String[]{"value"}, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMappingIndexOutOfBounds() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value1"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("name");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "comment", 0);
        Assert.assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5);
        Assert.assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        Assert.assertFalse(record.isMapped("name"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Assert.assertTrue(record.isSet("name"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertEquals("value1", record.iterator().next());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.putIn(map);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("value", map.get("name"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertEquals(2, record.size());
    }

    @Test
    public void testToList() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertEquals(values[0], record.toList().get(0));
        Assert.assertEquals(values[1], record.toList().get(1));
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        String[] values = {"value"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.toMap();
        Assert.assertEquals(1, map.size());
        Assert.assertEquals("value", map.get("name"));
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        Assert.assertEquals(values, record.values());
    }

    private enum TestEnum {
        TEST
    }
}