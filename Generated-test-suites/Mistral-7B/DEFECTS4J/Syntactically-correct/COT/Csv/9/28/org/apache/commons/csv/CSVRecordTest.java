package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordTest;
import org.junit.Test;

/**
 * Test for CSVRecord
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1, record.getRecordNumber());
        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);

        record = new CSVRecord(null, null, "comment", 1L);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.values());
        assertNull(record.mapping);
        assertEquals("comment", record.getComment());

        record = new CSVRecord(null, null, null, -1L);
        assertNull(record);
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // test get by Enum
        assertEquals("A1", record.get(CSVRecordTest.Header1.class));

        // test get by index
        assertEquals("A1", record.get(0));

        // test get by name
        assertEquals("B1", record.get("header2"));

        // test get with no header mapping
        CSVRecord recordNoMapping = new CSVRecord(values, null, null, 1L);
        assertThrows(IllegalArgumentException.class, () -> recordNoMapping.get("header1"));

        // test get with inconsistent record
        Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("header1", 0);
        mappingInconsistent.put("header2", 1);
        mappingInconsistent.put("header3", 4);
        CSVRecord recordInconsistent = new CSVRecord(values, mappingInconsistent, null, 1L);
        assertThrows(IllegalArgumentException.class, () -> recordInconsistent.get("header3"));
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record1 = new CSVRecord(values, mapping, "comment1", 1L);
        assertEquals("comment1", record1.getComment());

        CSVRecord record2 = new CSVRecord(values, mapping, null, 1L);
        assertNull(record2.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();

        CSVRecord record1 = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1, record1.getRecordNumber());

        CSVRecord record2 = new CSVRecord(values, mapping, null, 2L);
        assertEquals(2, record2.getRecordNumber());
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord recordConsistent = new CSVRecord(values, mapping, null, 1L);
        assertTrue(recordConsistent.isConsistent());

        Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("header1", 0);
        mappingInconsistent.put("header2", 1);
        mappingInconsistent.put("header3", 4);
        CSVRecord recordInconsistent = new CSVRecord(values, mappingInconsistent, null, 1L);
        assertFalse(recordInconsistent.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // test isMapped with existing header
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));

        // test isMapped with non-existent header
        assertFalse(record.isMapped("nonExistentHeader"));

        // test isMapped with null mapping
        CSVRecord recordNoMapping = new CSVRecord(values, null, null, 1L);
        assertFalse(recordNoMapping.isMapped("header1"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // test isSet with existing and set header
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));

        // test isSet with existing but not set header
        CSVRecord recordNotSet = new CSVRecord(new String[0], mapping, null, 1L);
        assertFalse(recordNotSet.isSet("header1"));

        // test isSet with non-existent header
        assertFalse(record.isSet("nonExistentHeader"));

        // test isSet with null mapping
        CSVRecord recordNoMapping = new CSVRecord(values, null, null, 1L);
        assertFalse(recordNoMapping.isSet("header1"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        List<String> expected = Arrays.asList("A1", "B1", "C1");
        Iterator<String> iterator = record.iterator();
        for (String value : expected) {
            assertEquals(value, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "A1");
        expected.put("header2", "B1");
        expected.put("header3", "C1");

        CSVRecord record = new CSVRecord(values, null, null, 1L);
        Map<String, String> map = record.putIn(new HashMap<String, String>(values.length));
        assertEquals(expected, map);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(3, record.size());
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expected = Arrays.asList("A1", "B1", "C1");
        assertEquals(expected, record.toList());
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Map<String, String> expected = new HashMap<>();
        expected.put("header1", "A1");
        expected.put("header2", "B1");
        expected.put("header3", "C1");
        assertEquals(expected, record.toMap());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("[A1, B1, C1]", record.toString());
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] values = new String[]{"A1", "B1", "C1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String[] expected = {"A1", "B1", "C1"};
        assertSame(expected, record.values());
    }

    /**
     * Test Enum class.
     */
    public enum Header1 {
        HEADER1
    }
}