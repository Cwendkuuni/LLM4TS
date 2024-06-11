package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} constructor.
     */
    @org.junit.Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, csvRecord.getRecordNumber());
        assertEquals(3, csvRecord.size());
        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} method.
     */
    @org.junit.Test
    public void testGetInt() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", csvRecord.get(0));
        assertEquals("Age", csvRecord.get(1));
        assertEquals("City", csvRecord.get(2));
        try {
            csvRecord.get(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            csvRecord.get(3);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CSVRecord#get(String)} method.
     */
    @org.junit.Test
    public void testGetString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals("Name", csvRecord.get("Name"));
        assertEquals("Age", csvRecord.get("Age"));
        assertEquals("City", csvRecord.get("City"));
        try {
            csvRecord.get("UnknownColumn");
            fail("Expected NullPointerException or IllegalArgumentException");
        } catch (NullPointerException | IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} method.
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isConsistent());
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);
        assertTrue(emptyRecord.isConsistent());
        String[] valuesWithExtra = new String[]{"Name", "Age", "City", "Extra"};
        Map<String, Integer> mappingWithExtra = new HashMap<>();
        mappingWithExtra.put("Name", 0);
        mappingWithExtra.put("Age", 1);
        mappingWithExtra.put("City", 2);
        mappingWithExtra.put("Extra", 3);
        CSVRecord recordWithExtra = new CSVRecord(valuesWithExtra, mappingWithExtra, null, 1);
        assertFalse(recordWithExtra.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} method.
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isMapped("Name"));
        assertTrue(csvRecord.isMapped("Age"));
        assertTrue(csvRecord.isMapped("City"));
        assertFalse(csvRecord.isMapped("UnknownColumn"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} method.
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertTrue(csvRecord.isSet("Name"));
        assertTrue(csvRecord.isSet("Age"));
        assertTrue(csvRecord.isSet("City"));
        assertFalse(csvRecord.isSet("UnknownColumn"));
        String[] valuesWithMissing = new String[]{"Name", "", "City"};
        Map<String, Integer> mappingWithMissing = new HashMap<>();
        mappingWithMissing.put("Name", 0);
        mappingWithMissing.put("Age", 1);
        mappingWithMissing.put("City", 2);
        CSVRecord recordWithMissing = new CSVRecord(valuesWithMissing, mappingWithMissing, null, 1);
        assertFalse(recordWithMissing.isSet("Age"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} method.
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        Iterator<String> iterator = csvRecord.iterator();
        assertEquals("Name", iterator.next());
        assertEquals("Age", iterator.next());
        assertEquals("City", iterator.next());
        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CSVRecord#values()} method.
     */
    @org.junit.Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        String[] recordValues = csvRecord.values();
        assertEquals("Name", recordValues[0]);
        assertEquals("Age", recordValues[1]);
        assertEquals("City", recordValues[2]);
    }

    /**
     * Test method for {@link CSVRecord#getComment()} method.
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertNull(csvRecord.getComment());
        CSVRecord csvRecordWithComment = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", csvRecordWithComment.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} method.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, csvRecord.getRecordNumber());
    }

}