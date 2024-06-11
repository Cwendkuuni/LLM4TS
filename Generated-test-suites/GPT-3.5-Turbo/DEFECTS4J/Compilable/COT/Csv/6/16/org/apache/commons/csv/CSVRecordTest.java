package org.apache.commons.csv;

import org.junit.*;
import java.util.*;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("value1", record.get(TestEnum.VALUE1));
        Assert.assertEquals("value2", record.get(TestEnum.VALUE2));
        Assert.assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("value1", record.get(0));
        Assert.assertEquals("value2", record.get(1));
        Assert.assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        Assert.assertEquals("value1", record.get("name1"));
        Assert.assertEquals("value2", record.get("name2"));
        Assert.assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("name3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "Test comment", 1);
        Assert.assertEquals("Test comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{}, null, null, 10);
        Assert.assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1"}, null, null, 1);
        Assert.assertTrue(record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        Assert.assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        Assert.assertTrue(record.isMapped("name1"));
        Assert.assertTrue(record.isMapped("name2"));
        Assert.assertFalse(record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        Assert.assertTrue(record.isSet("name1"));
        Assert.assertTrue(record.isSet("name2"));
        Assert.assertFalse(record.isSet("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals(3, record.size());
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Iterator<String> iterator = record.iterator();
        Assert.assertEquals("value1", iterator.next());
        Assert.assertEquals("value2", iterator.next());
        Assert.assertEquals("value3", iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        record.putIn(map);
        Assert.assertEquals("value1", map.get("name1"));
        Assert.assertEquals("value2", map.get("name2"));
        Assert.assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertEquals("[value1, value2, value3]", record.toString());
    }
    
    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
}

enum TestEnum {
    VALUE1,
    VALUE2,
    VALUE3
}
