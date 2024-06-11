package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file with correct format.
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25\n";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("name", record1.getHeaderMap().get("age"));
        assertEquals("John", record1.get("name"));
        assertEquals(30, record1.get("age").intValue());
        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));
        assertEquals("Mary", record2.get("name"));
        assertEquals(25, record2.get("age").intValue());
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testParseCustomCSV() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25\n";
        final CSVFormat format = new CSVFormatBuilder().setHeaderNames(Collections.singletonList("Name"),
                Collections.singletonList("Age"))
                .build();
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), format);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Age", record2.getHeaderMap().get("Name"));
        assertEquals("Name", record2.getHeaderMap().get("Age"));
        assertEquals("Mary", record2.get("Name"));
        assertEquals(25, record2.get("Age").intValue());
    }

    /**
     * Test parsing a CSV file with a header and no records.
     */
    @Test
    public void testParseCSVWithHeaderButNoRecords() throws IOException {
        final String csvData = "name,age";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);

        assertEquals(0, records.size());
    }

    /**
     * Test parsing a CSV file with an invalid format.
     */
    @Test
    public void testParseInvalidCSV() {
        assertThrows(IllegalArgumentException.class, () -> {
            parse(new ByteArrayInputStream("name,age\nJohn,30\nMary,25\nname".getBytes()), CSVFormat.DEFAULT);
        });
    }

    /**
     * Test parsing a CSV file with empty records.
     */
    @Test
    public void testParseEmptyRecords() throws IOException {
        final String csvData = "name,age\nJohn,\nMary,\n";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertNull(record1.get("name"));
        assertNull(record1.get("age"));
        assertNull(record2.get("name"));
        assertNull(record2.get("age"));
    }

    /**
     * Test parsing a CSV file with a comment.
     */
    @Test
    public void testParseCSVWithComment() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25# This is a comment\n";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("name", record1.getHeaderMap().get("name"));
        assertEquals("age", record1.getHeaderMap().get("age"));
        assertEquals("John", record1.get("name"));
        assertEquals(30, record1.get("age").intValue());
        assertEquals("age", record2.getHeaderMap().get("name"));
        assertEquals("name", record2.getHeaderMap().get("age"));
        assertEquals("Mary", record2.get("name"));
        assertEquals(25, record2.get("age").intValue());
        assertEquals("This is a comment", record2.getComment());
    }

    /**
     * Test parsing a CSV file with a custom header map.
     */
    @Test
    public void testParseCSVWithCustomHeaderMap() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25";
        final Map<String, Integer> customHeaderMap = new LinkedHashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
        }};
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT,
                customHeaderMap);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("FirstName", record1.getHeaderMap().get("name"));
        assertEquals("LastName", record1.getHeaderMap().get("age"));
        assertEquals("John", record1.get("FirstName"));
        assertEquals(30, record1.get("LastName").intValue());
        assertEquals("LastName", record2.getHeaderMap().get("name"));
        assertEquals("FirstName", record2.getHeaderMap().get("age"));
        assertEquals("Mary", record2.get("FirstName"));
        assertEquals(25, record2.get("LastName").intValue());
    }

    /**
     * Test parsing a CSV file with a custom header map and custom format.
     */
    @Test
    public void testParseCSVWithCustomHeaderMapAndCustomFormat() throws IOException {
        final String csvData = "FirstName,LastName\nJohn,30\nMary,25";
        final CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames(Collections.singletonList("FirstName"),
                Collections.singletonList("LastName"))
                .build();
        final Map<String, Integer> customHeaderMap = new LinkedHashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
        }};
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), customFormat,
                customHeaderMap);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("FirstName", record1.getHeaderMap().get("FirstName"));
        assertEquals("LastName", record1.getHeaderMap().get("LastName"));
        assertEquals("John", record1.get("FirstName"));
        assertEquals(30, record1.get("LastName").intValue());
        assertEquals("LastName", record2.getHeaderMap().get("FirstName"));
        assertEquals("FirstName", record2.getHeaderMap().get("LastName"));
        assertEquals("Mary", record2.get("FirstName"));
        assertEquals(25, record2.get("LastName").intValue());
    }

    /**
     * Test parsing a CSV file with a custom header map and custom format with comments.
     */
    @Test
    public void testParseCSVWithCustomHeaderMapAndCustomFormatWithComments() throws IOException {
        final String csvData = "FirstName,LastName\nJohn,30# This is a comment\nMary,25";
        final CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames(
                Collections.singletonList("FirstName"), Collections.singletonList("LastName"))
                .build();
        final Map<String, Integer> customHeaderMap = new LinkedHashMap<String, Integer>() {{
            put("FirstName", 0);
            put("LastName", 1);
        }};
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), customFormat,
                customHeaderMap);

        assertEquals(2, records.size());
        final CSVRecord record1 = records.get(0);
        final CSVRecord record2 = records.get(1);

        assertEquals("FirstName", record1.getHeaderMap().get("FirstName"));
        assertEquals("LastName", record1.getHeaderMap().get("LastName"));
        assertEquals("John", record1.get("FirstName"));
        assertEquals(30, record1.get("LastName").intValue());
        assertEquals("LastName", record2.getHeaderMap().get("FirstName"));
        assertEquals("FirstName", record2.getHeaderMap().get("LastName"));
        assertEquals("Mary", record2.get("FirstName"));
        assertEquals(25, record2.get("LastName").intValue());
        assertEquals("This is a comment", record2.getComment());
    }

    /**
     * Test closing the parser.
     */
    @Test
    public void testCloseParser() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);
        records.iterator().next();

        final CSVParser parser = new CSVParser(new ByteArrayReader(csvData.getBytes()), CSVFormat.DEFAULT);
        parser.close();

        assertThrows(NoSuchElementException.class, () -> parser.nextRecord());
    }

    /**
     * Test parsing a CSV file with a large input.
     */
    @Test
    public void testParseLargeCSV() throws IOException {
        final String csvData = "name,age\nJohn,30\nMary,25\nJohn Doe,35\nJane Doe,40\nJohn Doe,32\nJane Doe,38\nJohn Doe,37\nJane Doe,39\nJohn Doe,41\nJane Doe,42\nJohn Doe,43\nJane Doe,44\nJohn Doe,45\nJane Doe,46\nJohn Doe,47\nJane Doe,48\nJohn Doe,49\nJane Doe,50";
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), CSVFormat.DEFAULT);

        assertEquals(20, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertNotNull(record);
        }
    }

    /**
     * Test parsing a CSV file with a large input and a custom format.
     */
    @Test
    public void testParseLargeCSVWithCustomFormat() throws IOException {
        final String csvData = "FirstName,LastName\nJohn,30\nMary,25\nJohn Doe,35\nJane Doe,40\nJohn Doe,32\nJane Doe,38\nJohn Doe,37\nJane Doe,39\nJohn Doe,41\nJane Doe,42\nJohn Doe,43\nJane Doe,44\nJohn Doe,45\nJane Doe,46\nJohn Doe,47\nJane Doe,48\nJohn Doe,49\nJane Doe,50";
        final CSVFormat customFormat = new CSVFormatBuilder().setHeaderNames(Collections.singletonList("FirstName"),
                Collections.singletonList("LastName"))
                .build();
        final List<CSVRecord> records = parse(new ByteArrayInputStream(csvData.getBytes()), customFormat);

        assertEquals(20, records.size());
        for (int i = 0; i < records.size(); i++) {
            final CSVRecord record = records.get(i);
            assertNotNull(record);
        }
    }

    /**
     * Test parsing a CSV file with invalid characters.
     */
    @Test
    public void testParseInvalidCSVWithCharacters() {
        assertThrows(IOException.class, () -> parse(new ByteArrayInputStream("name,age\nJohn,30\nMary,25\nJohn,30\nMary,"
                + "25\nMary,25\n".getBytes()), CSVFormat.DEFAULT));
    }

    /**
     * Test parsing a CSV file with invalid characters and a custom format.
     */
    @Test
    public void testParseInvalidCSVWithCustomFormatAndCharacters() {
        assertThrows(IOException.class, () -> parse(new ByteArrayInputStream("name,age\nJohn,30\nMary,25\nJohn,30\nMary,"
                + "25\nMary,25\n".getBytes()), new CSVFormatBuilder().setHeaderNames(Collections.singletonList("name"),
                Collections.singletonList("age"))
                .build()));
    }
}