package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.apache.commons.csv.tokenizer.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test CSVParser class.
 */
@RunWith(Parameterized.class)
public class CSVParserTest {

    private static final String TEST_CSV = "a;b\nc;d\ne;f\nf;g";
    private static final byte[] TEST_CSV_BYTES = TEST_CSV.getBytes();

    private CSVFormat format;
    private CSVParser parser;

    @Parameters
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[] { DEFAULT });
        return data;
    }

    public CSVParserTest(CSVFormat format) {
        this.format = format;
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        try (CSVParser parser = CSVParser.parse(file, format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(3, records.size());
            assertSame(format, parser.getFormat());
        }
    }

    @Test
    public void testParseString() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(3, records.size());
            assertSame(format, parser.getFormat());
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CSV_BYTES);
        try (CSVParser parser = parse(inputStream, format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(3, records.size());
            assertSame(format, parser.getFormat());
        }
    }

    @Test
    public void testGetRecords() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(3, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                assertEquals("a", record.get(0));
                assertEquals("c", record.get(1));
                assertEquals("e", record.get(2));
            }
        }
    }

    @Test
    public void testGetRecordsWithHeader() throws IOException {
        String csv = "Name,Age\nJohn,21\nMary,25\n";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("Name", records.get(0).get(0));
            assertEquals("Age", records.get(0).get(1));
            assertEquals("John", records.get(0).get(1));
            assertEquals("21", records.get(0).get(1));
            assertEquals("Mary", records.get(1).get(0));
            assertEquals("25", records.get(1).get(1));
        }
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCsv() throws IOException {
        String invalidCsv = "a;b\nc:d\ne:f\nf:g\n";
        try (CSVParser parser = parse(new StringReader(invalidCsv), format)) {
            parser.getRecords();
        }
    }

    @Test
    public void testIsClosed() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            assertFalse(parser.isClosed());
            parser.close();
            assertTrue(parser.isClosed());
        }
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            assertEquals(1, parser.getCurrentLineNumber());
            parser.nextRecord();
            assertEquals(2, parser.getCurrentLineNumber());
            parser.nextRecord();
            assertEquals(3, parser.getCurrentLineNumber());
            parser.nextRecord();
            assertEquals(4, parser.getCurrentLineNumber());
            parser.close();
            assertNull(parser.getCurrentLineNumber());
        }
    }

    @Test
    public void testIterator() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            List<CSVRecord> records = parser.getRecords();
            Iterator<CSVRecord> iterator = parser.iterator();
            assertEquals(records.get(0), iterator.next());
            assertEquals(records.get(1), iterator.next());
            assertEquals(records.get(2), iterator.next());
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    public void testNextRecord() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            assertEquals(CSVRecord.class, parser.getClass());
            assertEquals(null, parser.nextRecord());

            List<CSVRecord> records = parser.getRecords();
            assertEquals(records.get(0), parser.nextRecord());
            assertEquals(records.get(1), parser.nextRecord());
            assertEquals(records.get(2), parser.nextRecord());
            assertEquals(null, parser.nextRecord());
        }
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        try (CSVParser parser = parse(new StringReader(TEST_CSV), format)) {
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertTrue(headerMap.isEmpty());
        }

        try (CSVParser parser = parse(new StringReader("Name,Age\nJohn,21\nMary,25"), format.withHeader())) {
            Map<String, Integer> headerMap = parser.getHeaderMap();
            Map<String, Integer> expectedHeaderMap = Collections.singletonMap("Name", 0)
                    .put("Age", 1);
            assertEquals(expectedHeaderMap, headerMap);
        }
    }

    @Test
    public void testGetRecordsWithEmptyCsv() throws IOException {
        try (CSVParser parser = parse(new StringReader(""), format)) {
            List<CSVRecord> records = parser.getRecords();
            assertTrue(records.isEmpty());
        }
    }

    @Test
    public void testGetRecordsWithOnlyHeader() throws IOException {
        try (CSVParser parser = parse(new StringReader("Name,Age"), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertTrue(records.isEmpty());
        }
    }

    @Test
    public void testGetRecordsWithTrailingComment() throws IOException {
        String csv = "Name,Age\nJohn,21\nMary,25# this is a comment";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("Name", records.get(0).get(0));
            assertEquals("Age", records.get(0).get(1));
            assertEquals("John", records.get(0).get(1));
            assertEquals("21", records.get(0).get(1));
            assertEquals("Mary", records.get(1).get(0));
            assertEquals("25", records.get(1).get(1));
            assertEquals("# this is a comment", records.get(1).getComment());
        }
    }

    @Test
    public void testStreamingMode() throws IOException {
        String csv = "Name,Age\nJohn,21\nMary,25# this is a comment\nBob,30";
        Set<String> expectedNames = new HashSet<>(Arrays.asList("John", "Mary", "Bob"));
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            for (CSVRecord record : parser) {
                assertEquals(2, record.size());
                assertEquals(expectedNames.remove(record.get(0)), record.get(0));
            }
            assertTrue(expectedNames.isEmpty());
        }
    }

    @Test
    public void testRecordWithSpacesInValues() throws IOException {
        String csv = "Name,Age\nJohn 'Doe' , 21\nMary Smith,25";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("John Doe", records.get(0).get(0));
            assertEquals("21", records.get(0).get(1));
            assertEquals("Mary Smith", records.get(1).get(0));
            assertEquals("25", records.get(1).get(1));
        }
    }

    @Test
    public void testRecordWithQuotedValues() throws IOException {
        String csv = "Name,Age\nJohn,\"Doe\" , 21\nMary \"Smith\" ,25";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("John Doe", records.get(0).get(0));
            assertEquals("21", records.get(0).get(1));
            assertEquals("Mary Smith", records.get(1).get(0));
            assertEquals("25", records.get(1).get(1));
        }
    }

    @Test
    public void testRecordWithQuotedValuesAndSpaces() throws IOException {
        String csv = "Name,Age\nJohn,\"Doe  'John Doe'\", 21\nMary \"Smith Smith\" ,25";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("John Doe 'John Doe'", records.get(0).get(0));
            assertEquals("21", records.get(0).get(1));
            assertEquals("Mary Smith Smith", records.get(1).get(0));
            assertEquals("25", records.get(1).get(1));
        }
    }

    @Test
    public void testRecordWithQuotedValuesAndEmptyCell() throws IOException {
        String csv = "Name,Age\nJohn,\"Doe \" , 21\nMary \"Smith\" ,\"\"";
        try (CSVParser parser = parse(new StringReader(csv), format.withHeader())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            assertEquals("John Doe", records.get(0).get(0));
            assertEquals("21", records.get(0).get(1));
            assertEquals("", records.get(1).get(0));
            assertEquals("", records.get(1).get(1));
        }
    }
}