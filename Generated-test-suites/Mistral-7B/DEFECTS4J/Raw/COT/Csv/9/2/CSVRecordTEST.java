package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

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
 * Test class for CSVRecord
 */
public class CSVRecordTest {

    private CSVFormat csvFormat;
    private CSVParser csvParser;

    @Before
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT.withHeader("name", "age", "comment");
        csvParser = new CSVParser(csvFormat.parse("name,age,comment\nAlice,25,comment1\nBob,30,comment2\n"));
    }

    @After
    public void tearDown() throws Exception {
        csvParser.close();
    }

    @Test
    public void testConstructor() {
        CSVRecord record = csvParser.nextRecord();
        CSVRecord testRecord = new CSVRecord(record.values, record.mapping, record.comment, record.recordNumber);
        assertSame(record, testRecord);
    }

    @Test
    public void testGetEnum() {
        CSVRecord record = csvParser.nextRecord();
        String result = record.get(Enum.class, "name");
        String expected = record.get("name");
        assertSame(expected, result);
    }

    @Test
    public void testGetInt() {
        CSVRecord record = csvParser.nextRecord();
        String result = record.get(0);
        String expected = record.get("name");
        assertSame(expected, result);
    }

    @Test
    public void testGetString() {
        CSVRecord record = csvParser.nextRecord();
        String result = record.get("name");
        String expected = "Alice";
        assertSame(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringWithNoMapping() {
        CSVRecord record = new CSVRecord(new String[]{"Alice", "25"}, null, null, 0);
        record.get("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringWithInvalidIndex() {
        CSVRecord record = csvParser.nextRecord();
        record.get(values.length);
    }

    @Test
    public void testGetComment() {
        CSVRecord record = csvParser.nextRecord();
        String result = record.getComment();
        String expected = "comment1";
        assertSame(expected, result);
    }

    @Test
    public void testGetRecordNumber() {
        CSVRecord record = csvParser.nextRecord();
        long result = record.getRecordNumber();
        long expected = 1;
        assertSame(expected, result);
    }

    @Test
    public void testIsConsistent() {
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();
        assertTrue(record1.isConsistent());
        assertTrue(record2.isConsistent());
        CSVRecord record3 = new CSVRecord(new String[]{"Alice"}, null, null, 0);
        assertFalse(record3.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVRecord record = csvParser.nextRecord();
        assertTrue(record.isMapped("name"));
        assertFalse(record.isMapped("age2"));
    }

    @Test
    public void testIsSet() {
        CSVRecord record = csvParser.nextRecord();
        assertTrue(record.isSet("name"));
        assertFalse(record.isSet("age2"));
    }

    @Test
    public void testIterator() {
        CSVRecord record = csvParser.nextRecord();
        List<String> expected = Arrays.asList("Alice", "25", "comment1");
        List<String> result = Arrays.asList(record.iterator().toArray());
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testPutIn() {
        CSVRecord record = csvParser.nextRecord();
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "Alice");
        expected.put("age", "25");
        expected.put("comment", "comment1");
        Map<String, String> result = record.putIn(new HashMap<String, String>());
        assertArrayEquals(expected.toArray(new HashMap[0].toArray()), result.toArray(new HashMap[0].toArray()));
    }

    @Test
    public void testSize() {
        CSVRecord record = csvParser.nextRecord();
        int result = record.size();
        int expected = 3;
        assertSame(expected, result);
    }

    @Test
    public void testToList() {
        CSVRecord record = csvParser.nextRecord();
        List<String> expected = Arrays.asList("Alice", "25", "comment1");
        List<String> result = record.toList();
        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testToMap() {
        CSVRecord record = csvParser.nextRecord();
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "Alice");
        expected.put("age", "25");
        expected.put("comment", "comment1");
        Map<String, String> result = record.toMap();
        assertArrayEquals(expected.toArray(new HashMap[0].toArray()), result.toArray(new HashMap[0].toArray()));
    }

    @Test
    public void testToString() {
        CSVRecord record = csvParser.nextRecord();
        String result = record.toString();
        String expected = "[Alice, 25, comment1]";
        assertSame(expected, result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValuesWithInvalidIndex() {
        CSVRecord record = csvParser.nextRecord();
        record.values(values.length);
    }
}