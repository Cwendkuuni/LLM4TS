package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithNoHeaderMapping() {
        String[] values = {"foo", "bar"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testCSVRecordWithEmptyValues() {
        String[] values = {};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(0, record.size());
    }

    @Test
    public void testCSVRecordWithValues() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertNull(record.get("invalid"));
    }

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(3, record.size());
        assertEquals("foo", record.get("foo"));
        assertEquals("bar", record.get("bar"));
        assertEquals("baz", record.get("baz"));
        assertNull(record.get("invalid"));
        assertSame(mapping, record.mapping);
    }

    @Test
    public void testCSVRecordWithComment() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        String comment = "Comment for this record";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals("Comment for this record", record.getComment());
    }

    @Test
    public void testCSVRecordWithInvalidIndex() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        try {
            record.get(3);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Index for header 'invalid' is 3 but CSVRecord only has 3 values!", e.getMessage());
        }
    }

    @Test
    public void testCSVRecordWithNullMapping() {
        String[] values = {"foo", "bar", "baz"};
        try {
            new CSVRecord(values, null, null, 1);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
        }
    }

    @Test
    public void testCSVRecordWithNullValues() {
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        try {
            new CSVRecord(null, mapping, null, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("CSVRecord cannot be null", e.getMessage());
        }
    }

    @Test
    public void testCSVRecordWithCommentAndValues() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        String comment = "Comment for this record";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals("Comment for this record", record.getComment());
        assertEquals("foo", record.get("foo"));
        assertEquals("bar", record.get("bar"));
        assertEquals("baz", record.get("baz"));
        assertNull(record.get("invalid"));
        assertSame(mapping, record.mapping);
    }

    @Test
    public void testCSVRecordWithCommentAndMappingAndValues() {
        String[] values = {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("foo", 0);
            put("bar", 1);
            put("baz", 2);
        }};
        String comment = "Comment for this record";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals("Comment for this record", record.getComment());
        assertEquals("foo", record.get("foo"));
        assertEquals("bar", record.get("bar"));
        assertEquals("baz", record.get("baz"));
        assertNull(record.get("invalid"));
        assertSame(mapping, record.mapping);
    }

    @Test
    public void testCSVRecordIterator() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("foo");
        expectedList.add("bar");
        expectedList.add("baz");
        Iterator<String> expectedIterator = expectedList.iterator();
        Iterator<String> recordIterator = record.iterator();
        assertTrue(recordIterator.hasNext());
        assertEquals("foo", recordIterator.next());
        assertTrue(recordIterator.hasNext());
        assertEquals("bar", recordIterator.next());
        assertTrue(recordIterator.hasNext());
        assertEquals("baz", recordIterator.next());
        assertFalse(recordIterator.hasNext());
        assertSame(expectedIterator, recordIterator);
    }

    @Test
    public void testCSVRecordPutInMap() {
        String[] values = {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> resultMap = record.putIn(new HashMap<String, String>());
        List<String> expectedList = new ArrayList<>();
        expectedList.add("foo");
        expectedList.add("bar");
        expectedList.add("baz");
        Iterator<Entry<String, String>> expectedMapIterator = expectedList.stream().map(s -> new AbstractMap.SimpleEntry<>(s, s)).iterator();
        Iterator<Entry<String, String>> resultMapIterator = resultMap.entrySet().iterator();
        assertTrue(resultMap.isEmpty());
        assertTrue(resultMapIterator.hasNext());
        Map.Entry<String, String> nextResultMapEntry = resultMapIterator.next();
        assertEquals("foo", nextResultMapEntry.getKey());
        assertEquals("foo", nextResultMapEntry.getValue());
        assertTrue(resultMapIterator.hasNext());
        Map.Entry<String, String> nextResultMapEntry2 = resultMapIterator.next();
        assertEquals("bar", nextResultMapEntry2.getKey());
        assertEquals("bar", nextResultMapEntry2.getValue());
        assertTrue(resultMapIterator.hasNext());
        Map.Entry<String, String> nextResultMapEntry3 = resultMapIterator.next();
        assertEquals("baz", nextResultMapEntry3.getKey());
        assertEquals("baz", nextResultMapEntry3.getValue());
        assertFalse(resultMapIterator.hasNext());
        assertSame(expectedMapIterator, resultMapIterator);
    }
}