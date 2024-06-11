package org.apache.commons.csv;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("First name", 0);
            put("Last name", 1);
            put("Age", 2);
            put("City", 3);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // Test get methods
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("New York", record.get(3));
        assertEquals("New York", record.get("City"));
        assertEquals("30", record.get("Age"));
        assertEquals(null, record.get("Unknown"));

        // Test size and isConsistent
        assertEquals(4, record.size());
        assertTrue(record.isConsistent());

        // Test isMapped and isSet
        assertTrue(record.isMapped("First name"));
        assertTrue(record.isSet("First name"));
        assertTrue(record.isMapped("Last name"));
        assertTrue(record.isSet("Last name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isMapped("Unknown"));
        assertFalse(record.isSet("Unknown"));

        // Test getComment and getRecordNumber
        assertNull(record.getComment());
        assertEquals(1L, record.getRecordNumber());

        // Test iterator and putIn
        List<String> list = record.iterator().asList();
        assertEquals("John", list.get(0));
        assertEquals("Doe", list.get(1));
        assertEquals("30", list.get(2));
        assertEquals("New York", list.get(3));

        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertEquals("John", map.get("First name"));
        assertEquals("Doe", map.get("Last name"));
        assertEquals("30", map.get("Age"));
        assertEquals("New York", map.get("City"));
        assertNull(map.get("Unknown"));

        // Test toString and values()
        assertEquals("[John, Doe, 30, New York]", record.toString());
        assertArrayEquals(new String[]{"John", "Doe", "30", "New York"}, record.values());
    }

    @Test
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        // Test get methods
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("New York", record.get(3));

        // Test size and isConsistent
        assertEquals(4, record.size());
        assertTrue(record.isConsistent());

        // Test isMapped and isSet
        assertFalse(record.isMapped("First name"));
        assertFalse(record.isSet("First name"));
        assertFalse(record.isMapped("Last name"));
        assertFalse(record.isSet("Last name"));
        assertFalse(record.isMapped("Age"));
        assertFalse(record.isSet("Age"));
        assertFalse(record.isMapped("City"));
        assertFalse(record.isSet("City"));

        // Test getComment and getRecordNumber
        assertNull(record.getComment());
        assertEquals(1L, record.getRecordNumber());

        // Test iterator and putIn
        List<String> list = record.iterator().asList();
        assertEquals("John", list.get(0));
        assertEquals("Doe", list.get(1));
        assertEquals("30", list.get(2));
        assertEquals("New York", list.get(3));

        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertNull(map.get("First name"));
        assertNull(map.get("Last name"));
        assertNull(map.get("Age"));
        assertNull(map.get("City"));
    }
}