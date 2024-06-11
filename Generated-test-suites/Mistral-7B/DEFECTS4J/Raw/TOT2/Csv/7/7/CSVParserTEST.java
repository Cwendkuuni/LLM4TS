package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.CSVRecord.iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CSVParser} class
 */
public class CSVParserTest {

    private CSVFormat format;
    private CSVParser parser;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT.withHeader();
    }

    /**
     * Test for {@link CSVParser#parse(File, CSVFormat)} method.
     *
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        final File file = new File("src/test/resources/CSVParserTestData.csv");
        final CSVParser parser = parse(file, format);

        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(4, firstRecord.size());
        assertEquals("Name1", firstRecord.get(0));
        assertEquals("Value1", firstRecord.get(1));
        assertEquals("Value2", firstRecord.get(2));
        assertEquals("Value3", firstRecord.get(3));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("Name2", secondRecord.get(0));
        assertEquals("Value4", secondRecord.get(1));

        final CSVRecord thirdRecord = records.get(2);
        assertEquals(1, thirdRecord.size());
        assertNull(thirdRecord.get(0));

        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(String, CSVFormat)} method.
     */
    @Test
    public void testParseString() {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4";
        final CSVParser parser = new CSVParser(new StringReader(csvData), format);

        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(4, firstRecord.size());
        assertEquals("Name1", firstRecord.get(0));
        assertEquals("Value1", firstRecord.get(1));
        assertEquals("Value2", firstRecord.get(2));
        assertEquals("Value3", firstRecord.get(3));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("Name2", secondRecord.get(0));
        assertEquals("Value4", secondRecord.get(1));

        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(URL, Charset, CSVFormat)} method.
     */
    @Test
    public void testParseURL() throws IOException {
        final String urlString = "file:src/test/resources/CSVParserTestData.csv";
        final URL url = new URL(urlString);
        final CSVParser parser = parse(url, format);

        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(4, firstRecord.size());
        assertEquals("Name1", firstRecord.get(0));
        assertEquals("Value1", firstRecord.get(1));
        assertEquals("Value2", firstRecord.get(2));
        assertEquals("Value3", firstRecord.get(3));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("Name2", secondRecord.get(0));
        assertEquals("Value4", secondRecord.get(1));

        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(Reader)} method.
     */
    @Test
    public void testParseReader() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        final List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());
        final CSVRecord firstRecord = records.get(0);
        assertEquals(4, firstRecord.size());
        assertEquals("Name1", firstRecord.get(0));
        assertEquals("Value1", firstRecord.get(1));
        assertEquals("Value2", firstRecord.get(2));
        assertEquals("Value3", firstRecord.get(3));

        final CSVRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.size());
        assertEquals("Name2", secondRecord.get(0));
        assertEquals("Value4", secondRecord.get(1));

        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(Reader)} method with empty CSV file.
     */
    @Test
    public void testParseEmptyCSVFile() throws IOException {
        final String csvData = "";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        final List<CSVRecord> records = parser.getRecords();
        assertEquals(0, records.size());

        parser.close();
    }

    /**
     * Test for {@link CSVParser#parse(Reader)} method with invalid CSV data.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidCSVData() throws IOException {
        final String csvData = "Name,Value1,Value2Value3";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        parser.getRecords();
    }

    /**
     * Test for {@link CSVParser#iterator()} method.
     */
    @Test
    public void testIterator() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        final List<CSVRecord> records = new ArrayList<>();
        records.addAll(parser.getRecords());

        int index = 0;
        for (CSVRecord record : parser) {
            assertEquals(records.get(index++), record);
        }

        parser.close();
    }

    /**
     * Test for {@link CSVParser#iterator()} method with empty CSV file.
     */
    @Test
    public void testIteratorEmptyCSVFile() {
        final StringReader reader = new StringReader("");
        final CSVParser parser = new CSVParser(reader, format);

        assertSame(Collections.emptyIterator(), parser.iterator());

        parser.close();
    }

    /**
     * Test for {@link CSVParser#iterator()} method with null CSV data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorNullCSVData() {
        final StringReader reader = new StringReader(null);
        final CSVParser parser = new CSVParser(reader, format);

        parser.iterator();
    }

    /**
     * Test for {@link CSVParser#close()} method.
     */
    @Test
    public void testClose() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        parser.close();

        try {
            parser.getRecords();
        } catch (final IOException e) {
            assertEquals("Parser has been closed", e.getMessage());
        }
    }

    /**
     * Test for {@link CSVParser#getCurrentLineNumber()} method.
     */
    @Test
    public void testCurrentLineNumber() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        assertEquals(1, parser.getCurrentLineNumber());

        parser.nextRecord();

        assertEquals(2, parser.getCurrentLineNumber());

        parser.close();
    }

    /**
     * Test for {@link CSVParser#getHeaderMap()} method.
     */
    @Test
    public void testGetHeaderMap() throws IOException {
        final String csvData = "Name1,Value1,Value2,Value3\nName2,Value4,Name3,Value5";
        final StringReader reader = new StringReader(csvData);
        final CSVParser parser = new CSVParser(reader, format);

        final Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(3, headerMap.size());
        assertEquals("Name1", headerMap.get("Name1"));
        assertEquals("Value1", headerMap.get("Name1"));
        assertEquals("Name2", headerMap.get("Name2"));
        assertEquals("Name3", headerMap.get("Name3"));

        parser.close();
    }
}