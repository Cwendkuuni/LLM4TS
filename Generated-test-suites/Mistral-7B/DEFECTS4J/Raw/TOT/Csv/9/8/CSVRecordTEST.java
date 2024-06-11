package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVRecord}.
 */
public class CSVRecordTest {

    /**
     * Test for constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(123, record.getRecordNumber());
        assertArrayEquals(values, record.values());
        assertEquals(mapping, record.mapping);
        assertEquals(comment, record.getComment());
        assertFalse(record.isConsistent());
        assertTrue(record.isMapped("name0"));
        assertTrue(record.isMapped("value"));
        assertFalse(record.isMapped("nonExistent"));
        assertNull(record.get("nonExistent"));
        assertEquals("value2", record.get("value"));
    }

    /**
     * Test for get method with enum.
     */
    @org.junit.Test
    public void testGetWithEnum() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value0", record.get(Enum.NAME0));
    }

    /**
     * Test for get method with int.
     */
    @org.junit.Test
    public void testGetWithInt() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value0", record.get(0));
    }

    /**
     * Test for get method with string.
     */
    @org.junit.Test
    public void testGetWithString() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals("value0", record.get("name0"));
    }

    /**
     * Test for get method with unknown header.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetWithUnknownHeader() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        record.get("nonExistent");
    }

    /**
     * Test for isConsistent method.
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());

        mapping = null;
        record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isConsistent());

        values = new String[1];
        record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isConsistent());

        values = new String[3];
        mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        mapping.put("another", 1);
        record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isConsistent());
    }

    /**
     * Test for isMapped method.
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isMapped("name0"));
        assertTrue(record.isMapped("value"));
        assertFalse(record.isMapped("nonExistent"));
    }

    /**
     * Test for isSet method.
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[] { "name0", null, null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 3);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isSet("nonExistent"));
        assertFalse(record.isSet("name0"));
        assertTrue(record.isSet("value"));
    }

    /**
     * Test for iterator method.
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        List<String> expectedList = Arrays.asList("name0", "value0", null, "value2");

        Iterator<String> iterator = record.iterator();

        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    /**
     * Test for putIn method.
     */
    @org.junit.Test
    public void testPutIn() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, String> map = new HashMap<>();
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name0", "value0");
        expectedMap.put("value", "value2");

        Map<String, String> actualMap = record.putIn(map);

        assertSame(expectedMap, actualMap);
    }

    /**
     * Test for size method.
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertEquals(values.length, record.size());
    }

    /**
     * Test for toList method.
     */
    @org.junit.Test
    public void testToList() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        List<String> expectedList = Arrays.asList("name0", "value0", null, "value2");

        List<String> actualList = record.toList();

        assertSame(expectedList, actualList);
    }

    /**
     * Test for toMap method.
     */
    @org.junit.Test
    public void testToMap() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name0", "value0");
        expectedMap.put("value", "value2");

        Map<String, String> actualMap = record.toMap();

        assertSame(expectedMap, actualMap);
    }

    /**
     * Test for toString method.
     */
    @org.junit.Test
    public void testToString() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        String expectedString = "[name0, value0, null, value2]";

        assertEquals(expectedString, record.toString());
    }

    /**
     * Test for values method.
     */
    @org.junit.Test
    public void testValues() {
        String[] values = new String[] { "name0", "value0", null, "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = "comment";
        long recordNumber = 123;
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertArrayEquals(values, record.values());
    }

    /**
     * Test parsing CSV record from a CSVParser.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String csv = "name0,\"value0\",value2,\"name1\"";
        CSVParser csvParser = csvFormat.parse(csv.getBytes());
        CSVRecord record = csvParser.next();

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("name0", 0);
        mapping.put("value", 2);
        String comment = null;
        long recordNumber = 1;
        String[] expectedValues = new String[] { "name0", "value0", null, "name1" };

        CSVRecord actualRecord = new CSVRecord(expectedValues, mapping, comment, recordNumber);

        assertEquals(actualRecord, record);
    }
}