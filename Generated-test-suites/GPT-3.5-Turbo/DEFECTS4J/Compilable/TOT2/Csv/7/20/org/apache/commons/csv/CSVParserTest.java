package org.apache.commons.csv;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
    }

    @Test
    public void testParseString() throws IOException {
        String data = "name,age\nJohn,25\nJane,30";
        CSVParser parser = CSVParser.parse(data, CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/test.csv");
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        assertNotNull(records);
        assertFalse(records.isEmpty());
    }

    @Test
    public void testInvalidFile() {
        try {
            File file = new File("invalid.csv");
            CSVParser.parse(file, CSVFormat.DEFAULT);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("I/O error"));
        }
    }

    @Test
    public void testInvalidURL() {
        try {
            URL url = new URL("http://example.com/invalid.csv");
            CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("I/O error"));
        }
    }

}