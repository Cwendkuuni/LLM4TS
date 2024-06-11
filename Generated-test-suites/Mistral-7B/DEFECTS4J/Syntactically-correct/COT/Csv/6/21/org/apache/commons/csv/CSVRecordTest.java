package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}
 */
public class CSVRecordTest {

    /**
     * Test simple constructor with valid arguments
     */
    @Test
    public void testSimpleConstructor() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertEquals(4, record.size());
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("12345", record.get(2));
        assertEquals("Male", record.get(3));
        assertNull(record.getComment());
        assertTrue(record.isConsistent());

    }

    /**
     * Test null values in constructor
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullValuesConstructor() {
        String[] values = new String[]{"John", null, "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);
        new CSVRecord(values, mapping, null, 1);
    }

    /**
     * Test null mapping in constructor
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullMappingConstructor() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        new CSVRecord(values, null, null, 1);
    }

    /**
     * Test null comment in constructor
     */
    @Test
     public void testNullCommentConstructor() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(null, record.getComment());
    }

    /**
     * Test get method with valid arguments
     */
    @Test
    public void testGet() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("12345", record.get(2));
        assertEquals("Male", record.get(3));
        assertNull(record.get(4));
        assertThrows(IllegalArgumentException.class, () -> record.get("InvalidName"));
    }

    /**
     * Test get by Enum
     */
    @Test
    public void testGetByEnum() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals("John", record.get(Enum.FIRSTNAME));
        assertEquals("Doe", record.get(Enum.LASTNAME));
        assertEquals("12345", record.get(Enum.ID));
        assertEquals("Male", record.get(Enum.GENDER));
        assertNull(record.get(Enum.INVALID));

        enum Enum { FIRSTNAME, LASTNAME, ID, GENDER, INVALID }
    }

    /**
     * Test get method with invalid index
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        record.get(values.length);
    }

    /**
     * Test getComment method with valid arguments
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 1);
        assertEquals("Comment", record.getComment());
        assertNull(record.getComment());
    }

    /**
     * Test getRecordNumber method with valid arguments
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test isConsistent method with valid arguments
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isConsistent());

        String[] invalidValues = new String[]{"John", "Doe"};
        CSVRecord invalidRecord = new CSVRecord(invalidValues, mapping, null, 1);
        assertFalse(invalidRecord.isConsistent());
    }

    /**
     * Test isMapped method with valid arguments
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isMapped("FirstName"));
        assertTrue(record.isMapped("LastName"));
        assertTrue(record.isMapped("ID"));
        assertTrue(record.isMapped("Gender"));
        assertFalse(record.isMapped("InvalidName"));
    }

    /**
     * Test isSet method with valid arguments
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertTrue(record.isSet("FirstName"));
        assertTrue(record.isSet("LastName"));
        assertTrue(record.isSet("ID"));
        assertTrue(record.isSet("Gender"));
        assertFalse(record.isSet("InvalidName"));

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 1);
        assertTrue(emptyRecord.isSet("FirstName"));
        assertFalse(emptyRecord.isSet("InvalidName"));
    }

    /**
     * Test iterator method with valid arguments
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> expectedList = Arrays.asList(values);

        Iterator<String> iterator = record.iterator();
        for (int i = 0; i < values.length; i++) {
            assertEquals(expectedList.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test putIn method with valid arguments
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, String> resultMap = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1);
        Map<String, String> map = record.putIn(resultMap);

        assertEquals(values.length, resultMap.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], resultMap.get(mapping.get(new String[]{ "FirstName", "LastName", "ID", "Gender" }[i])));
        }

        assertEquals(mapping.keySet(), resultMap.keySet());
    }

    /**
     * Test size method with valid arguments
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(values.length, record.size());
    }

    /**
     * Test toList method with valid arguments
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        List<String> expectedList = Arrays.asList(values);

        List<String> list = record.toList();
        assertEquals(expectedList, list);
    }

    /**
     * Test toMap method with valid arguments
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("FirstName", values[0]);
        expectedMap.put("LastName", values[1]);
        expectedMap.put("ID", values[2]);
        expectedMap.put("Gender", values[3]);

        Map<String, String> map = record.toMap();
        assertEquals(expectedMap, map);
    }

    /**
     * Test toString method with valid arguments
     */
    @Test
    public void testToString() {
        String[] values = new String[]{"John", "Doe", "12345", "Male"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("FirstName", 0);
        mapping.put("LastName", 1);
        mapping.put("ID", 2);
        mapping.put("Gender", 3);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        String expectedString = "[John, Doe, 12345, Male]";

        assertEquals(expectedString, record.toString());
    }
}