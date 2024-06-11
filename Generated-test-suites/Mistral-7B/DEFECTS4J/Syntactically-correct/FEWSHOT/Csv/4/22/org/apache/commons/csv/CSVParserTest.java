package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        String csvFilePath = "src/test/resources/sample.csv";
        CSVParser parser = CSVParser.parse(new File(csvFilePath), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test
    public void testParseString() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test
    public void testParseURL() throws Exception {
        String csvURL = "https://example.com/sample.csv";
        CSVParser parser = CSVParser.parse(new URL(csvURL), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size()); // Replace with the actual number of records in the sample CSV file.

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws Exception {
        String csvString = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Name")
                .setHeader("Age", "Age_Custom")
                .build();
        CSVParser parser = new CSVParser(new StringReader(csvString), customFormat);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test
    public void testIterateRecords() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Boston";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.RFC4180);
        Iterator<CSVRecord> iterator = parser.iterator();

        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test
    public void testIterateRecordsWithCustomFormat() throws Exception {
        String csvString = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Name")
                .setHeader("Age", "Age_Custom")
                .build();
        CSVParser parser = new CSVParser(new StringReader(csvString), customFormat);
        Iterator<CSVRecord> iterator = parser.iterator();

        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            System.out.println(record);
        }

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterateRecordsAfterClosedParser() throws Exception {
        String csvString = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.RFC4180);
        Iterator<CSVRecord> iterator = parser.iterator();

        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        parser.close();

        iterator.next(); // This should throw NoSuchElementException
    }
}