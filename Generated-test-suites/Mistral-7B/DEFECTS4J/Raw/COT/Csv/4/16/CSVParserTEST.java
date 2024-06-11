package org.apache.commons.csv; // import necessary packages here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Test;

/**
 * Test class for the CSVParser class.
 */
public class CSVParserTest {
    // Test data
    private static final byte[] CSV_DATA = "a;b\nc;d\ne:f".getBytes(StandardCharsets.UTF_8);
    private static final CSVFormat DEFAULT_FORMAT = CSVFormat.DEFAULT;

    /**
     * Test creating a parser for a file.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseFile() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = CSVParser.parse(file, DEFAULT_FORMAT)) {
            // Test parsing
            assertSame(parser.getClass(), CSVParser.class);
            assertEquals(parser.getFormat(), DEFAULT_FORMAT);
            assertFalse(parser.isClosed());

            // Test getRecords()
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("a,b\nc,d")), records);

            // Test iterator()
            Iterator<CSVRecord> iterator = parser.iterator();
            assertSame(iterator.next().get(0), "a");
            assertSame(iterator.next().get(1), "b");
            assertSame(iterator.next().get(0), "c");
            assertSame(iterator.next().get(1), "d");
            assertFalse(iterator.hasNext());

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            // Test getHeaderMap()
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(Collections.singletonMap("", 0), headerMap);

            // Test getRecordNumber()
            assertEquals(0, parser.getRecordNumber());

        } finally {
            file.delete();
        }
    }

    /**
     * Test creating a parser for a string.
     */
    @Test
    public void testParseString() {
        try (CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(CSV_DATA),
                                                                  StandardCharsets.UTF_8), DEFAULT_FORMAT)) {
            // Test parsing
            assertSame(parser.getClass(), CSVParser.class);
            assertEquals(parser.getFormat(), DEFAULT_FORMAT);
            assertFalse(parser.isClosed());

            // Test getRecords()
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("a,b\nc,d")), records);

            // Test iterator()
            Iterator<CSVRecord> iterator = parser.iterator();
            assertSame(iterator.next().get(0), "a");
            assertSame(iterator.next().get(1), "b");
            assertSame(iterator.next().get(0), "c");
            assertSame(iterator.next().get(1), "d");
            assertFalse(iterator.hasNext());

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            // Test getHeaderMap()
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(Collections.singletonMap("", 0), headerMap);

            // Test getRecordNumber()
            assertEquals(0, parser.getRecordNumber());

        }
    }

    /**
     * Test creating a parser for a URL.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseURL() throws IOException {
        try (URL url = new URL("http://example.com/csvparser.csv");
             CSVParser parser = CSVParser.parse(url, DEFAULT_FORMAT, StandardCharsets.UTF_8)) {
            // Test parsing
            assertSame(parser.getClass(), CSVParser.class);
            assertEquals(parser.getFormat(), DEFAULT_FORMAT);
            assertFalse(parser.isClosed());

            // Test getRecords()
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("a,b\nc,d")), records);

            // Test iterator()
            Iterator<CSVRecord> iterator = parser.iterator();
            assertSame(iterator.next().get(0), "a");
            assertSame(iterator.next().get(1), "b");
            assertSame(iterator.next().get(0), "c");
            assertSame(iterator.next().get(1), "d");
            assertFalse(iterator.hasNext());

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            // Test getHeaderMap()
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(Collections.singletonMap("", 0), headerMap);

            // Test getRecordNumber()
            assertEquals(0, parser.getRecordNumber());

        }
    }

    /**
     * Test creating a parser with a custom format.
     */
    @Test
    public void testParseCustomFormat() {
        CSVFormat customFormat = new CSVFormatBuilder().setHeader("MyColumnName")
                                                      .setDelimiter(';')
                                                      .build();

        try (CSVParser parser = new CSVParser(new StringReader("a;b\nc;d"), customFormat)) {
            // Test parsing
            assertSame(parser.getClass(), CSVParser.class);
            assertEquals(parser.getFormat(), customFormat);
            assertFalse(parser.isClosed());

            // Test getRecords()
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("MyColumnName=a,MyColumnName=b\nMyColumnName=c,MyColumnName=d")),
                         records);

            // Test iterator()
            Iterator<CSVRecord> iterator = parser.iterator();
            assertSame(iterator.next().get("MyColumnName"), "a");
            assertSame(iterator.next().get("MyColumnName"), "b");
            assertSame(iterator.next().get("MyColumnName"), "c");
            assertSame(iterator.next().get("MyColumnName"), "d");
            assertFalse(iterator.hasNext());

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            // Test getHeaderMap()
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(Collections.singletonMap("MyColumnName", 0), headerMap);

            // Test getRecordNumber()
            assertEquals(0, parser.getRecordNumber());

        }
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseEmptyFile() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = CSVParser.parse(file, DEFAULT_FORMAT)) {
            // Test parsing an empty file returns an empty list
            List<CSVRecord> records = parser.getRecords();
            assertTrue(records.isEmpty());

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            file.delete();
        }
    }

    /**
     * Test parsing a CSV file with malformed records.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseMalformedRecords() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = new CSVParser(new FileReader(file), DEFAULT_FORMAT)) {
            // Write malformed records to the file
            try (CSVFormatWriter writer = new CSVFormatWriter(new FileWriter(file))) {
                writer.writeRecord(new String[]{"a", "b"});
                writer.writeRecord(new String[]{"a", "b\nc"});
            }

            // Test parsing
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("a,b"), CSVRecord.parse("a,b\nc")), records);

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            file.delete();
        }
    }

    /**
     * Test parsing a CSV file with comments.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseWithComments() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = new CSVParser(new FileReader(file), DEFAULT_FORMAT)) {
            // Write records with comments to the file
            try (CSVFormatWriter writer = new CSVFormatWriter(new FileWriter(file))) {
                writer.writeRecord(new String[]{"a,b"}, CSVComment.LINE_COMMENT);
                writer.writeComment("This is a comment");
                writer.writeRecord(new String[]{"c,d"});
            }

            // Test parsing
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("a,b"), CSVRecord.parse("c,d")), records);

            // Test getting the comment for the first record
            CSVRecord firstRecord = records.get(0);
            String comment = firstRecord.getComment();
            assertEquals("This is a comment", comment);

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            file.delete();
        }
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseWithHeader() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = new CSVParser(new FileReader(file), DEFAULT_FORMAT)) {
            // Write records with a header to the file
            try (CSVFormatWriter writer = new CSVFormatWriter(new FileWriter(file))) {
                writer.writeHeader(new String[]{"Column1", "Column2"});
                writer.writeRecord(new String[]{"1,2"}, CSVComment.LINE_COMMENT);
                writer.writeRecord(new String[]{"3,4"}, CSVComment.LINE_COMMENT);
            }

            // Test parsing
            List<CSVRecord> records = parser.getRecords();
            Map<String, Integer> headerMap = parser.getHeaderMap();
            assertEquals(Arrays.asList(CSVRecord.parse("Column1=1,Column2=2", Arrays.asList("Column1", "Column2")),
                                         CSVRecord.parse("Column1=3,Column2=4", Arrays.asList("Column1", "Column2"))),
                         records);
            assertEquals(Arrays.asList("Column1", "Column2"), headerMap.keySet().toArray(new String[0]));

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            file.delete();
        }
    }

    /**
     * Test parsing a CSV file with a skip header record flag.
     *
     * @throws IOException on error
     */
    @Test
    public void testParseWithSkipHeaderRecordFlag() throws IOException {
        final File file = File.createTempFile("csvparser", ".csv");
        try (CSVParser parser = new CSVParser(new FileReader(file), DEFAULT_FORMAT)) {
            // Write records with a header to the file
            try (CSVFormatWriter writer = new CSVFormatWriter(new FileWriter(file))) {
                writer.writeHeader(new String[]{"Column1", "Column2"});
                writer.writeRecord(new String[]{"1,2"});
                writer.writeRecord(new String[]{"3,4"});
            }

            // Test parsing with skipHeaderRecord flag set to true
            parser.setSkipHeaderRecord(true);
            List<CSVRecord> records = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("3,4"), CSVRecord.parse("3,4")), records);

            // Test parsing with skipHeaderRecord flag set to false
            parser.setSkipHeaderRecord(false);
            List<CSVRecord> records2 = parser.getRecords();
            assertEquals(Arrays.asList(CSVRecord.parse("1,2"), CSVRecord.parse("3,4")), records2);

            // Test close()
            parser.close();
            assertTrue(parser.isClosed());

            file.delete();
        }
    }

    /**
     * Test creating a parser with an invalid format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        new CSVParser(new StringReader(""), null);
    }

    /**
     * Test creating a parser with a null reader.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullReader() {
        new CSVParser(null, DEFAULT_FORMAT);
    }

    /**
     * Test creating a parser with a null format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() {
        new CSVParser(new StringReader(""), null);
    }

    /**
     * Test creating a parser with a reader that throws an IOException.
     */
    @Test(expected = IOException.class)
    public void testParseIOException() throws IOException {
        new CSVParser(new StringReader("") {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        }, DEFAULT_FORMAT);
    }
}