package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1, record.size());
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));
        assertNull(record.getComment());
        assertFalse(record.isConsistent());

        record = new CSVRecord(values, mapping, "comment", 1L);
        assertEquals("comment", record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetByEnum() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Enum<String> fooEnum = new Enum<String>("FOO") {
            public String toString() {
                return "foo";
            }
        };
        assertEquals("foo", record.get(fooEnum));

        Enum<String> barEnum = new Enum<String>("BAR") {
            public String toString() {
                return "bar";
            }
        };
        assertEquals("bar", record.get(barEnum));

        Enum<String> bazEnum = new Enum<String>("BAZ") {
            public String toString() {
                return "baz";
            }
        };
        assertEquals("baz", record.get(bazEnum));

        Enum<String> unknownEnum = new Enum<String>("UNKNOWN") {
            public String toString() {
                return "unknown";
            }
        };
        assertNull(record.get(unknownEnum));
    }

    @Test
    public void testGetByIndex() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertNull(record.get(0));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("foo", record.get("foo"));
        assertEquals("bar", record.get("bar"));
        assertEquals("baz", record.get("baz"));

        Map<String, Integer> emptyMapping = new HashMap<>();
        record = new CSVRecord(values, emptyMapping, null, 1L);
        try {
            record.get("unknown");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetComment() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);

        assertEquals("comment", record.getComment());

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals(1L, record.getRecordNumber());

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isConsistent());

        String[] valuesWithMismatch = new String[] { "foo", "bar", "baz", "quux" };
        record = new CSVRecord(valuesWithMismatch, mapping, null, 1L);
        assertFalse(record.isConsistent());

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isMapped("foo"));
        assertTrue(record.isMapped("bar"));
        assertTrue(record.isMapped("baz"));

        String[] valuesWithMissingColumn = new String[] { "foo", "bar" };
        record = new CSVRecord(valuesWithMissingColumn, mapping, null, 1L);
        assertFalse(record.isMapped("baz"));

        Map<String, Integer> emptyMapping = new HashMap<>();
        record = new CSVRecord(values, emptyMapping, null, 1L);
        assertFalse(record.isMapped("foo"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("foo"));
        assertTrue(record.isSet("bar"));
        assertTrue(record.isSet("baz"));

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertFalse(record.isSet("foo"));

        String[] valuesWithMissingColumn = new String[] { "foo", "bar" };
        record = new CSVRecord(valuesWithMissingColumn, mapping, null, 1L);
        assertFalse(record.isSet("baz"));

        Map<String, Integer> mappingWithMissingColumn = new HashMap<>();
        mappingWithMissingColumn.put("foo", 0);
        mappingWithMissingColumn.put("bar", 1);
        record = new CSVRecord(values, mappingWithMissingColumn, null, 1L);
        assertFalse(record.isSet("baz"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        List<String> expectedList = Arrays.asList(values);
        Iterator<String> expectedIterator = expectedList.iterator();

        Iterator<String> recordIterator = record.iterator();

        assertEquals(expectedIterator.next(), recordIterator.next());
        assertEquals(expectedIterator.next(), recordIterator.next());
        assertEquals(expectedIterator.next(), recordIterator.next());
        assertFalse(recordIterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, String> targetMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        Map<String, String> resultMap = record.putIn(targetMap);

        assertEquals(values.length, resultMap.size());
        assertTrue(resultMap.containsKey("foo"));
        assertTrue(resultMap.containsKey("bar"));
        assertTrue(resultMap.containsKey("baz"));
        assertEquals("foo", resultMap.get("foo"));
        assertEquals("bar", resultMap.get("bar"));
        assertEquals("baz", resultMap.get("baz"));
    }

    @Test
    public void testSize() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        assertEquals(values.length, record.size());

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, null, null, 1L);
        assertEquals(0, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        List<String> expectedList = Arrays.asList(values);
        List<String> recordList = record.toList();

        assertEquals(expectedList, recordList);
    }

    @Test
    public void testToMap() {
        String[] values = new String[] { "foo", "bar", "baz" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("foo", 0);
        mapping.put("bar", 1);
        mapping.put("baz", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("foo", "foo");
        expectedMap.put("bar", "bar");
        expectedMap.put("baz", "baz");

        Map<String, String> resultMap = record.toMap();

        assertEquals(expectedMap, resultMap);
    }

    @Test
    public void testToString() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        String expectedString = "[foo, bar, baz]";
        String recordString = record.toString();

        assertEquals(expectedString, recordString);
    }

    @Test
    public void testValues() {
        String[] values = new String[] { "foo", "bar", "baz" };
        CSVRecord record = new CSVRecord(values, null, null, 1L);

        String[] expectedValues = values;
        String[] recordValues = record.values();

        assertArrayEquals(expectedValues, recordValues);
    }
}