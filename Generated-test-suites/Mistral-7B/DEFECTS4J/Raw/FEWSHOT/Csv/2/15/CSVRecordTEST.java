package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertArrayEquals(new String[0], record.values());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertArrayEquals(values, record.values());
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertEquals("City", record.get("City"));
        assertNull(record.get("unknown"));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.get(3));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);
        assertArrayEquals(values, record.values());
        assertEquals("Comment", record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertEquals("City", record.get("City"));
        assertNull(record.get("unknown"));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.get(3));
    }

    @Test
    public void testCSVRecordWithRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 123L);
        assertArrayEquals(values, record.values());
        assertNull(record.getComment());
        assertEquals(123, record.getRecordNumber());
        assertTrue(record.isConsistent());

        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertEquals("City", record.get("City"));
        assertNull(record.get("unknown"));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertNull(record.get(3));
    }

    @Test
    public void testCSVRecordConsistency() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.values = new String[3];
        assertFalse(record.isConsistent());

        String[] values2 = new String[]{"Name", "Age"};
        CSVRecord record2 = new CSVRecord(values2, mapping, null, 0);
        assertFalse(record2.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordNoMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordInvalidMapping() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 1);
        mapping.put("Age", 0);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
    }

    @Test
    public void testCSVRecordToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("[Name, Age, City]", record.toString());
    }
}