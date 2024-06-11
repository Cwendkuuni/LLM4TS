package org.apache.commons.csv.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     */
    @Test
    public void testSimpleCSV() throws IOException {
        final String csv = "Name,Age,City\nJohn,25,New York\nMary,30,Paris\n";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csv.getBytes()), format);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John", record1.get(1));
        assertEquals("Age", record1.get(1));
        assertEquals(25, record1.getInteger(1));
        assertEquals("City", record1.get(2));
        assertEquals("New York", record1.get(2));
        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Mary", record2.get(1));
        assertEquals("Age", record2.get(1));
        assertEquals(30, record2.getInteger(1));
        assertEquals("City", record2.get(2));
        assertEquals("Paris", record2.get(2));
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testCustomCSV() throws IOException {
        final String csv = "Name;Age;City\nJohn;25;New York\nMary;30;Paris\n";
        final CSVFormat format = CSVFormat.builder()
                .setHeader("Name", "Age", "City")
                .build();
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csv.getBytes()), format);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();
        assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        assertEquals("Name", record1.get(0));
        assertEquals("John", record1.get(1));
        assertEquals("Age", record1.get(1));
        assertEquals(25, record1.getInteger(1));
        assertEquals("City", record1.get(2));
        assertEquals("New York", record1.get(2));
        CSVRecord record2 = records.get(1);
        assertEquals("Name", record2.get(0));
        assertEquals("Mary", record2.get(1));
        assertEquals("Age", record2.get(1));
        assertEquals(30, record2.getInteger(1));
        assertEquals("City", record2.get(2));
        assertEquals("Paris", record2.get(2));
    }

    /**
     * Test parsing an empty CSV file.
     */
    @Test
    public void testEmptyCSV() throws IOException {
        final String csv = "";
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csv.getBytes()), format);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();
        assertEquals(0, records.size());
    }

    /**
     * Test parsing a CSV file with an invalid format.
     */
    @Test(expected = IOException.class)
    public void testInvalidCSV() throws IOException {
        final String csv = "Name,Age,City\nJohn,25,New York\nMary\n30,Paris\n";
        final CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(new ByteArrayInputStream(csv.getBytes()), format);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();
        fail("Did not throw IOException");
    }
}