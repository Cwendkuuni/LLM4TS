package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyMappingAndValues() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("columnName"));
        assertNull(record.get("columnName"));
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithMappingAndValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);

        String[] values = {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values, record.values());
        assertEquals(2, record.size());

        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("column1"));
        assertEquals("value1", record.get("column1"));
        assertTrue(record.isMapped("column2"));
        assertEquals("value2", record.get("column2"));
        assertFalse(record.isMapped("nonExistentColumn"));
        assertNull(record.get("nonExistentColumn"));
    }

    @Test
    public void testCSVRecordWithEmptyMappingAndValuesButValid() {
        String[] values = {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertFalse(record.isConsistent());
        assertEquals(2, record.size());
        assertFalse(record.isMapped("column1"));
        assertNull(record.get("column1"));
        assertFalse(record.isMapped("column2"));
        assertNull(record.get("column2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueWithNullMapping() {
        String[] values = {"value1", "value2"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("columnName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValueWithInconsistentRecord() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String[] values = {"value1", "value2"};

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
        record.get("column3");
    }

    @Test
    public void testGetValueWithEmptyMappingAndEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        assertEquals(0, record.size());
        assertNull(record.get("columnName"));
    }

    @Test
    public void testGetValueWithEmptyMappingAndValuesButValid() {
        String[] values = {"", "", ""};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(3, record.size());
        assertNull(record.get("columnName"));
    }

    @Test
    public void testGetValueWithEmptyMappingAndValuesButValidAndMapped() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("column1", 0);
        mapping.put("column2", 1);
        mapping.put("column3", 2);

        String[] values = {"", "", ""};

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("columnName"));
        assertNull(record.get("columnName"));
        assertTrue(record.isMapped("column1"));
        assertEquals("", record.get("column1"));
        assertTrue(record.isMapped("column2"));
        assertEquals("", record.get("column2"));
        assertTrue(record.isMapped("column3"));
        assertEquals("", record.get("column3"));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment.";
        CSVRecord record = new CSVRecord(new String[0], null, comment, 1);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 1;
        CSVRecord record = new CSVRecord(new String[0], null, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }
}