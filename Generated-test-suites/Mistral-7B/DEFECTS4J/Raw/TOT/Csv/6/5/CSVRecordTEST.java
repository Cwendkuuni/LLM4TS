package org.apache.commons.csv.test;

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
 * Test cases for {@link CSVRecord} class.
 */
public class CSVRecordTest {

    /** Test data for testing get methods. */
    private static final CSVRecord TEST_RECORD = new CSVRecord(
            new String[] { "value1", "value2", "value3" },
            new HashMap<String, Integer>() {{
                put("Header1", 0);
                put("Header2", 1);
                put("Header3", 2);
            }}, null, 1);

    /**
     * Test method for {@link CSVRecord#get(Enum)} with valid values.
     */
    @Test
    public void testGetEnum() {
        assertEquals("value1", TEST_RECORD.get(Enum.valueOf("Header1")));
        assertEquals("value2", TEST_RECORD.get(Enum.valueOf("Header2")));
        assertEquals("value3", TEST_RECORD.get(Enum.valueOf("Header3")));
    }

    /**
     * Test method for {@link CSVRecord#get(Enum)} with invalid values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidEnum() {
        TEST_RECORD.get(Enum.valueOf("InvalidHeader"));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with valid index.
     */
    @Test
    public void testGetInt() {
        assertEquals("value1", TEST_RECORD.get(0));
        assertEquals("value2", TEST_RECORD.get(1));
        assertEquals("value3", TEST_RECORD.get(2));
    }

    /**
     * Test method for {@link CSVRecord#get(int)} with invalid index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetIntInvalidIndex() {
        TEST_RECORD.get(-1);
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with valid name.
     */
    @Test
    public void testGetString() {
        assertEquals("value1", TEST_RECORD.get("Header1"));
        assertEquals("value2", TEST_RECORD.get("Header2"));
        assertEquals("value3", TEST_RECORD.get("Header3"));
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with invalid name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStringInvalidName() {
        TEST_RECORD.get("InvalidHeader");
    }

    /**
     * Test method for {@link CSVRecord#get(String)} with missing name.
     */
    @Test
    public void testGetStringMissingName() {
        assertNull(TEST_RECORD.get("MissingHeader"));
    }

    /**
     * Test method for {@link CSVRecord#getComment()} with valid comment.
     */
    @Test
    public void testGetCommentValid() {
        CSVRecord record = new CSVRecord(null, new HashMap<>(), "Comment", 1);
        assertEquals("Comment", record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getComment()} with empty comment.
     */
    @Test
    public void testGetCommentEmpty() {
        CSVRecord record = new CSVRecord(null, new HashMap<>(), null, 1);
        assertNull(record.getComment());
    }

    /**
     * Test method for {@link CSVRecord#getRecordNumber()} with valid number.
     */
    @Test
    public void testGetRecordNumber() {
        assertEquals(1, TEST_RECORD.getRecordNumber());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with valid record.
     */
    @Test
    public void testIsConsistentValid() {
        assertTrue(TEST_RECORD.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isConsistent()} with inconsistent record.
     */
    @Test
    public void testIsConsistentInconsistent() {
        CSVRecord inconsistentRecord = new CSVRecord(new String[] { "value1", "value2" }, null, null, 1);
        assertFalse(inconsistentRecord.isConsistent());
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} with valid name.
     */
    @Test
    public void testIsMappedValid() {
        assertTrue(TEST_RECORD.isMapped("Header1"));
        assertTrue(TEST_RECORD.isMapped("Header2"));
        assertTrue(TEST_RECORD.isMapped("Header3"));
    }

    /**
     * Test method for {@link CSVRecord#isMapped(String)} with invalid name.
     */
    @Test
    public void testIsMappedInvalid() {
        assertFalse(TEST_RECORD.isMapped("InvalidHeader"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} with valid name.
     */
    @Test
    public void testIsSetValid() {
        assertTrue(TEST_RECORD.isSet("Header1"));
        assertTrue(TEST_RECORD.isSet("Header2"));
        assertTrue(TEST_RECORD.isSet("Header3"));
    }

    /**
     * Test method for {@link CSVRecord#isSet(String)} with invalid name.
     */
    @Test
    public void testIsSetInvalid() {
        assertFalse(TEST_RECORD.isSet("InvalidHeader"));
    }

    /**
     * Test method for {@link CSVRecord#iterator()} with valid record.
     */
    @Test
    public void testIteratorValid() {
        List<String> values = TEST_RECORD.toList();
        Iterator<String> iterator = TEST_RECORD.iterator();

        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), iterator.next());
        }

        assertEquals(values.size(), iterator.nextCount());
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)} with valid record and empty map.
     */
    @Test
    public void testPutInEmptyMap() {
        Map<String, String> map = new HashMap<>();
        TEST_RECORD.putIn(map);
        assertEquals(3, map.size());
        assertTrue(map.containsKey("Header1"));
        assertTrue(map.containsKey("Header2"));
        assertTrue(map.containsKey("Header3"));
    }

    /**
     * Test method for {@link CSVRecord#putIn(Map)} with valid record and non-empty map.
     */
    @Test
    public void testPutInNonEmptyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Header4", "value4");
        TEST_RECORD.putIn(map);
        assertEquals(6, map.size());
        assertTrue(map.containsKey("Header1"));
        assertTrue(map.containsKey("Header2"));
        assertTrue(map.containsKey("Header3"));
        assertTrue(map.containsKey("Header4"));
        assertTrue(map.containsValue("value4"));
        assertTrue(map.containsValue("value1"));
    }

    /**
     * Test method for {@link CSVRecord#size()} with valid record.
     */
    @Test
    public void testSizeValid() {
        assertEquals(3, TEST_RECORD.size());
    }

    /**
     * Test method for {@link CSVRecord#toList()} with valid record.
     */
    @Test
    public void testToListValid() {
        List<String> expectedValues = Arrays.asList("value1", "value2", "value3");
        List<String> values = TEST_RECORD.toList();

        assertEquals(expectedValues, values);
    }

    /**
     * Test method for {@link CSVRecord#toMap()} with valid record.
     */
    @Test
    public void testToMapValid() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Header1", "value1");
        expectedMap.put("Header2", "value2");
        expectedMap.put("Header3", "value3");

        Map<String, String> map = TEST_RECORD.toMap();

        assertEquals(expectedMap, map);
    }

    /**
     * Test method for {@link CSVRecord#toString()} with valid record.
     */
    @Test
    public void testToStringValid() {
        assertEquals("[value1, value2, value3]", TEST_RECORD.toString());
    }

    /**
     * Test method for {@link CSVRecord#values()} with valid record.
     */
    @Test
    public void testValuesValid() {
        String[] expectedValues = {"value1", "value2", "value3"};
        String[] values = TEST_RECORD.values();

        assertSame(expectedValues, values);
    }
}