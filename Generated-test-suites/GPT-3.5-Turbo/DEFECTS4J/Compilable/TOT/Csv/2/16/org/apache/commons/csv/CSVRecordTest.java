package org.apache.commons.csv;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.*;

public class CSVRecordTest {

    private CSVRecord record;

    @Before
    public void setUp() {
        // Create a sample record for testing
        String[] values = {"John", "Doe", "25"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("Age", 2);
        record = new CSVRecord(values, mapping, "Comment", 1);
    }

    @Test
    public void testGetByIndex() {
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("25", record.get(2));

        // Edge case: Index out of bounds
        try {
            String value = record.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception, test passes
        }
    }

    @Test
    public void testGetByName() {
        assertEquals("John", record.get("First Name"));
        assertEquals("Doe", record.get("Last Name"));
        assertEquals("25", record.get("Age"));

        // Edge case: Invalid column name
        assertNull(record.get("Invalid Column Name"));
    }

    @Test
    public void testIsConsistent() {
        assertTrue(record.isConsistent());

        // Edge case: Record size does not match header size
        record = new CSVRecord(new String[]{"John", "Doe"}, new HashMap<>(), "Comment", 1);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("First Name"));
        assertTrue(record.isMapped("Last Name"));
        assertTrue(record.isMapped("Age"));

        // Edge case: Invalid column name
        assertFalse(record.isMapped("Invalid Column Name"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("First Name"));
        assertTrue(record.isSet("Last Name"));
        assertTrue(record.isSet("Age"));

        // Edge case: Column mapped but not set
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("Age", 3); // Mapping exists, but value is not set
        record = new CSVRecord(new String[]{"John", "Doe"}, mapping, "Comment", 1);
        assertFalse(record.isSet("Age"));

        // Edge case: Column not mapped
        assertFalse(record.isSet("Invalid Column Name"));
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertEquals("John", iterator.next());
        assertEquals("Doe", iterator.next());
        assertEquals("25", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testValues() {
        String[] values = record.values();
        assertEquals(3, values.length);
        assertEquals("John", values[0]);
        assertEquals("Doe", values[1]);
        assertEquals("25", values[2]);
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        assertEquals("[John, Doe, 25]", record.toString());
    }

}