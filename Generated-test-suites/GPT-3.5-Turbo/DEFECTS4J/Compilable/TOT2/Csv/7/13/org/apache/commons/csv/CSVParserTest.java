package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.RFC4180;
        CSVParser parser = CSVParser.parse(file, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(3, records.size());
    }

    @Test
    public void testParseString() throws IOException {
        String csvString = "name,age\nJohn,25\nJane,30";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(csvString, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        Charset charset = Charset.defaultCharset();
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = CSVParser.parse(url, charset, format);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(5, records.size());
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvString = "name,age\nJohn,25\nJane,30";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(csvString, format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertNotNull(headerMap);
        assertEquals(2, headerMap.size());
        assertTrue(headerMap.containsKey("name"));
        assertTrue(headerMap.containsKey("age"));
    }

    // Additional test cases for edge cases and exceptions can be added here

}