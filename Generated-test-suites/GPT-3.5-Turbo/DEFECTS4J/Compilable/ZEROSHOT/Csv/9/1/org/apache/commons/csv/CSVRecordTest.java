package org.apache.commons.csv;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(null, record.get(TestEnum.TEST));
    }

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName_NoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        try {
            record.get("column");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    @Test
    public void testGetByName_MappingNotFound() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        try {
            record.get("column4");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testGetByName_IndexOutOfBounds() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        try {
            record.get("column3");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "This is a comment", 0);
        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 5);
        assertEquals(5, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent_NoMapping() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSizeNotEqual() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistent_MappingSizeEqual() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("column2"));
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("column2"));
        assertFalse(record.isSet("column3"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        int i = 0;
        for (String value : record) {
            assertEquals(values[i], value);
            i++;
        }
    }

    @Test
    public void testPutIn() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
    }

    @Test
    public void testToMap() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertArrayEquals(values, record.values());
    }
}

enum TestEnum {
    TEST
}