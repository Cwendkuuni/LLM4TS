package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCtor() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
    }

    @Test
    public void testGetNullValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);

        assertNull(record.get(null));
        assertNull(record.get(-1));
        assertNull(record.get(""));
        assertNull(record.get("X"));
    }

    @Test
    public void testGetValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = "comment";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("baz", record.get(2));

        assertEquals("foo", record.get("A"));
        assertEquals("bar", record.get("B"));
        assertEquals("baz", record.get("C"));

        assertEquals("comment", record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testGetNonExistingValue() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = "comment";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertThrows(IllegalArgumentException.class, () -> record.get("X"));
    }

    @Test
    public void testGetConsistentRecord() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = "comment";
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testGetInconsistentRecord() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));
        assertFalse(record.isMapped("X"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertTrue(record.isSet("C"));
        assertFalse(record.isSet("X"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Iterator<String> iterator = record.iterator();

        List<String> expected = Arrays.asList(values);

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<String, String>(){{
            put("A", "valueA");
            put("B", "valueB");
            put("C", "valueC");
        }};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> result = record.putIn(map);

        assertEquals(map.size(), result.size());

        for (Entry<String, Integer> entry : record.mapping.entrySet()) {
            String key = entry.getKey();
            int index = entry.getValue().intValue();

            assertEquals(key, result.get(key));
        }

        assertNull(result.get("X"));
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};
        String comment = null;
        long recordNumber = 1;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(3, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"foo", "bar", "baz"};

        List<String> list = new CSVRecord(values, null, null, 1).toList();

        assertEquals(Arrays.asList(values), list);
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("A", 0);
            put("B", 1);
            put("C", 2);
        }};

        Map<String, String> map = new CSVRecord(values, mapping, null, 1).toMap();

        assertEquals(mapping.size(), map.size());

        for (Entry<String, Integer> entry : mapping.entrySet()) {
            String key = entry.getKey();
            int index = entry.getValue().intValue();

            assertEquals(key, map.get(key));
        }
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[foo, bar, baz]", record.toString());
    }
}