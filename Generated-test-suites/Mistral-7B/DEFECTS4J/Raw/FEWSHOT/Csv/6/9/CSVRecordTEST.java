package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] emptyValues = new String[0];
        CSVRecord record = new CSVRecord(emptyValues, null, null, 0);

        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name"));
        assertFalse(record.isSet("name"));
        assertSame(emptyValues, record.values());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(3, record.size());
        assertEquals("name1", record.get(0));
        assertEquals("name2", record.get(1));
        assertEquals("name3", record.get(2));
        assertEquals("name1", record.get("name1"));
        assertEquals("name2", record.get("name2"));
        assertEquals("name3", record.get("name3"));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isSet("name3"));
        assertSame(values, record.values());
    }

    @Test
    public void testGetWithInvalidIndex() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        try {
            record.get(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }

        try {
            record.get(3);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGetWithInvalidName() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        try {
            record.get("invalidName");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testGetWithMissingMapping() {
        String[] values = {"name1", "name2", "name3"};
        CSVRecord record = new CSVRecord(values, null, null, 0);

        try {
            record.get("name1");
            fail();
        } catch (IllegalStateException e) {
            // Expected result
        }
    }

    @Test
    public void testGetWithInconsistentRecord() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name4", 3); // Invalid index
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        try {
            record.get("name2");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testPutIn() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);

        Map<String, String> result = record.putIn(map);

        assertEquals("name1", map.get("name1"));
        assertEquals("name2", map.get("name2"));
        assertEquals("name3", map.get("name3"));
        assertNull(map.get("invalidName"));
        assertEquals(3, result.size());
    }

    @Test
    public void testToString() {
        String[] values = {"name1", "name2", "name3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("[name1, name2, name3]", record.toString());
    }
}