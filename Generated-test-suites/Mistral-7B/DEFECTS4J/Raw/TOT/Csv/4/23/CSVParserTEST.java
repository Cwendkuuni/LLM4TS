package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.junit.Test;

/**
 * Tests for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with no header.
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("a,b\nc,d\ne,f".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("a", "b"), record.getValues());
        }
        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a header.
     */
    @Test
    public void testParseCSVWithHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Name,Age\nJohn,32\nMary,27\n".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT.withHeader());
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("Name"), record.getHeaderMap().keySet());
            assertEquals("Name", record.getHeaderMap().get("Name"));
            assertEquals(Arrays.asList("John"), record.getValues());
        }
        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a comment.
     */
    @Test
    public void testParseCSVWithComment() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("a,b\n#comment\nc,d".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals(Arrays.asList("a"), record.getValues());
        assertNull(record.getComment());

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals(Arrays.asList("c"), record.getValues());
        assertEquals("comment", record.getComment());

        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with empty lines.
     */
    @Test
    public void testParseCSVWithEmptyLines() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("a,b\n\nc,d".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("a"), record.getValues());
            assertNull(record.getComment());
        }
        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a malformed record.
     */
    @Test(expected = IOException.class)
    public void testParseCSVMalformedRecord() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("a,b\nc,d\nincomplete".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    /**
     * Test parsing a CSV file with a missing header.
     */
    @Test
    public void testParseCSVWithoutHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("a,b\nc,d".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecord record = parser.nextRecord();
        assertEquals(1, record.getRecordNumber());
        assertEquals(Arrays.asList("a"), record.getValues());
        assertNull(record.getComment());

        record = parser.nextRecord();
        assertEquals(2, record.getRecordNumber());
        assertEquals(Arrays.asList("c"), record.getValues());
        assertNull(record.getComment());

        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a missing value.
     */
    @Test
    public void testParseCSVWithMissingValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Name,Age\nJohn,,32\n".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT.withHeader());
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("Name"), record.getHeaderMap().keySet());
            assertEquals("Name", record.getHeaderMap().get("Name"));
            if (count == 1) {
                assertEquals(Collections.emptyList(), record.getValues());
            } else {
                assertEquals(Arrays.asList("Age"), record.getHeaderMap().keySet());
                assertEquals("Age", record.getHeaderMap().get("Age"));
                assertEquals(Arrays.asList("32"), record.getValues());
            }
        }
        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a large number of records.
     */
    @Test
    public void testParseCSVLargerFile() throws IOException {
        String csvContent = "";
        for (int i = 0; i < 1000; i++) {
            csvContent += "Record" + i + ",Value" + i + "\n";
        }
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        CSVRecordIterator iterator = parser.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            CSVRecord record = iterator.next();
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList(new String[]{"Record" + count, "Value" + count}), record.getValues());
            assertNull(record.getComment());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Name;Value\nJohn;32\nMary;27\n".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT.withDelimiter(';'));
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("Name"), record.getHeaderMap().keySet());
            assertEquals("Name", record.getHeaderMap().get("Name"));
            assertEquals(Arrays.asList("John"), record.getValues());
        }
        assertFalse(parser.hasNext());
    }

    /**
     * Test parsing a CSV file with a custom format and a header.
     */
    @Test
    public void testParseCSVWithCustomFormatAndHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Name;Value\nName;Age\nJohn;32\nMary;27\n".getBytes());
        CSVParser parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT.withDelimiter(';').withHeader());
        CSVRecord record;
        int count = 0;
        while ((record = parser.nextRecord()) != null) {
            assertEquals(++count, record.getRecordNumber());
            assertEquals(Arrays.asList("Name"), record.getHeaderMap().keySet());
            assertEquals("Name", record.getHeaderMap().get("Name"));
            assertEquals(Arrays.asList("Name"), record.getHeaderMap().getNames());
            assertEquals(Arrays.asList("John"), record.getValues());
        }
        assertFalse(parser.hasNext());
    }
}