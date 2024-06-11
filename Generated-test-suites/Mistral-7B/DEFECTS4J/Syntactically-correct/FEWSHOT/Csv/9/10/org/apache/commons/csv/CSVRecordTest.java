package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValuesAndMapping() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));

        Map<String, String> map = record.toMap();
        assertTrue(map.isEmpty());
        assertEquals(Collections.emptyList(), map.values());
    }

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = new String[]{"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(3, record.size());
        assertEquals("name1", record.get(0));
        assertEquals("name2", record.get(1));
        assertEquals("name3", record.get(2));
        assertSame(values, record.values());
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));

        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("name1", map.get("name1"));
        assertEquals("name2", map.get("name2"));
        assertEquals("name3", map.get("name3"));
        assertEquals(Arrays.asList("name1", "name2", "name3"), map.values());
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecordWithNoMapping() {
        String[] values = new String[]{"name1", "name2", "name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        throw new IllegalStateException("No header mapping was specified, the record values can't be accessed by name");
    }

    @Test
    public void testGetNullString() {
        String[] values = new String[]{"name1", null, "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.get("name2"));
        assertEquals(CSVRecord.class.getDeclaredField("EMPTY_STRING_ARRAY").get(null), record.values());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[name1, name2, name3]", record.toString());
    }
}