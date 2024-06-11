import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File csvData = new File("path/to/csv.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());
        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());
        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com/data.csv");
        CSVParser parser = CSVParser.parse(url, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNullFormat() throws IOException {
        File csvData = new File("path/to/csv.csv");
        CSVParser parser = CSVParser.parse(csvData, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNullFile() throws IOException {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringNullFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringNullCSVData() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testParseFileInvalidFormat() throws IOException {
        File csvData = new File("path/to/invalid_format.csv");
        CSVParser parser = CSVParser.parse(csvData, null);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertTrue("Parser should be empty", records.isEmpty());
        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetRecordsAfterClose() throws IOException {
        File csvData = new File("path/to/csv.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        parser.getRecords();
        parser.close();
        List<CSVRecord> records = parser.getRecords();
    }

    @Test
    public void testGetHeaderMap() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        Assert.assertNotNull("Header map should not be null", headerMap);
        List<Map.Entry<String, Integer>> headerMapEntries = new ArrayList<>(headerMap.entrySet());
        Assert.assertEquals("Header map should have 2 entries", 2, headerMapEntries.size());
        Assert.assertEquals("name index should be 0", 0, headerMap.get("name").intValue());
        Assert.assertEquals("age index should be 1", 1, headerMap.get("age").intValue());
        parser.close();
    }

    @Test
    public void testGetRecordsWithCustomFormat() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVFormat customFormat = new CSVFormat(new CSVParserDefaultRecordParser(), '\t');
        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());
        parser.close();
    }

    @Test
    public void testGetRecordsWithSkippedHeader() throws IOException {
        String csvData = "name,age\nJohn,30\nMary,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        parser.setSkipLines(1);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertFalse("Parser should not be empty", records.isEmpty());
        parser.close();
    }

}