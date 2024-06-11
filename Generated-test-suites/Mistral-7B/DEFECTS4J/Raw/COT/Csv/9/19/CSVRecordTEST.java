package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.test.util.CSVRecordTestUtils;

/**
 * Test cases for {@link CSVRecord}.
 */
public class CSVRecordTest {

    @org.junit.Test
    public void testCSVRecordGetByEnum() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals("John Doe", record.get(Enum.ONE));
        assertEquals("Jane Doe", record.get(Enum.TWO));
        assertEquals("Jim Doe", record.get(Enum.THREE));
        assertEquals("Jack Doe", record.get(Enum.FOUR));
        assertNull(record.get(Enum.FIVE));
    }

    @org.junit.Test
    public void testCSVRecordGetByIndex() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals("John Doe", record.get(0));
        assertEquals("Jane Doe", record.get(1));
        assertEquals("Jim Doe", record.get(2));
        assertEquals("Jack Doe", record.get(3));
    }

    @org.junit.Test
    public void testCSVRecordGetByName() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals("John Doe", record.get("name1"));
        assertEquals("Jane Doe", record.get("name2"));
        assertEquals("Jim Doe", record.get("name3"));
        assertEquals("Jack Doe", record.get("name4"));
        assertNull(record.get("name5"));
    }

    @org.junit.Test
    public void testCSVRecordGetComment() {
        CSVFormat format = new CSVFormat();
        String comment = "This is a comment";
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4,comment\nJohn Doe,Jane Doe,Jim Doe,Jack Doe," + comment + "\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals(comment, record.getComment());
        assertNull(CSVRecordTestUtils.recordWithoutComment(parser).getComment());
    }

    @org.junit.Test
    public void testCSVRecordGetRecordNumber() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals(1, record.getRecordNumber());
        parser.parseNextRecord();
        assertEquals(2, record.getRecordNumber());
        assertFalse(parser.hasNextRecord());
    }

    @org.junit.Test
    public void testCSVRecordIsConsistent() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3,name4\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertTrue(record.isConsistent());
        String inconsistentCsv = "name1,name2,name3,name4,name5\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord inconsistentRecord = parser.parseNextRecord();

        assertFalse(inconsistentRecord.isConsistent());
    }

    @org.junit.Test
    public void testCSVRecordIsMapped() {
        CSVFormat format = new CSVFormat();
        String header = "name1,name2,name3";
        CSVParser parser = format.newParser(header);
        String csv = header + "\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertTrue(record.isMapped("name1"));
        assertTrue(record.isMapped("name2"));
        assertTrue(record.isMapped("name3"));
        assertFalse(record.isMapped("name4"));
    }

    @org.junit.Test
    public void testCSVRecordIsSet() {
        CSVFormat format = new CSVFormat();
        String header = "name1,name2,name3";
        CSVParser parser = format.newParser(header);
        String csv = header + "\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertTrue(record.isSet("name1"));
        assertTrue(record.isSet("name2"));
        assertTrue(record.isSet("name3"));
        assertFalse(record.isSet("name4"));
    }

    @org.junit.Test
    public void testCSVRecordIterator() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        List<String> values = Arrays.asList("John Doe", "Jane Doe", "Jim Doe");
        Iterator<String> iterator = record.iterator();

        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testCSVRecordPutIn() {
        CSVFormat format = new CSVFormat();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("name1", "John Doe");
        expectedMap.put("name2", "Jane Doe");
        expectedMap.put("name3", "Jim Doe");

        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        Map<String, String> map = record.putIn(new HashMap<>());
        assertEquals(expectedMap, map);
    }

    @org.junit.Test
    public void testCSVRecordSize() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals(3, record.size());
    }

    @org.junit.Test
    public void testCSVRecordToList() {
        CSVFormat format = new CSVFormat();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = new CSVRecord(null, null, null, 1);
        record.values = parser.parseNextRecord().values;

        List<String> expectedList = Arrays.asList("John Doe", "Jane Doe", "Jim Doe");
        List<String> list = record.toList();

        assertEquals(expectedList, list);
    }

    @org.junit.Test
    public void testCSVRecordToString() {
        CSVFormat format = new CSVFormat();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        assertEquals("[John Doe, Jane Doe, Jim Doe]", record.toString());
    }

    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCSVRecordGetByIndexOutOfBounds() {
        CSVFormat format = new CSVFormat();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        record.get(record.values.length);
        fail(); // should not be reached
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetByNameNotFound() {
        CSVFormat format = new CSVFormat();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe\n";
        CSVRecord record = parser.parseNextRecord();

        record.get("name4");
        fail(); // should not be reached
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVRecordGetByNameMismatchMappingSize() {
        CSVFormat format = new CSVFormat();
        String csv = "name1,name2,name3\nJohn Doe,Jane Doe,Jim Doe,Jack Doe\n";
        CSVRecord record = parser.parseNextRecord();

        record.get("name4");
        fail(); // should not be reached
    }

}

enum Enum {
    ONE, TWO, THREE, FOUR, FIVE
}