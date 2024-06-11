package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Tests that CSVParser can parse a simple CSV file.
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        String csvText = "name,age\nMark,25\nJohn,30\n";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("name", firstRecord.getHeaderMap().get("name").toString());
        assertEquals("Mark", firstRecord.get("name"));
        assertEquals("age", firstRecord.getHeaderMap().get("age").toString());
        assertEquals(25, firstRecord.get("age").intValue());

        CSVRecord secondRecord = records.get(1);
        assertEquals("name", secondRecord.getHeaderMap().get("name").toString());
        assertEquals("John", secondRecord.get("name"));
        assertEquals("age", secondRecord.getHeaderMap().get("age").toString());
        assertEquals(30, secondRecord.get("age").intValue());
    }

    /**
     * Tests that CSVParser can parse a CSV file with an empty line.
     */
    @org.junit.Test
    public void testParseCSVWithEmptyLine() throws IOException {
        String csvText = "name,age\nMark,25\nJohn,30\n\n";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(2, records.size());
    }

    /**
     * Tests that CSVParser can parse a CSV file with a single record.
     */
    @org.junit.Test
    public void testParseCSVWithSingleRecord() throws IOException {
        String csvText = "name,age\nMark,25";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals("name", record.getHeaderMap().get("name").toString());
        assertEquals("Mark", record.get("name"));
        assertEquals("age", record.getHeaderMap().get("age").toString());
        assertEquals(25, record.get("age").intValue());
    }

    /**
     * Tests that CSVParser can parse a CSV file with a missing header.
     */
    @org.junit.Test
    public void testParseCSVWithoutHeader() throws IOException {
        String csvText = "Mark,25\nJohn,30";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertNull(firstRecord.getHeaderMap());
        assertEquals("Mark", firstRecord.get(0));
        assertEquals(25, firstRecord.get(1).intValue());

        CSVRecord secondRecord = records.get(1);
        assertNull(secondRecord.getHeaderMap());
        assertEquals("John", secondRecord.get(0));
        assertEquals(30, secondRecord.get(1).intValue());
    }

    /**
     * Tests that CSVParser can parse a CSV file with a commented line.
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        String csvText = "name,age\nMark,25\n# This is a comment\nJohn,30";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("name", firstRecord.getHeaderMap().get("name").toString());
        assertEquals("Mark", firstRecord.get("name"));
        assertEquals("age", firstRecord.getHeaderMap().get("age").toString());
        assertEquals(25, firstRecord.get("age").intValue());
        assertNull(firstRecord.getComment());

        CSVRecord secondRecord = records.get(1);
        assertEquals("name", secondRecord.getHeaderMap().get("name").toString());
        assertEquals("John", secondRecord.get("name"));
        assertEquals("age", secondRecord.getHeaderMap().get("age").toString());
        assertEquals(30, secondRecord.get("age").intValue());
        assertEquals("# This is a comment\n", secondRecord.getComment());
    }

    /**
     * Tests that CSVParser can parse a CSV file with escaped values.
     */
    @org.junit.Test
    public void testParseCSVWithEscapedValues() throws IOException {
        String csvText = "name,value\nMark,\"Mark,Jones\"\nJohn,\"John Doe\"";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertSame(2, records.size());
        CSVRecord firstRecord = records.get(0);
        assertEquals("name", firstRecord.getHeaderMap().get("name").toString());
        assertEquals("Mark", firstRecord.get("name"));
        assertEquals("value", firstRecord.getHeaderMap().get("value").toString());
        assertEquals("Mark,Jones", firstRecord.get("value"));

        CSVRecord secondRecord = records.get(1);
        assertEquals("name", secondRecord.getHeaderMap().get("name").toString());
        assertEquals("John", secondRecord.get("name"));
        assertEquals("value", secondRecord.getHeaderMap().get("value").toString());
        assertEquals("John Doe", secondRecord.get("value"));
    }

    /**
     * Tests that CSVParser handles invalid CSV files.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvText = "name,age\nMark,25\nJohn,30\nMark,\n";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    /**
     * Tests that CSVParser handles missing records.
     */
    @org.junit.Test(expected = NoSuchElementException.class)
    public void testParseMissingRecord() throws IOException {
        String csvText = "name,age\nMark,25\n";
        CSVParser parser = parse(new StringReader(csvText), CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord(); // This should throw an exception
    }
}