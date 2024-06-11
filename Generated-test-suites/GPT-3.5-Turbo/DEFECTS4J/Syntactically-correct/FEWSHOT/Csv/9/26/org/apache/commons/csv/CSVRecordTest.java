package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private final String[] values = {"value1", "value2", "value3"};
    private final Map<String, Integer> mapping = new HashMap<>();

    @Before
    public void setup() {
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        record = new CSVRecord(values, mapping, "", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(TestEnum.ENUM1));
        assertEquals("value2", record.get(TestEnum.ENUM2));
        assertEquals("value3", record.get(TestEnum.ENUM3));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test
    public void testGetByName() {
        assertEquals("value1", record.get("col1"));
        assertEquals("value2", record.get("col2"));
        assertEquals("value3", record.get("col3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(values, null, "", 1);
        recordWithoutMapping.get("col1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        record.get("col4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidIndex() {
        record.get("col5");
    }

    @Test
    public void testGetComment() {
        assertEquals("", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithUnmappedValues() {
        CSVRecord recordWithUnmappedValues = new CSVRecord(values, mapping, "", 1);
        assertFalse(recordWithUnmappedValues.isConsistent());
    }

    @Test
    public void testIsConsistentWithMissingValues() {
        mapping.remove("col3");
        CSVRecord recordWithMissingValues = new CSVRecord(values, mapping, "", 1);
        assertFalse(recordWithMissingValues.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
    }

    @Test
    public void testIsMappedWithInvalidName() {
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
    }

    @Test
    public void testIsSetWithInvalidName() {
        assertFalse(record.isSet("col4"));
    }

    @Test
    public void testIterator() {
        int i = 0;
        for (String value : record) {
            assertEquals(values[i++], value);
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(values.length, record.toList().size());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
        assertEquals("value3", map.get("col3"));
    }

    @Test
    public void testToString() {
        String expected = "[" + String.join(", ", values) + "]";
        assertEquals(expected, record.toString());
    }

    @Test
    public void testValues() {
        assertEquals(3, record.values().length);
    }

    private enum TestEnum {
        ENUM1, ENUM2, ENUM3
    }
}