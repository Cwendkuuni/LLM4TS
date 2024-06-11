package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    private CSVParser parser;
    private CSVRecord record;

    @Before
    public void setUp() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25\nCharlie,45\n";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        record = parser.getRecords().iterator().next();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals(2, headerMap.size());
        assertEquals("name", headerMap.get("name"));
        assertEquals("age", headerMap.get("age"));
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        assertSame(record, records.get(0));
    }

    @Test
    public void testIterateRecords() throws IOException {
        CSVRecordIterator iterator = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (iterator.hasNext()) {
            records.add(iterator.next());
        }
        assertEquals(3, records.size());
        assertSame(record, records.get(0));
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(3, parser.getRecordNumber());
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        assertEquals(2, parser.getCurrentLineNumber());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoRecords() throws IOException {
        String emptyCsvData = "";
        parser = CSVParser.parse(new StringReader(emptyCsvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testGetRecordsFromMemory() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25\nCharlie,45\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        parser = new CSVParser(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        assertSame(record, records.get(0));
    }

    @Test
    public void testCustomFormat() throws IOException {
        String csvData = "name,age;role\nAlice,30,Employee\nBob,25,Manager\nCharlie,45,Engineer";
        CSVFormat customFormat = CSVFormat.builder()
                .setHeaderNames(Arrays.asList("name", "age", "role"))
                .build();
        parser = CSVParser.parse(new StringReader(csvData), customFormat);
        record = parser.nextRecord();
        assertEquals("Alice", record.get("name"));
        assertEquals(30, record.getInteger("age"));
        assertEquals("Employee", record.get("role"));
    }

    @Test
    public void testEmptyCsvData() throws IOException {
        String emptyCsvData = "name,age\n";
        parser = CSVParser.parse(new StringReader(emptyCsvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());
    }

    @Test
    public void testComment() throws IOException {
        String csvData = "name,age\nAlice,30# This is a comment\nBob,25";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        record = parser.nextRecord();
        assertNull(record.getComment());
        parser.nextRecord();
        assertEquals("# This is a comment", record.getComment());
    }

    @Test
    public void testTruncation() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25\nCharlie,45\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        parser = new CSVParser(inputStream, CSVFormat.DEFAULT);
        parser.readLine(); // read the header line
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        assertSame(record, records.get(0));
        parser.close();
        inputStream = new ByteArrayInputStream(csvData.substring(csvData.indexOf("\n", csvData.indexOf("\n") + 1)).getBytes());
        parser = new CSVParser(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> truncatedRecords = parser.getRecords();
        assertEquals(1, truncatedRecords.size());
    }

    @Test
    public void testNullString() throws IOException {
        String csvData = "name,age\nAlice,,Bob,30\nCharlie,null,Dave";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        record = parser.nextRecord();
        assertEquals("Alice", record.get("name"));
        assertNull(record.get("age"));
        assertEquals("Bob", record.get("age2")); // non-existing field
        assertEquals(null, record.get("age2"));
        record = parser.nextRecord();
        assertEquals("Charlie", record.get("name"));
        assertEquals(null, record.get("age"));
        assertEquals("Dave", record.get("name2")); // non-existing field
        assertEquals(null, record.get("name2"));
    }
}