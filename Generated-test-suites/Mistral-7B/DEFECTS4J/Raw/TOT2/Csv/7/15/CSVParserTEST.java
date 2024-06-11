import org.apache.commons.csv.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVParser parser;
    private CSVFormat format;

    @BeforeEach
    public void setup() {
        format = new CSVFormatBuilder()
                .setHeader("Name", "Age")
                .build();
        parser = new CSVParser(new StringReader("Name,Age\nAlice,30\nBob,25"), format);
    }

    @Test
    public void testParseBasic() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assertions.assertEquals(2, record.size());
            Assertions.assertEquals("Alice", record.get(0));
            Assertions.assertEquals(30, record.get(1).intValue());
            Assertions.assertEquals("Bob", records.get(1).get(0));
            Assertions.assertEquals(25, records.get(1).get(1).intValue());
        }
    }

    @Test
    public void testParseEmptyCsv() throws IOException {
        parser = new CSVParser(new StringReader(""), format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(0, records.size());
    }

    @Test
    public void testParseEmptyLine() throws IOException {
        parser = new CSVParser(new StringReader("\n"), format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(0, records.size());
    }

    @Test
    public void testParseNullValues() throws IOException {
        parser = new CSVParser(new StringReader("Name,Age\nAlice,null\nBob,30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assertions.assertEquals(2, record.size());
            Assertions.assertEquals("Alice", record.get(0));
            Assertions.assertNull(record.get(1));
            Assertions.assertEquals("Bob", records.get(1).get(0));
            Assertions.assertEquals(30, records.get(1).get(1).intValue());
        }
    }

    @Test
    public void testParseComment() throws IOException {
        parser = new CSVParser(new StringReader("Name,Age\nAlice,30#this is a comment\nBob,25"), format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assertions.assertEquals(2, record.size());
            Assertions.assertEquals("Alice", record.get(0));
            Assertions.assertEquals(30, record.get(1).intValue());
            Assertions.assertEquals("Bob", records.get(1).get(0));
            Assertions.assertEquals(25, records.get(1).get(1).intValue());
        }
    }

    @Test
    public void testParseQuotes() throws IOException {
        parser = new CSVParser(new StringReader("Name,\"Alice Smith\"\nAge,30"), format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assertions.assertEquals(2, record.size());
        Assertions.assertEquals("Alice Smith", record.get(0));
        Assertions.assertEquals(30, record.get(1).intValue());
    }

    @Test
    public void testParseMissingValues() throws IOException {
        parser = new StringReader("Name,Age\nAlice,\nBob,30");
        CSVParser parser2 = new CSVParser(parser, format);
        List<CSVRecord> records = parser2.getRecords();
        Assertions.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assertions.assertEquals(2, record.size());
            Assertions.assertEquals("Alice", record.get(0));
            Assertions.assertNull(record.get(1));
            Assertions.assertEquals("Bob", records.get(1).get(0));
            Assertions.assertEquals(30, records.get(1).get(1).intValue());
        }
    }

    @Test
    public void testParseInvalidCsv() throws IOException {
        parser = new StringReader("Name,Age\nAlice,30aple");
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assertions.assertEquals(2, record.size());
        Assertions.assertEquals("Alice", record.get(0));
        Assertions.assertEquals(30, record.get(1).intValue());
        Assertions.assertTrue(record.get(1).toString().endsWith("aple"), "Invalid value should be trimmed");
    }

    @Test
    public void testParseWithCustomFormat() throws IOException {
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("ID", "Name", "Age")
                .build();

        parser = new CSVParser(new StringReader("ID,Name,Age\n1,Alice,30"), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        Assertions.assertEquals(3, record.size());
        Assertions.assertEquals(1, record.get(0).intValue());
        Assertions.assertEquals("Alice", record.get(1));
        Assertions.assertEquals(30, record.get(2).intValue());
    }

    @Test
    public void testParseFile() throws IOException {
        parser = new CSVParser(new FileReader("path/to/csvfile.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        // Add your assertions here
    }

    @Test
    public void testParseInputStream() throws IOException {
        byte[] csvData = "Name,Age\nAlice,30\nBob,25".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData);
        parser = new CSVParser(inputStream, format);
        List<CSVRecord> records = parser.getRecords();
        Assertions.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assertions.assertEquals(2, record.size());
            Assertions.assertEquals("Alice", record.get(0));
            Assertions.assertEquals(30, record.get(1).intValue());
            Assertions.assertEquals("Bob", records.get(1).get(0));
            Assertions.assertEquals(25, records.get(1).get(1).intValue());
        }
    }

    @Test
    public void testParseUrl() throws IOException {
        parser = new CSVParser(new URL("http://example.com/csvfile.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        // Add your assertions here
    }
}