package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 *
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCSVRecordConstruction() {
        // Test empty constructor
        CSVRecord record = new CSVRecord(null, null, null, 0);
        assertNull(record.comment);
        assertNull(record.mapping);
        assertNull(record.values);
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());

        // Test constructor with values
        String[] values = new String[] { "Value1", "Value2" };
        CSVRecord recordWithValues = new CSVRecord(values, null, null, 0);
        assertEquals(values, recordWithValues.values());
        assertEquals(2, recordWithValues.size());
        assertFalse(recordWithValues.isConsistent());

        // Test constructor with mapping and values
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord recordWithMapping = new CSVRecord(values, mapping, null, 0);
        assertEquals(mapping, recordWithMapping.mapping);
        assertEquals(values, recordWithMapping.values());
        assertTrue(recordWithMapping.isConsistent());
        assertEquals("Value1", recordWithMapping.get("Header1"));
        assertEquals("Value2", recordWithMapping.get("Header2"));
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals("Value1", record.get("Header1"));
        assertEquals("Value2", record.get("Header2"));
        assertEquals("Value1", record.get(0));
        assertEquals("Value2", record.get(1));

        try {
            record.get("NonExistentHeader");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            record.get(-1);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }
    }

    /**
     * Test get comment method.
     */
    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(null, null, "Comment", 0);
        assertEquals("Comment", record.getComment());
        assertNull(new CSVRecord(null, null, null, 0).getComment());
    }

    /**
     * Test get recordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(null, null, null, 1);
        assertEquals(1, record.getRecordNumber());
        assertNull(new CSVRecord(null, null, null, 0).getRecordNumber());
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);

        CSVRecord recordWithMappingAndValues = new CSVRecord(values, mapping, null, 0);
        assertTrue(recordWithMappingAndValues.isConsistent());

        CSVRecord recordWithNoMapping = new CSVRecord(values, null, null, 0);
        assertFalse(recordWithNoMapping.isConsistent());

        CSVRecord recordWithExtraValues = new CSVRecord(new String[] { "Value1", "Value2", "Value3" }, mapping, null, 0);
        assertFalse(recordWithExtraValues.isConsistent());

        CSVRecord recordWithMissingValues = new CSVRecord(values, mapping, null, 1);
        assertFalse(recordWithMissingValues.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isMapped("Header1"));
        assertTrue(record.isMapped("Header2"));
        assertFalse(record.isMapped("NonExistentHeader"));

        CSVRecord recordWithNoMapping = new CSVRecord(values, null, null, 0);
        assertFalse(recordWithNoMapping.isMapped("Header1"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertTrue(record.isSet("Header1"));
        assertTrue(record.isSet("Header2"));
        assertFalse(record.isSet("NonExistentHeader"));

        String[] valuesWithMissingValue = new String[] { "Value1" };
        CSVRecord recordWithMissingValue = new CSVRecord(valuesWithMissingValue, mapping, null, 0);
        assertFalse(recordWithMissingValue.isSet("Header2"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        // Test basic functionality
        Iterator<String> iterator = record.iterator();
        assertEquals("Value1", iterator.next());
        assertEquals("Value2", iterator.next());
        assertFalse(iterator.hasNext());

        // Test exceptions
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            iterator = record.iterator();
            iterator.next();
            iterator.next();
            iterator.remove();
            fail("Expected ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected
        }
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "Value1");
        expectedMap.put("Header2", "Value2");

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> map = record.putIn(new HashMap<String, String>(values.length));
        assertEquals(expectedMap, map);

        Map<String, String> mapWithMissingValue = record.putIn(new HashMap<String, String>());
        assertNull(mapWithMissingValue);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        assertEquals(2, record.size());

        String[] emptyValues = new String[0];
        CSVRecord recordWithEmptyValues = new CSVRecord(emptyValues, null, null, 0);
        assertEquals(0, recordWithEmptyValues.size());
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[] { "Value1", "Value2" };
        CSVRecord record = new CSVRecord(values, null, null, 0);
        List<String> expectedList = Arrays.asList("Value1", "Value2");
        List<String> list = record.toList();
        assertEquals(expectedList, list);
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[] { "Value1", "Value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Header1", 0);
        mapping.put("Header2", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 0);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "Value1");
        expectedMap.put("Header2", "Value2");
        Map<String, String> map = record.toMap();
        assertEquals(expectedMap, map);

        CSVRecord recordWithEmptyValues = new CSVRecord(new String[0], null, null, 0);
        assertNull(recordWithEmptyValues.toMap());
    }
}