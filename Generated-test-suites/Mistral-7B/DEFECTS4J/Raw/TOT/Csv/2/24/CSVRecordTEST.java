package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}.
     */
    @org.junit.Test
    public void testCSVRecord() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertEquals(1, record.getRecordNumber());
        assertEquals(4, record.size());
        assertTrue(record.isConsistent());
        assertFalse(record.isSet("Unknown"));
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertNull(record.getComment());

        final List<String> valuesList = Arrays.asList(record.values());
        assertEquals(Arrays.asList("John", "Doe", "30", "Male"), valuesList);
    }

    /**
     * Test method for {@link CSVRecord#get(int)}.
     */
    @org.junit.Test
    public void testGetInt() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        // when & then
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("Male", record.get(3));
        assertNull(record.get(-1));
    }

    /**
     * Test method for {@link CSVRecord#get(String)}.
     */
    @org.junit.Test
    public void testGetString() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Doe")); // should be insensitive to case
        assertEquals("30", record.get("Age"));
        assertEquals("Male", record.get("Gender"));
        assertNull(record.get("Unknown"));

        // error cases
        try {
            record.get("Name1");
            fail("Should throw IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        try {
            record.get(null);
            fail("Should throw NullPointerException");
        } catch (final NullPointerException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @org.junit.Test
    public void testIsConsistent() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertTrue(record.isConsistent());

        // error cases
        final String[] valuesWithMismatch = new String[]{"John", "Doe", "30", "Male", "NotMapped"};
        final Map<String, Integer> mappingWithMismatch = new HashMap<>();
        mappingWithMismatch.put("Name", 0);
        mappingWithMismatch.put("Age", 2);
        mappingWithMismatch.put("Gender", 3);
        final CSVRecord recordWithMismatch = new CSVRecord(valuesWithMismatch, mappingWithMismatch, null, 1);
        assertFalse(recordWithMismatch.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}.
     */
    @org.junit.Test
    public void testIsMapped() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isMapped("Unknown"));

        // error cases
        final Map<String, Integer> emptyMapping = new HashMap<>();
        final CSVRecord emptyMappingRecord = new CSVRecord(values, emptyMapping, null, 1);
        assertFalse(emptyMappingRecord.isMapped("Name"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}.
     */
    @org.junit.Test
    public void testIsSet() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("Gender"));
        assertFalse(record.isSet("Unknown"));

        // error cases
        final String[] valuesWithoutAge = new String[]{"John", "Doe", "", "Male"};
        final Map<String, Integer> mappingWithoutAge = new HashMap<>();
        mappingWithoutAge.put("Name", 0);
        mappingWithoutAge.put("Age", null);
        mappingWithoutAge.put("Gender", 2);
        final CSVRecord recordWithoutAge = new CSVRecord(valuesWithoutAge, mappingWithoutAge, null, 1);
        assertFalse(recordWithoutAge.isSet("Age"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()}.
     */
    @org.junit.Test
    public void testIterator() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        // when & then
        final Iterator<String> iterator = record.iterator();
        assertEquals(Arrays.asList("John", "Doe", "30", "Male"), Arrays.asList(iterator.next(), iterator.next(), iterator.next(), iterator.next()));
        assertFalse(iterator.hasNext());

        // error cases
        try {
            iterator.next();
            fail("Should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // expected
        }

        try {
            iterator = record.iterator();
            iterator.remove();
            fail("Should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CSVRecord#values()}.
     */
    @org.junit.Test
    public void testValues() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final CSVRecord record = new CSVRecord(values, null, null, 1);

        // when & then
        assertArrayEquals(values, record.values());

        // error cases
        try {
            record.values();
            fail("Should throw UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CSVRecord#getComment()}.
     */
    @org.junit.Test
    public void testGetComment() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final String comment = "This record represents John Doe.";
        final CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        // when & then
        assertEquals(comment, record.getComment());

        // error cases
        final CSVRecord recordWithoutComment = new CSVRecord(values, mapping, null, 1);
        assertNull(recordWithoutComment.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}.
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        // given
        final String[] values = new String[]{"John", "Doe", "30", "Male"};
        final Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        mapping.put("Gender", 3);
        final CSVRecord record = new CSVRecord(values, mapping, null, 1);

        // when & then
        assertEquals(1, record.getRecordNumber());

        // error cases
        final CSVRecord recordWithoutRecordNumber = new CSVRecord(values, mapping, null, 0);
        assertEquals(0, recordWithoutRecordNumber.getRecordNumber());
    }
}