package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.exceptions.CsvException;
import org.apache.commons.csv.exceptions.CsvParserConfigurationException;
import org.apache.commons.csv.test.util.TestUtil;
import org.junit.Test;

/**
 * Test cases for CSVParser class.
 */
public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String csvFile = "data/test.csv";
        CSVParser parser = parse(new File(csvFile), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());
        for (CSVRecord record : records) {
            TestUtil.validateRecord(record);
        }
        parser.close();
    }

    @Test
    public void testParseInvalidFile() throws IOException {
        String csvFile = "data/invalid.csv";
        assertThrows(IOException.class, () -> parse(new File(csvFile), DEFAULT));
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nAlice,25\nBob,30";
        CSVParser parser = parse(new StringReader(csvString), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            TestUtil.validateRecord(record);
        }
        parser.close();
    }

    @Test
    public void testParseInvalidString() throws IOException {
        String csvString = "name,age\nAlice,25\nBob,30abcd";
        assertThrows(CsvException.class, () -> parse(new StringReader(csvString), DEFAULT));
    }

    @Test
    public void testParseUrl() throws IOException {
        String csvUrl = "https://example.com/data.csv";
        CSVParser parser = parse(new URL(csvUrl), StandardCharsets.UTF_8, DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        // Assuming there are records in the URL
        for (CSVRecord record : records) {
            TestUtil.validateRecord(record);
        }
        parser.close();
    }

    @Test
    public void testParseInvalidUrl() throws IOException {
        String csvUrl = "https://example.com/data.csv#invalid";
        assertThrows(IOException.class, () -> parse(new URL(csvUrl), StandardCharsets.UTF_8, DEFAULT));
    }

    @Test
    public void testParseReader() throws IOException {
        String csvString = "name,age\nAlice,25\nBob,30";
        Reader reader = new StringReader(csvString);
        CSVParser parser = new CSVParser(reader, DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        for (CSVRecord record : records) {
            TestUtil.validateRecord(record);
        }
        parser.close();
    }

    @Test
    public void testNextRecordNull() throws IOException {
        String csvString = "name,age\nAlice,25";
        CSVParser parser = parse(new StringReader(csvString), DEFAULT);
        CSVRecord record = parser.nextRecord();
        TestUtil.validateRecord(record);
        assertNull(parser.nextRecord());
        parser.close();
    }

    @Test
    public void testGetRecordsEmpty() throws IOException {
        String csvString = "";
        CSVParser parser = parse(new StringReader(csvString), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
        parser.close();
    }

    @Test
    public void testGetRecordsNullFormat() throws IOException {
        String csvString = "name,age\nAlice,25";
        assertThrows(IllegalArgumentException.class, () -> parse(new StringReader(csvString), null));
    }

    @Test
    public void testGetRecordsInvalidFormat() throws IOException {
        String csvString = "name,age\nAlice,25";
        CSVFormat invalidFormat = new CSVFormatBuilder().setHeaderNames(Arrays.asList("name", "age")).build();
        invalidFormat.setHeaderSuppressed(Collections.singleton("age"));
        CSVParser parser = parse(new StringReader(csvString), invalidFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        TestUtil.validateRecord(record);
        assertNull(record.get(1));
        parser.close();
    }

    @Test
    public void testGetRecordsInvalidReader() throws IOException {
        assertThrows(NullPointerException.class, () -> new CSVParser(null, DEFAULT));
    }

    @Test(expected = CsvParserConfigurationException.class)
    public void testInvalidFormat() throws IOException {
        String csvString = "name,age\nAlice,25\nBob,30,C";
        CSVParser parser = parse(new StringReader(csvString), DEFAULT);
        parser.getRecords();
    }
}