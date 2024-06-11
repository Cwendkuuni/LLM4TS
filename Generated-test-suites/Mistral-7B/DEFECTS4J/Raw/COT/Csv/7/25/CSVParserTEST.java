package org.apache.commons.csv; // Import any necessary packages here

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMary,30,Los Angeles\n";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), CSVFormat.DEFAULT);
    }

    

    @Test
    public void testBasicParserFunctionality() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertEquals(3, record.size());
            Assert.assertNotNull(record.getRecordNumber());
        }
    }

    @Test
    public void testEmptyCSVFile() throws IOException {
        String csvData = "";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test(expected = IOException.class)
    public void testNullCSVFormat() throws IOException {
        parser = new CSVParser(new StringReader(""), null);
    }

    @Test(expected = IOException.class)
    public void testNullCSVData() throws IOException {
        parser = new CSVParser(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testInvalidCSVData() throws IOException {
        String csvData = "Name,Age,CityJohn,25,New York\nMary,30,Los Angeles";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testMalformedCSVData() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMary,30,\n";
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), CSVFormat.DEFAULT);
    }

    @Test
    public void testSkipHeaderRecord() throws IOException {
        String csvData = "Name,Age,City\nJohn,25,New York\nMary,30,Los Angeles";
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        format.setSkipHeaderRecord(true);
        parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());
        for (CSVRecord record : records) {
            Assert.assertEquals(3, record.size());
        }
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Assert.assertEquals(3, headerMap.size());
        Assert.assertTrue(headerMap.containsKey("Name"));
        Assert.assertTrue(headerMap.containsKey("Age"));
        Assert.assertTrue(headerMap.containsKey("City"));
        Assert.assertEquals(0, headerMap.get("Name").intValue());
        Assert.assertEquals(1, headerMap.get("Age").intValue());
        Assert.assertEquals(2, headerMap.get("City").intValue());
    }

    @Test
    public void testGetRecord() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals("John", firstRecord.get("Name"));
        Assert.assertEquals(25, firstRecord.getInteger("Age"));
        Assert.assertEquals("New York", firstRecord.get("City"));
    }

    @Test
    public void testGetRecordWithInvalidHeader() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Assert.assertNull(firstRecord.get("InvalidHeader"));
    }

    @Test
    public void testGetRecordWithMissingHeader() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord firstRecord = records.get(0);
        Assert.assertNull(firstRecord.get(""));
    }

    @Test
    public void testGetRecordsWithEmptyCollection() throws IOException {
        List<CSVRecord> records = Collections.emptyList();
        parser = new CSVParser(records);
        List<CSVRecord> parsedRecords = parser.getRecords();
        Assert.assertEquals(0, parsedRecords.size());
    }

    
}