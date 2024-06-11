package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValidValues() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("M", record.get(3));
        assertEquals(1, record.size());
        assertFalse(record.isConsistent());
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isSet("Address"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertTrue(Arrays.equals(values, record.values()));

        List<String> list = record.toList();
        assertEquals(Arrays.asList("John", "Doe", "30", "M"), list);

        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("John", map.get("Name"));
        assertEquals("Doe", map.get("Age"));
        assertEquals("M", map.get("Gender"));
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("Name"));
        assertNull(record.get(0));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertNull(record.getRecordNumber());
        assertTrue(Arrays.equals(values, record.values()));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Age");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecordWithInvalidName() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get("Address");
    }

    @Test
    public void testCSVRecordWithInvalidRecordNumber() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, -1);

        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[]{"John", "Doe", "30", "M"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);

        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }
}