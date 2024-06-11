package org.apache.commons.csv; // replace with your package name if needed

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CSVParser class
 */
public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25\nTom,45\n";
        parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        CSVParser emptyParser = parse(new StringReader(""), CSVFormat.DEFAULT);
        assertTrue(emptyParser.isEmpty());
    }

    @Test
    public void testParseCSVWithHeader() throws IOException {
        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT.withHeader());

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());

        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("name", record.get(0).getName());
        assertEquals("John", record.get(0).getValue(0));
        assertEquals("age", record.get(1).getName());
        assertEquals("30", record.get(1).getValue(0));

        record = records.get(1);
        assertEquals("Mary", record.get(0).getValue(0));
        assertEquals("25", record.get(1).getValue(0));

        record = records.get(2);
        assertEquals("Tom", record.get(0).getValue(0));
        assertEquals("45", record.get(1).getValue(0));
    }

    @Test
    public void testParseCSVWithoutHeader() throws IOException {
        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT.withoutHeader());

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());

        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals(0, record.size() - record.names().length);
        assertEquals("John", record.getValue(0));
        assertEquals("30", record.getValue(1));

        record = records.get(1);
        assertEquals("Mary", record.getValue(0));
        assertEquals("25", record.getValue(1));

        record = records.get(2);
        assertEquals("Tom", record.getValue(0));
        assertEquals("45", record.getValue(1));
    }

    @Test
    public void testParseCSVWithInvalidFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25\nTom,45\n,\n"; // an empty line

        try {
            parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseCSVWithMissingValues() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,\nTom,45\n"; // missing value for Mary

        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("John", record.get(0).getValue(0));
        assertEquals("30", record.get(1).getValue(0));

        record = records.get(1);
        assertNull(record.get(0).getValue(0));

        record = records.get(2);
        assertEquals("Tom", record.get(0).getValue(0));
        assertEquals("45", record.get(1).getValue(0));
    }

    @Test
    public void testParseCSVWithTrailingComments() throws IOException {
        String csvData = "name,age\nJohn,30#comment1\nMary,25#comment2\nTom,45#comment3\n";

        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("John", record.getValue(0));
        assertEquals("30", record.getValue(1));
        assertEquals("#comment1", record.getComment());

        record = records.get(1);
        assertEquals("Mary", record.getValue(0));
        assertEquals("25", record.getValue(1));
        assertEquals("#comment2", record.getComment());

        record = records.get(2);
        assertEquals("Tom", record.getValue(0));
        assertEquals("45", record.getValue(1));
        assertEquals("#comment3", record.getComment());
    }

    @Test
    public void testParseCSVWithQuotedValues() throws IOException {
        String csvData = "name,age\nJohn,\"John Doe\"\nMary,25\nTom,\"Tom, Smith\"\n";

        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("John Doe", record.getValue(0).getValue());
        assertEquals("John", record.getValue(0).getName());
        assertEquals("30", record.getValue(1).getValue());

        record = records.get(1);
        assertEquals("Mary", record.getValue(0).getName());
        assertEquals("25", record.getValue(1).getValue());

        record = records.get(2);
        assertEquals("Tom, Smith", record.getValue(0).getValue().replaceAll("\\s", ""));
        assertEquals("Tom", record.getValue(0).getName());
        assertEquals("45", record.getValue(1).getValue());
    }

    @Test
    public void testParseCSVWithEscapedValues() throws IOException {
        String csvData = "name,age\nJohn,\"John, Doe\"\nMary,25\nTom,\"Tom\\, Smith\"\n";

        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("John, Doe", record.getValue(0).getValue());
        assertEquals("John", record.getValue(0).getName());
        assertEquals("30", record.getValue(1).getValue());

        record = records.get(1);
        assertEquals("Mary", record.getValue(0).getName());
        assertEquals("25", record.getValue(1).getValue());

        record = records.get(2);
        assertEquals("Tom, Smith", record.getValue(0).getValue().replaceAll("\\s", ""));
        assertEquals("Tom", record.getValue(0).getName());
        assertEquals("45", record.getValue(1).getValue());
    }

    @Test
    public void testParseCSVWithMissingHeader() throws IOException {
        String csvData = "age,name\n30,John\n25,Mary\n45,Tom\n";

        CSVParser parser = parse(new StringReader(csvData), CSVFormat.DEFAULT.withoutHeader());
        List<CSVRecord> records = parser.getRecords();

        assertEquals(3, records.size());
        CSVRecord record = records.get(0);
        assertEquals("30", record.getValue(0).getValue());
        assertEquals("age", record.getValue(0).getName());
        assertEquals("John", record.getValue(1).getValue());

        record = records.get(1);
        assertEquals("25", record.getValue(0).getValue());
        assertEquals("Mary", record.getValue(1).getValue());

        record = records.get(2);
        assertEquals("45", record.getValue(0).getValue());
        assertEquals("Tom", record.getValue(1).getValue());
    }
}