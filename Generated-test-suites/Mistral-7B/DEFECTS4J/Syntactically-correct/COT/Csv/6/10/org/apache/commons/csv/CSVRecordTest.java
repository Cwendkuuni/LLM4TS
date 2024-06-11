package org.apache.commons.csv.test;

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

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1L, record.getRecordNumber());
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isMapped("Header2"));
        assertFalse(record.isMapped("NonexistentHeader"));
        assertFalse(record.isSet("NonexistentHeader"));
        assertFalse(record.isSet("Header1"));
        assertFalse(record.isSet("Header2"));
    }

    /**
     * Test get method with Enum
     */
    @org.junit.Test
    public void testGetEnum() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("Value1", record.get(Enum.ONE));
        assertEquals("Value2", record.get(Enum.TWO));
    }

    /**
     * Test get method with int
     */
    @org.junit.Test
    public void testGetInt() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));
    }

    /**
     * Test get method with String
     */
    @org.junit.Test
    public void testGetString() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("Value1", record.get("Header1"));
        assertEquals("Value2", record.get("Header2"));
        assertNull(record.get("NonexistentHeader"));
        assertThrows(IllegalArgumentException.class, () -> record.get("NonexistentHeader"));
    }

    /**
     * Test getComment method
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1L);
        assertEquals("Comment", record.getComment());
        assertNull(new CSVRecord(values, mapping, null, 1L).getComment());
    }

    /**
     * Test getRecordNumber method
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1L);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 2L);
        assertEquals(1L, record1.getRecordNumber());
        assertEquals(2L, record2.getRecordNumber());
    }

    /**
     * Test isConsistent method
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record1 = new CSVRecord(values, mapping, null, 1L);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 1L);
        CSVRecord record3 = new CSVRecord(new String[0], mapping, null, 1L);
        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());
        assertFalse(record3.isConsistent());
        assertFalse(new CSVRecord(values, mapping, null, 1L).isConsistent());
    }

    /**
     * Test isMapped method
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isMapped("Header2"));
        assertFalse(record.isMapped("NonexistentHeader"));
    }

    /**
     * Test isSet method
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isSet("Header1"));
        assertTrue(record.isSet("Header2"));
        assertTrue(record.isSet("Header3"));
        assertFalse(record.isSet("NonexistentHeader"));
    }

    /**
     * Test iterator method
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"Value1", "Value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expectedList = Arrays.asList("Value1", "Value2");
        List<String> actualList = Arrays.asList(record.iterator().toArray(new String[0]));
        assertArrayEquals(expectedList.toArray(new String[0]), actualList.toArray());
    }

    /**
     * Test putIn method
     */
    @org.junit.Test
    public void testPutIn() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        Map<String, String> map = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        map.putAll(record.putIn(map));
        List<String> expectedList = Arrays.asList("Value1", "Value2", "Value3");
        List<String> actualList = Arrays.asList(map.values().toArray(new String[0]));
        assertArrayEquals(expectedList.toArray(new String[0]), actualList.toArray());
    }

    /**
     * Test size method
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(3, record.size());
    }

    /**
     * Test toList method
     */
    @org.junit.Test
    public void testToList() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expectedList = Arrays.asList("Value1", "Value2", "Value3");
        List<String> actualList = record.toList();
        assertArrayEquals(expectedList.toArray(new String[0]), actualList.toArray());
    }

    /**
     * Test toMap method
     */
    @org.junit.Test
    public void testToMap() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "Value1");
        expectedMap.put("Header2", "Value2");
        expectedMap.put("Header3", "Value3");
        Map<String, String> actualMap = new CSVRecord(values, mapping, null, 1L).toMap();
        assertEquals(expectedMap, actualMap);
    }

    /**
     * Test toString method
     */
    @org.junit.Test
    public void testToString() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String expectedString = "[Value1, Value2, Value3]";
        String actualString = record.toString();
        assertEquals(expectedString, actualString);
    }

    /**
     * Test values method
     */
    @org.junit.Test
    public void testValues() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        mapping.put("Header3", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String[] expectedValues = {"Value1", "Value2", "Value3"};
        String[] actualValues = record.values();
        assertArrayEquals(expectedValues, actualValues);
    }

    /**
     * Test edge cases
     */
    @org.junit.Test
    public void testEdgeCases() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        String[] values = new String[0];
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("Header1"));
        assertFalse(record.isMapped("Header2"));
        assertFalse(record.isSet("Header1"));
        assertFalse(record.isSet("Header2"));
        assertFalse(record.isSet("NonexistentHeader"));
        assertFalse(record.iterator().hasNext());
        assertNull(record.getComment());
        assertNull(record.get("Header1"));
        assertNull(record.get("Header2"));
        assertNull(record.get("NonexistentHeader"));
        assertThrows(IllegalStateException.class, () -> record.getRecordNumber());
        assertThrows(IllegalArgumentException.class, () -> record.get(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
    }

    /**
     * Test exception handling
     */
    @org.junit.Test
    public void testExceptionHandling() {
        String[] values = new String[]{"Value1", "Value2", "Value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertThrows(IllegalArgumentException.class, () -> record.get(-1));
        assertThrows(IllegalArgumentException.class, () -> record.get("NonexistentHeader"));
        assertThrows(IllegalStateException.class, () -> record.getRecordNumber());
    }
}

class Enum {
    static final int ONE = 0;
    static final int TWO = 1;
}