import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("testData.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            Assert.assertFalse(record.isEmpty());
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "col1,col2\nval1,val2";
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            Assert.assertFalse(record.isEmpty());
            Assert.assertEquals(2, record.size());
            Assert.assertEquals("col1", record.getHeaderMap().get("col1"));
            Assert.assertEquals("col2", record.getHeaderMap().get("col2"));
        }

        parser.close();
    }

    @Test
    public void testParseURL() throws IOException {
        String urlString = "http://example.com/data.csv";
        CSVParser parser = CSVParser.parse(new URL(urlString), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertFalse(records.isEmpty());
        for (CSVRecord record : records) {
            Assert.assertFalse(record.isEmpty());
        }

        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        CSVParser parser = CSVParser.parse(null, CSVFormat.RFC4180);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        CSVParser parser = new CSVParser(new StringReader("testData.csv"), null);
    }

    @Test(expected = IOException.class)
    public void testParseIOException() throws IOException {
        String csvData = "col1,col2\nval1,val2";
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes()), "UTF-16"), CSVFormat.RFC4180);
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEndOfStream() throws IOException {
        String csvData = "col1,col2\nval1,val2\n";
        CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.nextRecord();

        List<CSVRecord> remainingRecords = parser.getRecords();
        Assert.assertTrue(remainingRecords.isEmpty());
    }
}