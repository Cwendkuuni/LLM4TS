package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordWithValuesAndMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertEquals("foo", record.get("name1"));
        assertEquals("bar", record.get("name2"));
        assertEquals("baz", record.get("name3"));
        assertArrayEquals(values, record.values());
        assertEquals(3, record.size());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistentName"));
        assertFalse(record.isSet("nonExistentName"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testCSVRecordWithoutMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 0L);

        assertEquals(3, record.size());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("name1"));
        assertFalse(record.isMapped("name2"));
        assertFalse(record.isMapped("name3"));
        assertNull(record.getComment());
        assertNull(record.getRecordNumber());
    }

    @Test
    public void testGetWithNegativeIndex() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    public void testGetWithIndexOutOfBounds() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    public void testGetWithNull() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertThrows(IllegalArgumentException.class, () -> record.get(null));
    }

    @Test
    public void testGetWithNullMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        assertThrows(IllegalStateException.class, () -> record.get("name1"));
    }

    @Test
    public void testGetWithInvalidMapping() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertThrows(IllegalArgumentException.class, () -> record.get("name3"));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment";
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, comment, 0L);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 123456L;
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 3);

        CSVRecord record2 = new CSVRecord(values, mapping, null, 0L);

        assertFalse(record2.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistentName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("nonExistentName"));
    }

    @Test
    public void testPutIn() {
        Map<String, String> map = new HashMap<>();
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "foo");
        expectedMap.put("name2", "bar");
        expectedMap.put("name3", "baz");

        map = record.putIn(map);

        assertEquals(expectedMap, map);
    }

    @Test
    public void testToString() {
        String[] values = new String[] {"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertEquals("[foo, bar, baz]", record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[] {"foo", "bar", "baz"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        assertEquals(values, record.values());
    }
}