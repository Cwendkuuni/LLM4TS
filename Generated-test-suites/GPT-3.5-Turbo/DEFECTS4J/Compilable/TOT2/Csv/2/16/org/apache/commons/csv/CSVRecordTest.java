package org.apache.commons.csv;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testGetByIndex() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", csvRecord.get("column1"));
        assertEquals("value2", csvRecord.get("column2"));
        assertEquals("value3", csvRecord.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"value1", "value2", "value3"};
        CSVRecord csvRecord = new CSVRecord(values, null, null, 1L);
        csvRecord.get("column1");
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertTrue(csvRecord.isMapped("column1"));
        assertFalse(csvRecord.isMapped("column2"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertTrue(csvRecord.isSet("column1"));
        assertFalse(csvRecord.isSet("column2"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        List<String> valuesList = new ArrayList<>();
        for (String value : csvRecord) {
            valuesList.add(value);
        }

        assertEquals(3, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("value3", valuesList.get(2));
    }

    @Test
    public void testValues() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertArrayEquals(values, csvRecord.values());
    }

    @Test
    public void testGetComment() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, "This is a comment", 1L);

        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 5L);

        assertEquals(5L, csvRecord.getRecordNumber());
    }

    @Test
    public void testSize() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1L);

        assertEquals("[value1, value2, value3]", csvRecord.toString());
    }
}