package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    /**
     * Test method for constructor CSVRecord(String[], Map<String, Integer>, String, long).
     */
    @org.junit.Test
    public void testCSVRecord() {
        // Test case 1: Normal scenario
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("alice@example.com", record.get(2));
        assertEquals("Alice,30,alice@example.com", record.toString());
        assertTrue(record.isConsistent());
        assertFalse(record.isMapped("InvalidName"));
        assertNull(record.get("InvalidName"));

        // Test case 2: Empty record
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 1);
        assertEquals(0, emptyRecord.size());
        assertEquals(Arrays.toString(emptyValues), emptyRecord.toString());
        assertTrue(emptyRecord.isConsistent());
        assertFalse(emptyRecord.isMapped("InvalidName"));
        assertNull(emptyRecord.get("InvalidName"));

        // Test case 3: Record with comments
        String[] valuesWithComment = new String[]{"Alice", "30", "alice@example.com", "This record is a comment"};
        CSVRecord recordWithComment = new CSVRecord(valuesWithComment, mapping, "This record is a comment", 1);
        assertEquals("Alice", recordWithComment.get(0));
        assertEquals("30", recordWithComment.get(1));
        assertEquals("alice@example.com", recordWithComment.get(2));
        assertEquals("Alice,30,alice@example.com", recordWithComment.toString());
        assertTrue(recordWithComment.isConsistent());
        assertFalse(recordWithComment.isMapped("InvalidName"));
        assertNull(recordWithComment.get("InvalidName"));
        assertEquals("This record is a comment", recordWithComment.getComment());

        // Test case 4: Record with incorrect mapping
        String[] valuesWithIncorrectMapping = new String[]{"Alice", "30", "alice@example.com", "Age", "Name", "Email"};
        Map<String, Integer> incorrectMapping = new HashMap<>();
        incorrectMapping.put("Name", 3);
        incorrectMapping.put("Age", 0);
        incorrectMapping.put("Email", 2);
        CSVRecord incorrectRecord = new CSVRecord(valuesWithIncorrectMapping, incorrectMapping, null, 1);
        assertFalse(incorrectRecord.isConsistent());

        // Test case 5: Record with null values
        String[] nullValues = new String[]{null, "30", null, null};
        CSVRecord nullRecord = new CSVRecord(nullValues, null, null, 1);
        assertNull(nullRecord.get(0));
        assertNull(nullRecord.get(2));
        assertEquals("null, 30, null", nullRecord.toString());
        assertTrue(nullRecord.isConsistent());
        assertFalse(nullRecord.isMapped("InvalidName"));
        assertNull(nullRecord.get("InvalidName"));
    }

    /**
     * Test method for method get(int).
     */
    @org.junit.Test
    public void testGetInt() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("alice@example.com", record.get(2));
    }

    /**
     * Test method for method get(String).
     */
    @org.junit.Test
    public void testGetString() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("alice@example.com", record.get("Email"));
        assertNull(record.get("InvalidName"));
    }

    /**
     * Test method for method isConsistent().
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] incorrectValues = new String[]{"Alice", "30", "alice@example.com", "Age", "Name", "Email"};
        Map<String, Integer> incorrectMapping = new HashMap<>();
        incorrectMapping.put("Name", 3);
        incorrectMapping.put("Age", 0);
        incorrectMapping.put("Email", 2);
        CSVRecord incorrectRecord = new CSVRecord(incorrectValues, incorrectMapping, null, 1);
        assertFalse(incorrectRecord.isConsistent());
    }

    /**
     * Test method for method isMapped(String).
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Email"));
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test method for method isSet(String).
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Email"));
    }

    /**
     * Test method for method iterator().
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("Alice", iterator.next());
        assertEquals("30", iterator.next());
        assertEquals("alice@example.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for method values().
     */
    @org.junit.Test
    public void testValues() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String[] recordValues = record.values();
        assertEquals("Alice", recordValues[0]);
        assertEquals("30", recordValues[1]);
        assertEquals("alice@example.com", recordValues[2]);
    }

    /**
     * Test method for method getComment().
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"Alice", "30", "alice@example.com", "This record is a comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("This record is a comment", record.getComment());
        String[] valuesWithoutComment = new String[]{"Alice", "30", "alice@example.com"};
        CSVRecord recordWithoutComment = new CSVRecord(valuesWithoutComment, mapping, null, 1);
        assertNull(recordWithoutComment.getComment());
    }

    /**
     * Test method for method getRecordNumber().
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("Email", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test method for method size().
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[]{"Alice", "30", "alice@example.com"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(3, record.size());
    }
}