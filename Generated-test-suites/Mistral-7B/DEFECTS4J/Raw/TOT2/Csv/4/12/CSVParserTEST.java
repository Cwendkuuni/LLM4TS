import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago";
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).parseInt());
            Assert.assertEquals("New York", record.get(2));
        }
    }

    @Test
    public void testParseCSVWithNullValues() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n,A,B";
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).parseInt());
            Assert.assertEquals("New York", record.get(2));
            Assert.assertNull(record.get(2)); // Third value is null
        }
    }

    @Test
    public void testParseCSVWithTrimmedValues() throws IOException {
        String csvData = "Name, Age, City\nJohn,  30, New York\nMike, 25, Chicago";
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT.withHeader("Name", "Age", "City"));

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
            Assert.assertEquals("John", record.get(0).trim());
            Assert.assertEquals("30", record.get(1).trim());
            Assert.assertEquals("New York", record.get(2).trim());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyCSV() throws IOException {
        String csvData = "";
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\nName:Age";
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago";
        CSVFormat customFormat = new CSVFormat(',', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new String[]{"Name", "Age", "City"}, null);
        CSVParser parser = new CSVParser(new ByteArrayReader(new ByteArrayInputStream(csvData.getBytes())), customFormat);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray(new String[0]));
            Assert.assertEquals("Name", record.getHeaderMap().get("Name"));
            Assert.assertEquals("John", record.get(0));
            Assert.assertEquals(30, record.get(1).parseInt());
            Assert.assertEquals("New York", record.get(2));
        }
    }
}