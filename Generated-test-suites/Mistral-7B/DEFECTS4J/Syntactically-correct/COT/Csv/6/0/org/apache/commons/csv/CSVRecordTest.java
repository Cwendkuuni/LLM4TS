package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CSVRecord}.
 *
 * @author Vladimir Dinev
 */
public class CSVRecordTest {

    private static final String CSV = "name1,name2,name3\nvalue1,value2,value3\ncomment#1";

    private CSVParser parser;

    @Before
    public void setUp() throws Exception {
        parser = CSVFormat.DEFAULT.withCommentMarker("#").parse(CSV);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    /**
     * Test constructor with values.
     */
    @Test
    public void testConstructorWithValues() {
        CSVRecord record = parser.nextRecord();
        String[] expectedValues = record.values();
        String[] actualValues = new String[]{"name1", "value1", "comment#1"};
        assertArrayEquals(expectedValues, actualValues);
    }

    /**
     * Test constructor with empty values.
     */
    @Test
    public void testConstructorWithEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertArrayEquals(new String[0], record.values());
    }

    /**
     * Test constructor with mapping.
     */
    @Test
    public void testConstructorWithMapping() {
        CSVRecord record = parser.nextRecord();
        Map<String, Integer> expectedMapping = record.mapping;
        Map<String, Integer> actualMapping = new HashMap<>();
        actualMapping.put("name1", 0);
        actualMapping.put("name2", 1);
        actualMapping.put("name3", 2);
        assertArrayEquals(expectedMapping.toArray(new Map.Entry[0][]),
                actualMapping.entrySet().toArray(new Map.Entry[0][]));
    }

