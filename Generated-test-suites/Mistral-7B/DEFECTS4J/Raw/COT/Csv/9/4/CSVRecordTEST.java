package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * CSVRecordTest.java - JUnit test file for CSVRecord class.
 */
public class CSVRecordTest {

    // Basic test cases for all methods

    @Test
    public void testGet_Enum() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertEquals("John Doe", record.get(Enum.NAME));
    }

    @Test
    public void testGet_Int() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertEquals("30", record.get(1));
    }

    @Test
    public void testGet_String() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertEquals("New York", record.get("City"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, "comment", 1);

        assertEquals("comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 3);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));

        assertFalse(record.isMapped("Unknown"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 3);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertEquals(3, record.size());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        assertEquals("[Name=John Doe, Age=30, City=New York]", record.toString());
    }

    // Test cases for edge cases and exception handling

    @Test(expected = IllegalStateException.class)
    public void testGet_String_NoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);

        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        try {
            record.get("Unknown");
        } catch (IllegalArgumentException e) {
            fail("Expected IllegalStateException, but got " + e.getClass().getName());
        }

        record.mapping = null;
        record.get("Name"); // This should pass since mapping is not null inside the get method

        record.get("Unknown");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_Int_OutOfBounds() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        record.get(-1);
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.set("Name", "John Doe");
        record.set("Age", "30");
        record.set("City", "New York");

        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);

        assertEquals(3, map.size());
        assertEquals("John Doe", map.get("Name"));
        assertEquals("30", map.get("Age"));
        assertEquals("New York", map.get("City"));
    }
}