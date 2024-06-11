import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
    }

    @Test
    public void testParseWithDefaultFormat() throws Exception {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertEquals(3, record.size());
            for (int i = 0; i < record.size(); i++) {
                Assert.assertNotNull(record.get(i));
            }
        }
    }

    @Test
    public void testParseEmptyCsv() throws Exception {
        String emptyCsv = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyCsv.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCsv() throws Exception {
        String invalidCsv = "Name,Age,City\nJohn,25,New York\nMike,30,Los\nAngeles";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidCsv.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
    }

    @Test
    public void testParseWithCustomFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,30,Los Angeles\n";
        Map<String, Integer> headerMap = new HashMap<>();
        headerMap.put("Name", 0);
        headerMap.put("Age", 1);
        headerMap.put("City", 2);
        CSVFormat customFormat = CSVFormat.custom().setHeaderMap(headerMap).build();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(25, record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
        }
    }

    @Test
    public void testParseWithNullValue() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York\nMike,null,Los Angeles";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Mike", record.get(1));
            Assert.assertNull(record.get(2));
        }
    }

    @Test
    public void testParseWithEmptyValue() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(25, record.get(1).intValue());
            Assert.assertEquals(Collections.emptyList(), record.get(2));
        }
    }

    @Test
    public void testParseWithComment() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,New York#This is a comment\nMike,30,Los Angeles";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(25, record.get(1).intValue());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertEquals(null, record.getComment());
        }
    }

    @Test
    public void testParseWithMalformedRecord() throws Exception {
        String csvContent = "Name,Age,City\nJohn,25,\nMike,30,Los Angeles Apple";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Mike", record.get(1));
            Assert.assertEquals(30, record.get(2).intValue());
            List<String> values = Arrays.asList(record.get(0).split(","));
            Assert.assertEquals(2, values.size());
            Assert.assertEquals("Mike", values.get(1));
            Assert.assertEquals("Apple", values.get(0));
        }
    }

    @Test
    public void testParseWithMalformedCsv() throws Exception {
        String invalidCsv = "Name,Age,City\nJohn,25,\nMike,30,Los Angeles\nApple";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidCsv.getBytes());
        try {
            parser = CSVParser.parse(inputStream, CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            Assert.fail();
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
        } finally {
            parser.close();
        }
    }
}