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

    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    private static final String COMMENT = "comment";
    private static final long RECORD_NUMBER = 1;
    private CSVRecord record;

    @Before
    public void setup() {
        MAPPING.put("col1", 0);
        MAPPING.put("col2", 1);
        MAPPING.put("col3", 2);
        record = new CSVRecord(VALUES, MAPPING, COMMENT, RECORD_NUMBER);
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
        CSVRecord noMappingRecord = new CSVRecord(VALUES, null, COMMENT, RECORD_NUMBER);
        noMappingRecord.get("col");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        record.get("invalid");
    }

    @Test
    public void testGetComment() {
        assertEquals(COMMENT, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(RECORD_NUMBER, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithEmptyMapping() {
        CSVRecord emptyMappingRecord = new CSVRecord(VALUES, new HashMap<>(), COMMENT, RECORD_NUMBER);
        assertTrue(emptyMappingRecord.isConsistent());
    }

    @Test
    public void testIsConsistentWithDifferentSizeMapping() {
        Map<String, Integer> differentSizeMapping = new HashMap<>();
        differentSizeMapping.put("col1", 0);
        differentSizeMapping.put("col2", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertFalse(record.isMapped("col4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));
        assertFalse(record.isSet("col4"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : record) {
            assertEquals(VALUES[count], value);
            count++;
        }
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals(VALUES[0], map.get("col1"));
        assertEquals(VALUES[1], map.get("col2"));
        assertEquals(VALUES[2], map.get("col3"));
    }

    @Test
    public void testSize() {
        assertEquals(VALUES.length, record.size());
    }

    @Test
    public void testToList() {
        assertEquals(Arrays.asList(VALUES), record.toList());
    }

    @Test
    public void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals(VALUES[0], map.get("col1"));
        assertEquals(VALUES[1], map.get("col2"));
        assertEquals(VALUES[2], map.get("col3"));
    }

    @Test
    public void testToString() {
        assertEquals(Arrays.toString(VALUES), record.toString());
    }

    private enum TestEnum {
        ENUM1, ENUM2, ENUM3
    }
}