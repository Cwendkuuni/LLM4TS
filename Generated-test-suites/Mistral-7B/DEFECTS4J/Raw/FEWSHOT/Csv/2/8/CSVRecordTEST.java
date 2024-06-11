package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testNewCSVRecord() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("nonExistingName"));
        assertSame(Arrays.asList(record.values()), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[0], record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("nonExistingName"));
        assertFalse(record.isSet("nonExistingName"));
        assertSame(Collections.emptyList(), record.iterator());
    }

    @Test
    public void testNewCSVRecordWithEmptyMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
        assertNull(record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("nonExistingName"));
        assertFalse(record.isSet("nonExistingName"));
        assertSame(Arrays.asList(record.values()), record.iterator());
    }

    @Test
    public void testGetValueByNameWithEmptyMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertNull(record.get("nonExistingName"));
    }

    @Test
    public void testGetValueByNameWithMappedValues() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertSame("foo", record.get("name1"));
        assertSame("bar", record.get("name2"));
        assertSame("baz", record.get("name3"));
        assertNull(record.get("nonExistingName"));
    }

    @Test
    public void testGetValueByNameWithNullMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertNull(record.get("nonExistingName"));
    }

    @Test
    public void testGetValueByNameWithInconsistentRecord() {
        String[] values = new String[]{"foo", "bar"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.get("name3"));
        assertFalse(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueByNameWithNoMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("nonExistingName");
    }

    @Test
    public void testIsConsistentWithEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithEmptyMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMismatchedValuesAndMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMismatchedMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsConsistentWithMatchingValuesAndMapping() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("nonExistingName"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[]{"foo", "bar", "baz"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name1", 0);
        mapping.put("name2", 1);
        mapping.put("name3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("nonExistingName"));

        String[] valuesWithMissingValue = new String[]{"foo", "bar"};
        CSVRecord recordWithMissingValue = new CSVRecord(valuesWithMissingValue, mapping, null, 1);
        assertFalse(recordWithMissingValue.isSet("name3"));
    }

    @Test
    public void testGetIterator() {
        String[] values = new String[]{"foo", "bar", "baz"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        List<String> expectedList = Arrays.asList(values);
        Iterator<String> expectedIterator = expectedList.iterator();

        Iterator<String> actualIterator = record.iterator();

        assertEquals(expectedIterator, actualIterator);
    }
}