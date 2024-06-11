package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.class$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @org.junit.Test
    public void testParseEmptyCSVFile() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/empty.csv"), CSVFormat.DEFAULT);
        assertTrue(parser.hasNext());
        CSVRecord record = parser.nextRecord();
        assertNull(record);
        parser.close();
    }

    @org.junit.Test
    public void testParseCSVFileWithSingleRecord() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/singleRecord.csv"), CSVFormat.DEFAULT);
        assertTrue(parser.hasNext());
        CSVRecord record = parser.nextRecord();
        List<String> values = Arrays.asList("one", "two", "three");
        assertEquals(values.toArray(new String[values.size()]), record.toArray());
        parser.close();
    }

    @org.junit.Test
    public void testParseCSVFileWithMultipleRecords() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/multipleRecords.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        while (parser.hasNext()) {
            records.add(parser.nextRecord());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("A1", "B1", "C1"), Arrays.asList("A2", "B2", "C2"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test
    public void testParseCSVFileWithEmptyLines() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/emptyLines.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        while (parser.hasNext()) {
            records.add(parser.nextRecord());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("A1", "B1", "C1"), Arrays.asList(""),
                Arrays.asList("A3", "B3", "C3"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test
    public void testParseCSVFileWithComment() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/comment.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        while (parser.hasNext()) {
            records.add(parser.nextRecord());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("A1", "B1", "C1"), Arrays.asList("A2", "B2", "C2"),
                Arrays.asList("A3", "B3", "C3", "# this is a comment"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test
    public void testParseCSVFileWithCustomFormat() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/customFormat.csv"), CSVFormat.EXCEL);
        List<CSVRecord> records = new ArrayList<>();
        while (parser.hasNext()) {
            records.add(parser.nextRecord());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("A1", "B1", "C1"), Arrays.asList("A2", "B2", "C2"),
                Arrays.asList("A3", "B3", "C3"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test
    public void testParseCSVInputStream() throws IOException {
        byte[] csvContent = "one,two,three\nfour,five,six\nseven,eight,nine".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent);
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        while (parser.hasNext()) {
            records.add(parser.nextRecord());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("one", "two", "three"), Arrays.asList("four", "five", "six"),
                Arrays.asList("seven", "eight", "nine"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test
    public void testIterateOverCSVParser() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/multipleRecords.csv"), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        parser.close();
        List<List<String>> values = Arrays.asList(Arrays.asList("A1", "B1", "C1"), Arrays.asList("A2", "B2", "C2"));
        for (int i = 0; i < records.size(); i++) {
            List<String> recordValues = Arrays.asList(records.get(i).get(0), records.get(i).get(1),
                    records.get(i).get(2));
            assertEquals(values.get(i), recordValues);
        }
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void testIterateOverCSVParserAfterClosed() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/multipleRecords.csv"), CSVFormat.DEFAULT);
        parser.close();
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void testIterateOverCSVParserEmpty() throws IOException {
        CSVParser parser = parse(new File("src/test/resources/empty.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
    }
}