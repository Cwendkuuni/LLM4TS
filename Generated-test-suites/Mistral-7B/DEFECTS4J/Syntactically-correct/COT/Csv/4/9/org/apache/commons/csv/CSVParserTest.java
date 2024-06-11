package org.apache.commons.csv; //Make sure to import all necessary classes

import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for CSVParser class
 */
public class CSVParserTest {

    private static final String CSV_FILE = "name,age\nJohn,30\nMike,25\n";
    private static final String CSV_EXCEL = "Name,Age\nJohn,30\nMike,25\n";
    private static final String CSV_INVALID = "Name,Age\nJohn,30\nMike,";
    private static final String CSV_EMPTY = "";
    private static final String CSV_COMMENT = "Name,Age\nJohn,30\n--COMMENT--\nMike,25\n";

    private CSVFormat format;
    private CSVParser parser;
    private CSVRecord record;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(CSV_FILE), format);
        record = parser.nextRecord();
    }

    @Test(expected = IllegalStateException.class)
    public void testNullFile() {
        new CSVParser(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        new CSVParser(new StringReader(CSV_INVALID), format);
    }

    @Test
    public void testParse() throws IOException {
        Assert.assertNotNull(parser);
        Assert.assertEquals(CSVFormat.DEFAULT, parser.getFormat());
        Assert.assertTrue(parser.hasNext());

        CSVRecord nextRecord = parser.nextRecord();
        Assert.assertNotNull(nextRecord);
        Assert.assertEquals( Arrays.asList(new String[]{"name", "age"}), nextRecord.getHeaderMap().keySet());
        Assert.assertEquals(2, nextRecord.size());
        Assert.assertEquals("name", nextRecord.getHeaderMap().get("name"));
        Assert.assertEquals("age", nextRecord.getHeaderMap().get("age"));
        Assert.assertEquals("John", nextRecord.get("name"));
        Assert.assertEquals(30, nextRecord.get("age").intValue());

        nextRecord = parser.nextRecord();
        Assert.assertNotNull(nextRecord);
        Assert.assertEquals("Mike", nextRecord.get("name"));
        Assert.assertEquals(25, nextRecord.get("age").intValue());
        Assert.assertEquals(NoSuchElementException.class, parser.hasNext() ? Assert.fail() : null);
        parser.close();
    }

    @Test
    public void testParseExcelFormat() throws IOException {
        format = CSVFormat.EXCEL.withHeader();
        parser = new CSVParser(new StringReader(CSV_EXCEL), format);
        record = parser.nextRecord();
        Assert.assertEquals( Arrays.asList(new String[]{"Name", "Age"}), record.getHeaderMap().keySet());
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
        Assert.assertEquals("Age", record.getHeaderMap().get("Age"));
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals(30, record.get("Age").intValue());

        parser.nextRecord();
        Assert.assertEquals("Mike", record.get("Name"));
        Assert.assertEquals(25, record.get("Age").intValue());
        Assert.assertEquals(NoSuchElementException.class, parser.hasNext() ? Assert.fail() : null);
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        format = new CSVFormat();
        parser = new CSVParser(new StringReader(CSV_INVALID), format);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    @Test
    public void testParseEmptyFile() throws IOException {
        parser = new CSVParser(new StringReader(CSV_EMPTY), format);
        Assert.assertFalse(parser.hasNext());
        parser.close();
    }

    @Test
    public void testParseComment() throws IOException {
        format = CSVFormat.EXCEL.withHeader();
        parser = new CSVParser(new StringReader(CSV_COMMENT), format);
        record = parser.nextRecord();
        Assert.assertEquals( Arrays.asList(new String[]{"name", "age"}), record.getHeaderMap().keySet());
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("name", record.getHeaderMap().get("name"));
        Assert.assertEquals("age", record.getHeaderMap().get("age"));
        Assert.assertEquals("John", record.get("name"));
        Assert.assertEquals(30, record.get("age").intValue());

        parser.nextRecord();
        Assert.assertEquals("Mike", record.get("name"));
        Assert.assertEquals(25, record.get("age").intValue());
        Assert.assertEquals("--COMMENT--", record.getComment());

        Assert.assertEquals(NoSuchElementException.class, parser.hasNext() ? Assert.fail() : null);
        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/csv.csv";
        Charset charset = Charset.forName("UTF-8");
        parser = new CSVParser(new InputStreamReader(new URL(urlString).openStream(), charset), format);
        record = parser.nextRecord();
        // Perform tests similar to testParse()
        parser.close();
    }

    @Test
    public void testIterate() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            Assert.assertNotNull(record);
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("name", record.getHeaderMap().get("name"));
            Assert.assertEquals("age", record.getHeaderMap().get("age"));
            Assert.assertEquals("John", record.get("name"));
            Assert.assertEquals(30, record.get("age").intValue());
            Assert.assertEquals("Mike", record.get("name"));
            Assert.assertEquals(25, record.get("age").intValue());
        }
        Assert.assertEquals(NoSuchElementException.class, parser.hasNext() ? Assert.fail() : null);
        parser.close();
    }
}