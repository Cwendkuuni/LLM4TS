package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.exceptions.CsvException;
import org.apache.commons.csv.exceptions.MalformedCSVFormatException;
import org.apache.commons.csv.exceptions.NoSuchColumnException;

/**
 * JUnit 4 test cases for {@link CSVParser}.
 */
public class CSVParserTest {
    /** A simple format with no header. */
    private static final CSVFormat SIMPLE_FORMAT = CSVFormat.DEFAULT.withHeader();

    /** A format with a header. */
    private static final CSVFormat HEADER_FORMAT = CSVFormat.DEFAULT.withHeader("col1", "col2");

    /**
     * Test parsing a CSV file with no header.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseNoHeader() throws IOException, MalformedCSVFormatException {
        final String data = "1,2\n3,4";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, SIMPLE_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertNull(parser.getHeaderMap());

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("2", values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing a CSV file with a header.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseWithHeader() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n3,4";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("2", values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing a CSV file with a header and an empty line (comment) between records.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseWithHeaderAndComment() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n--\n3,4\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("2", values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing a CSV file with a header and a record with a missing value.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     * @throws NoSuchColumnException
     */
    @Test(expected = NoSuchColumnException.class)
    public void testParseWithMissingValue() throws IOException, MalformedCSVFormatException, NoSuchColumnException {
        final String data = "col1,col2\n1,2\n3,\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("2", values.next());
            assertTrue(values.hasNext());
            values.next(); // consume the missing value
        }

        parser.close();
        reader.close();
        inputStream.close();

        // The next call to getRecords should throw an exception
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with a header and a record with a null value.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseWithNullValue() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n3,null";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals(null, values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing a CSV file with a header and a record with an empty value.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseWithEmptyValue() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n3, ";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals(Collections.emptyList(), values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing a CSV file with a header and a record with a long value.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseWithLongValue() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n3,9223372036854775807";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("9223372036854775807", values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing an empty CSV file.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test
    public void testParseEmpty() throws IOException, MalformedCSVFormatException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, SIMPLE_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(Collections.emptyList(), records);
        assertNull(parser.getHeaderMap());

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test parsing an invalid CSV file.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test(expected = MalformedCSVFormatException.class)
    public void testParseInvalid() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\n3,4\n--\n5";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals(2, parser.getHeaderMap().size());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertSame("col1", headerMap.get("col1"));
        assertSame("col2", headerMap.get("col2"));

        for (CSVRecord record : records) {
            final Iterator<String> values = record.iterator();
            assertEquals(2, record.size());
            assertEquals("1", values.next());
            assertEquals("2", values.next());
        }

        parser.close();
        reader.close();
        inputStream.close();

        // The next call to getRecords should throw an exception
        parser.getRecords();
    }

    /**
     * Test parsing a CSV file with an inconsistent format.
     *
     * @throws IOException
     * @throws MalformedCSVFormatException
     */
    @Test(expected = MalformedCSVFormatException.class)
    public void testParseInconsistentFormat() throws IOException, MalformedCSVFormatException {
        final String data = "col1,col2\n1,2\ncol3";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = CSVParser.parse(reader, HEADER_FORMAT);

        assertFalse(parser.isClosed());

        parser.close();
        reader.close();
        inputStream.close();
    }

    /**
     * Test closing a parser that has already been closed.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ByteArrayReader reader = new ByteArrayReader(inputStream.readAllBytes());

        CSVParser parser = new CSVParser(reader, SIMPLE_FORMAT);
        parser.close();
        parser.close();
    }
}