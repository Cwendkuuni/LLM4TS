package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} .
     */
    @org.junit.Test
    public void testCSVRecord() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertArrayEquals(new String[]{"Name", "Age"}, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(1L, record.recordNumber);
    }

    /**
     * Test method for {@link CSVRecord#get(int)}.
     */
    @org.junit.Test
    public void testGetInt() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertSame("Name", record.get(0));
        assertSame("Age", record.get(1));
        assertNull(record.get(-1));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}.
     */
    @org.junit.Test
    public void testGetString() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertSame("Name", record.get("Name"));
        assertSame("Age", record.get("Age"));
        assertNull(record.get("Unknown"));

        mapping = null;
        record = new CSVRecord(values, null, null, 1L);
        assertNull(record.get("Name"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());

        mapping = null;
        record = new CSVRecord(values, null, null, 1L);
        assertTrue(record.isConsistent());

        values = new String[]{"Name", "Age", "City"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isConsistent());

        mapping.put("City", 2);
        record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}.
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("Unknown"));

        mapping = null;
        record = new CSVRecord(values, null, null, 1L);
        assertFalse(record.isMapped("Name"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}.
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));

        mapping = null;
        record = new CSVRecord(values, null, null, 1L);
        assertFalse(record.isSet("Name"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}.
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> expected = Arrays.asList(values);
        List<String> actual = Arrays.asList(record.iterator().toArray(new String[0]));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /**
     * Test method for {@link CSVRecord#values()}.
     */
    @org.junit.Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertArrayEquals(values, record.values());

        mapping = null;
        record = new CSVRecord(values, null, null, 1L);
        assertArrayEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
    }

    /**
     * Test method for {@link CSVRecord#getComment()}.
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertNull(record.getComment());

        String comment = "Comment";
        record = new CSVRecord(values, mapping, comment, 1L);
        assertSame(comment, record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1L, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#size()}.
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(2, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals("[Name, Age]", record.toString());
    }
}