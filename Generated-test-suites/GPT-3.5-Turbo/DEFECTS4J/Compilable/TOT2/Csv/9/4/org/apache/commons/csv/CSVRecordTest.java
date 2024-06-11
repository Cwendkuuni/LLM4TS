package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "comment", 1);
        Enum<?> testEnum = Enum.valueOf(Enum.class, "ENUM_VALUE");
        Assert.assertEquals("value1", record.get(testEnum));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "comment", 1);
        Assert.assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        Assert.assertEquals("value1", record.get("col1"));
        Assert.assertEquals("value2", record.get("col2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        record.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        record.get("col2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 2);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        record.get("col1");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "test comment", 1);
        Assert.assertEquals("test comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "comment", 5);
        Assert.assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        Assert.assertTrue(record.isConsistent());
    }

    @Test
    public void testIsNotConsistent() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1);
        Assert.assertFalse(record.isConsistent());
    }

}