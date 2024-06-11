package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CSVRecordTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        String comment = "comment";
        long recordNumber = 12345L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    /**
     * Test for get method using Enum.
     */
    @Test
    public void testGetMethodUsingEnum() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value2", record.get(Enum.TWO));
        assertEquals("value3", record.get(Enum.THREE));
    }

    /**
     * Test for get method using String.
     */
    @Test
    public void testGetMethodUsingString() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertEquals("value3", record.get("header3"));
        assertNull(record.get("nonexistentheader"));
    }

    /**
     * Test for get method with no mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetNoMapping() {
        String[] values = { "value1", "value2", "value3" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        record.get("header");
    }

    /**
     * Test for get method with inconsistent mapping.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInconsistentMapping() {
        String[] values = { "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        record.get("header3");
    }

    /**
     * Test for isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record1 = new CSVRecord(values, mapping, null, 0);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 0);

        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());

        String[] values1 = { "value1", "value2" };
        Map<String, Integer> mapping1 = new HashMap<>();
        mapping1.put("header1", 0);
        mapping1.put("header2", 1);

        CSVRecord record3 = new CSVRecord(values1, mapping1, null, 0);
        assertFalse(record3.isConsistent());
    }

    /**
     * Test for isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertFalse(record.isMapped("nonexistentheader"));
    }

    /**
     * Test for isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("nonexistentheader"));

        String[] values1 = { "value1" };
        Map<String, Integer> mapping1 = new HashMap<>();
        mapping1.put("header1", 0);

        CSVRecord record1 = new CSVRecord(values1, mapping1, null, 0);

        assertFalse(record1.isSet("header2"));
    }

    /**
     * Test for iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        List<String> list = Arrays.asList(values);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Iterator<String> it = record.iterator();

        assertEquals(it.next(), values[0]);
        assertEquals(it.next(), values[1]);
        assertEquals(it.next(), values[2]);
        assertFalse(it.hasNext());
    }

    /**
     * Test for putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("header1", "value1");
        expectedMap.put("header2", "value2");
        expectedMap.put("header3", "value3");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);
        Map<String, String> map = record.putIn(new HashMap<>());

        assertEquals(expectedMap, map);
    }

    /**
     * Test for size method.
     */
    @Test
    public void testSize() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(values.length, record.size());
    }

    /**
     * Test for toList method.
     */
    @Test
    public void testToList() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        List<String> expectedList = Arrays.asList(values);
        List<String> list = record.toList();

        assertSame(expectedList, list);
    }

    /**
     * Test for toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> expectedMap = record.toMap();
        Map<String, String> map = record.toMap();

        assertEquals(expectedMap, map);
    }

    /**
     * Test for toString method.
     */
    @Test
    public void testToString() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        String expectedString = "[value1, value2, value3]";
        String string = record.toString();

        assertEquals(expectedString, string);
    }

    /**
     * Test for values method.
     */
    @Test
    public void testValues() {
        String[] values = { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        String[] expectedValues = { "value1", "value2", "value3" };
        String[] values1 = record.values();

        assertSame(expectedValues, values1);
    }
}