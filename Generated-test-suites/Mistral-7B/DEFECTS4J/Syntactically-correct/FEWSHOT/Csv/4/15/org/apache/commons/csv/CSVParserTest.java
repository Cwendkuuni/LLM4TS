package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.extensions.ExtendedBufferedReader;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        String filePath = "testFiles/CSVParserTestFile.csv";

        CSVParser parser = CSVParser.parse(new File(filePath), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertNotNull(parser.getHeaderMap());
        assertEquals(4, parser.getHeaderMap().size());
        assertEquals("Name", parser.getHeaderMap().get("Name"));
        assertEquals("Age", parser.getHeaderMap().get("Age"));
        assertEquals("City", parser.getHeaderMap().get("City"));
        assertEquals("Country", parser.getHeaderMap().get("Country"));

        assertNull(parser.iterator().next());
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,30,New York,USA\n" +
                "Jane Doe,25,Paris,France";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertNotNull(parser.getHeaderMap());
        assertEquals(4, parser.getHeaderMap().size());
        assertEquals("Name", parser.getHeaderMap().get("Name"));
        assertEquals("Age", parser.getHeaderMap().get("Age"));
        assertEquals("City", parser.getHeaderMap().get("City"));
        assertEquals("Country", parser.getHeaderMap().get("Country"));

        assertNull(parser.iterator().next());
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "https://example.com/csvFile.csv";

        CSVParser parser = CSVParser.parse(new URL(urlString), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertNotNull(parser.getHeaderMap());
        assertEquals(4, parser.getHeaderMap().size());
        assertEquals("Name", parser.getHeaderMap().get("Name"));
        assertEquals("Age", parser.getHeaderMap().get("Age"));
        assertEquals("City", parser.getHeaderMap().get("City"));
        assertEquals("Country", parser.getHeaderMap().get("Country"));

        assertNull(parser.iterator().next());
        parser.close();
    }

    @Test
    public void testParseReader() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,30,New York,USA\n" +
                "Jane Doe,25,Paris,France";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        Reader reader = new InputStreamReader(inputStream, "UTF-8");

        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertNotNull(parser.getHeaderMap());
        assertEquals(4, parser.getHeaderMap().size());
        assertEquals("Name", parser.getHeaderMap().get("Name"));
        assertEquals("Age", parser.getHeaderMap().get("Age"));
        assertEquals("City", parser.getHeaderMap().get("City"));
        assertEquals("Country", parser.getHeaderMap().get("Country"));

        assertNull(parser.iterator().next());
        parser.close();
    }

    @Test
    public void testParseEmptyCsvFile() throws IOException {
        String filePath = "testFiles/CSVParserTestEmptyFile.csv";

        CSVParser parser = CSVParser.parse(new File(filePath), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        assertEquals(0, records.size());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File file = new File("testFiles/CSVParserTestFile.csv");
        CSVParser.parse(file, null);
    }

    @Test(expected = IOException.class)
    public void testParseNullReader() throws IOException {
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseEmptyReader() throws IOException {
        Reader reader = new StringReader("");
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCsvData() throws IOException {
        String csvData = "Name,Age,City,Country\n" +
                "John Doe,30,New York,USA\n" +
                "Jane Doe,25,Paris,France\n" +
                "John Doe,30,New York,USA\n" +
                "Name,Age,City,Country";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertEquals(3, records.size());

        assertNull(parser.iterator().next());
        parser.close();
    }
}