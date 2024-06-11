package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for the CSVRecord class.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}
     */
    @Test
    public void testCSVRecord() {
        String[] values = new String[] {"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
        assertSame(mapping, record.mapping);
        assertNull(record.comment);
        assertTrue(record.isConsistent());
        assertTrue(record.size() == values.length);
    }

    /**
     * Test method for {@link CSVRecord#get(int)}
     */
    @Test
    public void testGetInt() {
        String[] values = new String[] {"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertSame("John", record.get(0));
        assertSame("Doe", record.get(1));
        assertSame("john.doe@example.com", record.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}
     */
    @Test
    public void testGetString() {
        String[] values = new String[] {"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertSame("John", record.get("Name"));
        assertSame("Doe", record.get("Name")); // case insensitive
        assertSame("john.doe@example.com", record.get("Email"));
        assertNull(record.get("Unknown"));
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[] {"John", "Doe", "john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isConsistent());

        mapping = null;
        record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Name")); // case insensitive
        assertTrue(record.isMapped("Email"));
        assertTrue(record.isMapped("email")); // case insensitive
        assertFalse(record.isMapped("Unknown"));

        mapping = new HashMap<>();
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isMapped("Name"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}
     */
    @Test
    public void testIsSet() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Name")); // case insensitive
        assertTrue(record.isSet("Email"));
        assertTrue(record.isSet("email")); // case insensitive
        assertFalse(record.isSet("Unknown"));

        mapping = new HashMap<>();
        record = new CSVRecord(values, mapping, null, 1);
        assertFalse(record.isSet("Name"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}
     */
    @Test
    public void testIterator() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Iterator<String> it = record.iterator();
        assertSame(it.next(), "Name:John");
        assertSame(it.next(), "Email:john.doe@example.com");
        assertFalse(it.hasNext());
    }

    /**
     * Test method for {@link CSVRecord#values()}
     */
    @Test
    public void testValues() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertArrayEquals(values, record.values());
    }

    /**
     * Test method for {@link CSVRecord#getComment()}
     */
    @Test
    public void testGetComment() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com", "# This is a comment"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertSame("# This is a comment", record.getComment());

        values = new String[] {"Name:John,Email:john.doe@example.com"};
        record = new CSVRecord(values, mapping, null, 1);
        assertNull(record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());

        values = new String[] {"Name:John,Email:john.doe@example.com"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        record = new CSVRecord(values, mapping, null, 2);
        assertEquals(2, record.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#size()}
     */
    @Test
    public void testSize() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(2, record.size());
    }

    /**
     * Test method for {@link CSVRecord#toString()}
     */
    @Test
    public void testToString() {
        String[] values = new String[] {"Name:John,Email:john.doe@example.com"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("[Name:John, Email:john.doe@example.com]", record.toString());

        values = new String[] {"Name:John,Email:john.doe@example.com", "# This is a comment"};
        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Email", 1);

        record = new CSVRecord(values, mapping, "# This is a comment", 1);
        assertEquals("[Name:John, Email:john.doe@example.com, # This is a comment]", record.toString());
    }
}