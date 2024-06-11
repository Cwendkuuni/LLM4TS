package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

    private static CSVRecord csvRecord;

    @BeforeClass
    public static void setUp() {
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
    public void testGet() {
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertEquals("value3", csvRecord.get(2));
    }

    @Test
    public void testGet_NullMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1"}, null, "comment", 1);
        try {
            csvRecord.get("column");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void testGet_MappingNotFound() {
        try {
            csvRecord.get("column");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for column not found, expected one of [column1, column2, column3]", e.getMessage());
        }
    }

    @Test
    public void testGet_IndexOutOfBounds() {
        try {
            csvRecord.get("column1");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'column1' is 0 but CSVRecord only has 3 values!", e.getMessage());
        }
    }

    @Test
    public void testGetComment() {
        assertEquals("This is a comment", csvRecord.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, csvRecord.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_NullMapping() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    public void testIsConsistent_NotConsistent() {
        CSVRecord csvRecord = new CSVRecord(new String[]{"value1", "value2"}, new HashMap<>(), "comment", 1);
        assertFalse(csvRecord.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("column1"));
        assertTrue(csvRecord.isMapped("column2"));
        assertTrue(csvRecord.isMapped("column3"));
        assertFalse(csvRecord.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        assertTrue(csvRecord.isSet("column1"));
        assertTrue(csvRecord.isSet("column2"));
        assertTrue(csvRecord.isSet("column3"));
        assertFalse(csvRecord.isSet("column4"));
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
        HashMap<String, String> map = new HashMap<>();
        map.put("column1", "value");
        csvRecord.putIn(map);
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

    @Test
    public void testSize() {
        assertEquals(3, csvRecord.size());
    }

    @Test
    public void testToList() {
        assertEquals(3, csvRecord.toList().size());
        assertTrue(csvRecord.toList().contains("value1"));
        assertTrue(csvRecord.toList().contains("value2"));
        assertTrue(csvRecord.toList().contains("value3"));
    }

    @Test
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(3, map.size());
        assertEquals("value1", map.get("column1"));
        assertEquals("value2", map.get("column2"));
        assertEquals("value3", map.get("column3"));
    }

}