package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test CSVRecord class.
 */
public class CSVRecordTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values, csvRecord.values());
        assertEquals(mapping, csvRecord.mapping);
        assertEquals(comment, csvRecord.getComment());
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    /**
     * Test get method with Enum.
     */
    @Test
    public void testGetEnum() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", csvRecord.get(Enum.ONE));

        assertNull(csvRecord.get(Enum.TWO));
    }

    /**
     * Test get method with int.
     */
    @Test
    public void testGetInt() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", csvRecord.get(0));
        assertEquals("value2", csvRecord.get(1));
        assertNull(csvRecord.get(2));
    }

    /**
     * Test get method with String.
     */
    @Test
    public void testGetString() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("value1", csvRecord.get("name1"));
        assertEquals("value2", csvRecord.get("name2"));
        assertNull(csvRecord.get("nonExistentName"));
    }

    /**
     * Test isConsistent method.
     */
    @Test
    public void testIsConsistent() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(csvRecord.isConsistent());

        String[] valuesInconsistent = new String[]{"name1", "value1"};
        CSVRecord csvRecordInconsistent = new CSVRecord(valuesInconsistent, mapping, comment, recordNumber);
        assertFalse(csvRecordInconsistent.isConsistent());
    }

    /**
     * Test isMapped method.
     */
    @Test
    public void testIsMapped() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(csvRecord.isMapped("name1"));
        assertTrue(csvRecord.isMapped("name2"));
        assertFalse(csvRecord.isMapped("nonExistentName"));
    }

    /**
     * Test isSet method.
     */
    @Test
    public void testIsSet() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(csvRecord.isSet("nonExistentName"));
        assertTrue(csvRecord.isSet("name1"));
        assertTrue(csvRecord.isSet("name2"));
        assertFalse(csvRecord.isSet("name3"));
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> expectedList = new ArrayList<String>(Arrays.asList(values));

        Iterator<String> iterator = csvRecord.iterator();
        for (int i = 0; i < values.length; i++) {
            assertEquals(expectedList.get(i), iterator.next());
        }
    }

    /**
     * Test putIn method.
     */
    @Test
    public void testPutIn() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, String> map = new HashMap<String, String>();

        CSVRecord csvRecord = new CSVRecord(values, null, null, 0L);
        Map<String, String> expectedMap = new HashMap<String, String>(){{
            put("name1", "value1");
            put("name2", "value2");
        }};

        map = csvRecord.putIn(map);
        assertEquals(expectedMap, map);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, csvRecord.size());
    }

    /**
     * Test toList method.
     */
    @Test
    public void testToList() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> expectedList = Arrays.asList(values);

        List<String> list = csvRecord.toList();
        assertEquals(expectedList, list);
    }

    /**
     * Test toMap method.
     */
    @Test
    public void testToMap() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> expectedMap = new HashMap<String, String>(){{
            put("name1", "value1");
            put("name2", "value2");
        }};

        Map<String, String> map = csvRecord.toMap();
        assertEquals(expectedMap, map);
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, csvRecord.getComment());
        assertNull(csvRecord.getComment());
    }

    /**
     * Test getRecordNumber method.
     */
    @Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, csvRecord.getRecordNumber());
        assertThrows(IllegalStateException.class, () -> csvRecord.getRecordNumber());
    }

    /**
     * Test null mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testNullMapping() {
        String[] values = new String[]{"name1", "value1", "value2"};
        String comment = "comment";
        long recordNumber = 1L;

        new CSVRecord(values, null, comment, recordNumber);
    }

    /**
     * Test IllegalArgumentException with non-existent name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNonExistentName() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
            put("name3", 2);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        csvRecord.get("nonExistentName");
    }

    /**
     * Test IllegalArgumentException with inconsistent record.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInconsistentIndex() {
        String[] values = new String[]{"name1", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>(){{
            put("name1", 0);
            put("name2", 1);
        }};
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
        csvRecord.get(values.length + 1);
    }
}