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
    private Map<String, Integer> mapping;

    @Before
    public void setUp() {
        String[] values = new String[]{"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        record = new CSVRecord(values, mapping, "comment", 1);
    }

    @Test
    public void testGetByEnum() {
        assertEquals("value1", record.get(TestEnum.VALUE1));
    }

    @Test
    public void testGetByIndex() {
        assertEquals("value2", record.get(1));
    }

    @Test
    public void testGetByName() {
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameNoMapping() {
        CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"value1"}, null, null, 1);
        recordWithoutMapping.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidName() {
        record.get("column4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameInvalidIndex() {
        CSVRecord recordWithInvalidIndex = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        recordWithInvalidIndex.get("column1");
    }

    @Test
    public void testGetComment() {
        assertEquals("comment", record.getComment());
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
    public void testIsConsistentInvalidMapping() {
        mapping.put("column4", 3);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("column1"));
    }

    @Test
    public void testIsMappedInvalidName() {
        assertFalse(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("column2"));
    }

    @Test
    public void testIsSetInvalidName() {
        assertFalse(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[value1, value2, value3]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    private enum TestEnum {
        VALUE1
    }
}