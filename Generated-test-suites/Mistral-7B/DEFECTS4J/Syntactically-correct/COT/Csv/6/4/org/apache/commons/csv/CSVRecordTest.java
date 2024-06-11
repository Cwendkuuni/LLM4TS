package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(Arrays.toString(values), record.toString());
        assertEquals(mapping, record.mapping);
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    /**
     * Test get method
     */
    @org.junit.Test
    public void testGet() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get(0));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get(1));
        assertEquals("value2", record.get("header2"));
        assertEquals(null, record.get("nonExistentHeader"));
        assertNull(record.get(2));
    }

    /**
     * Test get method with empty values array
     */
    @org.junit.Test
    public void testGetWithEmptyValues() {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(new String[0], mapping, null, 1L);

        assertNull(record.get(0));
        assertNull(record.get("header1"));
        assertNull(record.get("header2"));
    }

    /**
     * Test get method with null mapping
     */
    @org.junit.Test
    public void testGetWithNullMapping() {
        String[] values = new String[]{"value1", "value2"};

        CSVRecord record = new CSVRecord(values, null, null, 1L);
        assertNull(record.get(0));
        assertNull(record.get("header1"));
        assertNull(record.get("header2"));
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    /**
     * Test isConsistent method
     */
    @org.junit.Test
    public void testIsConsistent() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);

        record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isConsistent());

        mapping = new HashMap<>();
        mapping.put("header1", 0);

        record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isConsistent());

        mapping = new HashMap<>();
        record = new CSVRecord(new String[0], mapping, null, 1L);
        assertTrue(record.isConsistent());
    }

    /**
     * Test isMapped method
     */
    @org.junit.Test
    public void testIsMapped() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertFalse(record.isMapped("nonExistentHeader"));
    }

    /**
     * Test getComment method
     */
    @org.junit.Test
    public void testGetComment() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);
        assertEquals("comment", record.getComment());
        assertNull(new CSVRecord(values, mapping, null, 1L).getComment());
    }

    /**
     * Test getRecordNumber method
     */
    @org.junit.Test
    public void testGetRecordNumber() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(1L, record.getRecordNumber());
        assertNotEquals(2L, record.getRecordNumber());
    }

    /**
     * Test iterator method
     */
    @org.junit.Test
    public void testIterator() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        List<String> list = record.toList();

        Iterator<String> it = record.iterator();
        assertEquals(list.get(0), it.next());
        assertEquals(list.get(1), it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Test putIn method
     */
    @org.junit.Test
    public void testPutIn() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, String> map = new HashMap<>();

        CSVRecord record = new CSVRecord(values, null, null, 1L);
        map.putAll(record.putIn(map));

        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertNull(map.get("nonExistentHeader"));
    }

    /**
     * Test size method
     */
    @org.junit.Test
    public void testSize() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertEquals(2, record.size());
    }

    /**
     * Test get method with Enum
     */
    @org.junit.Test
    public void testGetWithEnum() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertEquals("value1", record.get(Header.HEADER1));
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get(Header.HEADER2));
        assertEquals("value2", record.get("header2"));
        assertNull(record.get(Header.NON_EXISTENT_HEADER));

        enum Header {
            HEADER1, HEADER2, NON_EXISTENT_HEADER
        }
    }

    /**
     * Test isSet method
     */
    @org.junit.Test
    public void testIsSet() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertFalse(record.isSet("nonExistentHeader"));
    }

    /**
     * Test clone method
     */
    @org.junit.Test
    public void testClone() throws CloneNotSupportedException {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        CSVRecord originalRecord = new CSVRecord(values, mapping, null, 1L);
        CSVRecord clonedRecord = (CSVRecord) originalRecord.clone();

        assertEquals(originalRecord.toString(), clonedRecord.toString());
        assertEquals(originalRecord.getRecordNumber(), clonedRecord.getRecordNumber());
        assertEquals(originalRecord.mapping, clonedRecord.mapping);
        assertNotSame(originalRecord, clonedRecord);
    }
}