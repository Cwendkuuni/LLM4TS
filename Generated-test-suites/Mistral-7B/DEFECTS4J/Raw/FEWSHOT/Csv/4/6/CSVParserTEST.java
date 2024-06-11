package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        CSVParser parser = CSVParser.parse(new File("test.csv"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "a,b\nc,d";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(new InputStreamReader(url.openStream()), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        CSVParser parser = CSVParser.parse(new StringReader("a;b\nc;d"), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        parser.close();
    }

    @Test
    public void testGetRecords() throws IOException {
        String csvData = "a,b\nc,d";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals("a", records.get(0).get(0));
        assertEquals("b", records.get(0).get(1));
        assertEquals("c", records.get(1).get(0));
        assertEquals("d", records.get(1).get(1));

        parser.close();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();

        assertEquals(2, headerMap.size());
        assertEquals("Name", headerMap.get("Name"));
        assertEquals("Age", headerMap.get("Age"));

        parser.close();
    }

    @Test
    public void testGetRecordNumber() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        assertEquals(0, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(1, parser.getRecordNumber());
        parser.nextRecord();
        assertEquals(2, parser.getRecordNumber());

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreRecords() throws IOException {
        String csvData = "Name,Age\nJohn,30";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        parser.nextRecord();

        parser.nextRecord(); // Should throw NoSuchElementException

        parser.close();
    }
}