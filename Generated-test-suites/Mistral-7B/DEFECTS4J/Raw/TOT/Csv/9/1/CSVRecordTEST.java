package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing {@link CSVRecord}.
 */
public class CSVRecordTest {

    private static final String CSV_WITH_COMMENT = "name,age,comment\nAlice,25,\"This is a comment for Alice\"\nBob,30\n";
    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setup() throws Exception {
        format = new CSVFormat();
        parser = new CSVParser(getClass().getResourceAsStream("/testCSV.csv"), format);
    }

    @After
    public void teardown() throws Exception {
        parser.close();
    }

    /**
     * Test get String by enum.
     */
    @Test
    public void testGetByEnum() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        Enum<String> age = Enum.valueOf("age", String.class);
        assertEquals("25", firstRecord.get(age));
    }

    /**
     * Test get String by index.
     */
    @Test
    public void testGetByIndex() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals("Alice", firstRecord.get(0));
        assertEquals("25", firstRecord.get(1));
    }

    /**
     * Test get String by name.
     */
    @Test
    public void testGetByName() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals("Alice", firstRecord.get("name"));
        assertEquals("25", firstRecord.get("age"));
        assertNull(firstRecord.get("comment")); // no comment for the first record
    }

    /**
     * Test get comment.
     */
    @Test
    public void testGetComment() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals("This is a comment for Alice", firstRecord.getComment());
        assertNull(secondRecord.getComment()); // no comment for the second record
    }

    /**
     * Test get record number.
     */
    @Test
    public void testGetRecordNumber() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals(1, firstRecord.getRecordNumber());
        assertEquals(2, secondRecord.getRecordNumber());
    }

    /**
     * Test is consistent.
     */
    @Test
    public void testIsConsistent() {
        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        assertTrue(firstRecord.isConsistent());

        CSVRecord secondRecord = records.get(1);
        assertTrue(secondRecord.isConsistent());
    }

    /**
     * Test is mapped.
     */
    @Test
    public void testIsMapped() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertTrue(firstRecord.isMapped("name"));
        assertTrue(firstRecord.isMapped("age"));
        assertFalse(firstRecord.isMapped("comment"));
    }

    /**
     * Test is set.
     */
    @Test
    public void testIsSet() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertTrue(firstRecord.isSet("name"));
        assertTrue(firstRecord.isSet("age"));
        assertFalse(firstRecord.isSet("comment"));
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        List<String> values = firstRecord.iterator().toList();

        assertEquals(Arrays.asList("Alice", "25"), values);
    }

    /**
     * Test putIn.
     */
    @Test
    public void testPutIn() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Map<String, String> map = new HashMap<>();
        firstRecord.putIn(map);

        assertEquals("Alice", map.get("name"));
        assertEquals("25", map.get("age"));
        assertNull(map.get("comment"));
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        assertEquals(2, firstRecord.size());
    }

    /**
     * Test toList.
     */
    @Test
    public void testToList() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        List<String> values = firstRecord.toList();

        assertEquals(Arrays.asList("Alice", "25"), values);
    }

    /**
     * Test toMap.
     */
    @Test
    public void testToMap() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        Map<String, String> map = firstRecord.toMap();

        assertEquals("Alice", map.get("name"));
        assertEquals("25", map.get("age"));
        assertNull(map.get("comment"));
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);

        String result = firstRecord.toString();

        String expected = "[Alice, 25]";
        assertSame(expected, result);
    }
}