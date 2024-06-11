package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCreation() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1L, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
    }

    @Test
    public void testGet() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertEquals(null, record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("foo", record.get("name1"));
        assertEquals("bar", record.get("name2"));
        assertNull(record.get("nonExistentName"));
    }

    @Test
    public void testGetEnum() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("foo", record.get(Enum.ONE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByNameIfNoMapping() {
        String[] values = new String[]{"foo", "bar"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("name");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetByNameIfNoMappingSpecified() {
        String[] values = new String[]{"foo", "bar"};
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.get("name");
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMismatchedSize() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2", "name3").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("name1"));
        assertFalse(record.isMapped("nonExistentName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isSet("name1"));
        assertFalse(record.isSet("nonExistentName"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expected = Arrays.asList("foo", "bar");
        Iterator<String> iterator = record.iterator();
        List<String> actual = Arrays.asList(iterator.next(), iterator.next());
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testPutInMap() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        record.putIn(map);
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        List<String> actual = map.stream().map(Entry::getValue).collect(Collectors.toList());
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testGetComment() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        long recordNumber = 123L;
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("[foo, bar]", record.toString());
    }

    @Test
    public void testArrayAccess() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testArrayAccessOutOfBounds() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = Arrays.asList("name1", "name2").stream().collect(Collectors.toMap(
                String.class, i -> Integer.valueOf(i)));
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        record.get(2);
    }

}