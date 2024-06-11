package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    @Test
    public void testEmptyCSVRecord() {
        CSVRecord record = new CSVRecord(EMPTY_STRING_ARRAY, null, null, 0);
        assertEquals(0, record.size());
        assertNull(record.getComment());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testSimpleCSVRecord() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(3, record.size());
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownColumn() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("UnknownColumn");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetColumnBeforeInitialization() {
        String[] values = new String[] { "Name", "Age", "City" };
        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Name");
    }

    @Test
    public void testGetComment() {
        String comment = "This is a comment for the record";
        CSVRecord record = new CSVRecord(EMPTY_STRING_ARRAY, null, comment, 1);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(EMPTY_STRING_ARRAY, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
    }

    @Test
    public void testIsSet() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
    }

    @Test
    public void testIterator() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> valuesList = record.toList();
        Iterator<String> iterator = record.iterator();
        assertEquals(valuesList.get(0), iterator.next());
        assertEquals(valuesList.get(1), iterator.next());
        assertEquals(valuesList.get(2), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPutIn() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, String> targetMap = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> resultMap = record.putIn(targetMap);
        assertEquals(3, resultMap.size());
        assertEquals("Name", resultMap.get("Name"));
        assertEquals("Age", resultMap.get("Age"));
        assertEquals("City", resultMap.get("City"));
    }

    @Test
    public void testToString() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String expectedToString = "[Name, Age, City]";
        assertEquals(expectedToString, record.toString());
    }

    @Test
    public void testValues() {
        String[] values = new String[] { "Name", "Age", "City" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String[] resultValues = record.values();
        assertEquals(3, resultValues.length);
        assertEquals("Name", resultValues[0]);
        assertEquals("Age", resultValues[1]);
        assertEquals("City", resultValues[2]);
    }
}