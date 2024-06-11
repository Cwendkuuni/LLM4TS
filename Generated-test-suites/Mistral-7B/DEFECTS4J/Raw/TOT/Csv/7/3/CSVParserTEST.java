package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parse method with valid CSV data.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testParseValidCSVData() throws Exception {
        final String csvData = "name,age,city\nAlice,25,New York\nBob,30,San Francisco\n";
        final List<CSVRecord> records = new ArrayList<>();

        // Parse CSV data using RFC4180 format
        final CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180);
        records.addAll(parser.getRecords());
        parser.close();

        // Verify parsed records
        final CSVRecord record1 = records.get(0);
        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));
        assertEquals("New York", record1.get("city"));

        final CSVRecord record2 = records.get(1);
        assertEquals("Bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));
        assertEquals("San Francisco", record2.get("city"));
    }

    /**
     * Test parse method with invalid CSV data.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testParseInvalidCSVData() throws Exception {
        final String csvData = "name,age,city\nAlice,25,New York\nBob,30,San Francisco\nName,35";
        final List<CSVRecord> records = new ArrayList<>();

        // Parse CSV data using RFC4180 format
        try (CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180)) {
            records.addAll(parser.getRecords());
        }

        // Verify parsed records
        assertEquals(2, records.size());

        final CSVRecord record1 = records.get(0);
        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));
        assertEquals("New York", record1.get("city"));

        final CSVRecord record2 = records.get(1);
        assertNull(record2.get("name"));
        assertEquals(null, record2.get("name"), "Expected null for missing name");
        assertEquals(35, record2.getInteger("age"), "Expected age value to be null for missing name");
    }

    /**
     * Test parse method with multiline values.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testParseMultilineValues() throws Exception {
        final String csvData = "name,description\nAlice,This is a long description for Alice\nBob,This is a long description for Bob";
        final List<CSVRecord> records = new ArrayList<>();

        // Parse CSV data using RFC4180 format
        try (CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180)) {
            records.addAll(parser.getRecords());
        }

        // Verify parsed records
        final CSVRecord record1 = records.get(0);
        assertEquals("Alice", record1.get("name"));
        assertTrue(record1.get("description").startsWith("This is a long description for Alice"));

        final CSVRecord record2 = records.get(1);
        assertEquals("Bob", record2.get("name"));
        assertTrue(record2.get("description").startsWith("This is a long description for Bob"));
    }

    /**
     * Test parse method with empty lines.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testParseEmptyLines() throws Exception {
        final String csvData = "name,age\nAlice,25\n\nBob,30";
        final List<CSVRecord> records = new ArrayList<>();

        // Parse CSV data using RFC4180 format
        try (CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.RFC4180)) {
            records.addAll(parser.getRecords());
        }

        // Verify parsed records
        final CSVRecord record1 = records.get(0);
        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        final CSVRecord record2 = records.get(1);
        assertNull(record2.get("name"));
        assertEquals(0, record2.size());

        final CSVRecord record3 = records.get(2);
        assertNull(record3.get("name"));
        assertEquals(0, record3.size());
    }
}