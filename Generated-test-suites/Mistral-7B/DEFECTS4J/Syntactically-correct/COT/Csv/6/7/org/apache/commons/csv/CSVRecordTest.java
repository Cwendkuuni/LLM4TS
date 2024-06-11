package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    private CSVFormat csvFormat;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @Before
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        values = new String[]{"name1", "age1", "gender1", "comment1"};
        mapping = new HashMap<String, Integer>() {{
            put("name", 0);
            put("age", 1);
            put("gender", 2);
            put("comment", 3);
        }};
        comment = "comment1";
        recordNumber = 1;
    }

    @Test
    public void testCSVRecordInstance() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertSame(values, record.values());
        assertSame(mapping, record.mapping);
        assertSame(comment, record.getComment());
        assertSame(recordNumber, record.getRecordNumber());
        assertFalse(record.isConsistent());
    }

    @Test
    public void testGetByEnum() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("name1", record.get(CSVRecordTest.class.getEnumConstants()[0]));
    }

    @Test
    public void testGetByIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("name1", record.get(0));
    }

    @Test
    public void testGetByName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("name1", record.get("name"));
        assertEquals("comment1", record.get("comment"));
        assertNull(record.get("nonexistentcolumn"));
    }

    @Test
    public void testGetComment() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("comment1", record.getComment());
        assertNull(record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());

        mapping = null;
        record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        values = new String[3];
        record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("nonexistentcolumn"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("nonexistentcolumn"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> list = record.iterator().toList();
        assertArrayEquals(values, list.toArray());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);
        List<String> list = Arrays.asList(map.values().toArray(new String[0]));
        assertArrayEquals(values, list.toArray());
    }

    @Test
    public void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, record.size());
    }

    @Test
    public void testToList() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        List<String> list = record.toList();
        assertArrayEquals(values, list.toArray());
    }

    @Test
    public void testToMap() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();
        List<String> list = Arrays.asList(map.values().toArray(new String[0]));
        assertArrayEquals(values, list.toArray());
    }

    @Test
    public void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("[name1, age1, gender1, comment1]", record.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInvalidIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        record.get("nonexistentcolumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentRecord() {
        String[] valuesInconsistent = new String[2];
        CSVRecord record = new CSVRecord(valuesInconsistent, mapping, comment, recordNumber);
        record.get("name");
    }
}