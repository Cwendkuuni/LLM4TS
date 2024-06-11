package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test cases for get method by index
    @Test
    public void testGetByIndex() {
        // Basic test case
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals(null, record.get(2));

        // Test with negative index
        CSVRecord recordNegativeIndex = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
        try {
            recordNegativeIndex.get(-1);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }

        // Test with null array
        CSVRecord recordNullArray = new CSVRecord(null, null, null, 1);
        try {
            recordNullArray.get(0);
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test cases for get method by name
    @Test
    public void testGetByNameFromMappedRecord() {
        // Basic test case
        Map<String, Integer> mapping = Map.of("Name1", 0, "Name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, mapping, null, 1);
        assertEquals("John", record.get("Name1"));
        assertEquals("Doe", record.get("Name2"));
        assertNull(record.get("Name3"));

        // Test with invalid column name
        Map<String, Integer> mappingInvalid = Map.of("Name1", 0, "Name2", 1, "NameInvalid", 2);
        CSVRecord recordInvalid = new CSVRecord(new String[]{"John", "Doe"}, mappingInvalid, null, 1);
        assertNull(recordInvalid.get("NameInvalid"));

        // Test with null header mapping
        CSVRecord recordNullMapping = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
        try {
            recordNullMapping.get("Name1");
            fail();
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    // Test cases for isConsistent() method
    @Test
    public void testIsConsistentWithConsistentRecord() {
        // Basic test case
        CSVRecord record = new CSVRecord(new String[]{"Name1", "Name2"}, null, null, 1);
        assertTrue(record.isConsistent());

        // Test with mismatched header and record size
        CSVRecord recordMismatched = new CSVRecord(new String[]{"Name1", "Name2"}, null, null, 1);
        recordMismatched.values = new String[1];
        assertFalse(recordMismatched.isConsistent());
    }

    // Test cases for isMapped() method
    @Test
    public void testIsMapped() {
        // Test with mapped record
        Map<String, Integer> mapping = Map.of("Name1", 0, "Name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, mapping, null, 1);
        assertTrue(record.isMapped("Name1"));
        assertTrue(record.isMapped("Name2"));
        assertFalse(record.isMapped("Name3"));

        // Test with un-mapped record
        CSVRecord recordUnmapped = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
        assertFalse(recordUnmapped.isMapped("Name1"));
    }

    // Test cases for isSet() method
    @Test
    public void testIsSet() {
        // Test with mapped and set record
        Map<String, Integer> mapping = Map.of("Name1", 0, "Name2", 1);
        CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, mapping, null, 1);
        assertTrue(record.isSet("Name1"));
        assertTrue(record.isSet("Name2"));
        assertFalse(record.isSet("Name3"));

        // Test with mapped but not set record
        CSVRecord recordMappedNotSet = new CSVRecord(new String[]{"John", "Doe"}, mapping, null, 1);
        recordMappedNotSet.values = new String[1];
        assertFalse(recordMappedNotSet.isSet("Name1"));

        // Test with un-mapped record
        CSVRecord recordUnmapped = new CSVRecord(new String[]{"John", "Doe"}, null, null, 1);
        assertFalse(recordUnmapped.isSet("Name1"));
    }

    // Test cases for iterator() method
    @Test
    public void testIterator() {
        String[] values = new String[]{"Name1", "Name2", "Name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        Iterator<String> iterator = record.iterator();
        assertEquals("Name1", iterator.next());
        assertEquals("Name2", iterator.next());
        assertEquals("Name3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test cases for values() method
    @Test
    public void testValues() {
        String[] values = new String[]{"Name1", "Name2", "Name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(values, record.values());
    }

    // Test cases for getComment() method
    @Test
    public void testGetComment() {
        String comment = "This is a comment";
        CSVRecord record = new CSVRecord(new String[]{"Name1", "Name2"}, null, comment, 1);
        assertEquals(comment, record.getComment());
        CSVRecord recordNoComment = new CSVRecord(new String[]{"Name1", "Name2"}, null, null, 1);
        assertNull(recordNoComment.getComment());
    }

    // Test cases for getRecordNumber() method
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(new String[]{"Name1", "Name2"}, null, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    // Test cases for size() method
    @Test
    public void testSize() {
        String[] values = new String[]{"Name1", "Name2", "Name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals(values.length, record.size());
    }

    // Test cases for toString() method
    @Test
    public void testToString() {
        String[] values = new String[]{"Name1", "Name2", "Name3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        assertEquals("[Name1, Name2, Name3]", record.toString());
    }
}