package org.apache.commons.csv; // Make sure to import all the necessary classes

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CSVParser class
 */
public class CSVParserTest {
    // Test data
    private static final String CSV_DATA = "a,b\n1,2\n3,4";
    private static final String CSV_DATA_WITH_NULL = "a,b,c\n1,2,null\n3,4,5";
    private static final String CSV_DATA_WITH_COMMENT = "a,b\n1,2\n#comment\n3,4";
    private static final String CSV_DATA_WITH_MISSING_SEPARATOR = "a,b\n1,2\n3";
    private static final String CSV_DATA_WITH_MISSING_LINE_ENDING = "a,b\n1,2";

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT.withHeader();
    }

    @Test
    public void testParseFile() throws IOException {
        // Test parsing from a file
        File csvDataFile = new File("test_data.csv");
        try (Writer writer = new FileWriter(csvDataFile)) {
            writer.write(CSV_DATA);
            writer.flush();
        }

        try (CSVParser parser = CSVParser.parse(csvDataFile, format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testParseString() throws IOException {
        // Test parsing from a string
        try (CSVParser parser = CSVParser.parse(CSV_DATA, format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testParseURL() throws IOException {
        // Test parsing from a URL
        URL url = this.getClass().getResource("/test_data.csv");
        try (CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testParseReader() throws IOException {
        // Test parsing from a Reader
        try (Reader reader = new StringReader(CSV_DATA)) {
            try (CSVParser parser = new CSVParser(reader, format)) {
                List<CSVRecord> records = parser.getRecords();
                assertEquals(2, records.size());
                assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
                assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
            }
        }
    }

    @Test
    public void testParseIterable() throws IOException {
        // Test parsing from an iterable
        List<String> lines = new ArrayList<>();
        lines.add(CSV_DATA);
        try (Reader reader = new StringReader(String.join("\n", lines))) {
            try (CSVParser parser = new CSVParser(reader, format)) {
                List<CSVRecord> records = parser.getRecords();
                assertEquals(2, records.size());
                assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
                assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
            }
        }
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        // Test getting the current line number
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            assertEquals(1, parser.getCurrentLineNumber());
            parser.nextRecord();
            parser.nextRecord();
            assertEquals(3, parser.getCurrentLineNumber());
        }
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        // Test getting the record number
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            assertEquals(0, parser.getRecordNumber());
            parser.nextRecord();
            assertEquals(1, parser.getRecordNumber());
            parser.nextRecord();
            assertEquals(2, parser.getRecordNumber());
        }
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        // Test getting the header map
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertNull(headerMap);
            parser.nextRecord();
            headerMap = parser.getHeaderMap();
            assertEquals(2, headerMap.size());
            assertEquals("a", headerMap.get("0"));
            assertEquals("b", headerMap.get("1"));
        }
    }

    @Test
    public void testGetRecords() throws IOException {
        // Test getting records
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testGetRecordsEmpty() throws IOException {
        // Test getting records from an empty input
        try (CSVParser parser = new CSVParser(new StringReader(""), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(0, records.size());
        }
    }

    @Test
    public void testClose() throws IOException {
        // Test closing the parser
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            parser.close();
            try {
                parser.getRecords();
                fail("Expected IOException on closed parser");
            } catch (IOException e) {
                // expected
            }
        }
    }

    @Test
    public void testInvalidInput() throws IOException {
        // Test parsing invalid input
        try (CSVParser parser = new CSVParser(new StringReader("a,b"), format)) {
            try {
                parser.nextRecord();
                fail("Expected IOException on invalid input");
            } catch (IOException e) {
                // expected
            }
        }
    }

    @Test
    public void testParseNullInput() throws IOException {
        // Test parsing null input
        try (CSVParser parser = new CSVParser(new StringReader(null), format)) {
            try {
                parser.nextRecord();
                fail("Expected IOException on null input");
            } catch (IOException e) {
                // expected
            }
        }
    }

    @Test
    public void testParseEmptyInput() throws IOException {
        // Test parsing empty input
        try (CSVParser parser = new CSVParser(new StringReader(""), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(0, records.size());
        }
    }

    @Test
    public void testParseWithNullValue() throws IOException {
        // Test parsing CSV data with null values
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA_WITH_NULL), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertNull(records.get(0).get(1));
            assertEquals("2", records.get(1).get(1));
        }
    }

    @Test
    public void testParseWithComment() throws IOException {
        // Test parsing CSV data with comments
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA_WITH_COMMENT), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals(2, records.get(0).size());
            assertEquals(1, records.get(0).getRecordsComment().size());
            assertEquals("#comment", records.get(0).getRecordsComment().get(0).toString());
            assertEquals(2, records.get(1).size());
        }
    }

    @Test
    public void testParseWithMissingSeparator() throws IOException {
        // Test parsing CSV data with missing separators
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA_WITH_MISSING_SEPARATOR), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(1, records.size());
            assertEquals(Arrays.asList("a", "b3"), records.get(0).valuesList());
        }
    }

    @Test
    public void testParseWithMissingLineEnding() throws IOException {
        // Test parsing CSV data with missing line endings
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA_WITH_MISSING_LINE_ENDING), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testIterator() throws IOException {
        // Test parsing CSV data using an iterator
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            List<CSVRecord> records = new ArrayList<>();
            for (CSVRecord record : parser) {
                records.add(record);
            }
            assertEquals(2, records.size());
            assertArrayEquals(new String[]{"a", "b"}, records.get(0).values());
            assertArrayEquals(new String[]{"1", "2"}, records.get(1).values());
        }
    }

    @Test
    public void testIteratorEmpty() throws IOException {
        // Test parsing empty CSV data using an iterator
        try (CSVParser parser = new CSVParser(new StringReader(""), format)) {
            List<CSVRecord> records = new ArrayList<>();
            assertFalse(parser.hasNext());
        }
    }

    @Test
    public void testIteratorClosed() throws IOException {
        // Test parsing CSV data from a closed stream using an iterator
        try (CSVParser parser = new CSVParser(new StringReader(CSV_DATA), format)) {
            parser.close();
            assertFalse(parser.hasNext());
        }
    }

    @Test
    public void testIteratorNullInput() throws IOException {
        // Test parsing CSV data from a null input using an iterator
        try (CSVParser parser = new CSVParser(new StringReader(null), format)) {
            List<CSVRecord> records = new ArrayList<>();
            assertFalse(parser.hasNext());
        }
    }
}