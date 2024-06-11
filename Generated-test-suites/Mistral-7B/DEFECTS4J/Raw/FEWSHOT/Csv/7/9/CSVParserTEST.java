package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        String fileContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n";
        File testFile = new File("testFile.csv");
        try (Writer writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        CSVParser parser = CSVParser.parse(testFile, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords().toList();
        assertTrue(records.size() == 2);
        assertArrayEquals(new String[] { "Name", "Age", "City" }, records.get(0).getNames());
        assertArrayEquals(new String[] { "John Doe", "30", "New York" }, records.get(0).toArray());
        assertArrayEquals(new String[] { "Jane Doe", "25", "London" }, records.get(1).toArray());
    }

    @Test
    public void testParseString() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords().toList();
        assertTrue(records.size() == 2);
        assertArrayEquals(new String[] { "Name", "Age", "City" }, records.get(0).getNames());
        assertArrayEquals(new String[] { "John Doe", "30", "New York" }, records.get(0).toArray());
        assertArrayEquals(new String[] { "Jane Doe", "25", "London" }, records.get(1).toArray());
    }

    @Test
    public void testParseUrl() throws Exception {
        String baseUrl = "http://example.com/csv/";
        String csvUrl = baseUrl + "sample.csv";
        CSVParser parser = CSVParser.parse(new URL(csvUrl), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords().toList();
        assertTrue(records.size() > 0);
        assertFalse(records.isEmpty());
    }

    @Test
    public void testParseCustomFormat() throws Exception {
        List<String> expectedContent = Arrays.asList(
                "Name,Age,City",
                "John Doe,30,New York",
                "Jane Doe,25,London"
        );
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name", "Age", "City")
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(""), customFormat);
        Iterator<CSVRecord> iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        assertSame(expectedContent, records);
    }

    @Test
    public void testGetRecords() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords().toList();
        assertSame(records, parser.getRecords());
        assertArrayEquals(records.toArray(new CSVRecord[0]), parser.getRecords().toArray());
    }

    @Test
    public void testGetRecordsWithEmptyCollection() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
        ArrayList<CSVRecord> emptyList = new ArrayList<>();
        List<CSVRecord> records = parser.getRecords(emptyList);
        assertSame(records, emptyList);
    }

    @Test
    public void testGetRecordNumber() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
        CSVRecord record1 = parser.nextRecord();
        CSVRecord record2 = parser.nextRecord();
        CSVRecord record3 = parser.nextRecord();
        assertNull(record3);

        long recordNumber1 = parser.getRecordNumber();
        long recordNumber2 = parser.getRecordNumber();
        long recordNumber3 = parser.getRecordNumber();

        assertTrue(recordNumber1 == 1);
        assertTrue(recordNumber2 == 2);
        assertTrue(recordNumber3 == 0);
    }

    @Test
    public void testCloseParser() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
        parser.close();
        assertThrows(NoSuchElementException.class, () -> parser.nextRecord());
    }

    @Test
    public void testIterator() throws Exception {
        String csvString = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London";
        CSVParser parser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        Iterator<CSVRecord> iterator = parser.iterator();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        assertSame(records, parser.getRecords().toList());
    }
}