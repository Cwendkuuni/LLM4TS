package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_Empty() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
        assertNull(record.get("0"));
        assertNull(record.get("name"));
    }

    @Test
    public void testCSVRecord_SingleValue() {
        String[] values = new String[]{"foo"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(1, record.size());
        assertEquals("foo", record.get(0));
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
    }

    @Test
    public void testCSVRecord_MultipleValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get("name2"));
        assertEquals("baz", record.get("name3"));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
    }

    @Test
    public void testCSVRecord_NullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_NoMapping_GetByName() {
        String[] values = new String[]{"foo", "bar"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_Inconsistent_GetByName() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.values = new String[1];
        record.get("name2");
    }

    @Test
    public void testCSVRecord_ToString() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        String expected = "[foo, bar, baz]";
        assertEquals(expected, record.toString());
    }
}