    /**
     * Test constructor with null mapping.
     */
    @Test
    public void testConstructorWithNullMapping() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        assertNull(record.mapping);
    }

    /**
     * Test get method with enum.
     */
    @Test
    public void testGetWithEnum() {
        CSVRecord record = parser.nextRecord();
        String value = record.get(CSVRecordTest.ClassColumn.NAME1);
        String expectedValue = "name1";
        assertEquals(expectedValue, value);
    }

    /**
     * Test get method with name.
     */
    @Test
    public void testGetWithName() {
        CSVRecord record = parser.nextRecord();
        String value = record.get("name1");
        String expectedValue = "value1";
        assertEquals(expectedValue, value);
    }

    /**
     * Test get method with non existent name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNonExistentName() {
        CSVRecord record = parser.nextRecord();
        record.get("nonExistentName");
    }

    /**
     * Test get method with inconsistent record.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.get("name1");
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        CSVRecord record = parser.nextRecord();
        String comment = record.getComment();
        String expectedComment = "#1";
        assertEquals(expectedComment, comment);
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        CSVRecord record = parser.nextRecord();
        long recordNumber = record.getRecordNumber();
        long expectedRecordNumber = 1;
        assertEquals(expectedRecordNumber, recordNumber);
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        CSVRecord record = parser.nextRecord();
        boolean isConsistent = record.isConsistent();
        assertTrue(isConsistent);
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        CSVRecord record = parser.nextRecord();
        boolean isMapped = record.isMapped("name1");
        assertTrue(isMapped);
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        CSVRecord record = parser.nextRecord();
        boolean isSet = record.isSet("name1");
        assertTrue(isSet);
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        CSVRecord record = parser.nextRecord();
        Iterator<String> iterator = record.iterator();
        String[] values = record.values();
        int index = 0;
        while (iterator.hasNext()) {
            String next = iterator.next();
            assertEquals(values[index], next);
            index++;
        }
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");
        expectedMap.put("name3", "comment#1");
        assertEquals(expectedMap, map);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        CSVRecord record = parser.nextRecord();
        int size = record.size();
        int expectedSize = 3;
        assertEquals(expectedSize, size);
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        CSVRecord record = parser.nextRecord();
        List<String> list = record.toList();
        String[] values = record.values();
        List<String> expectedList = Arrays.asList(values);
        assertEquals(expectedList, list);
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = record.toMap();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "value1");
        expectedMap.put("name2", "value2");
        expectedMap.put("name3", "comment#1");
        assertEquals(expectedMap, map);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        CSVRecord record = parser.nextRecord();
        String toString = record.toString();
        String expectedToString = "[value1, value2, comment#1]";
        assertEquals(expectedToString, toString);
    }

    /**
     * Test values method.
     */
    @Test
    public void testValues() {
        CSVRecord record = parser.nextRecord();
        String[] values = record.values();
        String[] expectedValues = new String[]{"name1", "value1", "comment#1"};
        assertArrayEquals(expectedValues, values);
    }

    /**
     * Test get method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullRecord() {
        CSVRecord record = null;
        record.get(CSVRecordTest.ClassColumn.NAME1);
    }

    /**
     * Test get method with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullName() {
        CSVRecord record = parser.nextRecord();
        record.get(null);
    }

    /**
     * Test get method with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullValues() {
        CSVRecord record = new CSVRecord(null, null, null, 0);
        record.get("name1");
    }

    /**
     * Test get method with empty mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWithEmptyMapping() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.get("name1");
    }

    /**
     * Test get method with null mapping and name.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullMappingAndName() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.get(null);
    }

    /**
     * Test get method with name that is not mapped.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameNotMapped() {
        CSVRecord record = parser.nextRecord();
        record.get("nonMappedName");
    }

    /**
     * Test get method with name that is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNameNotSet() {
        CSVRecord record = parser.nextRecord();
        record.get("nonSetName");
    }

    /**
     * Test get method with null comment.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWithNullComment() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.getComment();
    }

    /**
     * Test get method with negative record number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNegativeRecordNumber() {
        CSVRecord record = parser.nextRecord();
        record.getRecordNumber(-1);
    }

    /**
     * Test isConsistent method with inconsistent record.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsConsistentWithInconsistentRecord() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        record.isConsistent();
    }

    /**
     * Test isMapped method with null mapping.
     */
    @Test(expected = NullPointerException.class)
    public void testIsMappedWithNullMapping() {
        CSVRecord record = parser.nextRecord();
        record.isMapped(null);
    }

    /**
     * Test isSet method with null mapping.
     */
    @Test(expected = NullPointerException.class)
    public void testIsSetWithNullMapping() {
        CSVRecord record = parser.nextRecord();
        record.isSet(null);
    }

    /**
     * Test iterator method with null iterator.
     */
    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullIterator() {
        CSVRecord record = parser.nextRecord();
        Iterator<String> iterator = null;
        record.iterator();
    }

    /**
     * Test putIn method with null map.
     */
    @Test(expected = NullPointerException.class)
    public void testPutInWithNullMap() {
        CSVRecord record = parser.nextRecord();
        record.putIn(null);
    }

    /**
     * Test putIn method with null column name.
     */
    @Test(expected = NullPointerException.class)
    public void testPutInWithNullColumnName() {
        CSVRecord record = parser.nextRecord();
        record.putIn(null);
    }

    /**
     * Test putIn method with empty record.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutInWithEmptyRecord() {
        CSVRecord record = new CSVRecord(new String[0], null, null, 0);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
    }

    /**
     * Test putIn method with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testPutInWithNullValues() {
        CSVRecord record = parser.nextRecord();
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        map = null;
        record.putIn(map);
    }

    /**
     * Test size method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testSizeWithNullRecord() {
        CSVRecord record = null;
        int size = record.size();
    }

    /**
     * Test toList method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testToListWithNullRecord() {
        CSVRecord record = null;
        List<String> list = record.toList();
    }

    /**
     * Test toMap method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testToMapWithNullRecord() {
        CSVRecord record = null;
        Map<String, String> map = record.toMap();
    }

    /**
     * Test toString method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testToStringWithNullRecord() {
        CSVRecord record = null;
        String toString = record.toString();
    }

    /**
     * Test values method with null record.
     */
    @Test(expected = NullPointerException.class)
    public void testValuesWithNullRecord() {
        CSVRecord record = null;
        String[] values = record.values();
    }

    enum ClassColumn {
        NAME1
    }

}