import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Los Angeles";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(2, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.get(0));
        Assert.assertEquals("John", record1.get(1));
        Assert.assertEquals("Age", record1.get(2));
        Assert.assertEquals(30, record1.get(2, Integer.class));
        Assert.assertEquals("City", record1.get(3));
        Assert.assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        Assert.assertEquals("Name", record2.get(0));
        Assert.assertEquals("Mary", record2.get(1));
        Assert.assertEquals("Age", record2.get(2));
        Assert.assertEquals(25, record2.get(2, Integer.class));
        Assert.assertEquals("City", record2.get(3));
        Assert.assertEquals("Los Angeles", record2.get(3));
    }

    @Test
    public void testParseEmptyCSVFile() throws Exception {
        String csvContent = "";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    @Test
    public void testParseCSVFileWithInvalidFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Los Angeles\nName,Age,City";
        try {
            new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseCSVFileWithInvalidToken() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMary,25,Los Angeles\nName,Age,City#";
        try {
            new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseCSVFileWithMissingDelimiter() throws Exception {
        String csvContent = "Name,Age City\nJohn,30,New York\nMary,25,Los Angeles";
        try {
            new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseCSVFileWithEmptyRecord() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\n,\nMary,25,Los Angeles";
        CSVParser parser = new CSVParser(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertEquals(3, records.size());
        CSVRecord record1 = records.get(0);
        Assert.assertEquals("Name", record1.get(0));
        Assert.assertEquals("John", record1.get(1));
        Assert.assertEquals("Age", record1.get(2));
        Assert.assertEquals(30, record1.get(2, Integer.class));
        Assert.assertEquals("City", record1.get(3));
        Assert.assertEquals("New York", record1.get(3));

        CSVRecord record2 = records.get(1);
        Assert.assertNull(record2);

        CSVRecord record3 = records.get(2);
        Assert.assertEquals("Name", record3.get(0));
        Assert.assertEquals("Mary", record3.get(1));
        Assert.assertEquals("Age", record3.get(2));
        Assert.assertEquals(25, record3.get(2, Integer.class));
        Assert.assertEquals("City", record3.get(3));
        Assert.assertEquals("Los Angeles", record3.get(3));
    }
}