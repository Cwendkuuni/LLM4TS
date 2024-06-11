package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;

/**
 * Test cases for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file using the RFC 4180 format.
     */
    @org.junit.Test
    public void testParseRfc4180() throws IOException {
        // Given
        final String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,40";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.RFC4180);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeaderNames());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals(40, record2.get("Age").intValue());

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @org.junit.Test
    public void testParseCustomFormat() throws IOException {
        // Given
        final String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,40";

        // When
        final CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Name", "Age"))
                .build();
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), customFormat);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeaderNames());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals(40, record2.get("Age").intValue());

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with an empty header.
     */
    @org.junit.Test
    public void testParseEmptyHeader() throws IOException {
        // Given
        final String csvContent = "A,B\n1,2\n3,4";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.DEFAULT);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[0], record1.getHeaderNames());
        assertArrayEquals(new String[0], record2.getHeaderNames());
        assertFalse(record1.hasHeaderMap());
        assertFalse(record2.hasHeaderMap());

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with a comment.
     */
    @org.junit.Test
    public void testParseComment() throws IOException {
        // Given
        final String csvContent = "Name,Age\n# This is a comment\nJohn Doe,30\nJane Doe,40";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.RFC4180);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeaderNames());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals(40, record2.get("Age").intValue());
        assertEquals("This is a comment", record1.getComment());
        assertNull(record2.getComment());

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with invalid content.
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidContent() throws IOException {
        // Given
        final String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,40\n# This is a comment\nInvalid content";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.RFC4180);

        // Then
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // Throws an IOException

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with an empty line.
     */
    @org.junit.Test
    public void testParseEmptyLine() throws IOException {
        // Given
        final String csvContent = "Name,Age\nJohn Doe,30\nJane Doe,40\n";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.RFC4180);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeaderNames());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals(40, record2.get("Age").intValue());
        assertTrue(parser.hasNext());
        parser.nextRecord(); // Throws a NoSuchElementException

        // Cleanup
        parser.close();
    }

    /**
     * Test parsing a CSV file with the header repeated multiple times.
     */
    @org.junit.Test
    public void testParseHeaderRepeated() throws IOException {
        // Given
        final String csvContent = "Name,Age\nName,Age\nJohn Doe,30\nJane Doe,40";

        // When
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csvContent.getBytes()), CSVFormat.RFC4180);

        // Then
        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Name", "Age"}, record1.getHeaderNames());
        assertArrayEquals(new String[]{"Name", "Age"}, record2.getHeaderNames());
        assertEquals("Name", record1.getHeaderMap().get("Name"));
        assertEquals("Age", record1.getHeaderMap().get("Age"));
        assertEquals("John Doe", record1.get("Name"));
        assertEquals(30, record1.get("Age").intValue());
        assertEquals("Jane Doe", record2.get("Name"));
        assertEquals(40, record2.get("Age").intValue());

        // Cleanup
        parser.close();
    }
}