package org.apache.commons.csv; // Make sure to import all necessary classes

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
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
 * Tests for CSVParser class.
 */
public class CSVParserTest {
    // 

    /**
     * Test constructor with valid arguments.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testCSVParserWithValidArguments() throws IOException {
        final File file = new File("testCSVFile.csv");
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(file, format);
        assertNull(parser.getRecords());
        parser.close();
    }

    /**
     * Test constructor with null argument.
     *
     * @throws IOException for input/output error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCSVParserWithNullArguments() throws IOException {
        final String string = null;
        final CSVFormat format = new CSVFormat();
        CSVParser.parse(string, format);
    }

    /**
     * Test parse method with valid arguments.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseWithValidArguments() throws IOException {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name");
        assertEquals(records.get(0).getName(1), "Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "Mike");
        assertEquals(records.get(1).get(1), "25");
        assertEquals(records.get(1).get(2), "Chicago");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
    }

    /**
     * Test parse method with invalid arguments.
     *
     * @throws IOException for input/output error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidArguments() throws IOException {
        final String csvContent = "Name,Age,City\nName,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        try {
            parser.getRecords();
        } finally {
            parser.close();
        }
    }

    /**
     * Test parsing CSV records with custom format.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Collections.singletonList("Name1"))
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name1");
        assertEquals(records.get(0).getName(1), "Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "John");
        assertEquals(records.get(1).get(1), "30");
        assertEquals(records.get(1).get(2), "New York");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
    }

    /**
     * Test parsing CSV records with comment line.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVWithCommentLine() throws IOException {
        final String csvContent = "Name,Age,City\nJohn,30,New York\n# This is a comment line\nMike,25,Chicago\n";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name");
        assertEquals(records.get(0).getName(1), "Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "Mike");
        assertEquals(records.get(1).get(1), "25");
        assertEquals(records.get(1).get(2), "Chicago");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        assertEquals(records.get(1).getComment(), "# This is a comment line");
        parser.close();
    }

    /**
     * Test iterating CSV records using iterator.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testIteratingCSVRecordsUsingIterator() throws IOException {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        Iterator<CSVRecord> iterator = parser.iterator();
        assertEquals(iterator.next(), records.get(0));
        assertEquals(iterator.next(), records.get(1));
        assertFalse(iterator.hasNext());
        parser.close();
    }

    /**
     * Test parsing records from a file.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseRecordsFromFile() throws IOException {
        final String filePath = "CSVParserTest.class";
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final File file = new File(filePath);
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name");
        assertEquals(records.get(0).getName(1), "Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "Mike");
        assertEquals(records.get(1).get(1), "25");
        assertEquals(records.get(1).get(2), "Chicago");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
        file.delete();
    }

    /**
     * Test parsing empty CSV file.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseEmptyCSVFile() throws IOException {
        final String filePath = "CSVParserTest.class";
        final File file = new File(filePath);
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.isEmpty());
        parser.close();
        file.delete();
    }

    /**
     * Test parsing CSV file with invalid format.
     *
     * @throws IOException for input/output error
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSVFile() throws IOException {
        final String filePath = "CSVParserTest.class";
        final File file = new File(filePath);
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(file, format);
        try {
            parser.getRecords();
        } finally {
            parser.close();
        }
    }

    /**
     * Test parsing CSV records with custom header names.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVRecordsWithCustomHeaderNames() throws IOException {
        final String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        final Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("Name1", 0);
        headerMap.put("Age1", 1);
        headerMap.put("City1", 2);
        final CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Collections.singletonList("Name1"))
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name1");
        assertEquals(records.get(0).getName(1), "Age1");
        assertEquals(records.get(0).getName(2), "City1");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "John");
        assertEquals(records.get(1).get(1), "30");
        assertEquals(records.get(1).get(2), "New York");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
    }
    
    /**
     * Test parsing CSV records with whitespace in header names.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVRecordsWithWhitespaceHeaderNames() throws IOException {
        final String csvContent = "Name, Age, City\nJohn, 30, New York\nMike, 25, Chicago\n";
        final CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Name", " Age", "City"))
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name");
        assertEquals(records.get(0).getName(1), " Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "John");
        assertEquals(records.get(1).get(1), "30");
        assertEquals(records.get(1).get(2), "New York");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
    }

    /**
     * Test parsing CSV records with empty lines.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVRecordsWithEmptyLines() throws IOException {
        final String csvContent = "Name, Age, City\nJohn, 30, New York\n\nMike, 25, Chicago\n";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name");
        assertEquals(records.get(0).getName(1), "Age");
        assertEquals(records.get(0).getName(2), "City");
        assertEquals(records.get(0).getRecordNumber(), 1L);
        assertEquals(records.get(1).get(0), "Mike");
        assertEquals(records.get(1).get(1), "25");
        assertEquals(records.get(1).get(2), "Chicago");
        assertEquals(records.get(1).getRecordNumber(), 2L);
        parser.close();
    }

    /**
     * Test exception handling when parsing invalid CSV format.
     *
     * @throws IOException for input/output error
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSVFormat() throws IOException {
        final String csvContent = "Name, Age, City\nJohn, 30, New York\nMike, 25, Chicago\n123";
        final CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        try {
            parser.getRecords();
        } finally {
            parser.close();
        }
    }

    /**
     * Test skipping header record when parsing CSV.
     *
     * @throws IOException for input/output error
     */
    @Test
    public void testParseCSVWithSkippedHeaderRecord() throws IOException {
        final String csvContent = "Name, Age, City\nName1, Age1, City1\nJohn, 30, New York\nMike, 25, Chicago\n";
        final CSVFormat format = new CSVFormatBuilder()
                .setHeaderNames(Collections.singletonList("Name1"))
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), format);
        List<CSVRecord> records = parser.getRecords();
        assertFalse(records.isEmpty());
        assertEquals(records.get(0).size(), 3);
        assertEquals(records.get(0).getName(0), "Name1");
        assertEquals(records.get(0).getName(1), "Age1");
        assertEquals(records.get(0).getName(2), "City1");
        assertEquals(records.get(0).getRecordNumber(), 2L);
        assertEquals(records.get(1).get(0), "John");
        assertEquals(records.get(1).get(1), "30");
        assertEquals(records.get(1).get(2), "New York");
        assertEquals(records.get(1).getRecordNumber(), 3L);
        parser.close();
    }

    // 
}