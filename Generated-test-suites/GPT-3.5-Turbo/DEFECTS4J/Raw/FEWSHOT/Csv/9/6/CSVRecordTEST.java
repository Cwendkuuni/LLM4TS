package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(MyEnum.VALUE1));
        assertEquals("value2", record.get(MyEnum.VALUE2));
        assertEquals("value3", record.get(MyEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        assertEquals("value1", record.get("name1"));
        assertEquals("value2", record.get("name2"));
        assertEquals("value3", record.get("name3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
        record.get("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        record.get("name3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(true, record1.isConsistent());

        CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
        assertEquals(false, record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        record3 = new CSVRecord(record3.values(), mapping, null, 1);
        assertEquals(true, record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals(true, record.isMapped("name1"));
        assertEquals(true, record.isMapped("name2"));
        assertEquals(false, record.isMapped("name3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
        assertEquals(true, record.isSet("name1"));
        assertEquals(true, record.isSet("name2"));
        assertEquals(false, record.isSet("name3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        int count = 0;
        for (String value : record) {
            switch (count) {
                case 0:
                    assertEquals("value1", value);
                    break;
                case 1:
                    assertEquals("value2", value);
                    break;
                case 2:
                    assertEquals("value3", value);
                    break;
            }
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("name1", "value1");
        map.put("name2", "value2");
        map.put("name3", "value3");

        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        record.putIn(map);

        assertEquals("value1", map.get("name1"));
        assertEquals("value2", map.get("name2"));
        assertEquals("value3", map.get("name3"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(Arrays.asList("value1", "value2", "value3"), record.toList());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");
        expectedMap.put("name3", "value3");

        assertEquals(expectedMap, record.toMap());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
        assertEquals(new String[]{"value1", "value2", "value3"}, record.values());
    }
    
    private enum MyEnum {
        VALUE1,
        VALUE2,
        VALUE3;
    }
}