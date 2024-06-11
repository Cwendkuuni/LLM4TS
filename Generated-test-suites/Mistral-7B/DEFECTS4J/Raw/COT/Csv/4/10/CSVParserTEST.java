package org.apache.commons.csv; // Change the package name if necessary

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)} with valid arguments.
     *
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        File file = new File("test.csv");
        writeToFile(file, csvContent);
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(records.get(0), "Name", "John Doe", "Age", "30");
        assertRecordEquals(records.get(1), "Name", "Jane Doe", "Age", "25");
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)} with valid arguments.
     *
     * @throws IOException
     */
    @Test
    public void testParseString() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        CSVParser parser = parse(csvContent, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(records.get(0), "Name", "John Doe", "Age", "30");
        assertRecordEquals(records.get(1), "Name", "Jane Doe", "Age", "25");
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)} with valid arguments.
     *
     * @throws IOException
     */
    @Test
    public void testParseURL() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        URL url = new URL("file:test.csv");
        CSVParser parser = parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(records.get(0), "Name", "John Doe", "Age", "30");
        assertRecordEquals(records.get(1), "Name", "Jane Doe", "Age", "25");
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with valid arguments.
     *
     * @throws IOException
     */
    @Test
    public void testParseReader() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertRecordEquals(records.get(0), "Name", "John Doe", "Age", "30");
        assertRecordEquals(records.get(1), "Name", "Jane Doe", "Age", "25");
    }

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)} with null file argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNullFile() throws IOException {
        CSVParser parser = parse(null, CSVFormat.DEFAULT);
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)} with null content argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseStringNullContent() throws IOException {
        CSVParser parser = parse(null, CSVFormat.DEFAULT);
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)} with null URL argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseURLNullURL() throws IOException {
        CSVParser parser = parse(null, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with null reader argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseReaderNullReader() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
    }

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)} with non-existent file.
     */
    @Test
    public void testParseNonExistentFile() throws IOException {
        File file = new File("test_non_existent.csv");
        CSVParser parser = parse(file, CSVFormat.DEFAULT);
        assertNull(parser.getRecords());
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)} with empty content.
     */
    @Test
    public void testParseEmptyString() throws IOException {
        String csvContent = "";
        CSVParser parser = parse(csvContent, CSVFormat.DEFAULT);
        assertNull(parser.getRecords());
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)} with empty URL content.
     */
    @Test
    public void testParseEmptyURL() throws IOException {
        String csvContent = "";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        URL url = new URL("file:test.csv");
        CSVParser parser = parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        assertNull(parser.getRecords());
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with empty reader content.
     */
    @Test
    public void testParseEmptyReader() throws IOException {
        String csvContent = "";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        assertNull(parser.getRecords());
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)} with invalid URL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidURL() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        URL url = new URL("file:test_invalid.csv");
        CSVParser parser = parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with an invalid token.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidToken() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25\n%";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        parser.nextRecord();
    }

    /**
     * Test method for {@link CSVParser#nextRecord()} with EOF.
     */
    @Test
    public void testNextRecordEOF() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        CSVRecord nextRecord = parser.nextRecord();
        assertNull(nextRecord);
    }

    /**
     * Test method for {@link CSVParser#nextRecord()} after parsing all records.
     */
    @Test
    public void testNextRecordAllRecordsParsed() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertNull(parser.nextRecord());
    }

    /**
     * Test method for {@link CSVParser#iterator()} with multiple records.
     */
    @Test
    public void testIteratorMultipleRecords() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        assertEquals(records.size(), 2);
    }

    /**
     * Test method for {@link CSVParser#iterator()} with empty content.
     */
    @Test
    public void testIteratorEmptyContent() throws IOException {
        String csvContent = "";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        assertFalse(parser.iterator().hasNext());
    }

    /**
     * Test method for {@link CSVParser#iterator()} after parsing all records.
     */
    @Test
    public void testIteratorAllRecordsParsed() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()} with multiple records.
     */
    @Test
    public void testGetRecordNumberMultipleRecords() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        assertEquals(record.getRecordNumber(), 1);
        CSVRecord nextRecord = parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        assertEquals(nextRecord.getRecordNumber(), 2);
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()} with empty content.
     */
    @Test
    public void testGetRecordNumberEmptyContent() throws IOException {
        String csvContent = "";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        assertEquals(parser.getRecordNumber(), 0);
    }

    /**
     * Test method for {@link CSVParser#getRecordNumber()} after parsing all records.
     */
    @Test
    public void testGetRecordNumberAllRecordsParsed() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord record = parser.nextRecord();
        assertEquals(parser.getRecordNumber(), 1);
        assertEquals(record.getRecordNumber(), 1);
        CSVRecord nextRecord = parser.nextRecord();
        assertEquals(parser.getRecordNumber(), 2);
        assertEquals(nextRecord.getRecordNumber(), 2);
        assertNull(parser.nextRecord());
        assertEquals(parser.getRecordNumber(), 2);
    }

    /**
     * Test method for {@link CSVParser#getRecords()} with multiple records.
     */
    @Test
    public void testGetRecordsMultipleRecords() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(records.size(), 2);
        assertEquals(records.get(0).getRecordNumber(), 1);
        assertEquals(records.get(1).getRecordNumber(), 2);
    }

    /**
     * Test method for {@link CSVParser#getRecords()} with empty content.
     */
    @Test
    public void testGetRecordsEmptyContent() throws IOException {
        String csvContent = "";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(records.size(), 0);
    }

    /**
     * Test method for {@link CSVParser#getRecords()} after parsing all records.
     */
    @Test
    public void testGetRecordsAllRecordsParsed() throws IOException {
        String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,25";
        Reader reader = new StringReader(csvContent);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(records.size(), 2);
        assertNull(parser.nextRecord());
        assertEquals(parser.getRecords().size(), 2);
    }

    /**
     * Test method for {@link CSVRecord#values()}.
     */
    private void assertRecordEquals(CSVRecord record, String... expectedValues) {
        List<String> values = Arrays.asList(expectedValues);
        List<String> resultValues = Arrays.asList(record.values());
        assertEquals(values.size(), resultValues.size());
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), resultValues.get(i));
        }
    }

    /**
     * Write a given string content to a file.
     */
    private void writeToFile(File file, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        }
    }
}