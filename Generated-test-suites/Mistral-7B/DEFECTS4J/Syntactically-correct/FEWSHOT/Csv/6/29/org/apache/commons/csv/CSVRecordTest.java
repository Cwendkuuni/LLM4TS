package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
        assertNull(record.get("name"));
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(4, record.size());
        assertEquals("value0", record.get(0));
        assertEquals("value1", record.get(2));
        assertNull(record.get("name2"));
        assertSame("name0", record.get("name0"));
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name0"));
        assertTrue(record.isSet("name0"));
        assertFalse(record.isSet("name2"));
        assertEquals(Arrays.asList("name0", "value0", "name1", "value1"), record.toList());
        assertEquals(Arrays.toString(values), record.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullMapping() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("name0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownName() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("name2");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get(4);
    }

    @Test
    public void testPutInEmptyMap() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, String> map = new HashMap<String, String>();
        map = new CSVRecord(values, null, null, 0).putIn(map);
        assertEquals(4, map.size());
        assertEquals("value0", map.get("name0"));
        assertEquals("value1", map.get("name1"));
        assertNull(map.get("name2"));
    }

    @Test
    public void testPutInNonEmptyMap() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, String> map = new HashMap<String, String>() {{
            put("name1", "value1");
            put("name2", "value2");
        }};
        map = new CSVRecord(values, null, null, 0).putIn(map);
        assertEquals(6, map.size());
        assertEquals("value0", map.get("name0"));
        assertEquals("value1", map.get("name1"));
        assertEquals("value1", map.get("name1")); // Should not overwrite existing values
        assertEquals("value2", map.get("name2"));
        assertNull(map.get("name3"));
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 2);
        }};
        String comment = "This is a comment.";

        CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertEquals(4, record.size());
        assertEquals("value0", record.get(0));
        assertEquals("value1", record.get(2));
        assertNull(record.get("name2"));
        assertEquals(comment, record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name0"));
        assertTrue(record.isSet("name0"));
        assertFalse(record.isSet("name2"));
        assertEquals(Arrays.asList("name0", "value0", "name1", "value1"), record.toList());
        assertEquals(Arrays.toString(values), record.toString());
    }

    @Test
    public void testCSVRecordWithEmptyComment() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 2);
        }};
        String comment = "";

        CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertEquals(4, record.size());
        assertEquals("value0", record.get(0));
        assertEquals("value1", record.get(2));
        assertNull(record.get("name2"));
        assertEquals("", record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name0"));
        assertTrue(record.isSet("name0"));
        assertFalse(record.isSet("name2"));
        assertEquals(Arrays.asList("name0", "value0", "name1", "value1"), record.toList());
        assertEquals(Arrays.toString(values), record.toString());
    }

    @Test
    public void testCSVRecordWithNullValues() {
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 1);
        }};

        CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertNull(record.getComment());
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.get("name0"));
        assertNull(record.get("name1"));
    }

    @Test
    public void testCSVRecordWithEmptyMapping() {
        String[] values = new String[] { "name0", "value0", "name1", "value1" };

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(4, record.size());
        assertEquals("value0", record.get(0));
        assertEquals("value1", record.get(2));
        assertNull(record.get("name1"));
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name0"));
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isSet("name0"));
        assertFalse(record.isSet("name1"));
        assertEquals(Arrays.asList("name0", "value0", "name1", "value1"), record.toList());
        assertEquals(Arrays.toString(values), record.toString());
    }

    @Test
    public void testCSVRecordWithInconsistentSize() {
        String[] values = new String[] { "name0", "value0" };
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("name0", 0);
            put("name1", 1);
            put("name2", 2);
        }};

        try {
            new CSVRecord(values, mapping, null, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}