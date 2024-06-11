package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.apache.commons.csv.util.Assertions;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvFile = new File("test-data/csv-parser-test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }
    }

    @Test
    public void testParseURL() throws IOException {
        String urlString = "https://example.com/test-data/csv-parser-test.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }
    }

    @Test
    public void testParseReader() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";
        Reader reader = new StringReader(csvData);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }
    }

    @Test
    public void testParsingIntoMemory() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
    }

    @Test(expected = IOException.class)
    public void testParsingNullFile() throws IOException {
        File csvFile = null;
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testParsingNullString() throws IOException {
        String csvData = null;
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParsingInvalidFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Chicago";
        CSVFormat invalidFormat = new CSVFormat(new String[]{"Name", "Age", "City"}, null, null, null, null, null, false, false);
        CSVParser parser = new CSVParser(new StringReader(csvData), invalidFormat);
    }

    @Test(expected = IOException.class)
    public void testParsingInvalidCharset() throws IOException {
        URL url = new URL("https://example.com/test-data/csv-parser-test.csv");
        Charset invalidCharset = null;
        CSVParser parser = CSVParser.parse(url, CSVFormat.DEFAULT, invalidCharset);
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIterable() {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
        iterator.hasNext();
    }
}