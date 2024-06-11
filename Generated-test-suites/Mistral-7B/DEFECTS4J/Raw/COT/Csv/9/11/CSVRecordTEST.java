package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testCSVRecord() {
        String[] values = {"apple", "10", "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Quantity", 1);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertEquals(3, record.size());
        assertEquals("apple", record.get(0));
        assertEquals("10", record.get(1));
        assertEquals("red", record.get(2));
        assertEquals(null, record.getComment());
    }

    /**
     * Test get method.
     */
    @org.junit.Test
    public void testGet() {
        String[] values = {"apple", "10", "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Quantity", 1);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // Test getting by enum
        assertEquals("apple", record.get(CSVRecord.class.getField("Fruit")));

        // Test getting by index
        assertEquals("apple", record.get(0));
        assertEquals("10", record.get(1));
        assertEquals("red", record.get(2));

        // Test getting by name
        assertEquals("apple", record.get("Fruit"));
        assertEquals("10", record.get("Quantity"));
        assertEquals("red", record.get("Color"));

        // Test getting comment
        assertNull(record.getComment());

        // Test getting record number
        assertEquals(1, record.getRecordNumber());

        // Test isConsistent
        assertTrue(record.isConsistent());

        // Test isMapped
        assertTrue(record.isMapped("Fruit"));
        assertTrue(record.isMapped("Quantity"));
        assertTrue(record.isMapped("Color"));
        assertFalse(record.isMapped("Invalid"));

        // Test isSet
        assertTrue(record.isSet("Fruit"));
        assertTrue(record.isSet("Quantity"));
        assertFalse(record.isSet("Invalid"));

        // Test iterator
        List<String> list = record.toList();
        Iterator<String> iterator = record.iterator();
        for (int i = 0; i < 3; i++) {
            assertEquals(values[i], iterator.next());
            assertEquals(values[i], list.get(i));
        }

        // Test putIn
        Map<String, String> map = record.putIn(new HashMap<String, String>());
        assertEquals(3, map.size());
        assertEquals("apple", map.get("Fruit"));
        assertEquals("10", map.get("Quantity"));
        assertEquals("red", map.get("Color"));

        // Test size
        assertEquals(3, record.size());

        // Test toList
        List<String> list2 = record.toList();
        assertEquals(Arrays.toString(values), list2.toString());

        // Test toMap
        Map<String, String> map2 = record.toMap();
        assertEquals(map.size(), map2.size());
        assertEquals("apple", map2.get("Fruit"));
        assertEquals("10", map2.get("Quantity"));
        assertEquals("red", map2.get("Color"));

        // Test toString
        assertEquals("[apple, 10, red]", record.toString());
    }

    /**
     * Test null values.
     */
    @org.junit.Test
     public void testNullValues() {
        String[] values = {"apple", null, "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(null, record.get("Quantity"));
        assertNull(record.getComment());
    }

    /**
     * Test empty record.
     */
    @ org.junit.Test
    public void testEmptyRecord() {
        CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, null, null, 1);
        assertEquals(0, record.size());
    }

    /**
     * Test get method with invalid index.
     */
    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        String[] values = {"apple", "10", "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Quantity", 1);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(3);
    }

    /**
     * Test get method with null mapping.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetWithNullMapping() {
        String[] values = {"apple", "10", "red"};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.get("Fruit");
    }

    /**
     * Test get method with invalid column name.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetInvalidColumnName() {
        String[] values = {"apple", "10", "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Quantity", 1);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Invalid");
    }

    /**
     * Test isConsistent method with mismatched size.
     */
    @org.junit.Test
    public void testIsConsistentMismatchedSize() {
        String[] values = {"apple", "10", "red"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        mapping.put("Quantity", 1);
        mapping.put("Color", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        mapping.put("Invalid", 3);
        assertFalse(record.isConsistent());
    }
}