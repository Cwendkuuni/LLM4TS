import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CSVParserTest {

    @Test
    public void testBasicParsing() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals("Name", record.getHeaderMap().get("Name").toString());
            Assert.assertEquals("John Doe", record.get("Name"));
            Assert.assertEquals(30, record.getInteger("Age"));
            Assert.assertEquals("New York", record.get("City"));
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyFile() throws IOException {
        CSVParser parser = new CSVParser(new StringReader(""), CSVFormat.RFC4180);
        parser.getRecords();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles\nInvalidRecord";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);

        try {
            parser.getRecords();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCommentHandling() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York#Comment1\nJane Doe,25,Los Angeles#Comment2";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180);

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());
        for (CSVRecord record : records) {
            Assert.assertArrayEquals(new String[]{"Name", "Age", "City"}, record.getHeaderMap().keySet().toArray());
            Assert.assertEquals("Name", record.getHeaderMap().get("Name").toString());
            Assert.assertEquals("John Doe", record.get("Name"));
            Assert.assertEquals(30, record.getInteger("Age"));
            Assert.assertEquals("New York", record.get("City"));
            Assert.assertNull(record.getComment());
        }
    }

    @Test
    public void testSkippedHeader() throws IOException {
        String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.RFC4180.withHeader().withHeaderComment("Header"));
        parser.nextRecord(); // skip the header

        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(1, records.size());

        CSVRecord record = records.get(0);
        Assert.assertArrayEquals(new String[]{"Age", "City", "Name"}, record.getHeaderMap().keySet().toArray());
        Assert.assertEquals("Age", record.getHeaderMap().get("Age").toString());
        Assert.assertEquals(30, record.getInteger("Age"));
        Assert.assertEquals("New York", record.get("City"));
        Assert.assertEquals("John Doe", record.get("Name"));
        Assert.assertNull(record.getComment());
    }
}