package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord.
 */
public class CSVRecordTest {

    @Test
    public void testCSVRecord_DefaultConstructor() {
        CSVRecord record = new CSVRecord(null, null, null, 0L);
        assertEquals(CSVRecord.EMPTY_STRING_ARRAY, record.values());
        assertNull(record.comment);
        assertNull(record.mapping);
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("headerName"));
        assertFalse(record.isSet("headerName"));
        assertEquals(0, record.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVRecord_InvalidMappingSize() {
        String[] values = new String[]{"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
    }

    @Test
    public void testCSVRecord_ValidConstructor() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        assertEquals(values, record.values());
        assertEquals("header1", record.get("header1"));
        assertEquals("header2", record.get("header2"));
        assertEquals("header3", record.get("header3"));
        assertTrue(record.isConsistent());
        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));
        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));
        assertEquals(3, record.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_InvalidName() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        record.get("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_MissingMapping() {
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);
        record.get("header3"); // This test passes, but the following line should cause an exception
        record.get("header4");
    }

    @Test
    public void testGetComment() {
        String comment = "A comment for this record";
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, comment, 0L);
        assertEquals(comment, record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        long recordNumber = 1L;
        String[] values = new String[]{"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        String[] values = {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record1 = new CSVRecord(values, mapping, null, 0L);
        CSVRecord record2 = new CSVRecord(values, mapping, null, 0L);
        CSVRecord record3 = new CSVRecord(new String[values.length + 1], mapping, null, 0L);

        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());
        assertFalse(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isMapped("header1"));
        assertTrue(record.isMapped("header2"));
        assertTrue(record.isMapped("header3"));

        assertFalse(record.isMapped("nonExistentHeader"));
    }

    @Test
    public void testIsSet() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertTrue(record.isSet("header1"));
        assertTrue(record.isSet("header2"));
        assertTrue(record.isSet("header3"));

        assertFalse(record.isSet("nonExistentHeader"));
    }

    @Test
    public void testIterator() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        List<String> valuesList = record.toList();
        Iterator<String> iterator = record.iterator();

        assertEquals(valuesList.get(0), iterator.next());
        assertEquals(valuesList.get(1), iterator.next());
        assertEquals(valuesList.get(2), iterator.next());
    }

    @Test
    public void testToMap() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> map = record.toMap();

        assertEquals(values[0], map.get("header1"));
        assertEquals(values[1], map.get("header2"));
        assertEquals(values[2], map.get("header3"));

        assertNull(map.get("nonExistentHeader"));
    }

    @Test
    public void testToString() {
        String[] values = {"value1", "value2", "value3"};
        Map<String, Integer> mapping = new HashMap<String, Integer>() {{
            put("header1", 0);
            put("header2", 1);
            put("header3", 2);
        }};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        assertEquals("[value1, value2, value3]", record.toString());
    }
}