package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"Name", "Age", "Gender"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(3, record.size());
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("Gender", record.get(2));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertArrayEquals(values, record.values());
        assertNull(record.getComment());
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1L);
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.getComment());
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = new String[]{"Name", "Age", "Gender"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertFalse(record.isConsistent());
        assertNull(record.get("Name"));
        assertNull(record.get("Age"));
        assertNull(record.get("Gender"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        new CSVRecord(null, mapping, null, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"Name", "Age", "Gender"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        new CSVRecord(values, mapping, null, 1L);
        assertEquals(3, values.length);
        assertEquals(3, mapping.size());
        try {
            String value = record.get(3);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Given index is out of bounds", e);
        }
    }

    @Test
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"Name", "Age", "Gender"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Gender", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertNull(record.get("InvalidName"));
    }
}