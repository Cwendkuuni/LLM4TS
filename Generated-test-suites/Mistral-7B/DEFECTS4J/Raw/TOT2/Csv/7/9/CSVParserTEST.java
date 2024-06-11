import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws Exception {
        String csvContent = "Name,Age\nJohn,30\nMike,25\n";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Name", map.get("Name"));
            Assert.assertEquals("Age", map.get("Age"));
            Assert.assertEquals("John", map.get("Name"));
            Assert.assertEquals(30, map.get("Age"));
            Assert.assertEquals("Mike", map.get("Name"));
            Assert.assertEquals(25, map.get("Age"));
        }
    }

    @Test
    public void testParseEmptyCSVFile() throws Exception {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseCSVFileWithInvalidFormat() throws Exception {
        String csvContent = "Name,Age\nJohn,30\nMike,25\nName:Mike,25";
        try {
            CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
            parser.getRecords();
            Assert.fail("Expected IOException, but no exception was thrown");
        } catch (IOException e) {
            Assert.assertEquals("(line 3) invalid parse sequence: Name:Mike", e.getMessage());
        }
    }

    @Test
    public void testParseCSVFileWithMissingHeader() throws Exception {
        String csvContent = "John,30\nMike,25";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertNull(map.get("Name"));
            Assert.assertEquals("John", map.get(0));
            Assert.assertEquals(30, map.get(1));
            Assert.assertNull(map.get("Age"));
            Assert.assertEquals("Mike", map.get(2));
            Assert.assertEquals(25, map.get(3));
        }
    }

    @Test
    public void testParseCSVFileWithExtraData() throws Exception {
        String csvContent = "Name,Age\nJohn,30\nMike,25\nExtraData";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Name", map.get("Name"));
            Assert.assertEquals("Age", map.get("Age"));
            Assert.assertEquals("John", map.get("Name"));
            Assert.assertEquals(30, map.get("Age"));
            Assert.assertEquals("Mike", map.get("Name"));
            Assert.assertEquals(25, map.get("Age"));
        }
        Assert.assertFalse(parser.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyCSVFileWithIterator() throws Exception {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testParseCSVFileWithCustomFormat() throws Exception {
        String csvContent = "Name,Age\nJohn,30\nMike,25\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Column1", "Column2")
                .build();
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), customFormat);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Column1", map.get("Column1"));
            Assert.assertEquals("Column2", map.get("Column2"));
            Assert.assertEquals("John", map.get("Column1"));
            Assert.assertEquals(30, map.get("Column2"));
            Assert.assertEquals("Mike", map.get("Column1"));
            Assert.assertEquals(25, map.get("Column2"));
        }
    }

    @Test
    public void testParseCSVFileWithComment() throws Exception {
        String csvContent = "Name,Age\nJohn,30\n---\nMike,25";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Name", map.get("Name"));
            Assert.assertEquals("Age", map.get("Age"));
            Assert.assertEquals("John", map.get("Name"));
            Assert.assertEquals(30, map.get("Age"));
            Assert.assertEquals("Mike", map.get("Name"));
            Assert.assertEquals(25, map.get("Age"));
            Assert.assertEquals("---\n", record.getComment());
        }
    }

    @Test
    public void testParseCSVFileWithSkippedHeader() throws Exception {
        String csvContent = "Name,Age\n---\nJohn,30\nMike,25";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        parser.getFormat().setSkipHeaderRecord(true);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Name", map.get("Name"));
            Assert.assertEquals("Age", map.get("Age"));
            Assert.assertEquals("John", map.get("Name"));
            Assert.assertEquals(30, map.get("Age"));
            Assert.assertEquals("Mike", map.get("Name"));
            Assert.assertEquals(25, map.get("Age"));
        }
    }

    @Test
    public void testParseCSVFileWithNullValues() throws Exception {
        String csvContent = "Name,Age\nJohn,30\nMike,null";
        CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(csvContent.getBytes())), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Map<String, Object> map = record.toMap();
            Assert.assertEquals("Name", map.get("Name"));
            Assert.assertEquals("Age", map.get("Age"));
            Assert.assertEquals("John", map.get("Name"));
            Assert.assertEquals(30, map.get("Age"));
            Assert.assertEquals("Mike", map.get("Name"));
            Assert.assertNull(map.get("Age"));
        }
    }
}