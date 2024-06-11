package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordImpl;
import org.apache.commons.csv.test.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    private CSVFormat format;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormat();
    }

    /**
     * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)}.
     */
    @Test
    public void testCSVRecord() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        CSVRecord expected = new CSVRecordImpl(values, mapping, null, 1L);
        assertSame(expected, record);
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)}.
     */
    @Test
    public void testGetEnum() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.get(Enum.valueOf("Name"));
        String expected = "Name";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#get(int)}.
     */
    @Test
    public void testGetInt() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.get(0);
        String expected = "Name";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#get(String)}.
     */
    @Test
    public void testGetString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.get("Name");
        String expected = "Name";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#getComment()}.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.getComment();
        assertNull(result);
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()}.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        long result = record.getRecordNumber();
        long expected = 1L;
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()}.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        boolean result = record.isConsistent();
        assertTrue(result);

        mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 3);
        record = new CSVRecord(values, mapping, null, 1L);
        result = record.isConsistent();
        assertFalse(result);
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)}.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        boolean result = record.isMapped("Name");
        assertTrue(result);

        result = record.isMapped("City");
        assertTrue(result);

        result = record.isMapped("Unknown");
        assertFalse(result);
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)}.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        boolean result = record.isSet("Name");
        assertTrue(result);

        result = record.isSet("Age");
        assertTrue(result);

        result = record.isSet("City");
        assertTrue(result);

        result = record.isSet("Unknown");
        assertFalse(result);
    }

    /**
     * Test method for {@link CSVRecord#iterator()}.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Iterator<String> iterator = record.iterator();
        List<String> expected = Arrays.asList("Name", "Age", "City");
        List<String> result = TestUtil.toList(iterator);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)}.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        List<String> expected = Arrays.asList("Name=Name", "Age=Age", "City=City");
        List<String> result = TestUtil.toList(map.entrySet());
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#size()}.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        int result = record.size();
        int expected = values.length;
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#toList()}.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> result = record.toList();
        List<String> expected = Arrays.asList("Name", "Age", "City");
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#toMap()}.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        Map<String, String> result = record.toMap();
        Map<String, String> expected = new HashMap<>();
        expected.put("Name", "Name");
        expected.put("Age", "Age");
        expected.put("City", "City");
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#toString()}.
     */
    @Test
    public void testToString() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.toString();
        String expected = "[Name, Age, City]";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVRecord#values()}.
     */
    @Test
    public void testValues() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String[] result = record.values();
        String[] expected = values;
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for
     * {@link CSVRecord#get(String)} when {@link CSVRecord#isMapped(String)} returns false.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownColumn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        String result = record.get("Unknown");
        fail();
    }

    /**
     * Test method for
     * {@link CSVRecord#get(String)} when {@link CSVRecord#isConsistent()} returns false.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMissingColumn() {
        String[] values = new String[]{"Name", "Age", "City"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 1);
        mapping.put("City", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        mapping = new HashMap<>();
        record.mapping = mapping;
        String result = record.get("Name");
        fail();
    }
}