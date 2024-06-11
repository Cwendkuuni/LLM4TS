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
    public void testGet() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("gender", record.get(2));
        assertNull(record.get(3));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        final String[] values = new String[]{"name", "age", "gender"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("age");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        mapping.put("id", 3); // inconsistent size
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("id");
    }

    @Test
    public void testIsConsistent() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        final String[] valuesInconsistent = new String[]{"name", "age"};
        final Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("name", 0);
        mappingInconsistent.put("age", 1);
        mappingInconsistent.put("gender", 2);
        final CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, mappingInconsistent, null, 1);
        assertFalse(recordInconsistent.isConsistent());
    }

    @Test
    public void testIsMapped() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("gender"));
        assertFalse(record.isMapped("id"));
    }

    @Test
    public void testIsSet() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("gender"));
        assertFalse(record.isSet("id"));
    }

    @Test
    public void testIterator() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        final Iterator<String> iterator = record.iterator();
        assertSame(iterator.next(), "name");
        assertSame(iterator.next(), "age");
        assertSame(iterator.next(), "gender");
        assertNull(iterator.next());
    }

    @Test
    public void testValues() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testComment() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final String comment = "Some comment";
        final long recordNumber = 1;
        final CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testRecordNumber() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final String comment = "Some comment";
        final long recordNumber = 1;
        final CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        final String[] values = new String[]{"name", "age", "gender"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("gender", 2);
        final String comment = "Some comment";
        final long recordNumber = 1;
        final CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("[name, age, gender]", record.toString());
    }
}