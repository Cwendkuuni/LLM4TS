package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecord.EmptyCSVRecord;
import org.apache.commons.csv.CSVRecordImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;

/**
 * Test class for CSVRecord class
 *
 * @author <Your Name>
 */
public class CSVRecordTest {

    // Basic test cases for get methods

    @Test
    public void testGetEnum() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertEquals("John", record.get(Enum.NAME));
    }

    @Test
    public void testGetInt() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertEquals("30", record.get(0));
    }

    @Test
    public void testGetString() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertEquals("John", record.get(0));
        assertEquals("New York", record.get(2));
    }

    @Test
    public void testGetComment() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York#This is a comment";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertEquals("This is a comment", record.getComment());
    }

    @Test
    public void testGetRecordNumber() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertEquals(0, record.getRecordNumber());
    }

    @Test
    public void testIsConsistent() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertTrue(record.isConsistent());
    }

    @Test
    public void testIsMapped() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nName,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("Age"));
    }

    @Test
    public void testIsSet() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nName,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        assertTrue(record.isSet("Name"));
        assertFalse(record.isSet("Age"));
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullMapping() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);
        record.mapping = null;

        try {
            record.get("Name");
        } catch (IllegalArgumentException e) {
            assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownName() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        try {
            record.get("UnknownName");
        } catch (IllegalArgumentException e) {
            assertEquals("Mapping for UnknownName not found, expected one of Name, Age, City", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        String csvText = "Name,Age,City\nJohn,30,New York";
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(csvText));
        CSVRecord record = records.get(0);

        record.mapping = new HashMap<String, Integer>() {{
            put("Name", 0);
            put("Age", 1);
            put("City", 2);
        }};

        try {
            record.get(3);
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Index for header 'City' is 2 but CSVRecord only has 3 values!", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testEmptyCSVRecord() {
        CSVFormat format = new CSVFormat();
        CSVParser parser = format.newParser();
        List<CSVRecord> records = parser.parseAllRecords(new StringReader(""));

        CSVRecord record = records.get(0);

        assertEquals(EmptyCSVRecord.class, record.getClass());
        assertNull(record.comment);
        assertNull(record.mapping);
        assertArrayEquals(new String[0], record.values());
    }
}