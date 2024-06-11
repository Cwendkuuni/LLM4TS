package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.class$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test parsing of simple CSV file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String csvContent = "name, age\nAlice, 25\nbob, 30";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertNotNull(record1);
        assertNotNull(record2);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));

        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));

        assertEquals("bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));
    }

    /**
     * Test parsing of CSV with header.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseCSVWithHeader() throws IOException {
        final String csvContent = "name, age\nAlice, 25\nbob, 30";
        final CSVFormat format = CSVFormat.DEFAULT.withHeader();
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertNotNull(record1);
        assertNotNull(record2);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));

        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));

        assertEquals("bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));

        assertNotNull(record1.getHeaderMap());
        assertEquals(2, record1.getHeaderMap().size());
    }

    /**
     * Test parsing of CSV with empty lines.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseCSVWithEmptyLines() throws IOException {
        final String csvContent = "name, age\nAlice, 25\n\nBob, 30";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertNotNull(record1);
        assertNotNull(record2);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));

        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));

        assertEquals("Bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));
    }

    /**
     * Test parsing of CSV with comment.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseCSVWithComment() throws IOException {
        final String csvContent = "name, age\nAlice, 25# This is a comment\nbob, 30";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertNotNull(record1);
        assertNotNull(record2);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));

        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));

        assertEquals("bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));

        assertEquals("This is a comment", record1.getComment());
        assertNull(record2.getComment());
    }

    /**
     * Test parsing of empty CSV.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        final String csvContent = "";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(0, records.size());
    }

    /**
     * Test parsing of non-CSV file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseNonCSV() throws IOException {
        final String xmlContent = "<xml version=\"1.0\">...</xml>";
        final CSVFormat format = CSVFormat.DEFAULT;
        parse(new InputStreamReader(new ByteArrayInputStream(xmlContent.getBytes()), StandardCharsets.UTF_8), format);
    }

    /**
     * Test parsing of huge CSV file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseHugeCSV() throws IOException {
        final int recordsSize = 1000;
        final String csvContent = generateHugeCSVContent(recordsSize);
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(recordsSize, records.size());
        for (CSVRecord record : records) {
            assertEquals(recordsSize, record.getRecordNumber());
        }
    }

    /**
     * Test parsing of CSV file with custom separator.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @org.junit.Test
    public void testParseCSVWithCustomSeparator() throws IOException {
        final String csvContent = "name:age\nAlice:25\nbob:30";
        final CSVFormat format = new CSVFormatBuilder().setSeparator(':').build();
        final CSVParser parser = parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes()),
                StandardCharsets.UTF_8), format);

        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);

        assertNotNull(record1);
        assertNotNull(record2);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));

        assertEquals("Alice", record1.get("name"));
        assertEquals(25, record1.getInteger("age"));

        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));

        assertEquals("bob", record2.get("name"));
        assertEquals(30, record2.getInteger("age"));
    }

    private static String generateHugeCSVContent(int recordsSize) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < recordsSize; i++) {
            content.append("name, age\nAlice, ").append(i).append("\n");
        }
        return content.toString();
    }
}