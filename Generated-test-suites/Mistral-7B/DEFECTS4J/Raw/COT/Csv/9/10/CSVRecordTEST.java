package org.apache.commons.csv;

import static org.junit.Assert.AssertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}
 *
 * @author John Doe
 */
public class CSVRecordTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = new CSVFormat();
        parser = format.newParser();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(Arrays.asList(values), record.toList());
        assertEquals(mapping, record.mapping);
        assertNull(record.comment);
        assertEquals(0, record.recordNumber);
        assertTrue(record.isConsistent());

        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, null, null, 0);
        assertTrue(emptyRecord.isConsistent());
    }

    /**
     * Test get method by Enum.
     */
    @Test
    public void testGetEnum() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get(Name.NAME));
        assertEquals("Doe", record.get(Gender.GENDER)); // this is an imaginary Enum
    }

    /**
     * Test get method by index.
     */
    @Test
    public void testGetIndex() {
        String[] values = new String[]{"John", "Doe", "30", "New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("John", record.get(0));
        assertEquals("Doe", record.get(1));
        assertEquals("30", record.get(2));
        assertEquals("New York", record.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(4));
    }

    /**
     * Test get method by name.
     */
    @Test
    public void testGetName() {
        String[] values = new String[]{"John,Doe,30,New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertEquals("John", record.get("Name"));
        assertEquals("Doe", record.get("Gender")); // this is an imaginary Column
        assertNull(record.get("Gender2")); // this is an imaginary Column
        assertThrows(IllegalArgumentException.class, () -> record.get("Gender3")); // this is an imaginary Column
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"John,Doe,30,New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);
        assertEquals("Comment", record.getComment());
        assertNull(new CSVRecord(values, mapping, null, 0).getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"John,Doe,30,New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertEquals(0, new CSVRecord(values, mapping, null, 0).getRecordNumber());
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"John,Doe,30,New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);
        CSVRecord inconsistentRecord = new CSVRecord(values, mapping, null, 0);
        assertFalse(inconsistentRecord.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Gender")); // this is an imaginary Column
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Gender")); // this is an imaginary Column
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Iterator<String> iterator = record.iterator();
        List<String> valuesList = Arrays.asList("Name:John", "Age:30", "City:New York");

        assertEquals(valuesList.get(0), iterator.next());
        assertEquals(valuesList.get(1), iterator.next());
        assertEquals(valuesList.get(2), iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 0);
        map = record.putIn(map);

        assertEquals("John", map.get("Name"));
        assertEquals("30", map.get("Age"));
        assertEquals("New York", map.get("City"));
        assertNull(map.get("Gender")); // this is an imaginary Column
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals(3, record.size());
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        List<String> expectedList = Arrays.asList("Name:John", "Age:30", "City:New York");
        AssertArrayEquals(expectedList.toArray(new String[0]), record.toList().toArray());
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "John");
        expectedMap.put("Age", "30");
        expectedMap.put("City", "New York");

        Map<String, String> map = record.toMap();
        assertEquals(expectedMap, map);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        assertEquals("[Name:John, Age:30, City:New York]", record.toString());
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        String[] values = new String[]{"Name:John,Age:30,City:New York"};

        CSVRecord record = new CSVRecord(values, null, null, 0);
        String[] expectedValues = new String[]{"Name:John", "Age:30", "City:New York"};
        AssertArrayEquals(expectedValues, record.values());
    }
}