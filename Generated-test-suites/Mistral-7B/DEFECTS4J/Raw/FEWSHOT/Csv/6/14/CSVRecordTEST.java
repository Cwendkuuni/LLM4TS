package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordIsSerializable() {
        CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
        CSVRecord deserializedRecord = (CSVRecord) new ObjectInputStream(new ByteArrayInputStream(
                new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(record))).readObject();
        assertEquals(record, deserializedRecord);
    }

    @Test
    public void testGet() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals("qux", record.get(2));
        assertEquals("qux", record.get("2"));
        assertEquals("qux", record.get("header2"));

        assertThrows(IllegalArgumentException.class, () -> record.get("nonExistentHeader"));
    }

    @Test
    public void testGetReturnsNullIfNullMapping() {
        String[] values = new String[]{"foo", "bar", "qux"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertNull(record.get(null));
        assertNull(record.get(""));
        assertNull(record.get("nonExistentHeader"));
    }

    @Test
    public void testGetThrowsIllegalArgumentExceptionForInvalidIndex() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertThrows(IllegalArgumentException.class, () -> record.get(-1));
        assertThrows(IllegalArgumentException.class, () -> record.get(values.length));
        assertThrows(IllegalArgumentException.class, () -> record.get("nonExistentHeader"));
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
        }};
        String comment = "This is a comment.";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
        }};
        String comment = "This is a comment.";
        long recordNumber = 1;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());

        mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 2);
        }};
        record = new CSVRecord(values, mapping, null, 1);

        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("nonExistentHeader"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("nonExistentHeader"));

        String[] emptyValues = new String[0];
        record = new CSVRecord(emptyValues, mapping, null, 1);

        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header2"));
        assertFalse(record.isSet("header3"));
    }

    @Test
    public void testIteratorReturnsCorrectValues() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList("foo", "bar", "qux");
        List<String> actualList = Arrays.stream(record.values()).boxed().collect(Collectors.toList());

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, String> targetMap = new HashMap<String, String>(){{
            put("header1", "");
            put("header2", "");
            put("header3", "");
        }};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Map<String, String> actualMap = record.putIn(targetMap);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], targetMap.get("header" + (i + 1)));
        }

        assertEquals(values.length, actualMap.size());
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> expectedList = Arrays.asList("foo", "bar", "qux");
        List<String> actualList = record.toList();

        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testToMap() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> expectedMap = new HashMap<String, String>(){{
            put("header1", "foo");
            put("header2", "bar");
            put("header3", "qux");
        }};
        Map<String, String> actualMap = record.toMap();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expectedString = "[foo, bar, qux]";
        String actualString = record.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testValues() {
        String[] values = new String[]{"foo", "bar", "qux"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String[] expectedValues = {"foo", "bar", "qux"};
        String[] actualValues = record.values();

        assertArrayEquals(expectedValues, actualValues);
    }
}