package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testCSVRecordCreation() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals(2, record.size());
        assertArrayEquals(new String[]{"foo", "bar"}, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(true, record.isConsistent());
        assertFalse(record.isMapped("nonExistentHeader"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testCSVRecordCreationWithNullValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(null, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(true, record.isConsistent());
        assertFalse(record.isMapped("nonExistentHeader"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testCSVRecordCreationWithEmptyMapping() {
        String[] values = new String[]{"foo", "bar"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals(2, record.size());
        assertArrayEquals(new String[]{"foo", "bar"}, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertEquals(true, record.isConsistent());
        assertFalse(record.isMapped("nonExistentHeader"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testGetByEnum() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get(Enum.ONE));
        assertEquals("bar", record.get(Enum.TWO));

        assertThrows(IllegalArgumentException.class, () -> record.get(Enum.THREE));
    }

    @Test
    public void testGetByIndex() {
        String[] values = new String[]{"foo", "bar"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("foo", record.get(0));
        assertEquals("bar", record.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(2));
    }

    @Test
    public void testGetByName() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("foo", record.get("header1"));
        assertEquals("bar", record.get("header2"));

        assertThrows(IllegalArgumentException.class, () -> record.get("nonExistentHeader"));
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment";
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals("This is a comment", record.getComment());
        assertNull(new CSVRecord(values, mapping, null, 1).getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
        assertFalse(new CSVRecord(new String[0], null, null, 1).isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        String[] valuesWithThreeColumns = new String[]{"foo", "bar", "baz"};
        CSVRecord recordWithThreeColumns = new CSVRecord(valuesWithThreeColumns, mapping, null, 1);

        assertFalse(recordWithThreeColumns.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("nonExistentHeader"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[]{"foo", "bar"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expectedList = Arrays.asList(values);

        Iterator<String> iterator = record.iterator();

        assertEquals(values[0], iterator.next());
        assertEquals(values[1], iterator.next());
        assertFalse(iterator.hasNext());

        List<String> actualList = Arrays.asList(record);

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testPutIn() {
        Map<String, String> targetMap = new HashMap<>();
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Map<String, String> actualMap = record.putIn(targetMap);

        assertEquals(2, targetMap.size());
        assertEquals("foo", targetMap.get("header1"));
        assertEquals("bar", targetMap.get("header2"));
    }

    @Test
    public void testSize() {
        String[] values = new String[]{"foo", "bar"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(2, record.size());
    }

    @Test
    public void testToString() {
        String[] values = new String[]{"foo", "bar"};

        CSVRecord record = new CSVRecord(values, null, null, 1);

        String expectedString = "[foo, bar]";
        String actualString = record.toString();

        assertEquals(expectedString, actualString);
    }
}