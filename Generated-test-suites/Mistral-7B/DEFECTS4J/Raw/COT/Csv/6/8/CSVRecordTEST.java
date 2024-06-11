package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVRecord class
 */
public class CSVRecordTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testCSVRecordConstructor() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(Long.valueOf(recordNumber), record.getRecordNumber());
    }

    /**
     * Test get method with Enum
     */
    @org.junit.Test
    public void testGetWithEnum() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("John", record.get(FirstName.class));
    }

    /**
     * Test get method with index
     */
    @org.junit.Test
    public void testGetWithIndex() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("123 Main St", record.get(2));
        assertEquals(null, record.get(3));
    }

    /**
     * Test get method with name
     */
    @org.junit.Test
    public void testGetWithName() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("John", record.get("FirstName"));
        assertEquals("Doe", record.get("LastName"));
        assertEquals("123 Main St", record.get("Address"));
        assertEquals(null, record.get("InvalidName"));
    }

    /**
     * Test getComment method
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(comment, record.getComment());
        assertEquals(null, record.getComment());
    }

    /**
     * Test getRecordNumber method
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(0L, record.getRecordNumber());
    }

    /**
     * Test isConsistent method
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};
        String comment = "This is a test record";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());
        assertFalse(new CSVRecord(new String[0], mapping, comment, recordNumber).isConsistent());
    }

    /**
     * Test isMapped method
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isMapped("FirstName"));
        assertTrue(record.isMapped("LastName"));
        assertTrue(record.isMapped("Address"));
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test isSet method
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isSet("FirstName"));
        assertTrue(record.isSet("LastName"));
        assertTrue(record.isSet("Address"));
        assertFalse(record.isSet("InvalidName"));
    }

    /**
     * Test iterator method
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        List<String> expectedList = Arrays.asList("John", "Doe", "123 Main St");
        Iterator<String> expectedIterator = expectedList.iterator();

        Iterator<String> iterator = record.iterator();

        assertEquals(expectedIterator.next(), iterator.next());
        assertEquals(expectedIterator.next(), iterator.next());
        assertEquals(expectedIterator.next(), iterator.next());
    }

    /**
     * Test putIn method
     */
    @org.junit.Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("FirstName", "John");
            put("LastName", "Doe");
            put("Address", "123 Main St");
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> resultMap = record.putIn(new HashMap<String, String>(3));

        assertEquals(expectedMap, resultMap);
    }

    /**
     * Test size method
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertEquals(3, record.size());
        assertEquals(0, new CSVRecord(new String[0], mapping, null, 0L).size());
    }

    /**
     * Test toList method
     */
    @org.junit.Test
    public void testToList() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        List<String> expectedList = Arrays.asList("John", "Doe", "123 Main St");

        List<String> resultList = record.toList();

        assertEquals(expectedList, resultList);
    }

    /**
     * Test toMap method
     */
    @org.junit.Test
    public void testToMap() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        Map<String, String> expectedMap = new HashMap<String, String>() {{
            put("FirstName", "John");
            put("LastName", "Doe");
            put("Address", "123 Main St");
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> resultMap = record.toMap();

        assertEquals(expectedMap, resultMap);
    }

    /**
     * Test empty constructor
     */
    @org.junit.Test
    public void testEmptyConstructor() {
        CSVRecord record = new CSVRecord(null, null, null, 0L);

        assertEquals(new String[0], record.values());
        assertEquals(null, record.mapping);
        assertEquals(null, record.getComment());
        assertEquals(0L, record.getRecordNumber());
    }

    /**
     * Test get method with invalid index
     */
    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        record.get(3);
    }

    /**
     * Test get method with null mapping
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetWithNullMapping() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};

        CSVRecord record = new CSVRecord(values, null, null, 0L);

        record.get("FirstName");
    }

    /**
     * Test get method with unmatched index and name
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetWithUnmatchedIndexAndName() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 2);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        record.get(3);
        record.get("InvalidName");
    }

    /**
     * Test isConsistent method with mismatched size
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIsConsistentWithMismatchedSize() {
        String[] values = new String[]{"John", "Doe", "123 Main St"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
            put("Address", 3);
        }};

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        record.isConsistent();
    }
}