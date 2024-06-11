package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isInstanceOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testNewCSVRecordWithEmptyValuesArray() {
        final Map<String, Integer> mapping = new HashMap<>();
        final CSVRecord record = new CSVRecord(null, mapping, null, 0);
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertSame(Collections.emptyList(), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithValuesArray() {
        final Map<String, Integer> mapping = new HashMap<>();
        final String[] values = new String[]{"foo", "bar", "baz"};
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(Arrays.asList(values), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithEmptyMapping() {
        final String[] values = new String[]{"foo", "bar", "baz"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(Arrays.asList(values), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithValidMapping() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertEquals("name", record.get("name"));
        assertEquals("age", record.get("age"));
        assertEquals("city", record.get("city"));
        assertNull(record.get("unknown"));
        assertFalse(record.isSet("unknown"));
        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("city"));
        assertArrayEquals(values, record.values());
        assertSame(Arrays.asList(values), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithInvalidMapping() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        mapping.put("unknown", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertFalse(record.isConsistent());
        assertNull(record.get("unknown"));
        assertFalse(record.isSet("unknown"));
        assertArrayEquals(values, record.values());
        assertSame(Arrays.asList(values), record.iterator());
    }

    @Test
    public void testGetMapping() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
        assertSame(mapping, record.mapping);
    }

    @Test
    public void testGetValues() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        assertArrayEquals(values, record.values());
    }

    @Test
    public void testGetComment() {
        final String comment = "This is a comment";
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        final CSVRecord record = new CSVRecord(values, mapping, comment, 0);
        assertNull(record.getComment());
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        final long recordNumber = 12345;
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        final CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertNull(record.getRecordNumber());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final Map<String, Integer> mapping = new HashMap<>();
        final CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());
        final String[] valuesWithSameSize = new String[]{"name:foo", "age:25", "city:Paris", "unknown:unknown"};
        final CSVRecord recordWithValidMapping = new CSVRecord(valuesWithSameSize, mapping, null, 0);
        assertTrue(recordWithValidMapping.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithNoMapping() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get(-1);
    }

    @Test
    public void testToString() {
        final String[] values = new String[]{"name:foo", "age:25", "city:Paris"};
        final CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("[name:foo, age:25, city:Paris]", record.toString());
    }
}