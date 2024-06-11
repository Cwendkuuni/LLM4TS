package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Tests for the {@link CSVRecord} class
 */
public class CSVRecordTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testCSVRecordConstruction() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    /**
     * Test get method
     */
    @org.junit.Test
    public void testGetMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("value1", record.get(0));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get(1));
        assertEquals("value2", record.get("header2"));
        assertEquals(null, record.get("nonExistentHeader"));
        try {
            record.get(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test get method with null comment
     */
    @org.junit.Test
    public void testGetWithNullComment() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(null, record.getComment());
    }

    /**
     * Test get method with comment
     */
    @org.junit.Test
    public void testGetWithComment() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
        assertEquals("comment", record.getComment());
    }

    /**
     * Test isConsistent method
     */
    @org.junit.Test
    public void testIsConsistentMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        values = new String[]{"value1", "value2", "value3"};
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
        values = new String[]{"value1", "value2"};
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        CSVRecord record2 = new CSVRecord(null, mapping, null, 1);
        assertFalse(record2.isConsistent());
    }

    /**
     * Test isMapped method
     */
    @org.junit.Test
    public void testIsMappedMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test for an existing header
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));

        // Test for a non-existing header
        assertFalse(record.isMapped("nonExistentHeader"));

        // Test for a null header mapping
        CSVRecord recordNullMapping = new CSVRecord(values, null, null, 1);
        assertFalse(recordNullMapping.isMapped("header1"));
    }

    /**
     * Test isSet method
     */
    @org.junit.Test
    public void testIsSetMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test for an existing and set header
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));

        // Test for an existing but not set header
        String[] valuesNotSet = new String[]{"value1", null};
        CSVRecord recordNotSet = new CSVRecord(valuesNotSet, mapping, null, 1);
        assertFalse(recordNotSet.isSet("header2"));

        // Test for a non-existing header
        assertFalse(record.isSet("nonExistentHeader"));

        // Test for a null header mapping
        CSVRecord recordNullMapping = new CSVRecord(values, null, null, 1);
        assertFalse(recordNullMapping.isSet("header1"));
    }

    /**
     * Test iterator method
     */
    @org.junit.Test
    public void testIteratorMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> expectedList = Arrays.asList("value1", "value2");
        Iterator<String> iterator = record.iterator();
        List<String> actualList = Arrays.asList(iterator.next(), iterator.next());
        assertEquals(expectedList, actualList);
    }

    /**
     * Test putIn method
     */
    @org.junit.Test
    public void testPutInMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Map<String, String> actualMap = record.putIn(new HashMap<String, String>());
        assertEquals(expectedMap, actualMap);
    }

    /**
     * Test size method
     */
    @org.junit.Test
    public void testSizeMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(2, record.size());
    }

    /**
     * Test toList method
     */
    @org.junit.Test
    public void testToListMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> expectedList = Arrays.asList("value1", "value2");
        List<String> actualList = record.toList();
        assertEquals(expectedList, actualList);
    }

    /**
     * Test toMap method
     */
    @org.junit.Test
    public void testToMapMethod() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");
        Map<String, String> actualMap = record.toMap();
        assertEquals(expectedMap, actualMap);
    }
}