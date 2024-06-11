package org.apache.commons.csv; // Make sure to adjust the package if necessary

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    private CSVFormat rfc4180Format;
    private CSVFormat excelFormat;
    private CSVParser parser;
    private CSVRecord record;

    @Before
    public void setUp() throws IOException {
        rfc4180Format = CSVFormat.RFC4180;
        excelFormat = CSVFormat.EXCEL;
    }

    @Test
    public void testParseFile_ValidCSV_RFC4180Format() throws IOException {
        File testFile = new File("test_file.csv");
        parser = CSVParser.parse(testFile, rfc4180Format);

        Assert.assertNotNull(parser);

        record = parser.nextRecord();
        Assert.assertNotNull(record);

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFile_NullFile_RFC4180Format() {
        parser = CSVParser.parse(null, rfc4180Format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFile_NullFormat_RFC4180Format() {
        File testFile = new File("test_file.csv");
        parser = CSVParser.parse(testFile, null);
    }

    @Test(expected = IOException.class)
    public void testParseFile_InvalidFile_RFC4180Format() throws IOException {
        File testFile = new File("invalid_file.csv");
        parser = CSVParser.parse(testFile, rfc4180Format);
    }

    @Test
    public void testParseString_ValidCSV_RFC4180Format() throws IOException {
        String testString = "field1,field2,field3";
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertArrayEquals(new String[]{"field1", "field2", "field3"}, record.getValues());

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseString_NullString_RFC4180Format() {
        parser = new CSVParser(null, rfc4180Format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseString_NullFormat_RFC4180Format() {
        String testString = "field1,field2,field3";
        parser = new CSVParser(new StringReader(testString), null);
    }

    @Test
    public void testParseURL_ValidCSV_RFC4180Format() throws IOException {
        String testURLString = "http://example.com/test.csv";
        parser = CSVParser.parse(new URL(testURLString), rfc4180Format);

        record = parser.nextRecord();
        Assert.assertNotNull(record);

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseURL_NullURL_RFC4180Format() {
        parser = CSVParser.parse(null, rfc4180Format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseURL_NullFormat_RFC4180Format() {
        String testURLString = "http://example.com/test.csv";
        parser = CSVParser.parse(new URL(testURLString), null);
    }

    @Test(expected = IOException.class)
    public void testParseURL_InvalidURL_RFC4180Format() throws IOException {
        String testURLString = "invalid://example.com/test.csv";
        parser = CSVParser.parse(new URL(testURLString), rfc4180Format);
    }

    @Test
    public void testParseReader_ValidCSV_RFC4180Format() throws IOException {
        String testString = "field1,field2,field3\nfield4,field5,field6";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testString.getBytes());
        parser = new CSVParser(new InputStreamReader(testInput), rfc4180Format);

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertArrayEquals(new String[]{"field1", "field2", "field3"}, record.getValues());

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertArrayEquals(new String[]{"field4", "field5", "field6"}, record.getValues());

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseReader_NullReader_RFC4180Format() {
        parser = new CSVParser(null, rfc4180Format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseReader_NullFormat_RFC4180Format() {
        String testString = "field1,field2,field3\nfield4,field5,field6";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testString.getBytes());
        parser = new CSVParser(new InputStreamReader(testInput), null);
    }

    @Test(expected = IOException.class)
    public void testParseReader_InvalidInput_RFC4180Format() throws IOException {
        String testString = "field1,field2,field3\nfield4,field5,field6\ninvalid_input";
        ByteArrayInputStream testInput = new ByteArrayInputStream(testString.getBytes());
        parser = new CSVParser(new InputStreamReader(testInput), rfc4180Format);
    }

    @Test
    public void testParseCustomFormat_ValidCSV_ExcelFormat() throws IOException {
        String testString = "[[Column1,Column2],[Value1,Value2],[Value3,Value4]]";
        parser = new CSVParser(new StringReader(testString), excelFormat);

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("Column1", record.getColumnName(0));
        Assert.assertEquals("Column2", record.getColumnName(1));
        Assert.assertEquals("Value1", record.get(0));
        Assert.assertEquals("Value2", record.get(1));

        record = parser.nextRecord();
        Assert.assertNotNull(record);
        Assert.assertEquals(2, record.size());
        Assert.assertEquals("Value3", record.get(0));
        Assert.assertEquals("Value4", record.get(1));

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseCustomFormat_InvalidFormat_ExcelFormat() throws IOException {
        String testString = "[Column1,Column2],[Value1,Value2],[Value3,Value4]"; // Missing brackets at the beginning
        parser = new CSVParser(new StringReader(testString), excelFormat);
    }

    @Test
    public void testGetRecords_ValidCSV_RFC4180Format() throws IOException {
        File testFile = new File("test_file.csv");
        parser = CSVParser.parse(testFile, rfc4180Format);

        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertFalse(records.isEmpty());

        record = records.get(0);
        Assert.assertNotNull(record);
        Assert.assertArrayEquals(new String[]{"field1", "field2", "field3"}, record.getValues());

        parser.close();
    }

    @Test
    public void testGetRecords_EmptyCSV_RFC4180Format() throws IOException {
        String testString = "";
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertTrue(records.isEmpty());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testGetRecords_InvalidCSV_RFC4180Format() throws IOException {
        String testString = "field1,field2,field3\nfield4,field5,field6\n"; // Missing trailing comma in last line
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        List<CSVRecord> records = parser.getRecords();

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRecord_EndOfStream() throws IOException {
        String testString = "field1,field2,field3";
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        record = parser.nextRecord();
        Assert.assertNotNull(record);

        List<CSVRecord> records = parser.getRecords();
        record = parser.nextRecord(); // Consume the next record

        Assert.assertFalse(parser.hasNext());
        Assert.assertThrows(NoSuchElementException.class, () -> parser.nextRecord());
    }

    @Test
    public void testGetHeaderMap_ValidCSV_RFC4180Format() throws IOException {
        File testFile = new File("test_file.csv");
        parser = CSVParser.parse(testFile, rfc4180Format);

        Map<String, Integer> headerMap = parser.getHeaderMap();

        Assert.assertNotNull(headerMap);
        Assert.assertEquals(3, headerMap.size());
        Assert.assertEquals("field1", headerMap.get("field1"));
        Assert.assertEquals("field2", headerMap.get("field2"));
        Assert.assertEquals("field3", headerMap.get("field3"));

        parser.close();
    }

    @Test
    public void testGetHeaderMap_EmptyCSV_RFC4180Format() throws IOException {
        String testString = "";
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        Map<String, Integer> headerMap = parser.getHeaderMap();

        Assert.assertNotNull(headerMap);
        Assert.assertTrue(headerMap.isEmpty());

        parser.close();
    }

    @Test
    public void testGetHeaderMap_CustomFormat_ExcelFormat() throws IOException {
        String testString = "[[Column1,Column2],[Value1,Value2],[Value3,Value4]]";
        parser = new CSVParser(new StringReader(testString), excelFormat);

        Map<String, Integer> headerMap = parser.getHeaderMap();

        Assert.assertNotNull(headerMap);
        Assert.assertEquals(2, headerMap.size());
        Assert.assertEquals("Column1", headerMap.get("Column1"));
        Assert.assertEquals("Column2", headerMap.get("Column2"));

        parser.close();
    }

    @Test
    public void testGetRecordNumber_ValidCSV() throws IOException {
        File testFile = new File("test_file.csv");
        parser = CSVParser.parse(testFile, rfc4180Format);

        int recordNumber = parser.getRecordNumber();

        parser.nextRecord(); // Consume the first record
        parser.nextRecord(); // Consume the second record

        int currentRecordNumber = parser.getRecordNumber();

        Assert.assertEquals(3, recordNumber);
        Assert.assertEquals(5, currentRecordNumber);

        parser.close();
    }

    @Test
    public void testGetRecordNumber_EmptyCSV() throws IOException {
        String testString = "";
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        int recordNumber = parser.getRecordNumber();

        parser.close();

        Assert.assertEquals(0, recordNumber);
    }

    @Test
    public void testGetRecordNumber_InvalidCSV() throws IOException {
        String testString = "field1,field2,field3\nfield4,field5,field6\n"; // Missing trailing comma in last line
        parser = new CSVParser(new StringReader(testString), rfc4180Format);

        int recordNumber = parser.getRecordNumber();

        parser.close();

        Assert.assertEquals(-1, recordNumber);
    }
}