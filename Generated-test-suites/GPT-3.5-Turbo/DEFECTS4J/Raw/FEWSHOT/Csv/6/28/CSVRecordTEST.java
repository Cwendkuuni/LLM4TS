package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetEnum() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(TestEnum.VALUE1));
        assertEquals("value2", record.get(TestEnum.VALUE2));
        assertEquals("value3", record.get(TestEnum.VALUE3));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);

        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2"}, mapping, null, 1);
        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);
        record.get("column3");
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertTrue(record1.isConsistent());

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record2 = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 2);
        assertTrue(record2.isConsistent());

        CSVRecord record3 = new CSVRecord(new String[] {"value1"}, mapping, null, 3);
        assertTrue(!record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);

        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(!record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);

        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(!record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);

        int count = 0;
        for (String value : record) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        map.put("column1", "value");
        map.put("column2", "value");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        CSVRecord record = new CSVRecord(new String[] {"new value1", "new value2", "value3"}, mapping, null, 1);

        record.putIn(map);
        assertEquals("new value1", map.get("column1"));
        assertEquals("new value2", map.get("column2"));
    }

    @Test
    public void testSize() {
        CSVRecord record1 = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals(3, record1.size());

        CSVRecord record2 = new CSVRecord(new String[] {}, null, null, 2);
        assertEquals(0, record2.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals(Arrays.asList("value1", "value2", "value3"), record.toList());
    }

    @Test
    public void testToMap() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, mapping, null, 1);

        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(new String[] {"value1", "value2", "value3"}, null, null, 1);
        assertEquals(new String[] {"value1", "value2", "value3"}, record.values());
    }

    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}