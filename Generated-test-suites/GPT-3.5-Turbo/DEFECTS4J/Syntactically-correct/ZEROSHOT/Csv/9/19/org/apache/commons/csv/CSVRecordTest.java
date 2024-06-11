package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CSVRecordTest {

    private CSVRecord csvRecord;

    @Before
    public void setUp() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);
        String comment = "This is a comment";
        long recordNumber = 1;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    public void testGetEnum() {
        String result = csvRecord.get(TestEnum.COLUMN1);
        assertEquals("value1", result);
    }

    @Test
    public void testGetIndex() {
        String result = csvRecord.get(1);
        assertEquals("value2", result);
    }

    @Test
    public void testGetName() {
        String result = csvRecord.get("column3");
        assertEquals("value3", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNameWithNoMapping() {
        csvRecord = new CSVRecord(new String[]{"value1"}, null, null, 1);
        csvRecord.get("column1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidName() {
        csvRecord.get("invalidColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNameWithInvalidIndex() {
        csvRecord.get("column2");
    }

    @Test
    public void testGetComment() {
        String result = csvRecord.getComment();
        assertEquals("This is a comment", result);
    }

    @Test
    public void testGetRecordNumber() {
        long result = csvRecord.getRecordNumber();
        assertEquals(1, result);
    }

    @Test
    public void testIsConsistent() {
        boolean result = csvRecord.isConsistent();
        assertTrue(result);
    }

    @Test
    public void testIsConsistentWithInconsistentMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        csvRecord = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
        boolean result = csvRecord.isConsistent();
        assertFalse(result);
    }

    @Test
    public void testIsMapped() {
        boolean result = csvRecord.isMapped("column2");
        assertTrue(result);
    }

    @Test
    public void testIsSet() {
        boolean result = csvRecord.isSet("column3");
        assertTrue(result);
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (String value : csvRecord) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = csvRecord.putIn(new HashMap<String, String>());
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        int result = csvRecord.size();
        assertEquals(3, result);
    }

    @Test
    public void testToList() {
        int count = 0;
        for (String value : csvRecord.toList()) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testValues() {
        String[] result = csvRecord.values();
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testToString() {
        String result = csvRecord.toString();
        assertEquals("[value1, value2, value3]", result);
    }

    enum TestEnum {
        COLUMN1,
        COLUMN2,
        COLUMN3
    }
}