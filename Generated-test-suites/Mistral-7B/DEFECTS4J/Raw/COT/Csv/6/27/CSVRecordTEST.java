package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CSVRecord}.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class CSVRecordTest {

    private static final String CSV_LINE = "header1,header2,comment\nvalue1,value2";

    private CSVParser parser;

    @Before
    public void setUp() {
        CSVFormat format = new CSVFormat();
        parser = format.newParser();
    }

    @After
    public void tearDown() {
        parser.close();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testCSVRecordConstruction() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertNull(record.getComment());
        assertEquals(-1, record.getRecordNumber());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isMapped("header3"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header3"));
        assertEquals(2, record.size());
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertNull(map.get("header3"));
    }

    /**
     * Test comment.
     */
    @Test
    public void testGetComment() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record = new CSVRecord(record.values(), record.mapping, "comment", 0);
        assertEquals("comment", record.getComment());
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("comment", record.getComment());
        assertEquals("header1", record.get("header1"));
        assertEquals("header2", record.get("header2"));
        assertNull(record.get("header3"));
        assertEquals(-1, record.getRecordNumber());
        assertFalse(record.isConsistent());
        assertFalse(record.isMapped("header1"));
        assertFalse(record.isMapped("header3"));
        assertFalse(record.isSet("header1"));
        assertFalse(record.isSet("header3"));
        assertEquals(2, record.size());
        record = new CSVRecord(record.values(), record.mapping, "comment", 0);
        assertEquals("comment", record.getComment());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
    }

    /**
     * Test get with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullName() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record.get(null);
    }

    /**
     * Test get with index out of bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithIndexOutOfBounds() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record.get(records.size());
    }

    /**
     * Test get with negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record.get(-1);
    }

    /**
     * Test get with enum.
     */
    @Test
    public void testGetWithEnum() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals("value1", record.get(Enum.ONE));
        assertEquals("value2", record.get(Enum.TWO));
        assertEquals("comment", record.getComment());
    }

    /**
     * Test record number.
     */
    @Test
    public void testGetRecordNumber() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals(0, record.getRecordNumber());
        record = new CSVRecord(record.values(), record.mapping, "comment", 1);
        assertEquals(1, record.getRecordNumber());
    }

    /**
     * Test is consistent.
     */
    @Test
    public void testIsConsistent() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertFalse(record.isConsistent());
        record = new CSVRecord(new String[]{"value1"}, null, null, 0);
        assertTrue(record.isConsistent());
    }

    /**
     * Test is mapped.
     */
    @Test
    public void testIsMapped() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertFalse(record.isMapped("header1"));
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        record = new CSVRecord(record.values(), mapping, null, 0);
        assertTrue(record.isMapped("header1"));
        assertFalse(record.isMapped("header3"));
    }

    /**
     * Test is set.
     */
    @Test
    public void testIsSet() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertFalse(record.isSet("header1"));
        record = new CSVRecord(new String[]{"value1", "value3"}, null, null, 0);
        assertTrue(record.isSet("header1"));
        assertFalse(record.isSet("header3"));
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        Iterator<String> iterator = record.iterator();
        List<String> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    /**
     * Test put in.
     */
    @Test
    public void testPutIn() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        Map<String, String> map = new HashMap<>();
        record.putIn(map);
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertNull(map.get("header3"));
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
    }

    /**
     * Test array conversion to list.
     */
    @Test
    public void testToList() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        List<String> values = record.toList();
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    /**
     * Test map conversion.
     */
    @Test
    public void testToMap() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        Map<String, String> map = record.toMap();
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertNull(map.get("header3"));
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        assertEquals("[value1, value2]", record.toString());
    }

    /**
     * Test values.
     */
    @Test
    public void testValues() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        String[] values = record.values();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    /**
     * Test null header mapping.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWithNullMapping() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record.get("header1");
    }

    /**
     * Test get with inconsistent mapping.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithInconsistentMapping() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 2);
        record = new CSVRecord(record.values(), mapping, null, 0);
        record.get("header1");
    }

    /**
     * Test get with null comment.
     */
    @Test
    public void testGetWithNullComment() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record = new CSVRecord(record.values(), record.mapping, null, 0);
        assertNull(record.getComment());
    }

    /**
     * Test get with incorrect record number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWithIncorrectRecordNumber() {
        List<CSVRecord> records = parser.parse(CSV_LINE);
        CSVRecord record = records.get(0);
        record = new CSVRecord(record.values(), record.mapping, null, Long.MAX_VALUE);
        record.get(0);
    }
}

class Enum {

    static final Enum ONE = new Enum("ONE");

    static final Enum TWO = new Enum("TWO");

    private final String name;

    public Enum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}