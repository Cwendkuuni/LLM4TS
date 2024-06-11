package org.apache.commons.csv.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.exceptions.CsvException;
import org.junit.Test;

/**
 * Test class for CSVParser
 */
public class CSVParserTest {

    private static final String CSV_CONTENTS = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles\n";

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        try (Writer writer = new FileWriter(file)) {
            writer.write(CSV_CONTENTS);
        }

        try (CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name", "Age", "City"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
        }
    }

    @Test
    public void testParseString() throws IOException {
        try (CSVParser parser = CSVParser.parse(CSV_CONTENTS, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name", "Age", "City"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
        }
    }

    @Test
    public void testParseURL() throws IOException {
        String urlString = "http://example.com/test.csv";
        try (Reader reader = new InputStreamReader(new URL(urlString).openStream());
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name", "Age", "City"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
        }
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvContentsCustom = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Name2", "Age2", "City2")
                .build();

        try (Reader reader = new StringReader(csvContentsCustom);
             CSVParser parser = new CSVParser(reader, customFormat)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name2", "Age2", "City2"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
        }
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String csvContents = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles";
        try (CSVParser parser = new CSVParser(new StringReader(csvContents), CSVFormat.DEFAULT)) {
            assertEquals(0, parser.getRecordNumber());
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(record.getRecordNumber(), 1 + parser.getRecordNumber());
            }
            parser.nextRecord();
            assertNull(parser.nextRecord());
            assertEquals(3, parser.getRecordNumber());
            parser.close();
        }
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvContents = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles";
        try (CSVParser parser = new CSVParser(new StringReader(csvContents), CSVFormat.DEFAULT)) {
            assertArrayEquals(new Object[]{}, parser.getRecords().toArray());
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name", "Age", "City"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
            parser.close();
        }
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        File emptyFile = new File("test.csv");
        try (Writer writer = new FileWriter(emptyFile);
             CSVParser parser = CSVParser.parse(emptyFile, CSVFormat.DEFAULT)) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(0, records.size());
            parser.close();
        }
    }

    @Test
    public void testParseNullFormat() throws IOException {
        try {
            CSVParser.parse(new StringReader(CSV_CONTENTS), null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseNullReader() throws IOException {
        try {
            new CSVParser(null, CSVFormat.DEFAULT);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseClosedFile() throws IOException {
        File file = new File("test.csv");
        try (Writer writer = new FileWriter(file)) {
            writer.write(CSV_CONTENTS);
        }

        try {
            FileReader reader = new FileReader(file);
            reader.close();
            try (CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
                parser.nextRecord();
                List<CSVRecord> records = parser.getRecords();
                assertEquals(2, records.size());
                parser.close();
            }
            reader.close();
            try (CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT)) {
                fail();
            } catch (IOException e) {
                // expected
            }
        } finally {
            file.delete();
        }
    }

    @Test
    public void testParseFormatWithEmptyHeader() throws IOException {
        String csvContentsEmptyHeader = "Name,Age,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles";
        try (Reader reader = new StringReader(csvContentsEmptyHeader);
             CSVParser parser = new CSVParser(reader, new CSVFormatBuilder().build())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(2, records.size());
            for (CSVRecord record : records) {
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name", "Age", "City"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "35", "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
            parser.close();
        }
    }

    @Test
    public void testParseFormatWithNonEmptyHeader() throws IOException {
        String csvContentsNonEmptyHeader = "Name,Age,City\nName1,Age1,City1\nJohn Doe,35,New York\nJane Doe,27,Los Angeles";
        try (Reader reader = new StringReader(csvContentsNonEmptyHeader);
             CSVParser parser = new CSVParser(reader,
                     new CSVFormatBuilder().setHeader(new String[]{"Name1", "Name2", "Name3"}).build())) {
            List<CSVRecord> records = parser.getRecords();
            assertEquals(3, records.size());
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                assertEquals(3, record.size());
                String[] expectedValues = new String[]{"Name1", "Name2", "Name3"};
                assertArrayEquals(expectedValues, record.getNames());
                Object[] expectedValuesData = new Object[]{ "John Doe", "Name2_value" + i, "New York" };
                assertArrayEquals(expectedValuesData, record.toArray());
            }
            parser.close();
        }
    }

    @Test(expected = CsvException.class)
    public void testParseInvalidCsv() throws IOException {
        String csvContentsInvalid = "Name,Age,City\nName,35,City\nJohn Doe,35,New York\nJane Doe,27,Los Angeles\nName";
        try (Reader reader = new StringReader(csvContentsInvalid);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            parser.nextRecord();
            parser.nextRecord();
            parser.nextRecord();
            parser.nextRecord();
            parser.nextRecord();
        }
    }
}