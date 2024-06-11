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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord class
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}
     */
    @Test
    public void testCSVRecord() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values.length, record.size());
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertFalse(record.isConsistent());
        assertEquals(0, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)}
     */
    @Test
    public void testGetEnum() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get(Enum.NAME));
    }

    /**
     * Test method for {@link CSVRecord#get(int)}
     */
    @Test
    public void testGetInt() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}
     */
    @Test
    public void testGetString() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertNull(record.get("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#getComment()}
     */
    @Test
    public void testGetComment() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);
        assertEquals("Comment", record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}
     */
    @Test
    public void testIsConsistent() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertFalse(record.isConsistent());

        record = new CSVRecord(values, mapping, null, 0);
        mapping.put("Unknown", 3);
        record.mapping = mapping;
        assertFalse(record.isConsistent());

        record = new CSVRecord(values, mapping, null, 0);
        record.values = new String[values.length + 1];
        assertFalse(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}
     */
    @Test
    public void testIsMapped() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}
     */
    @Test
    public void testIsSet() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}
     */
    @Test
    public void testIterator() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Iterator<String> iterator = record.iterator();
        List<String> expectedValues = Arrays.asList(values);
        List<String> actualValues = Arrays.asList(iterator.toArray(new String[0]));
        assertArrayEquals(expectedValues.toArray(), actualValues.toArray());
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)}
     */
    @Test
    public void testPutIn() {
        String[] values = {"John", "Doe", "30"};
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, null, null, 0);
        map = record.putIn(map);
        assertEquals("John", map.get("Name"));
        assertEquals("Doe", map.get("Doe"));
        assertEquals("30", map.get("Age"));
        assertEquals(3, map.size());
    }

    /**
     * Test method for {@link CSVRecord#size()}
     */
    @Test
    public void testSize() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals(3, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toList()}
     */
    @Test
    public void testToList() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        List<String> expectedValues = Arrays.asList(values);
        List<String> actualValues = record.toList();
        assertArrayEquals(expectedValues.toArray(), actualValues.toArray());
    }

    /**
     * Test method for {@link CSVRecord#toMap()}
     */
    @Test
    public void testToMap() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "30");
        Map<String, String> actualMap = record.toMap();
        assertEquals(expectedMap, actualMap);
    }

    /**
     * Test method for {@link CSVRecord#toString()}
     */
    @Test
    public void testToString() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("[John, Doe, 30]", record.toString());
    }

    /**
     * Test method for {@link CSVRecord#values()}
     */
    @Test
    public void testValues() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertArrayEquals(values, record.values());
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with null mapping
     */
    @Test(expected = IllegalStateException.class)
    public void testGetStringNoMapping() {
        String[] values = {"John", "Doe", "30"};
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("Name");
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with invalid index
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidIndex() {
        String[] values = {"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("Unknown");
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with inconsistent record
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInconsistent() {
        String[] values = {"John", "Doe", "30", "unknown"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("Unknown");
    }
}