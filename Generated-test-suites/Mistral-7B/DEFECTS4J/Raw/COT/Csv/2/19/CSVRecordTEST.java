package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} .
     */
    @Test
    public void testCSVRecord() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#get(int)} .
     */
    @Test
    public void testGet() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", record.get(0));
        assertEquals("Age", record.get(1));
        assertEquals("City", record.get(2));
        assertNull(record.get(3));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} .
     */
    @Test
    public void testGetString() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", record.get("Name"));
        assertEquals("Age", record.get("Age"));
        assertEquals("City", record.get("City"));
        assertNull(record.get("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} .
     */
    @Test
    public void testIsConsistent() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] valuesInconsistent = {"Name", "Age", "City", "Unknown"};
        Map<String, Integer> mappingInconsistent = new HashMap<>();
        mappingInconsistent.put("Name", 0);
        mappingInconsistent.put("Age", 1);
        mappingInconsistent.put("City", 2);
        mappingInconsistent.put("Unknown", 3);

        CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, mappingInconsistent, null, 1);
        assertFalse(recordInconsistent.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} .
     */
    @Test
    public void testIsMapped() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} .
     */
    @Test
    public void testIsSet() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} .
     */
    @Test
    public void testIterator() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> expected = new ArrayList<>();
        expected.add("Name");
        expected.add("Age");
        expected.add("City");

        Iterator<String> actual = record.iterator();
        List<String> actualList = new ArrayList<>();
        while (actual.hasNext()) {
            actualList.add(actual.next());
        }
        assertArrayEquals(expected.toArray(), actualList.toArray());
    }

    /**
     * Test method for {@link CSVRecord#values()} .
     */
    @Test
    public void testValues() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertArrayEquals(values, record.values());
    }

    /**
     * Test method for {@link CSVRecord#getComment()} .
     */
    @Test
    public void testGetComment() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} .
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#size()} .
     */
    @Test
    public void testSize() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toString()} .
     */
    @Test
    public void testToString() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[Name, Age, City]", record.toString());
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with negative index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(-1);
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with unknown column.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownColumn() {
        String[] values = {"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get("Unknown");
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with empty mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testIsConsistentEmptyMapping() {
        String[] values = {"Name", "Age", "City"};

        CSVRecord record = new CSVRecord(values, null, null, 1);
        record.isConsistent();
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with inconsistent mapping size.
     */
    @Test(expected = IllegalStateException.class)
    public void testIsConsistentInconsistentMappingSize() {
        String[] values = {"Name", "Age", "City", "Unknown"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.isConsistent();
    }
}