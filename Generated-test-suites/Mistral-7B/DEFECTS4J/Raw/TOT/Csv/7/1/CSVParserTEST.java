package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Tests for {@link CSVParser} class.
 */
public class CSVParserTest {

    /**
     * Test parsing records from a simple CSV file using default format.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        final String csvContent = "name,age\nJohn Doe,30\nJane Doe,25";
        try (CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                String[] values = record.toArray(String[].class);
                switch (i) {
                case 0:
                    assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(String[]
                            .class));
                    assertEquals("John Doe", values[0]);
                    assertEquals(30, Integer.parseInt(values[1]));
                    break;
                case 1:
                    assertArrayEquals(new String[]{"name", "age"}, record.getHeaderMap().keySet().toArray(String[]
                            .class));
                    assertEquals("Jane Doe", values[0]);
                    assertEquals(25, Integer.parseInt(values[1]));
                    break;
                default:
                    fail("More records than expected");
                }
            }
        }
    }

    /**
     * Test parsing records from a CSV file with custom format.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final String csvContent = "name,age\nJohn Doe,30\nJane Doe,25";

        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Name", "Age"))
                .build();

        try (CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                String[] values = record.toArray(String[].class);
                switch (i) {
                case 0:
                    assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(String[]
                            .class));
                    assertEquals("John Doe", values[0]);
                    assertEquals(30, Integer.parseInt(values[1]));
                    break;
                case 1:
                    assertArrayEquals(new String[]{"Name", "Age"}, record.getHeaderMap().keySet().toArray(String[]
                            .class));
                    assertEquals("Jane Doe", values[0]);
                    assertEquals(25, Integer.parseInt(values[1]));
                    break;
                default:
                    fail("More records than expected");
                }
            }
        }
    }

    /**
     * Test parsing records from an empty CSV file.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test
    public void testParseEmptyCSV() throws IOException {
        try (CSVParser parser = CSVParser.parse(new File("test/empty.csv"), CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(0, records.size());
        }
    }

    /**
     * Test parsing records from a CSV file with invalid format.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvContent = "name,age\nJohn Doe,30\nJane Doe,25\ninvalid";
        try (CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT)) {
            parser.getRecords();
            fail("Expected IOException");
        }
    }

    /**
     * Test parsing records from a CSV file with multiline values.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test
    public void testParseCSVWithMultilineValues() throws IOException {
        final String csvContent = "name,description\nJohn Doe,\"First name: John\nLast name: Doe\"\n";
        try (CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(1, records.size());
            CSVRecord record = records.get(0);
            String[] values = record.toArray(String[].class);
            assertEquals(2, values.length);
            assertEquals("name", values[0]);
            assertEquals("First name: John\nLast name: Doe", values[1]);
        }
    }

    /**
     * Test parsing records from a CSV file with comments.
     *
     * @throws IOException if parsing fails
     */
    @org.junit.Test
    public void testParseCSVWithComments() throws IOException {
        final String csvContent = "name,age\nJohn Doe,30\n# Comment\nJane Doe,25";
        try (CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                if (i == 1) {
                    assertEquals(Collections.emptyList(), record.getComments());
                    continue;
                }
                String comment = record.getComments().get(0);
                assertEquals("Comment", comment);
            }
        }
    }
}