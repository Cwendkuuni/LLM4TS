package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setup() throws IOException {
        String csvData = "name,age,city\nMike,30,New York\nSara,25,Los Angeles\nJohn,45,Chicago";
        this.parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        if (this.parser != null) {
            this.parser.close();
        }
    }

    @Test
    public void testParseRecords() throws IOException {
        List<CSVRecord> records = this.parser.getRecords();
        Assert.assertEquals(3, records.size());

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            String[] values = record.toArray(String[].class);
            Assert.assertEquals("name", values[0]);
            Assert.assertEquals("Mike", values[1]);
            Assert.assertEquals("New York", values[2]);

            if (i == 0) {
                Assert.assertNull(record.getComment());
            } else {
                Assert.assertEquals("", record.getComment());
            }
            Assert.assertEquals(i + 1, record.getRecordNumber());
        }
    }

    @Test
    public void testParseRecordsWithHeader() throws IOException {
        String csvData = "name,age,city\nMike,30,New York\nSara,25,Los Angeles\nJohn,45,Chicago";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertEquals(3, records.size());
        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(Arrays.asList("name", "age", "city"), firstRecord.getHeader());
        Assert.assertEquals(3, firstRecord.size());
        Assert.assertEquals("name", firstRecord.getName(0));
        Assert.assertEquals("age", firstRecord.getName(1));
        Assert.assertEquals("city", firstRecord.getName(2));

        for (int i = 1; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            Assert.assertNull(record.getHeader());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Mike", record.get(0));
            Assert.assertEquals(30, record.get(1).getInt(0));
            Assert.assertEquals("New York", record.get(2));
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyCsvFile() throws IOException {
        String csvData = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCsvFile() throws IOException {
        String csvData = "name,age,city\nMike,30,New York\nSara,25,Los Angeles\nJohn,45,Chicago\nname,age,city";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertEquals(3, records.size());
        for (CSVRecord record : records) {
            String[] values = record.toArray(String[].class);
            // The last record has an extra line which is not part of the CSV data
            if (values.length == 3) {
                throw new AssertionError("Invalid number of columns: " + values.length);
            }
        }
    }

    @Test
    public void testParseRecordsWithCustomFormat() throws IOException {
        String csvData = "name;age;city\nMike;30;New York\nSara;25;Los Angeles\nJohn;45;Chicago";
        CSVFormat format = new CSVFormatBuilder()
                .setHeader("name", "age", "city")
                .setDelimiter(';')
                .build();
        CSVParser parser = CSVParser.parse(new StringReader(csvData), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        Assert.assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            String[] values = record.toArray(String[].class);
            Assert.assertEquals(3, values.length);
            Assert.assertEquals("name", values[0]);
            Assert.assertEquals("age", values[1]);
            Assert.assertEquals("city", values[2]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        String csvData = "name,age,city\nMike,30,New York\nSara,25,Los Angeles\nJohn,45,Chicago";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullCsvData() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }
}