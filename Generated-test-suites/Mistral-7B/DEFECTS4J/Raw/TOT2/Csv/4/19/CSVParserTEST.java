package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.*;
import static org.apache.commons.csv.CSVRecord.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test cases for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\nMary,25"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals("Mary", record2.get(0));
        assertEquals(25, record2.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with a custom format.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCustomCSV() throws IOException {
        CSVFormat format = new CSVFormat(';', '\'\'');
        format.setHeader("ID", "Name", "Age");

        CSVParser parser = parse(new StringReader("ID;;Name;Age\n1;;John;30\n2;;Mary;25"), format);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("ID", record1.getColumnLabel(0));
        assertEquals("Name", record1.getColumnLabel(1));
        assertEquals("Age", record1.getColumnLabel(2));
        assertEquals(1, record1.get(0).intValue());
        assertEquals("John", record1.get(1));
        assertEquals(30, record1.get(2).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals("ID", record2.getColumnLabel(0));
        assertEquals("Name", record2.getColumnLabel(1));
        assertEquals("Age", record2.getColumnLabel(2));
        assertEquals(2, record2.get(0).intValue());
        assertEquals("Mary", record2.get(1));
        assertEquals(25, record2.get(2).intValue());
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\nMary,25"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Map<String, Integer> headerMap = record1.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertEquals("Name", headerMap.get("Name").toString());
        assertEquals("Age", headerMap.get("Age").toString());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals("John", record2.get(0));
        assertEquals(30, record2.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with a comment.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age#This is a comment\nJohn,30\nMary,25"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());
        assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals("Mary", record2.get(0));
        assertEquals(25, record2.get(1).intValue());
        assertEquals("#This is a comment", record2.getComment());
    }

    /**
     * Test parsing a CSV file with an empty record.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithEmptyRecord() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\n,\nMary,25"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals(null, record2.get(0));
        assertNull(record2.get(1));

        CSVRecord record3 = records.get(2);
        assertEquals("Name", record3.getColumnLabel(0));
        assertEquals("Age", record3.getColumnLabel(1));
        assertEquals("Mary", record3.get(0));
        assertEquals(25, record3.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with multiple empty records.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithMultipleEmptyRecords() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\n,\n,\nMary,25"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals(null, record2.get(0));
        assertNull(record2.get(1));

        CSVRecord record3 = records.get(2);
        assertEquals("Name", record3.getColumnLabel(0));
        assertEquals("Age", record3.getColumnLabel(1));
        assertEquals(null, record3.get(0));
        assertNull(record3.get(1));

        CSVRecord record4 = records.get(3);
        assertEquals("Name", record4.getColumnLabel(0));
        assertEquals("Age", record4.getColumnLabel(1));
        assertEquals("Mary", record4.get(0));
        assertEquals(25, record4.get(1).intValue());
    }

    /**
     * Test parsing a CSV file with a trailing empty line.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithTrailingEmptyLine() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\nMary,25\n"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));
        assertEquals("John", record1.get(0));
        assertEquals(30, record1.get(1).intValue());

        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.getColumnLabel(0));
        assertEquals("Age", record2.getColumnLabel(1));
        assertEquals("Mary", record2.get(0));
        assertEquals(25, record2.get(1).intValue());

        // Trailing empty line should not be included in the records
        assertEquals(2, records.size());
    }

    /**
     * Test parsing a CSV file with a malformed record.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCSVWithMalformedRecord() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30\nMary Age=25\n"), CSVFormat.DEFAULT);
        parser.getRecords();
        fail("Expected IOException");
    }

    /**
     * Test parsing a CSV file with a malformed record (missing comma).
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCSVWithMalformedRecordMissingComma() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30Mary\n"), CSVFormat.DEFAULT);
        parser.getRecords();
        fail("Expected IOException");
    }

    /**
     * Test parsing a CSV file with a malformed record (missing value).
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseCSVWithMalformedRecordMissingValue() throws IOException {
        CSVParser parser = parse(new StringReader("Name,Age\nJohn,30,\nMary\n"), CSVFormat.DEFAULT);
        parser.getRecords();
        fail("Expected IOException");
    }

    /**
     * Test parsing a CSV file with a null format object.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseCSVWithNullFormat() throws IOException {
        new CSVParser(new StringReader("Name,Age\nJohn,30\nMary,25"), null);
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test parsing a CSV file with a null reader.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseCSVWithNullReader() throws IOException {
        new CSVParser(null, CSVFormat.DEFAULT);
        fail("Expected IllegalArgumentException");
    }

    /**
     * Test parsing a CSV file with an empty string as a reader.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithEmptyStringReader() throws IOException {
        CSVParser parser = parse(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
    }

    /**
     * Test parsing a CSV file with a large number of records.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testParseCSVWithLargeNumberOfRecords() throws IOException {
        String largeCSVFile = "Name,Age\n" + Arrays.asList(new String[100000]).stream().map(s -> String.valueOf(Math.random())).collect(Collectors.joining("\n"))
                + "\n";

        CSVParser parser = parse(new StringReader(largeCSVFile), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(100001, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.getColumnLabel(0));
        assertEquals("Age", record1.getColumnLabel(1));

        CSVRecord recordLast = records.get(100000);
        assertEquals("Name", recordLast.getColumnLabel(0));
        assertEquals("Age", recordLast.getColumnLabel(1));
    }
}