package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = { "col1", "col2", "col3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(3, record.size());
        assertArrayEquals(values, record.values());
        assertEquals("col1", record.get(0));
        assertEquals("col2", record.get(1));
        assertEquals("col3", record.get(2));
        assertEquals("col1", record.get("col1"));
        assertEquals("col2", record.get("col2"));
        assertEquals("col3", record.get("col3"));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));
        assertTrue(record.isSet("col1"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));

        List<String> valuesList = Arrays.asList(record.values());
        Iterator<String> iterator = record.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(values[count++], iterator.next());
        }

        assertEquals(null, record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithNullValues() {
        CSVRecord record = new CSVRecord(null, null, null, 1L);
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] emptyValues = {};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(emptyValues, mapping, null, 1L);
        assertEquals(0, record.size());
        assertNull(record.get(0));
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithInvalidGet() {
        String[] values = { "col1", "col2", "col3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        try {
            record.get(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Index -1 out of bounds for length: 3", e.getMessage());
        }

        try {
            record.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            record.get("not_mapped");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("No value for key: not_mapped", e.getMessage());
        }
    }

    @Test
    public void testCSVRecordWithInvalidIsConsistent() {
        String[] values = { "col1", "col2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isConsistent());

        values = new String[] { "col1", "col2", "col3" };
        record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithInvalidIsMapped() {
        String[] values = { "col1", "col2", "col3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertFalse(record.isMapped("not_mapped"));
        assertTrue(record.isMapped("col1"));
        assertTrue(record.isMapped("col2"));
        assertTrue(record.isMapped("col3"));

        mapping = null;
        record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isMapped("not_mapped"));
        assertFalse(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));
        assertFalse(record.isMapped("col3"));
    }

    @Test
    public void testCSVRecordWithInvalidIsSet() {
        String[] values = { "col1", "col2", "col3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("not_mapped"));
        assertTrue(record.isSet("col2"));
        assertTrue(record.isSet("col3"));

        mapping.put("not_mapped", 3);
        assertTrue(record.isSet("not_mapped"));
        assertEquals("", record.get("not_mapped"));

        mapping.remove("col1");
        assertFalse(record.isSet("col1"));
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = { "col1", "col2", "col3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("[col1, col2, col3]", record.toString());
    }
}