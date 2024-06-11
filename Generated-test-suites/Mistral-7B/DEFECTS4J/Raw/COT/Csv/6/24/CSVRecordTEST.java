package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.test.utils.TestUtils;

/**
 * Test cases for {@link CSVRecord} class
 */
public class CSVRecordTest {

    @org.junit.Test
    public void testGetByEnum() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertSame("John Doe", record.get(Enum.NAME));
        assertSame("30", record.get(Enum.AGE));
        assertSame("New York", record.get(Enum.CITY));
        assertNull(record.get(Enum.COMMENT));
    }

    @org.junit.Test
    public void testGetByIndex() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertSame("John Doe", record.get(0));
        assertSame("30", record.get(1));
        assertSame("New York", record.get(2));
        assertNull(record.get(3));
    }

    @org.junit.Test
    public void testGetByName() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertSame("John Doe", record.get("Name"));
        assertSame("30", record.get("Age"));
        assertSame("New York", record.get("City"));
        assertNull(record.get("Comment"));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetByNameWhenNoHeader() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "John Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        record.get("Name");
    }

    @org.junit.Test
    public void testGetComment() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertNull(record.getComment());
    }

    @org.junit.Test
    public void testGetRecordNumber() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertEquals(0, record.getRecordNumber());
    }

    @org.junit.Test
    public void testIsConsistent() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertTrue(record.isConsistent());
    }

    @org.junit.Test
    public void testIsMapped() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertTrue(record.isMapped("Name"));
        assertTrue(record.isMapped("Age"));
        assertTrue(record.isMapped("City"));
        assertFalse(record.isMapped("Comment"));
    }

    @org.junit.Test
    public void testIsSet() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertTrue(record.isSet("Name"));
        assertTrue(record.isSet("Age"));
        assertTrue(record.isSet("City"));
        assertFalse(record.isSet("Comment"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetByNameWithInvalidName() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        record.get("InvalidName");
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetByIndexOutOfBounds() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        record.get(record.size() + 1);
    }

    @org.junit.Test
    public void testIteratorEquals() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record1 = records.get(0);

        Iterator<String> iterator = record1.iterator();
        String value = iterator.next();
        assertSame(value, record1.get(0));
        assertSame(iterator, record1.iterator());
    }

    @org.junit.Test
    public void testPutIn() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        Map<String, String> map = new HashMap<>();
        map = record.putIn(map);

        assertSame(record.get(Enum.NAME), map.get("Name"));
        assertSame(record.get(Enum.AGE), map.get("Age"));
        assertSame(record.get(Enum.CITY), map.get("City"));
        assertNull(map.get("Comment"));
    }

    @org.junit.Test
    public void testSize() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertEquals(record.size(), record.values().length);
    }

    @org.junit.Test
    public void testToString() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        assertEquals("[Name=John Doe, Age=30, City=New York]", record.toString());
    }

    @org.junit.Test
    public void testValues() {
        CSVFormat format = TestUtils.createDefaultFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City,Comment\nJohn Doe,30,New York,abc\n";
        List<CSVRecord> records = parser.parseAllRecords(csvText);
        CSVRecord record = records.get(0);

        String[] values = record.values();

        assertSame("John Doe", values[0]);
        assertSame("30", values[1]);
        assertSame("New York", values[2]);
        assertNull(values[3]);
    }

}

enum Enum {
    NAME, AGE, CITY
}