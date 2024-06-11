package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Tests default constructor and getters.
     */
    @Test
    public void testDefaultConstructorAndGetters() {
        final CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.getComment());
        assertNull(record.getMapping());
        assertEquals(0, record.getRecordNumber());
        assertArrayEquals(new String[0], record.getValues());
        assertFalse(record.isConsistent());
    }

    /**
     * Tests constructor with valid values and getters.
     */
    @Test
    public void testConstructorWithValidValuesAndGetters() {
        final String comment = "This is a comment";
        final String[] values = new String[] { "value1", "value2" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        final CSVRecord record = new CSVRecord(values, mapping, comment, 1);
        assertEquals(comment, record.getComment());
        assertSame(mapping, record.getMapping());
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(values, record.getValues());
        assertTrue(record.isConsistent());
    }

    /**
     * Tests getter for {@link CSVRecord#get(Enum)} and {@link CSVRecord#get(String)} methods.
     */
    @Test
    public void testGetters() {
        final String comment = "This is a comment";
        final String[] values = new String[] { "value1", "value2" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        final CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get(Enum.TWO));
        assertEquals("value2", record.get("header2"));
        assertNull(record.get(Enum.THREE));
        assertNull(record.get("nonExistentHeader"));
    }

    /**
     * Tests {@link CSVRecord#isConsistent()} method.
     */
    @Test
    public void testIsConsistent() {
        final String comment = "This is a comment";
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        final CSVRecord record1 = new CSVRecord(new String[2], mapping, comment, 1);
        final CSVRecord record2 = new CSVRecord(new String[] { "value1", "value1", "value2" }, mapping, comment, 1);
        final CSVRecord record3 = new CSVRecord(new String[] { "value1", "value1", "value2" }, mapping, comment, 2);

        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());
        assertTrue(record3.isConsistent());

        mapping.put("header4", 3);
        final CSVRecord record4 = new CSVRecord(new String[] { "value1", "value1", "value2", "value4" }, mapping, comment, 1);
        assertTrue(record4.isConsistent());

        mapping.remove("header3");
        final CSVRecord record5 = new CSVRecord(new String[] { "value1", "value1", "value2" }, mapping, comment, 1);
        assertFalse(record5.isConsistent());

        final String[] values = new String[0];
        final CSVRecord record6 = new CSVRecord(values, mapping, comment, 1);
        assertFalse(record6.isConsistent());
    }

    /**
     * Tests {@link CSVRecord#isMapped(String)}, {@link CSVRecord#isSet(String)}, and {@link CSVRecord#get(String)} methods.
     */
    @Test
    public void testIsMappedSetAndGet() {
        final String comment = "This is a comment";
        final String[] values = new String[] { "value1", "value2" };
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        final CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("nonExistentHeader"));

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertFalse(record.isSet("nonExistentHeader"));
        assertNull(record.get("nonExistentHeader"));

        assertSame("value1", record.get("header1"));
        assertSame("value2", record.get("header2"));

        mapping.put("header3", 2);
        assertFalse(record.isSet("header3"));
        assertNull(record.get("header3"));
    }

    /**
     * Tests {@link CSVRecord#putIn(Map)} method.
     */
    @Test
    public void testPutIn() {
        final String comment = "This is a comment";
        final String[] values = new String[] { "value1", "value2", "value3" };
        final Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");
        expectedMap.put("header3", "value3");

        final CSVRecord record = new CSVRecord(values, null, comment, 1);
        final Map<String, String> actualMap = record.putIn(new HashMap<String, String>());

        assertEquals(expectedMap, actualMap);
    }

    /**
     * Tests {@link CSVRecord#size()} method.
     */
    @Test
    public void testSize() {
        final String comment = "This is a comment";
        final String[] values = new String[] { "value1", "value2", "value3" };

        final CSVRecord record = new CSVRecord(values, null, comment, 1);
        assertEquals(values.length, record.size());
    }

    private enum Enum {
        ONE, TWO, THREE;
    }

    private void assertArrayEquals(final Object expected, final Object actual) {
        if (expected == null) {
            assertTrue(actual == null);
        } else if (expected.getClass() != actual.getClass()) {
            throw new AssertionError("Expected array but was " + actual.getClass());
        } else {
            assertArrayEquals((String[]) expected, (String[]) actual);
        }
    }

}