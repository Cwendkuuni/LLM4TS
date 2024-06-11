package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {
    private static final String[] VALUES = {"value1", "value2", "value3"};
    private static final Map<String, Integer> MAPPING = new HashMap<>();
    static {
        MAPPING.put("column1", 0);
        MAPPING.put("column2", 1);
        MAPPING.put("column3", 2);
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertNull(record.get(3));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals("value1", record.get("column1"));
        assertEquals("value2", record.get("column2"));
        assertEquals("value3", record.get("column3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, null, 1);
        record.get("column1");
    }

    @Test
    public void testGetByNameNotFound() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertNull(record.get("column4"));
    }

    @Test
    public void testIsConsistentWithMapping() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithoutMapping() {
        CSVRecord record = new CSVRecord(VALUES, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record.isMapped("column1"));
        assertTrue(record.isMapped("column2"));
        assertTrue(record.isMapped("column3"));
    }

    @Test
    public void testIsNotMapped() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record.isMapped("column4"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record.isSet("column1"));
        assertTrue(record.isSet("column2"));
        assertTrue(record.isSet("column3"));
    }

    @Test
    public void testIsNotSet() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertTrue(record.isSet("column4"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        int count = 0;
        for (String value : record) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testValues() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        String[] values = record.values();
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals("value3", values[2]);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(VALUES, MAPPING, null, 1);
        assertEquals("[value1, value2, value3]", record.toString());
    }
}