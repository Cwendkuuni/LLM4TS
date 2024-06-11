package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGet() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
    }

    @Test
    public void testGetWithName() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get("column1"));
        assertEquals("bar", record.get("column2"));
        assertEquals("baz", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidName() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("column2");
    }

    @Test
    public void testGetComment() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "This is a comment", 1);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 10);

        assertEquals(10, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentNoMapping() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentInvalidMapping() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("column1"));
        assertFalse(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        int count = 0;
        for (String value : record) {
            count++;
        }

        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        String[] values = {"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> map = record.putIn(new HashMap<String, String>());

        assertNotNull(map);
        assertEquals("foo", map.get("column1"));
        assertEquals("bar", map.get("column2"));
    }

    @Test
    public void testSize() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(3, record.toList().size());
        assertEquals("foo", record.toList().get(0));
        assertEquals("bar", record.toList().get(1));
        assertEquals("baz", record.toList().get(2));
    }

    @Test
    public void testToMap() {
        String[] values = {"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> map = record.toMap();

        assertNotNull(map);
        assertEquals("foo", map.get("column1"));
        assertEquals("bar", map.get("column2"));
    }

    @Test
    public void testToString() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("[foo, bar, baz]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values, record.values());
    }

    // Add more test cases for edge cases and additional methods

}