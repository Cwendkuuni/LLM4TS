package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * JUnit test file for the CSVRecord class.
 */
public class CSVRecordTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testCSVRecord_ValidArguments() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
        assertEquals(4, record.size());
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("New York", record.get(2));
        assertEquals("30", record.get(3));
        assertEquals(null, record.getComment());
        assertTrue(record.isConsistent());
    }

    /**
     * Test constructor with null values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_NullValues() {
        String[] values = null;
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        new CSVRecord(values, mapping, null, 1);
    }

    /**
     * Test get method with valid argument.
     */
    @Test
    public void testGet_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("New York", record.get(2));
        assertEquals("30", record.get(3));
        assertNull(record.get(4));
    }

    /**
     * Test get method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(values.length);
    }

    /**
     * Test get method with Enum argument.
     */
    @Test
    public void testGet_EnumArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get(CSVRecord.Enum.FIRST_NAME));
        assertEquals("Doe", record.get(CSVRecord.Enum.LAST_NAME));
        assertEquals("New York", record.get(CSVRecord.Enum.CITY));
        assertEquals("30", record.get(CSVRecord.Enum.AGE));
        assertNull(record.get(CSVRecord.Enum.INVALID));
    }

    /**
     * Test get method with invalid Enum argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGet_InvalidEnumArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.get(CSVRecord.Enum.class.newInstance("INVALID"));
    }

    /**
     * Test getComment method with valid argument.
     */
    @Test
    public void testGetComment_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        String comment = "This is a test record.";

        CSVRecord record = new CSVRecord(values, mapping, comment, 1);

        assertEquals(comment, record.getComment());
        assertNull(record.getComment()); // test null comment
    }

    /**
     * Test getComment method with null argument.
     */
    @Test
    public void testGetComment_NullArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertNull(record.getComment());
    }

    /**
     * Test getRecordNumber method with valid argument.
     */
    @Test
    public void testGetRecordNumber_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test getRecordNumber method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetRecordNumber_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
    }

    /**
     * Test isConsistent method with valid argument.
     */
    @Test
    public void testIsConsistent_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }

    /**
     * Test isConsistent method with invalid argument.
     */
    @Test
    public void testIsConsistent_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        mapping.put("Unknown", 4);

        assertFalse(record.isConsistent());
    }

    /**
     * Test isMapped method with valid argument.
     */
    @Test
    public void testIsMapped_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("First Name"));
        assertTrue(record.isMapped("Last Name"));
        assertTrue(record.isMapped("City"));
        assertTrue(record.isMapped("Age"));
        assertFalse(record.isMapped("Unknown"));
    }

    /**
     * Test isMapped method with invalid argument.
     */
    @Test
    public void testIsMapped_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record = null;

        assertFalse(record.isMapped("First Name"));
    }

    /**
     * Test isSet method with valid argument.
     */
    @Test
    public void testIsSet_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("First Name"));
        assertTrue(record.isSet("Last Name"));
        assertTrue(record.isSet("City"));
        assertTrue(record.isSet("Age"));
        assertFalse(record.isSet("Unknown"));
    }

    /**
     * Test isSet method with invalid argument.
     */
    @Test
    public void testIsSet_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        record.values = new String[3];

        assertFalse(record.isSet("Age"));
    }

    /**
     * Test iterator method with valid argument.
     */
    @Test
    public void testIterator_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        List<String> valuesList = record.toList();

        Iterator<String> iterator = record.iterator();

        assertEquals(valuesList.get(0), iterator.next());
        assertEquals(valuesList.get(1), iterator.next());
        assertEquals(valuesList.get(2), iterator.next());
        assertEquals(valuesList.get(3), iterator.next());
    }

    /**
     * Test iterator method with empty argument.
     */
    @Test
    public void testIterator_EmptyArgument() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        CSVRecord record = new CSVRecord(new String[0], mapping, null, 1);

        Iterator<String> iterator = record.iterator();

        assertFalse(iterator.hasNext());
    }

    /**
     * Test putIn method with valid argument.
     */
    @Test
    public void testPutIn_ValidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        Map<String, String> map = record.putIn(new HashMap<String, String>(values.length));

        assertEquals("John", map.get("First Name"));
        assertEquals("Doe", map.get("Last Name"));
        assertEquals("New York", map.get("City"));
        assertEquals("30", map.get("Age"));
        assertNull(map.get("Unknown"));
    }

    /**
     * Test putIn method with invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void testPutIn_InvalidArgument() {
        String[] values = new String[]{"John", "Doe", "New York", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("First Name", 0);
        mapping.put("Last Name", 1);
        mapping.put("City", 2);
        mapping.put("Age", 3);

        map = record.putIn(null);
    }

}