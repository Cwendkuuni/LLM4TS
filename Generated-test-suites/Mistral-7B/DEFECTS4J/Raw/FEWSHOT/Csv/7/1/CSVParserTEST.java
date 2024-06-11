package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseWithHeader() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        Map<String, Integer> headerMap = parser.getHeaderMap();

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderNames());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.toArray());
        assertEquals(0, headerMap.get("Name").intValue());
        assertEquals(1, headerMap.get("Age").intValue());
        assertEquals(2, headerMap.get("City").intValue());

        record = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderNames());
        assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, record.toArray());
        assertEquals(0, headerMap.get("Name").intValue());
        assertEquals(1, headerMap.get("Age").intValue());
        assertEquals(2, headerMap.get("City").intValue());
    }

    @Test
    public void testParseWithoutHeader() throws IOException {
        String csv = "John Doe,30,New York\nJane Doe,25,Los Angeles";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.withHeaderCount(0));

        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, record2.toArray());
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        String csv = "";

        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertTrue(records.size() == 1);
        assertNull(records.get(0).toArray());
    }

    @Test
    public void testParseEmptyCSVWithHeader() throws IOException {
        String csv = "";

        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertTrue(records.size() == 1);
        assertNull(records.get(0).toArray());
        assertSame(Collections.emptyMap(), parser.getHeaderMap());
    }

    @Test
    public void testParseInvalidCSV() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\nName,City,Age";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        try {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
            parser.getRecords();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVFormat customFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();

        CSVParser parser = CSVParser.parse(reader, customFormat);

        List<CSVRecord> records = parser.getRecords();

        Map<String, Integer> headerMap = parser.getHeaderMap();

        CSVRecord record = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record.toArray());
        assertEquals(0, headerMap.get("Name").intValue());
        assertEquals(1, headerMap.get("Age").intValue());
        assertEquals(2, headerMap.get("City").intValue());

        record = records.get(1);
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record.toArray());

        record = records.get(2);
        assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, record.toArray());
    }

    @Test
    public void testParseCSVWithComment() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York#This is a comment\nJane Doe,25,Los Angeles";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record1.toArray());
        assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record2.getHeaderNames());
        assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, record2.toArray());
        assertEquals("#This is a comment", record2.getComment());
    }

    @Test
    public void testParseCSVWithComments() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York#This is a comment for John\nJane Doe,25,Los Angeles#This is a comment for Jane";

        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());
        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"John Doe", "30", "New York"}, record1.toArray());
        assertNull(record1.getComment());

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age", "City"}, record2.getHeaderNames());
        assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, record2.toArray());
        assertEquals("This is a comment for Jane", record2.getComment());
    }

    @Test(expected = IOException.class)
    public void testParseWithNullFormat() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";

        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVParser parser = CSVParser.parse(reader, null);
        parser.getRecords();
        fail("Expected IOException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidFormat() throws IOException {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";

        ByteArrayReader reader = new ByteArrayReader(csv.getBytes());

        CSVFormat format = new CSVFormat();
        format.setHeader(null);

        try {
            CSVParser parser = CSVParser.parse(reader, format);
            parser.getRecords();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}