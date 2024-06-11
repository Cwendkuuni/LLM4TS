package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord_empty() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.getComment());
        assertEquals(0, record.getRecordNumber());
        assertTrue(record.isConsistent());
        assertTrue(record.isEmpty());
    }

    @Test
    public void testCSVRecord_basic() {
        String[] values = new String[]{"name", "age", "city"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name", 0);
        mapping.put("age", 1);
        mapping.put("city", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.getComment());
        assertEquals(1, record.getRecordNumber());
        assertTrue(record.isConsistent());

        assertEquals("name", record.get(0));
        assertEquals("age", record.get(1));
        assertEquals("city", record.get(2));
        assertNull(record.get("unknown"));

        assertTrue(record.isMapped("name"));
        assertTrue(record.isMapped("age"));
        assertTrue(record.isMapped("city"));

        assertTrue(record.isSet("name"));
        assertTrue(record.isSet("age"));
        assertTrue(record.isSet("city"));

        assertFalse(record.isSet("unknown"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCSVRecord_missingMapping() {
        String[] values = new String[]{"name", "age"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("city");
    }

    @Test
    public void testCSVRecord_getComment() {
        String comment = "This is a comment.";
        CSVRecord record = new CSVRecord(null, null, comment, 1);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testCSVRecord_getRecordNumber() {
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(null, null, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testCSVRecord_size() {
        String[] values = new String[]{"name", "age", "city"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(values.length, record.size());
    }

    @Test
    public void testCSVRecord_toString() {
        String[] values = new String[]{"name", "age", "city"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expected = "[name, age, city]";
        assertEquals(expected, record.toString());
    }

    @Test
    public void testCSVRecord_iterator() {
        String[] values = new String[]{"name", "age", "city"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        List<String> expected = Arrays.asList(values);
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(expected.get(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(expected.get(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(expected.get(2), iterator.next());
        assertFalse(iterator.hasNext());
    }
}