package org.apache.commons.csv; // update the package name if necessary

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;
import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVToken;
import org.apache.commons.csv.iterable.CSVRecordIterator;
import org.junit.Before;
import org.junit.Test;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String data = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n";
        parser = parse(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), DEFAULT);
    }

    @Test
    public void testParse_File_ReturnsParser() {
        File file = new File("test.csv"); // replace with your test file
        CSVParser result = parse(file, DEFAULT);
        assertNotNull(result);
    }

    @Test
    public void testParse_String_ReturnsParser() {
        String data = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n";
        CSVParser result = parse(data, DEFAULT);
        assertNotNull(result);
    }

    @Test
    public void testParse_URL_ReturnsParser() throws IOException {
        String urlData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n";
        URL url = new URL("http://example.com/test.csv"); // replace with your test URL
        CSVParser result = parse(url, StandardCharsets.UTF_8, DEFAULT);
        assertNotNull(result);
    }

    @Test
    public void testParse_Reader_ReturnsParser() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n").getBytes());
        Reader reader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()), StandardCharsets.UTF_8);
        CSVParser result = parse(reader, DEFAULT);
        assertNotNull(result);
    }

    @Test
    public void testIterator_ReturnsRecords() {
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        // Verify the first record
        assertEquals(3, firstRecord.size());
        assertEquals("Name", firstRecord.getName(0));
        assertEquals("John Doe", firstRecord.get(0));
        assertEquals("Age", firstRecord.getName(1));
        assertEquals(30, firstRecord.get(1).toInt());
        assertEquals("City", firstRecord.getName(2));
        assertEquals("New York", firstRecord.get(2));
        assertFalse(firstRecord.isComment());

        // Verify the second record
        assertEquals(3, secondRecord.size());
        assertEquals("Name", secondRecord.getName(0));
        assertEquals("Jane Doe", secondRecord.get(0));
        assertEquals("Age", secondRecord.getName(1));
        assertEquals(25, secondRecord.get(1).toInt());
        assertEquals("City", secondRecord.getName(2));
        assertEquals("London", secondRecord.get(2));
        assertFalse(secondRecord.isComment());
    }

    @Test
    public void testGetRecords_ReturnsRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
    }

    @Test
    public void testGetRecords_EmptyList_WhenEndOfStreamIsReached() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        parser.nextRecord(); // consume the first record
        records = parser.getRecords();
        assertEquals(0, records.size());
    }

    @Test
    public void testGetRecords_Null_WhenClosed() throws IOException {
        parser.close();
        List<CSVRecord> records = parser.getRecords();
        assertNull(records);
    }

    @Test
    public void testGetCurrentLineNumber_ReturnsTheCorrectLineNumber() throws IOException {
        long currentLineNumber = parser.getCurrentLineNumber();
        assertEquals(1, currentLineNumber);
        parser.nextRecord();
        currentLineNumber = parser.getCurrentLineNumber();
        assertEquals(2, currentLineNumber);
    }

    @Test
    public void testGetRecordNumber_ReturnsTheCorrectRecordNumber() throws IOException {
        long currentRecordNumber = parser.getRecordNumber();
        assertEquals(0, currentRecordNumber);
        parser.nextRecord();
        currentRecordNumber = parser.getRecordNumber();
        assertEquals(1, currentRecordNumber);
    }

    @Test
    public void testGetRecordNumber_ReturnsZero_WhenEndOfStreamIsReached() throws IOException {
        long currentRecordNumber = parser.getRecordNumber();
        assertEquals(0, currentRecordNumber);
        parser.nextRecord();
        parser.nextRecord();
        currentRecordNumber = parser.getRecordNumber();
        assertEquals(0, currentRecordNumber);
    }

    @Test
    public void testGetRecords_WithCustomFormat_ReturnsRecords() throws IOException {
        CSVFormat excelFormat = EXCEL;
        List<CSVRecord> records = parser.getRecords(excelFormat);
        assertEquals(2, records.size());
    }

    @Test
    public void testGetRecords_WithCustomFormat_EmptyList_WhenEndOfStreamIsReached() throws IOException {
        CSVFormat excelFormat = EXCEL;
        List<CSVRecord> records = parser.getRecords(excelFormat);
        assertEquals(2, records.size());
        parser.nextRecord(); // consume the first record
        records = parser.getRecords(excelFormat);
        assertEquals(0, records.size());
    }

    @Test
    public void testGetRecords_WithCustomFormat_Null_WhenClosed() throws IOException {
        parser.close();
        CSVFormat excelFormat = EXCEL;
        List<CSVRecord> records = parser.getRecords(excelFormat);
        assertNull(records);
    }

    @Test
    public void testGetHeaderMap_ReturnsMapWithColumnNamesAsKeysAndZeroBasedIndicesAsValues() throws IOException {
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Map<String, Integer> expectedHeaderMap = new HashMap<>();
        expectedHeaderMap.put("Name", 0);
        expectedHeaderMap.put("Age", 1);
        expectedHeaderMap.put("City", 2);
        assertEquals(expectedHeaderMap, headerMap);
    }

    @Test
    public void testGetHeaderMap_ReturnsNull_WhenFormatHasNoHeader() throws IOException {
        CSVFormat noHeaderFormat = CSVFormat.DEFAULT.withHeader();
        parser = parse(new ByteArrayInputStream("Name,Age,City\n".getBytes()), noHeaderFormat);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNull(headerMap);
    }

    @Test
    public void testGetHeaderMap_WithCustomFormat_ReturnsMapWithColumnNamesAsKeysAndZeroBasedIndicesAsValues() throws IOException {
        CSVFormat excelFormat = EXCEL;
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Map<String, Integer> expectedHeaderMap = new HashMap<>();
        expectedHeaderMap.put("Name", 0);
        expectedHeaderMap.put("Age", 1);
        expectedHeaderMap.put("City", 2);
        assertEquals(expectedHeaderMap, headerMap);
    }

    @Test
    public void testNextRecord_ReturnsRecord_WhenSuccess() throws IOException {
        CSVRecord result = parser.nextRecord();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testNextRecord_ReturnsNull_WhenEndOfStreamIsReached() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        CSVRecord result = parser.nextRecord();
        assertNull(result);
    }

    @Test
    public void testNextRecord_ThrowsIOException_WhenErrorDuringParsing() throws IOException {
        parser = parse(new ByteArrayInputStream("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\nName,Age,City".getBytes()), DEFAULT);
        assertThrows(IOException.class, () -> parser.nextRecord());
    }

    @Test
    public void testNextRecord_ThrowsIOException_WhenErrorDuringReading() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(("Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,London\n").getBytes());
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()), StandardCharsets.UTF_8);
        parser = parse(reader, DEFAULT);
        parser.nextRecord();
        parser.nextRecord();
        reader.close();

        assertThrows(IOException.class, () -> parser.nextRecord());
    }

    @Test
    public void testNextRecord_ThrowsIOException_WhenClosed() throws IOException {
        parser.close();
        assertThrows(IOException.class, () -> parser.nextRecord());
    }

    @Test
    public void testGetRecordNumber_ThrowsIOException_WhenClosed() throws IOException {
        parser.close();
        assertThrows(IOException.class, () -> parser.getRecordNumber());
    }

    @Test
    public void testGetRecordNumber_ThrowsIOException_WhenEndOfStreamIsReached() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        assertThrows(IOException.class, () -> parser.getRecordNumber());
    }

    @Test
    public void testGetRecords_ThrowsIOException_WhenClosed() throws IOException {
        parser.close();
        assertThrows(IOException.class, () -> parser.getRecords());
    }

    @Test
    public void testGetRecords_ThrowsIOException_WhenEndOfStreamIsReached() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        assertThrows(IOException.class, () -> parser.getRecords());
    }

    @Test
    public void testGetRecords_WithCustomFormat_ThrowsIOException_WhenClosed() throws IOException {
        parser.close();
        CSVFormat excelFormat = EXCEL;
        assertThrows(IOException.class, () -> parser.getRecords(excelFormat));
    }

    @Test
    public void testGetRecords_WithCustomFormat_ThrowsIOException_WhenEndOfStreamIsReached() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        CSVFormat excelFormat = EXCEL;
        assertThrows(IOException.class, () -> parser.getRecords(excelFormat));
    }

    @Test
    public void testGetRecordsIterator_ReturnsRecords() {
        List<CSVRecord> records = new ArrayList<>();
        CSVRecordIterator iterator = parser.iterator();

        while (iterator.hasNext()) {
            records.add(iterator.next());
        }

        assertEquals(2, records.size());
    }
}